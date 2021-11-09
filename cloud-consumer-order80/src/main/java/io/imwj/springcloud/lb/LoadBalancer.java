package io.imwj.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author langao_q
 * @since 2021-10-12 11:26
 */
public interface LoadBalancer {

    /**
     * 得到当前需要提供服务的服务对象
     * 收集服务器总共有多少台能够提供服务的机器，并放到list里面
     *
     * @param serviceInstances
     * @return
     */
    ServiceInstance instances(List<ServiceInstance> serviceInstances);
}
