package io.imwj.springcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author langao_q
 * @since 2021-10-13 16:01
 */
@Configuration
public class FeignConfig {

    /**
     * NONE: 默认的，不显示任何日志
     * BASIC：仅记录请求方法、URL、响应状态码以及执行时间
     * HEADERS：除了BASIC中定义的信息以外，还有请求和响应的头信息
     * FULL： 除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
