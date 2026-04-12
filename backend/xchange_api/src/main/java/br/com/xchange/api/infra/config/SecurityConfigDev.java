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

import br.com.xchange.api.domain.ports.services.AccessTokenServicePort;
import br.com.xchange.api.infra.filters.AccessTokenFilter;
import br.com.xchange.api.infra.filters.XchangeAuthenticationFilter;
import br.com.xchange.api.infra.handlers.XchangeAccessDeniedHandler;
import br.com.xchange.api.infra.handlers.XchangeAuthenticationEntryPoint;
import br.com.xchange.api.infra.handlers.XchangeAuthenticationFailureHandler;

@Profile("dev")
@EnableWebSecurity
@Configuration
public class SecurityConfigDev {
  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    XchangeAuthenticationFilter xchangeAuthenticationFilter,
    AccessTokenFilter accessTokenFilter,
    XchangeAuthenticationEntryPoint restAuthenticationEntryPoint,
    XchangeAccessDeniedHandler restAccessDeniedHandler
  ) throws Exception {
    return http
      .httpBasic(basic -> basic.disable())
      .csrf(csrf -> csrf.disable())
      .formLogin(form -> form.disable())
      .exceptionHandling(exceptions -> exceptions
        .authenticationEntryPoint(restAuthenticationEntryPoint)
        .accessDeniedHandler(restAccessDeniedHandler)
      )
      .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterAt(xchangeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/profile/**").authenticated()
        .anyRequest().permitAll())
      .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
    return config.getAuthenticationManager();
  }

  @Bean
  public XchangeAuthenticationFilter xchangeAuthenticationFilter(
    AuthenticationManager authenticationManager,
    XchangeAuthenticationFailureHandler xchangeAuthenticationFailureHandler,
    AccessTokenServicePort accessTokenServicePort
  ) {
    XchangeAuthenticationFilter filter = new XchangeAuthenticationFilter(authenticationManager, accessTokenServicePort);

    filter.setUsernameParameter("email");
    filter.setPasswordParameter("password");
    filter.setFilterProcessesUrl("/auth/login");
    filter.setAuthenticationFailureHandler(xchangeAuthenticationFailureHandler);

    return filter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }
}
