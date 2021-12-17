package io.imwj.springcloud.controller;

import io.imwj.springcloud.entities.CommonResult;
import io.imwj.springcloud.entities.Order;
import io.imwj.springcloud.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author langao_q
 * @since 2021-12-17 11:14
 */
@Slf4j
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;


    @GetMapping("/order/create")
    public CommonResult create(Order order)
    {
        orderService.create(order);
        return CommonResult.success("订单创建成功");
    }

}
