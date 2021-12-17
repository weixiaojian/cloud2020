package io.imwj.springcloud.service;

import io.imwj.springcloud.entities.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author langao_q
 * @since 2021-12-17 11:09
 */
@FeignClient(value = "seata-storage-service")
public interface StorageService {

    @RequestMapping(value = "/storage/decrease")
    CommonResult decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
