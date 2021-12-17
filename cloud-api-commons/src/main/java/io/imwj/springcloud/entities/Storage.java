package io.imwj.springcloud.entities;

import lombok.Data;

/**
 * @author langao_q
 * @since 2021-12-17 11:47
 */

@Data
public class Storage {

    private Long id;

    // 产品id
    private Long productId;

    //总库存
    private Integer total;


    //已用库存
    private Integer used;


    //剩余库存
    private Integer residue;
}