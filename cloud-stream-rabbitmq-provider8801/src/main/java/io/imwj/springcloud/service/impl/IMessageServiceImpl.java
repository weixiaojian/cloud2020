package io.imwj.springcloud.service.impl;

import io.imwj.springcloud.service.IMessageService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author langao_q
 * @since 2021-12-07 16:02
 */
@EnableBinding(Source.class) //标识消息的推送管道
public class IMessageServiceImpl implements IMessageService {

    @Resource
    private MessageChannel output;

    @Override
    public String send() {
        String uuid = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(uuid).build());
        System.out.println("===========UUID：" + uuid);
        return uuid;
    }
}
