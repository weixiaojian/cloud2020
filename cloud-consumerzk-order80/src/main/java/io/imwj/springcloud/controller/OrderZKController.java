package io.imwj.springcloud.controller;

import io.imwj.springcloud.entities.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author langao_q
 * @since 2021-09-09 17:49
 */
@RestController
public class OrderZKController {

    public static final String INVOME_URL = "http://cloud-provider-payment";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/zk")
    public CommonResult payment (){
        String result = restTemplate.getForObject(INVOME_URL+"/payment/zk",String.class);
        return CommonResult.success(result);
    }
}
