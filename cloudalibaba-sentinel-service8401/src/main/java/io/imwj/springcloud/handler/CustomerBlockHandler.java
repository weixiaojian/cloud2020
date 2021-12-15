package io.imwj.springcloud.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.imwj.springcloud.entities.CommonResult;

/**
 * 自定义全局限流处理类
 * @author langao_q
 * @since 2021-12-15 15:34
 */
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
