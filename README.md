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


## 实现一次刷新配置中心  其余项目的配置生效
* 1.新建项目cloud-config-client-3355、cloud-config-client-3366  

* 2.在两个项目的pom中增加依赖和添加rabbitmq配置  
```
        <!--bus-amqp-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
```

* 3.修改github上的配置后 手动刷新3344配置中心  
```
curl -X POST "http://localhost:3344/actuator/bus-refresh"
```

* 刷新指定服务3355
```
curl -X POST "http://localhost:3344/actuator/bus-refresh/config-client:3355"
```

# Spring Cloud Stream
> 屏蔽底层消息中间件的差异（rabbitmq、kafka），统一消息的编程模型；  
> 注意：目前仅支持rabbitmq、kafka  

## 常用注解
| 组成        | 说明    | 
| --------   | -----  | 
| Middleware        | 中间件，目前只支持RabbitMQ和Kafka      | 
| Binder        | Binder是应用于消息中间件之间的封装，目前实行了Kafka和RabbitMQ的Binder，通过Binder可以很方便的连接中间件，可以动态的改变消息类型（对应于Kafka的topic，RabbitMQ的exchange），这些都可以通过配置文件来实现      | 
| @Input        | 注解标识输入通道，通过该输入通道接收到的消息进入应用程序      | 
| @Output        | 注解标识输出通道，发布的消息将通过该通道离开应用程      | 
| @StreamListener        | 监听队列，用于消费者的队列的消息接收      | 
| @EnableBinding        | 	指信道channel和exchange绑定在一起      | 

## 生产者cloud-stream-rabbitmq-provider8801
* 1.yml配置
```
server:
  port: 8801

spring:
  application:
    name: cloud-stream-provider
  cloud:
    stream:
      binders: # 在此处配置要绑定的rabbitmq的服务信息；
        defaultRabbit: # 表示定义的名称，用于于binding整合
          type: rabbit # 消息组件类型
          environment: # 设置rabbitmq的相关的环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings: # 服务的整合处理
        output: # 这个名字是一个通道的名称
          destination: studyExchange # 表示要使用的Exchange名称定义
          content-type: application/json # 设置消息类型，本次为json，文本则设置“text/plain”
          binder: defaultRabbit  # 设置要绑定的消息服务的具体设置

eureka:
  client: # 客户端进行Eureka注册的配置
    service-url:
      defaultZone: http://localhost:7001/eureka
  instance:
    lease-renewal-interval-in-seconds: 2 # 设置心跳的时间间隔（默认是30秒）
    lease-expiration-duration-in-seconds: 5 # 如果现在超过了5秒的间隔（默认是90秒）
    instance-id: send-8801.com  # 在信息列表时显示主机名称
    prefer-ip-address: true     # 访问的路径变为IP地址
```

* 2.发送消息实现类
```
@EnableBinding(Source.class) //标识消息的推送管道
public class IMessageServiceImpl implements IMessageService {

    @Resource
    private MessageChannel output;

    @Override
    public String send() {
        String uuid = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(uuid).build());
        System.out.println("===========UUID：" + uuid);
        return uuid;
    }
}
```

## 消费者cloud-stream-rabbitmq-consumer8802、cloud-stream-rabbitmq-consumer8803
* 1.yml配置，注意：一定要配置group分组名称，两个消费者在相同分组时同一条消息只会被一个消费 如果不在一个消费者组则两个消费者都会消费到，且配置了消费者组之后生产者宕机重启也数据也不会丢失
```
server:
  port: 8802

spring:
  application:
    name: cloud-stream-consumer
  cloud:
    stream:
      binders: # 在此处配置要绑定的rabbitmq的服务信息；
        defaultRabbit: # 表示定义的名称，用于于binding整合
          type: rabbit # 消息组件类型
          environment: # 设置rabbitmq的相关的环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings: # 服务的整合处理
        input: # 这个名字是一个通道的名称
          destination: studyExchange # 表示要使用的Exchange名称定义
          content-type: application/json # 设置消息类型，本次为对象json，如果是文本则设置“text/plain”
          binder: defaultRabbit # 设置要绑定的消息服务的具体设置
          group: GROUP-ALL



eureka:
  client: # 客户端进行Eureka注册的配置
    service-url:
      defaultZone: http://localhost:7001/eureka
  instance:
    lease-renewal-interval-in-seconds: 2 # 设置心跳的时间间隔（默认是30秒）
    lease-expiration-duration-in-seconds: 5 # 如果现在超过了5秒的间隔（默认是90秒）
    instance-id: receive-8802.com  # 在信息列表时显示主机名称
    prefer-ip-address: true     # 访问的路径变为IP地址
```
* 2.接收消息实体
```
@Component
@EnableBinding(Sink.class)//标识消息的接收管道
public class ReceiveMessageListenerController {

    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> msgStr){
        System.out.println("===原始报文：" + msgStr);
        System.out.println("===serverPort："+ serverPort +" ===消费者接收到数据===" + msgStr.getPayload());
    }
}
```

## 重复消费和持久化
* 配置了group分组名称即可


# Spring Cloud Sleuth
> 分布式请求的链路追踪
 
* 1.下载jar包并启动：https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec
```
java -jar zipkin-server-2.12.9-exec.jar
```
* 2.访问：http://127.0.0.1:9411/zipkin

* 3.在项目中引入依赖和配置yml
```
<!--包含了sleuth+zipkin-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
        
        
spring:
  application:
    name: cloud-payment-service
  zipkin:
    base-url: http://localhost:9411
  sleuth:
    sampler:
      #采样率值介于 0 到 1 之间，1 则表示全部采集
      probability: 1
```

## Spring Cloud Alibaba Nacos
> 可用于服务注册中心和服务配置中心，是注册中心和配置中心的组合体，支持CP/AP模式切换
* 官网：[https://nacos.io/zh-cn/index.html](https://nacos.io/zh-cn/index.html)  
* 安装和启动教程见官网文档即可

## nacos做服务注册中心-生产者
* 1.新建cloudalibaba-provider-payment9001、cloudalibaba-provider-payment9002，增加pom
```
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```
* 2.application.yml配置
```
server:
  port: 9001

spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #配置Nacos地址
```

* 3.启动类
```
@EnableDiscoveryClient
@SpringBootApplication
public class PaymentMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain9001.class,args);
    }
}
```

## nacos做服务注册中心-消费者
* 1.新建cloudalibaba-consumer-nacos-order83，增加pom
```
        <!--SpringCloud ailibaba nacos -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```
* 2.application.yml配置
```
server:
  port: 83
spring:
  application:
    name: nacos-order-consumer
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848

#消费者将要去访问的微服务名称(注册成功进nacos的微服务提供者)
service-url:
  nacos-user-service: http://nacos-payment-provider
```

* 3.启动类
```
@EnableDiscoveryClient
@SpringBootApplication
public class PaymentMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain9001.class,args);
    }
}
```

* 4.config配置类
```
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}
```

* 5.controller类
```
@Slf4j
@RestController
public class OrderNacosController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    /**
     * 请求会转发到cloudalibaba-provider-payment9001、cloudalibaba-provider-payment9002上
     * @param id
     * @return
     */
    @GetMapping(value = "/consumer/payment/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Long id)
    {
        return restTemplate.getForObject(serverURL+"/payment/nacos/"+id,String.class);
    }
}
```

## nacos做配置中心
* 1.新建项目cloudalibaba-config-nacos-client3377  增加pom依赖
```
    <!--nacos-config-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
```

* 2.bootstrap.yml配置
```
# nacos配置
server:
  port: 3377

spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #Nacos服务注册中心地址
      config:
        server-addr: localhost:8848 #Nacos作为配置中心地址
        file-extension: yaml #指定yaml格式的配置
        #namespace: 86030e14-8a0a-4e61-a36b-5fec96b705a5 #dev
        #group: DEFAULT_GROUP

# 命名公式：${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
# DataId情况：nacos-config-client-dev.yaml
```
* 3.application.yml配置
```
spring:
  profiles:
    #active: prod # 表示开发环境
    active: dev # 测试group用
```

* 4.controller增加@RefreshScope动态刷新注解（nacos修改配置后 服务会自动刷新）
```
@RestController
@RefreshScope //支持Nacos的动态刷新功能。
public class ConfigClientController
{
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo() {
        return configInfo;
    }
}
```

* 5.nacos的配置列表中增加配置  DataId规则
```
${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
```

* 6.nacos还可以配置namespace、group等等来进行配置隔离：namespace > group > DataId