package com.ns1soft.demo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RestaurantService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    HashOperations<String, String, Object> hashOperations;

    // @PostConstruct: 빈(여기선 @Service)의 초기화가 완료되고, 모든 의존성 주입이 이루어진 후 자동으로 한 번만 호출되어 초기화를 수행함
    @PostConstruct
    private void initializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void saveOrUpdateRestaurant(String id, Map<String, Object> restaurantInfo) {
        hashOperations.putAll("Restaurant:" + id, restaurantInfo);
    }

    public Map<String, Object> getRestaurantInfo(String id) {
        return hashOperations.entries("Restaurant:" + id);
    }
}