package io.imwj.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;

/**
 * @author langao_q
 * @since 2021-10-12 11:15
 */
public class MySelfRule {

    @Bean
    public IRule myRule()
    {
        return new RoundRobinRule();//轮询算法
        //return new RandomRule();//随机算法
    }

}
