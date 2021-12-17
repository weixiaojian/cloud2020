package io.imwj.springcloud.service;

/**
 * @author langao_q
 * @since 2021-12-17 11:50
 */
public interface StorageService {

    // 扣减库存
    void decrease(Long productId, Integer count);
}
