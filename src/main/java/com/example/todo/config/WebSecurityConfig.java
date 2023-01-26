package com.example.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration // 설정 파일로 등록하겠다
@EnableWebSecurity //스프링 부트가 지원하는 자동 설정 지원함
public class WebSecurityConfig {

    //패스워드 인코딩 클래스를 등록
    // <bean id=? class=? / >
    // <bean id="encoder" class="PasswordEncoder" />로 된다

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
