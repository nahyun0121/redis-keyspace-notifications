package com.ns1soft.demo.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;


@Service
// MessageListener Interface를 상속하는 클래스를 통해 이벤트 알림을 수신함
public class MessageSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Message received: " + message.toString());
    }
}
