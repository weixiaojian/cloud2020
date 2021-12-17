
# Seata分布式事务处理
> 一次业务操作需要跨多个数据源或者跨多个系统进行远程调用，就会产生分布式事务问题  
* 官网：[https://seata.io/zh-cn/](https://seata.io/zh-cn/)

## 官网术语
* ID（Transaction ID XID） + 三组件（TC事务协调者、TM事务管理器、RM资源管理器）     
* TC事务协调者：维护全局和分支事务的状态，驱动全局事务提交或回滚。                                             --seata服务器
* TM事务管理器：定义全局事务的范围：开始全局事务、提交或回滚全局事务。                                          --事务发起方
* RM资源管理器：管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。     --事务参与方

![image](https://raw.githubusercontent.com/weixiaojian/cloud2020/master/seata-process.png)

## 安装
* 1.下载地址[https://github.com/seata/seata/releases](https://github.com/seata/seata/releases)
* 2.修改file.conf配置文件
```
  # service指定分组名称
  vgroup_mapping.my_test_tx_group = "java_tx_group"
  
  # store下的配置修改为db配置
  mode = "db"
  db {
      ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
      datasource = "dbcp"
      ## mysql/oracle/h2/oceanbase etc.
      db-type = "mysql"
      driver-class-name = "com.mysql.jdbc.Driver"
      url = "jdbc:mysql://127.0.0.1:3306/seata"
      user = "root"
      password = "123456"
      min-conn = 1
      max-conn = 3
      global.table = "global_table"
      branch.table = "branch_table"
      lock-table = "lock_table"
      query-limit = 100
    }  
```
* 3.mysql创建seata库，并建表（seata的conf目录下有sql文件），注意 0.9版本之后conf目录下就没有sql文件了 需要到github上去获取，详情可见conf下的README-zh.md
```
seata数据库所需表：
[server](https://github.com/seata/seata/tree/develop/script/server)

业务数据库所需表：
[client](https://github.com/seata/seata/tree/develop/script/client) 
```
* 4.修改registry.conf配置文件，配置为nacos
```
  type = "nacos"

  nacos {
    serverAddr = "localhost:8848"
    namespace = ""
    cluster = "default"
  }
```
* 5.先启动nacos再启动seata

## 项目中使用
> 参考seata-order-service2001、seata-storage-service2002、seata-account-service2003三个服务  
* 1.项目整体流程：seata-order-service2001是订单服务，收到订单数据后会将订单数据插入到tab_order，然后调用seata-storage-service2002扣减库存、调用seata-account-service2003扣减金额，最后回写tab_order订单状态
* 2.三个项目中都增加file.conf、registry.conf两个配置文件
* 3.2001项目的service增加全局事务注解
```
    @Override
    @GlobalTransactional(name = "java-create-order",rollbackFor = Exception.class)
    public void create(Order order){
        log.info("----->开始新建订单");
        //新建订单
        orderDao.create(order);

        //扣减库存
        log.info("----->订单微服务开始调用库存，做扣减Count");
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("----->订单微服务开始调用库存，做扣减end");

        //扣减账户
        log.info("----->订单微服务开始调用账户，做扣减Money");
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("----->订单微服务开始调用账户，做扣减end");

        //修改订单状态，从零到1代表已经完成
        log.info("----->修改订单状态开始");
        orderDao.update(order.getUserId(),0);
        log.info("----->修改订单状态结束");

        log.info("----->下订单结束了");
    }
```

* 4.2002服务中增加超时异常
```
    @Override
    public void decrease(Long userId, BigDecimal money) {
        log.info("------->account-service中扣减账户余额开始");
        //模拟超时异常20s，全局事务回滚，OpenFeign默认超时时间是1s
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        accountDao.decrease(userId, money);
        log.info("------->account-service中扣减账户余额结束");
    }
```

* 5.在有`@GlobalTransactional`或者没有该注解的情况下调用2001的`/order/create`查看三个数据库中的订单表、库存表、余额表的事务情况，Seata默认是AT 模式