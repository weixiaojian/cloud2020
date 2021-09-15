# SpringBoot与SpringCloud版本选型
> SpringBoot2.X版和SpringCloud H版  
> 版本对应关系：https://start.spring.io/actuator/info
* spring cloud：Hoxton.SR1
* spring boot：2.2.2.RELEASE
* spring cloud Alibaba：2.1.0.RELEASE
* java：JAVA8
* maven：3.5及以上
* mysql：5.7及以上

# 关于Cloud各种组件的停更/升级/替换
* 服务注册中心：Eureka（停用） 》》》   Nacos/Zookeeper/Consul
* 服务调用：Ribbon（停更）   》》》     LoadBalancer
* 服务调用：Feign（停用）    》》》     OpenFeign
* 服务降级：Hystrix（停用）  》》》     Sentienl/Resilience4j
* 服务网关：zuul（停用）     》》》     Gateway
* 服务配置：Config（停用）   》》》     Nacos
* 服务总线：Bus（停用）      》》》     Nacos

# 端口/服务汇总
cloud-api-commons                   公共依赖模块 存放实体类等
cloud-consumer-order80              消费者订单模块
cloud-consumerconsul-order80        consul注册中心下的消费者订单模块
cloud-consumerzk-order80            zookeeper注册中心下的消费者订单模块
cloud-eureka-server7001             eureka注册中心
cloud-eureka-server7002             eureka注册中心
cloud-provider-payment8001          生产者支付模块
cloud-provider-payment8002          生产者支付模块
cloud-provider-payment8004          生产者支付模块
cloud-providerconsul-payment8006    consul注册中心下的生产者支付模块

# Eureka 
> Eureka Server提供服务注册和发现，目前已经停止更新 推荐使用Nacos、consul
* Eureka传送门：[http://imwj.club/article/122](http://imwj.club/article/122)

# Zookeeper
> Eureka停更后也可用Zookeeper来作为注册中心（不推荐）
* Zookeeper的整合可以参照：cloud-provider-payment8004、cloud-consumerzk-order80

# Consul
> Consul提供：服务发现（提供HTTP和DNS两种发现方式）、健康监测、KV存储（key , Value的存储方式）、多数据中心、可视化Web界面
* 官网：[https://www.consul.io/downloads.html](https://www.consul.io/downloads.html)
* 1.安装Consul：下载完成后只有一个consul.exe文件，cmd路径下输入`java -version`查看当前版本
* 2.运行：consul.exe目录下使用cmd输入命令`consul agent -dev`启动
* 3.访问页面：[http;//localhost:8500](http;//localhost:8500)
* 4.服务注册参考：cloud-providerconsul-payment8006
* 5.服务消费参考：cloud-consumerconsul-order80
* 6.注意：参考项目创建流程：POM文件中的依赖、YML文件中的配置、主启动类上的注解


# 三个注册中对比
* CAP：C:Consistency(强一致性)、A:Availability(可用性)、P:Partition tolerance(分区容错)(P是永远要保证的，三选二)
* Eureka优先保证AP
* Zookeeper/Consul优先保证CP