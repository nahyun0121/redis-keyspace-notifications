package com.ns1soft.demo.config;

import com.ns1soft.demo.service.MessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // Spring에서 제공하는 Redis와의 상호작용을 위한 템플릿 클래스
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // Redis 연결을 위한 커넥션 팩토리 설정
        template.setConnectionFactory(connectionFactory);
        // serializer 설정으로 redis-cli를 통해 직접 데이터를 조회할 수 있도록 함
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }

    // Redis에서 발행된 메시지를 구독하기 위한 컨테이너
    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 'hset' 명령이 발생할 때마다 알림을 받음
        container.addMessageListener(listenerAdapter, new PatternTopic("__keyevent@0__:hset"));
        return container;
    }

    // 실제 메시지 처리 로직을 포함한 컴포넌트
    @Bean
    MessageListenerAdapter listenerAdapter(MessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

}
