package io.imwj.springcloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author langao_q
 * @since 2021-12-17 11:20
 */
@Configuration
@MapperScan({"io.imwj.springcloud.dao"})
public class MyBatisConfig {

}