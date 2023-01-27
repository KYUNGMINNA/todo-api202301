package com.example.todo.userapi.dto;

import com.example.todo.userapi.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
@Builder
public class LoginResponseDTO {
    private String email;
    private String userName;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDate joinDate;

    private String token; //인증 토큰(로그인 성공하면 토큰을 발급하여 전달)

    private String message; //응답 메시지
    
    
    
    
    //엔터티를 DTO로 변경
    public LoginResponseDTO(UserEntity userEntity,String token){
        this.email=userEntity.getEmail();
        this.userName=userEntity.getUserName();
        this.joinDate=LocalDate.from(userEntity.getJoinDate());
        this.token=token;
    }
}
