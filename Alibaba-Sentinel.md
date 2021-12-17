# Sentinel
> 官网：[https://sentinelguard.io/zh-cn/](https://sentinelguard.io/zh-cn/)   
* <u>注意：在Sentinel中配置规则时 要使用@SentinelResource配置的资源名而不是@GetMapping的资源名，前者blockHandler是在注解里面自己配置的，后者一直是默认的`Blocked by Sentinel (flow limiting)`</u>

## 流控规则
* 直接、关联、链路等等方面
* 在请求接口到达一定QPS的时候  进行流量控制（快速失败、Warm Up、排队等待 等等）

## 降级规则
* RT、异常比例、异常数等等方面
* 在请求接口到达一定等待时间、或者达到一定的异常比例或异常数量  进行降级控制（服务直接返回降级提示）

## 热点规则
* 针对指定接口的指定参数 进行QPS控制，参数例外项（针对指定参数值进行QPS控制）

## 系统规则
* LOAD、RT、线程数、入口 QPS、CPU 使用率等等方面
* 从整体维度对应用入口流量进行控制

## SentinelResource配置
* 全局blockHandler（自定义全局限流处理类 使其与业务逻辑分离）
```
    @RequestMapping("/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException2")
    public CommonResult customerBlockHandler()
    {
        return CommonResult.success(new Payment(1l,"2021","serial002"));
    }
    
    
```
```
public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException exception)
    {
        return CommonResult.fail("自定义全局限流处理类,global handlerException----1");
    }
    public static CommonResult handlerException2(BlockException exception)
    {
        return CommonResult.fail("自定义全局限流处理类,global handlerException----2");
    }
}
```
* blockHandler：负责处理流控、服务降级等Sentinel制造的配置违规
```
    @RequestMapping("/consumer/fallback/{id}")
    @SentinelResource(value = "fallback",blockHandler = "blockHandler")
    public CommonResult<Payment> fallback(@PathVariable Long id)
    {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/"+id, CommonResult.class,id);
        if (id == 4) {
            throw new IllegalArgumentException ("IllegalArgumentException,非法参数异常....");
        }else if (result.getData() == null) {
            throw new NullPointerException ("NullPointerException,该ID没有对应记录,空指针异常");
        }
        return result;
    }
    
    //本例是blockHandler
    public CommonResult blockHandler(@PathVariable  Long id, BlockException blockException) {
        Payment payment = new Payment(id,"null","");
        return new CommonResult<>(445,"blockHandler-sentinel限流,无此流水: blockException  "+blockException.getMessage(),payment);
    }
```
* fallback：负责处理程序抛出业务异常（相当于服务降级）
```
    @RequestMapping("/consumer/fallback/{id}")
    @SentinelResource(value = "fallback",fallback = "handlerFallback",exceptionsToIgnore = {NullPointerException.class})
    public CommonResult<Payment> fallback(@PathVariable Long id)
    {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/"+id, CommonResult.class,id);
        if (id == 4) {
            throw new IllegalArgumentException ("IllegalArgumentException,非法参数异常....");
        }else if (result.getData() == null) {
            throw new NullPointerException ("NullPointerException,该ID没有对应记录,空指针异常");
        }
        return result;
    }

    //本例是fallback
    public CommonResult handlerFallback(@PathVariable  Long id,Throwable e) {
        Payment payment = new Payment(id,"null","");
        return new CommonResult<>(444,"兜底异常handlerFallback,exception内容  "+e.getMessage(),payment);
    }
```

## Sentinel持久化
* 需要将sentinel的配置写到nacos中 即实现了持久化
* 1.yml配置调整 增加nacos数据源配置
```
spring:
  cloud:
    sentinel:
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            dataid: cloudalibaba-sentinel-service   #服务名称
            groupid: DEFAULT_GROUP
            data-type: json
            rule-type: flow
```
* 2.在nacos的配置列表中增加如下配置
```
Data ID：前面的服务名称cloudalibaba-sentinel-service
配置格式：json

[
    {
         "resource": "/testA",
         "limitApp": "default",
         "grade":   1,
         "count":   1,
         "strategy": 0,
         "controlBehavior": 0,
         "clusterMode": false    
    }
]
```
* 参数解析
```
resource：资源名称
limitApp：来源应用
grade：阈值类型 0线程数 1QPS
count：单机阈值
strategy：流控模式 0直接 1关联 2链路
controlBehavior：流控效果 0快速失败 1Warm Up 2排队等待
clusterMode：是否集群
```
