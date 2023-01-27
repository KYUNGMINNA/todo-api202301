package com.example.todo.security;

//클라이언트에 헤더에 담아서 보낸토큰을 검사하는 필터

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component //무슨 기능인지 애매하면 Component라고 붙인다.
@Slf4j
@RequiredArgsConstructor           //요청 당 한 번씩 작동하는 필터
public class JwtAuthFilter extends OncePerRequestFilter {
    private final TokenProvider provider;

    //필터의 할 일
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            //요청 헤더에서 토큰 가져오기
            String token=parseBearerToken(request);
            log.info("Jwt Token Filter is running.... - token : {}",token);

            //토큰 위조 여부 검사
            if (token !=null){
                String userId = provider.validateANdGetUserId(token);
                log.info("인증된 userId :{}",userId);


                //시큐리티 정보 생성
                // 인증 완료!! api서버에서는 SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, // 인증 정보 값(다른 것도 가능) :: 컨트롤러의 @AuthenticationPrincipal 값 -->컨트롤러에 이 어노테이션 붙이면 인증 정보 값을 보여줌
                        null, // 권한 등록
                        AuthorityUtils.NO_AUTHORITIES
                );
                //시큐리티 컨텍스트에 등록하는 과정
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);

                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("인증되지 않은 사용자입니다.");
        }

        //필터 체인에 내가 만든 커스텀필터를 실행시킴  :: 그냥 실행시키는것!!! --> 연결 과정은 설정이 필요함
        filterChain.doFilter(request,response);




    }

    // Authorization: <type> <credentials> 의 구조 인데 , jWT는 type을 Bearer을 사용 함 !
    private String parseBearerToken(HttpServletRequest request){
        //요청 헤더에서 토큰을 읽어온다
        //순수한 토큰값이 아니라 앞에 Beare 가 붙어있으므로 제거해야 된다.
        String bearerToken= request.getHeader("Authorization");

        //Spring Utils 사용
        if (StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }else{
            return null;
        }


    }
}
