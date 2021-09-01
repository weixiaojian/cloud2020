package io.imwj.springcloud.service;

import io.imwj.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * @author langao_q
 * @since 2021-08-27 16:54
 */
public interface PaymentService {

    public int create(Payment payment); //写

    public Payment getPaymentById(@Param("id") Long id);  //读取

}
