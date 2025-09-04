package edu.cnu.swacademy.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequestMapping("/api/v1/hello-world")
@RestController
public class TestController {

  private final RestTemplate restTemplate = new RestTemplate();

  private final String exchangeServerHost;
  private final String exchangeServerPort;

  public TestController(
      @Value("${exchange-server.host}") String host,
      @Value("${exchange-server.port}") String port) {
    this.exchangeServerHost = host;
    this.exchangeServerPort = port;
  }

  @GetMapping
  public String helloWorld() {
    log.info("[Security-Server -> Exchange-Server] Hello World!");
    String url = String.format("http://%s:%s/api/v1/hello-world", exchangeServerHost, exchangeServerPort);
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    return response.getBody();
  }
}
