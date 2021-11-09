package io.imwj.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author langao_q
 * @since 2021-08-31 18:08
 */
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced  //  负载均衡
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
