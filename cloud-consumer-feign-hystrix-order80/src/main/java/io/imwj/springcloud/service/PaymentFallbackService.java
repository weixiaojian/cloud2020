package io.imwj.springcloud.service;

import org.springframework.stereotype.Service;

/**
 * @author langao_q
 * @since 2021-10-15 11:18
 */
@Service
public class PaymentFallbackService implements PaymentService{
    @Override
    public String paymentInfo_OK(String id) {
        return "-----PaymentFallbackService fall back-paymentInfo_OK , (┬＿┬)";
    }

    @Override
    public String paymentInfo_TimeOut(String id) {
        return "-----PaymentFallbackService fall back-paymentInfo_OK , (┬＿┬)";
    }
}
