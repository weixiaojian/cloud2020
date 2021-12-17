package io.imwj.springcloud.service;

import io.imwj.springcloud.entities.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author langao_q
 * @since 2021-12-17 11:10
 */
@FeignClient(value = "seata-account-service")
public interface AccountService {

    @RequestMapping(value = "/account/decrease")
    CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
