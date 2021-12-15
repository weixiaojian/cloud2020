package io.imwj.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.imwj.springcloud.entities.CommonResult;
import io.imwj.springcloud.entities.Payment;
import io.imwj.springcloud.handler.CustomerBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author langao_q
 * @since 2021-12-15 15:10
 */
@RestController
public class RateLimitController {

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handleException")
    public CommonResult byResource()
    {
        return CommonResult.success( new Payment(1l,"2021", "serial001"));
    }
    public CommonResult handleException(BlockException exception)
    {
        return CommonResult.fail(exception.getClass().getCanonicalName()+" 服务不可用!");
    }

    @GetMapping("/byUrl")
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl()
    {
        return new CommonResult(200,"按url限流测试OK",new Payment(1l,"2021","serial002"));
    }

    @RequestMapping("/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException2")
    public CommonResult customerBlockHandler()
    {
        return CommonResult.success(new Payment(1l,"2021","serial002"));
    }
}
