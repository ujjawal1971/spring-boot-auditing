package com.example.spring_boot_project_demo.prod_ready_features.config;

import com.example.spring_boot_project_demo.prod_ready_features.auth.AuditorAwareImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "getauditorAwareImpl")
public class AppConfig {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    AuditorAware<String> getauditorAwareImpl() {
        return new AuditorAwareImpl();
    }
}
