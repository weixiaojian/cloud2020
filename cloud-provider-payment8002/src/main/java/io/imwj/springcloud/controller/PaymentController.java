package io.imwj.springcloud.controller;

import io.imwj.springcloud.entities.CommonResult;
import io.imwj.springcloud.entities.Payment;
import io.imwj.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author langao_q
 * @since 2021-08-27 16:56
 */
@Slf4j
@RestController
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("****{}****查询结果：{}", serverPort, result);
        if (result > 0) {
            return CommonResult.success(result);
        } else {
            return CommonResult.fail("操作失败");
        }
    }

    @GetMapping("/payment/getPaymentById/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        payment.setProt(serverPort);
        log.info("****{}****查询结果：{}", serverPort, payment);
        if (payment != null) {
            return CommonResult.success(payment);
        } else {
            return CommonResult.fail("操作失败");
        }
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverPort;
    }
}
