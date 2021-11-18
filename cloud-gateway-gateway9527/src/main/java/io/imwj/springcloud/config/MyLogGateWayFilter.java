package io.imwj.springcloud.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * Gatway Filters全局过滤器
 * @author langao_q
 * @since 2021-11-18 15:04
 */
@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //日志记录
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        log.info("===请求参数["+new Date()+"]：" + queryParams.toString());
        //用户鉴权
        String username = request.getQueryParams().getFirst("username");
        if(StringUtils.isEmpty(username)){
            log.info("===用户名为Null 非法用户,(┬＿┬)");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);//给人家一个回应
            return exchange.getResponse().setComplete();
        }
        //放行
        return chain.filter(exchange);
    }

    /**
     * 顺序 0表示第一个过滤器
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
