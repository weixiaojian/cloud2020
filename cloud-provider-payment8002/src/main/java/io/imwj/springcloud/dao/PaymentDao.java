package io.imwj.springcloud.dao;

import io.imwj.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author langao_q
 * @since 2021-08-27 16:44
 */
@Mapper
public interface PaymentDao {

    /**
     * 添加数据
     * @param payment
     * @return
     */
    public int create(Payment payment);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    public Payment getPaymentById(@Param("id") Long id);

}
