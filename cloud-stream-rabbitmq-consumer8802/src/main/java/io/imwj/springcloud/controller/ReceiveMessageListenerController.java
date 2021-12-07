package io.imwj.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author langao_q
 * @since 2021-12-07 16:37
 */
@Component
@EnableBinding(Sink.class)//标识消息的接收管道
public class ReceiveMessageListenerController {

    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> msgStr){
        System.out.println("===原始报文：" + msgStr);
        System.out.println("===serverPort："+ serverPort +" ===消费者接收到数据===" + msgStr.getPayload());
    }
}
