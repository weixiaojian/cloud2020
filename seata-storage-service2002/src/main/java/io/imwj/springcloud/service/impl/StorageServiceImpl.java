package io.imwj.springcloud.service.impl;

import io.imwj.springcloud.dao.StorageDao;
import io.imwj.springcloud.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author langao_q
 * @since 2021-12-17 11:50
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageDao storageDao;


    // 扣减库存
    @Override
    public void decrease(Long productId, Integer count) {
        //int i = 1/0;
        log.info("------->storage-service中扣减库存开始");
        storageDao.decrease(productId,count);
        log.info("------->storage-service中扣减库存结束");
    }
}
