package io.imwj.springcloud.controller;

import io.imwj.springcloud.entities.CommonResult;
import io.imwj.springcloud.service.StorageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author langao_q
 * @since 2021-12-17 11:51
 */
@RestController
public class StorageController {

    @Resource
    private StorageService storageService;


    //扣减库存
    @RequestMapping("/storage/decrease")
    public CommonResult decrease(Long productId, Integer count) {
        storageService.decrease(productId, count);
        return CommonResult.success("扣减库存成功！");
    }
}
