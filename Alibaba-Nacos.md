# Spring Cloud Alibaba Nacos
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

## nacos集群配置
> linux服务器下配置1个nginx + 3个nacos
* 1.nacos-3333配置conf/cluster.conf
```
192.168.31.129:3333
192.168.31.129:4444
192.168.31.129:5555
```

* 2.nacos-3333修改配置conf/application.properties
```
server.port=3333
spring.datasource.platform=mysql
db.url.0=jdbc:mysql://192.168.31.129:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=123123
```

* 3.nacos-4444、.nacos-5555同理修改以上两个配置cluster.conf和application.properties
```
server.port=4444
spring.datasource.platform=mysql
db.url.0=jdbc:mysql://192.168.31.129:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=123123

server.port=5555
spring.datasource.platform=mysql
db.url.0=jdbc:mysql://192.168.31.129:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=123123
```

* 4.nginx配置
```
    upstream cluster {
        #weigth参数表示权值，权值越高被分配到的几率越大
        server 127.0.0.1:3333 weight=1;
        server 127.0.0.1:4444 weight=1;
		server 127.0.0.1:5555 weight=1;
    }
    server {
        listen       8989;
        server_name  localhost;
        access_log off;              
        location / {         
               proxy_pass http://cluster;
        }
    }
```

* 5.集群模式启动三个nacos`sh startup.sh`，启动nginx 浏览器中访问
```
http://192.168.31.129:1111/nacos
```