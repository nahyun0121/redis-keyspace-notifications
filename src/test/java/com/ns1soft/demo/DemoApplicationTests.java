package com.ns1soft.demo;

import com.ns1soft.demo.service.RestaurantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private RedisMessageListenerContainer redisMessageListenerContainer;

	// CountDownLatch: 한 스레드가 다른 스레드에서 작업이 완료될 때까지 기다릴 수 있도록 해주는 클래스
	private CountDownLatch latch;


	@BeforeEach
	public void setUp() {
		// 메인 스레드에서 1개의 스레드를 생성함
		latch = new CountDownLatch(1);
		// 테스트용 리스너를 추가하여 latch.countDown()을 호출. (원래 리스너도 동작하므로 실행 성공 시 콘솔창에 메시지 뜸)
		MessageListener testMessageListener = (message, pattern) -> {
			latch.countDown(); // 메시지 수신 시 latch 카운트 감소
		};
		redisMessageListenerContainer.addMessageListener(testMessageListener, new PatternTopic("__keyevent@0__:hset"));
	}

	@AfterEach
	public void tearDown() {
		// 리스너 해제는 RedisMessageListenerContainer가 관리하므로 별도의 해제 로직  필요 X
		// 필요에 따라 리소스 정리 로직 추가하기
	}

	@Test
	public void testSaveOrUpdateRestaurant() throws InterruptedException {

		// 식당 정보 업데이트
		String restaurantId = "1";
		Map<String, String> restaurantInfo = new HashMap<>();
		restaurantInfo.put("name", "오하라식당교토");
		restaurantInfo.put("location", "논현동");
		restaurantInfo.put("phone", "02-543-6779");
		restaurantService.saveOrUpdateRestaurant(restaurantId, restaurantInfo);

		// 메시지 수신 대기(latch의 숫자가 0이 될 때까지 기다림)
		boolean completed = latch.await(1, TimeUnit.SECONDS);

		// 메시지를 성공적으로 수신했을 때만 테스트 통과
		assertThat(completed).isTrue();

		// 레디스에 맛집 정보가 잘 저장되었는지 확인
		Map<Object, Object> storedRestaurantInfo = redisTemplate.opsForHash().entries("Restaurant:" + restaurantId);
		assertThat(storedRestaurantInfo).isNotNull();
		assertThat(storedRestaurantInfo.get("name")).isEqualTo("오하라식당교토");
		assertThat(storedRestaurantInfo.get("location")).isEqualTo("논현동");
		assertThat(storedRestaurantInfo.get("phone")).isEqualTo("02-543-6779");
	}

}
