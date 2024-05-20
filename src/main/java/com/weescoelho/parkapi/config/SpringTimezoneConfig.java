package com.weescoelho.parkapi.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class SpringTimezoneConfig {
  @PostConstruct // <=-- Define que este método será o primeiro a ser executado após a
                 // instanciação da classe
  public void timezoneConfig() {
    TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
  }
}
