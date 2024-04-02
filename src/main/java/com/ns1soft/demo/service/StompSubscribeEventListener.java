package com.ns1soft.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

// 웹소켓 구독이 잘 되었는지 확인하는 이벤트 리스너
@Component
public class StompSubscribeEventListener  implements ApplicationListener<SessionSubscribeEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StompSubscribeEventListener.class);
    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        logger.info("구독 성공!!! : " + event.getMessage());
    }
}
