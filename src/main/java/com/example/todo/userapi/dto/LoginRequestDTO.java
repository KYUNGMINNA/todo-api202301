package com.example.todo.userapi.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
@Builder
public class LoginRequestDTO {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8,max = 20)
    private String password;
}
