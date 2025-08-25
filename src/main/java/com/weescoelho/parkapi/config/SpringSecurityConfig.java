package com.weescoelho.parkapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.weescoelho.parkapi.jwt.JwtAuthFilter;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csfr -> csfr.disable()) // Desabilitar para aplicação stateless
        .formLogin(form -> form.disable()) // Desabilitar o login via formulário
        .httpBasic(httpBasic -> httpBasic.disable()) // Desabilitar o login via HTTP
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "api/v1/users").permitAll() // Permitir acesso a todos os endpoints de
            .requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll()
            .anyRequest().authenticated() // Exigir autenticação para qualquer outra requisição
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configurar a aplicação como stateless
        )
        .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public JwtAuthFilter jwtAuthFilter() {
    return new JwtAuthFilter();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
