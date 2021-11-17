package io.imwj.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GateWay代码配置替换yml中的配置
 * @author langao_q
 * @since 2021-11-17 18:00
 */
@Configuration
public class GateWayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("baidu", r -> r.path("/guonei")
                        .uri("http://news.baidu.com/guonei"))
                .route("baidu2",r -> r.path("/guoji")
                        .uri("http://news.baidu.com/guoji"))
                .build();
    }
}
