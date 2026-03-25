package br.com.xchange.api.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.xchange.api.infra.security.XchangeAuthenticationFilter;

@Profile("dev")
@EnableWebSecurity
@Configuration
public class SecurityConfigDev {
  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    XchangeAuthenticationFilter xchangeAuthenticationFilter
  ) {
    return http
      .httpBasic(basic -> basic.disable())
      .csrf(csrf -> csrf.disable())
      .formLogin(form -> form.disable())
      .addFilterBefore(xchangeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
    return config.getAuthenticationManager();
  }

  @Bean
  public XchangeAuthenticationFilter xchangeAuthenticationFilter(AuthenticationManager authenticationManager) {
    XchangeAuthenticationFilter filter = new XchangeAuthenticationFilter(authenticationManager);

    filter.setUsernameParameter("email");
    filter.setPasswordParameter("password");
    filter.setFilterProcessesUrl("/auth/login");

    return filter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }
}
