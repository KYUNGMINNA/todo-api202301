package com.example.todo.security;

import com.example.todo.userapi.entity.UserEntity;
import io.jsonwebtoken.Claims;
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

    /**
     *  클라이언트가 보낸 토큰을 디코딩 및 파싱해서 토큰의 위조여부 확인
     *
     * @param token - 클라이언트가 전송한 인코딩된토큰
     * @return - 토큰에서 subject(userId)를 꺼내서 반환
     */
    public String validateANdGetUserId(String token){

        Claims claims = Jwts.parserBuilder()
                //토큰 발급자의 발급당시 서명을 넣어줌
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                //parseClaimsJws - 토큰을 디코딩 -> 서명 기록 파싱
                //클라이언트 토큰의 서명과 서버발급당시 서명을 비교
                //위조되지 않았다면 body에 페이로드(=Claims)를 리턴
                //위조 되었으면 예외를 발생시킴
                .parseClaimsJws(token)
                .getBody();// Claims가 Body임 --> Claims에 토큰에 관한 정보 다 들어 있음

        return claims.getSubject();
    }





}
