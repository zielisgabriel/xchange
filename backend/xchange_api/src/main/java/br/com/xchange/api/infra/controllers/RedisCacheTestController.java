package br.com.xchange.api.infra.controllers;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisCacheTestController {
  @GetMapping("/cache")
  @Cacheable(value = "test")
  public String cache() {
    return LocalDateTime.now().toString();
  }
}
