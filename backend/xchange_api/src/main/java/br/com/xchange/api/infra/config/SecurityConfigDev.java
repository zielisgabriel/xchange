package br.com.xchange.api.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Profile("dev")
@EnableWebSecurity
@Configuration
public class SecurityConfigDev {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http
      .httpBasic(basic -> basic.disable())
      .csrf(csrf -> csrf.disable())
      .formLogin(form -> form.disable())
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .build();
  }
}
