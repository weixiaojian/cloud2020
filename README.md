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

# Ribbon
* 默认的轮询算法原理
```
负载均衡算法: rest接口第几次请求数%服务器集群总数量=实际调用服务器位置下标，每次服务重启动后rest接口计数从1开始

假设有两台服务提供者：服务器集群总数量即为2
当总请求数为1时:1%2=1对应下标位置为1
当总请求数为2时:2%2=0对应下标位置为0
当总请求数为3时:3%2=1对应下标位置为1
```
* Ribbon传送门：[http://imwj.club/article/123](http://imwj.club/article/123)

# OpenFeign
* Feign传送门：[http://imwj.club/article/124](http://imwj.club/article/124)

* OpenFeign：通过@FeignClient注解+接口实现服务调用，controler中直接注入该service然后调用其中的方法即可
```
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {

    @GetMapping(value = "/payment/getPaymentById/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id);

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout();
}
```


* 配置超时时间：默认是1秒  容易报错
```
ribbon:
  ReadTimeout:  5000
  ConnectTimeout: 5000
```

* 配置日志输出
1.增加配置bean
```
@Configuration
public class FeignConfig {

    /**
     * NONE: 默认的，不显示任何日志
     * BASIC：仅记录请求方法、URL、响应状态码以及执行时间
     * HEADERS：除了BASIC中定义的信息以外，还有请求和响应的头信息
     * FULL： 除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
}
```
2.配置文件中开启日志
```
logging:
  level:
    com.atguigu.springcloud.service.PaymentFeignService: debug
```

# Hystrix
* Hystrix传送门：[http://imwj.club/article/125](http://imwj.club/article/125)
* 服务降级
* 服务熔断
* 服务限流

# Gateway
* Route路由：路由是网关最基础的部分，路由信息有一个ID、一个目的URL、一组断言和一组Filter组成。如果断言路由为真，则说明请求的URL和配置匹配  
* Predicate断言：参考的是Java8中的断言函数。Spring Cloud Gateway中的断言函数输入类型是Spring5.0框架中的ServerWebExchange。Spring Cloud Gateway中的断言函数允许开发者去定义匹配来自于http request中的任何信息，比如请求头和参数等。如果请求与断言相匹配则进行该路由。  
* Filter过滤：一个标准的Spring webFilter。Spring cloud gateway中的filter分为两种类型的Filter，分别是Gateway Filter(路由过滤)和Global Filter(全局过滤)。过滤器Filter将会对请求和响应进行修改处理  

## yml配置实现路由匹配
```
server:
  port: 9527
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: false  #开启从注册中心动态创建路由的功能，利用微服务名进行路由
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: lb://CLOUD-PAYMENT-SERVICE
          predicates:
            - Path=/payment/getPaymentById/**   #断言,路径相匹配的进行路由

        - id: payment_routh2
          uri: lb://CLOUD-PAYMENT-SERVICE
          predicates:
            - Path=/payment/lb/**   #断言,路径相匹配的进行路由
```

* 配置类的方式实现路由匹配
```
@Configuration
public class GateWayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("baidu", r -> r.path("/guonei")
                        .uri("http://news.baidu.com/guonei"))
                .route("baidu2",r -> r.path("/guoji")
                        .uri("http://news.baidu.com/guoji"))
                .build();
    }
}
```

## Route Predicate Factorie（路由谓词）
```
1.After=2021-11-18T14:45:00.102+08:00[Asia/Shanghai]    #在此时间后这个断言生效

2.Before=2021-11-18T14:45:00.102+08:00[Asia/Shanghai]   #在此时间前这个断言有效

3.Between=2017-01-20T17:42:47.789-07:00[America/Denver], 2017-01-21T17:42:47.789-07:00[America/Denver]  #在此时间范围内这个断言有效

4.Cookie=username,zs    #请求头cookie中 要求username=zs

5.Header=X-Request-Id, \d+      #请求头Header中 要求有请求参数 X-Request-Id,且是数字

...
```

## Gatway Filters
> 分为单一过滤器、全局过滤器(主要)

* GlobalFilter
主要功能：全局日志记录、统一网关鉴权等等

* 例子
```
@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //日志记录
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        log.info("===请求参数["+new Date()+"]：" + queryParams.toString());
        //用户鉴权
        String username = request.getQueryParams().getFirst("username");
        if(StringUtils.isEmpty(username)){
            log.info("===用户名为Null 非法用户,(┬＿┬)");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);//给人家一个回应
            return exchange.getResponse().setComplete();
        }
        //放行
        return chain.filter(exchange);
    }

    /**
     * 顺序 0表示第一个过滤器
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
```

# springcloud config
* springcloud config传送门：[http://imwj.club/article/128](http://imwj.club/article/128)

## 例子
* 1.新建一个springcloud-config项目存放配置信息
```
https://github.com/weixiaojian/cloud2020/tree/master/springcloud-config
```
* 2.新建cloud-config-center-3344项目做配置中心，yml如下
```
server:
  port: 3344
spring:
  application:
    name: cloud-config-center
  cloud:
    config:
      server:
        git:
          uri: git@github.com:weixiaojian/cloud2020.git
          search-paths:
            - springcloud-config
      label: master
eureka:
  client:
    service-url:
      defaultZone:  http://localhost:7001/eureka
```
* 3.访问http://127.0.0.1:3344/master/config-dev.yml即可读取到配置文件信息