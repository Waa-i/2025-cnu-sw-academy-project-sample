package edu.cnu.swacademy.exchange.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/hello-world")
@RestController
public class TestController {

  @GetMapping
  public String helloWorld() {
    log.info("[Exchange-Server -> Security-Server] Hello World!");
    return "Hello World!";
  }
}
