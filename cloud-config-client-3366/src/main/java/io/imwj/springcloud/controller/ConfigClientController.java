package io.imwj.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author langao_q
 * @since 2021-11-19 11:43
 */
@RefreshScope
@RestController
public class ConfigClientController {


    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo(){

        return configInfo;
    }

}
