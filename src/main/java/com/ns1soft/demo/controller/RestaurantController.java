package com.ns1soft.demo.controller;

import com.ns1soft.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdateRestaurant(@RequestParam String restaurantId, @RequestBody Map<String, String> restaurantInfo) {
        restaurantService.saveOrUpdateRestaurant(restaurantId, restaurantInfo);
        return "식당 정보 저장/업데이트 됨 !!!";
    }

}
