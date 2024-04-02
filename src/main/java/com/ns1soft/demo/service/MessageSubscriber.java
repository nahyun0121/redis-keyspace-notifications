package com.ns1soft.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
// MessageListener Interface를 상속하는 클래스를 통해 이벤트 알림을 수신함
public class MessageSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final RestaurantService restaurantService;

    @Autowired
    public MessageSubscriber(SimpMessagingTemplate messagingTemplate, RestaurantService restaurantService) {
        this.messagingTemplate = messagingTemplate;
        this.restaurantService = restaurantService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // keyspace notification 확인
        String messageBody = message.toString();
        System.out.println("Message received: " + messageBody);

        // 변경된 식당 정보 가져와 websocket pub
        String restaurantId = getRestaurantId(messageBody);

        Map<String, Object> restaurantInfo = restaurantService.getRestaurantInfo(restaurantId);

        messagingTemplate.convertAndSend("/topic/public", restaurantInfo);
    }

    private String getRestaurantId(String message) {
        return (message != null && message.startsWith("Restaurant:")) ? message.substring("Restaurant:".length()) : null;
    }
}