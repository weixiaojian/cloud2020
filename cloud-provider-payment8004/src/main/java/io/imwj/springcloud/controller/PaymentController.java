package io.imwj.springcloud.controller;

import io.imwj.springcloud.entities.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author langao_q
 * @since 2021-09-09 17:20
 */
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/payment/zk")
    public CommonResult paymentzk(){
        return CommonResult.success("springcloud with zookeeper:"+serverPort+"\t"+ UUID.randomUUID().toString());
    }

}

