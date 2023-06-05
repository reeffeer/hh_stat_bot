package com.example.demo_hh_stat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {

    @Bean
    public Environment environment() {
        return new StandardEnvironment();
    }
}
