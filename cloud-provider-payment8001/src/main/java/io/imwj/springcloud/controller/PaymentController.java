package io.imwj.springcloud.controller;

import entities.CommonResult;
import entities.Payment;
import io.imwj.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author langao_q
 * @since 2021-08-27 16:56
 */
@Slf4j
@RestController
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("********插入结果：{}", result);
        if(result > 0){
            return CommonResult.success(result);
        }else {
            return CommonResult.fail("操作失败");
        }
    }

    @GetMapping("/payment/getPaymentById/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("********查询结果：{}", payment);
        if(payment != null){
            return CommonResult.success(payment);
        }else {
            return CommonResult.fail("操作失败");
        }
    }

}
