package com.example.todo.config;

import com.example.todo.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

//@Configuration // 설정 파일로 등록하겠다
@EnableWebSecurity //스프링 부트가 지원하는 자동 설정 지원함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    //패스워드 인코딩 클래스를 등록
    // <bean id=? class=? / >
    // <bean id="encoder" class="PasswordEncoder" />로 된다

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }


    //시큐리티 설정 -- 2.6 버전부터 빈 등록 방식으로 바뀜
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //시큐리티 빌더
        http.cors()    //크로스 오리진 정책
                .and()
                .csrf() //CSRF 정책
                .disable() //사용안함 --> 스프링 시큐리티가 만든 cors,csrf 정책을 사용안하겠다
                .httpBasic().disable()// 기본 시큐리티 인증 해제 --> 이 프로젝트에선 토큰인증을 사용할거라서

                //세션 기반 인증 안함 --> 이 프로젝트에서 토큰 인증 사용하기 때문
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //인증 요청중에서 '/' 경로랑 '/api/auth'로 시작하는 경로는 인증하지 않고 모두 허용
                .authorizeRequests().antMatchers("/","/api/auth/**").permitAll()
                //그 외의 모든 경로는 인증을 거쳐야 함
                .anyRequest().authenticated();


        //토큰 인증 필터 등록  --토큰 필터는 일반적으로 CorsFilter 앞에 붙임
        http.addFilterAfter(
                jwtAuthFilter
                , CorsFilter.class //Spring의 Cors FIlter !!! -- import 주의

        );
        return http.build();
    }



}
