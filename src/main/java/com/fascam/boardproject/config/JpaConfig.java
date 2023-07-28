package com.fascam.boardproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing // Jpa Auditing 기능 활성화
@Configuration // 각종 설정을 잡는 bean
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("un"); // TODO: 스프링 시큐리티로 인증 기능 붙이게 될 때 수정
    }
}

/*
*   auditing이란? TODO: 찾아볼것 
* 
* 
*/


