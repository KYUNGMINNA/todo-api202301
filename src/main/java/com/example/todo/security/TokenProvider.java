package com.example.todo.security;

import com.example.todo.userapi.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

//토큰을 발급하고 ,서명위조를 확인해주는 객체
@Service
@Slf4j
public class TokenProvider {

    //토큰 서명에 사용할  불변성을 가진 비밀 키 (512바이트 이상의 랜덤 문자열로 구성)
    private static final String SECRET_KEY="Q4NSl604sgyHJj1qwEkRasdUeR4uUAt7WJraD7EN3O9DVccyYuHxMEbuuuXXyYJkal13eqgB0F7Bq4H";

    //토큰 발급 메서드
    public String createToken(UserEntity userEntity){
        //만료 시간 설정
        Date expireDate=Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );

        //토큰 생성
        return Jwts.builder()
                //header에 들어갈 서명
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                        SignatureAlgorithm.HS512)
                .setSubject(userEntity.getId())  //sub :토큰 식별자(유일값 -중복없는값) :보통 로그인 회원 식별 코드
                .setIssuer("todo app")//iss :발급자 정보 --보통 서비스 이름 ex)naver ,kakao..
                .setIssuedAt(new Date()) // iat :토큰 발급 시간
                .setExpiration(expireDate) //exp :토큰 만료 시간
                .compact();
        }
}
