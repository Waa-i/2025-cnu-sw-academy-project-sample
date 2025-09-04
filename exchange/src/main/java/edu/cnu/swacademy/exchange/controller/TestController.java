package edu.cnu.swacademy.exchange.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RequestMapping("/api/v1/hello-world")
@RestController
public class TestController {

  private final StringRedisTemplate stringRedisTemplate;

  public TestController(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  @GetMapping
  public String helloWorld() {
    log.info("[Exchange-Server -> Security-Server] Hello World!");
    return "Hello World!";
  }

  @PostMapping("/redis")
  public void saveHelloWorld() {
    stringRedisTemplate.opsForValue()
        .set("key", "hello world", Duration.ofHours(1));
  }

  @GetMapping("/redis")
  public String getHelloWorld() {
    String value = stringRedisTemplate.opsForValue()
        .get("key");
    log.info("Got value from redis: {}", value);
    return value;
  }
}
