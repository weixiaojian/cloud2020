package io.imwj.springcloud.service;

import io.imwj.springcloud.entities.Order;

/**
 * @author langao_q
 * @since 2021-12-17 11:02
 */
public interface OrderService {

    void create(Order order);
}
