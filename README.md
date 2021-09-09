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

# 端口汇总
consume-orderr：80
provider-payment：8001
eureka-server：7001

# Eureka 

# Zookeeper

# Consul

# 三个注册中对比
* CAP：C:Consistency(强一致性)、A:Availability(可用性)、P:Partition tolerance(分区容错)(P是永远要保证的，三选二)
* Eureka优先保证AP
* Zookeeper/Consul优先保证CP