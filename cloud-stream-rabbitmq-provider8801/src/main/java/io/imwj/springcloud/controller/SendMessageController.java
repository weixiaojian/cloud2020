package io.imwj.springcloud.controller;

import io.imwj.springcloud.service.IMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author langao_q
 * @since 2021-12-07 16:09
 */
@RestController
public class SendMessageController {

    @Resource
    private IMessageService messageService;


    @RequestMapping("send")
    public String send(){
        return messageService.send();
    }

}
