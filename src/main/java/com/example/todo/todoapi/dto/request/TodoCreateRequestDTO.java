package com.example.todo.todoapi.dto.request;

import com.example.todo.todoapi.entity.TodoEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor  @EqualsAndHashCode     //@Data 있으면 한 번에 써주기는 하지만, 비추천
@Builder
public class TodoCreateRequestDTO {

    @NotBlank
    @Size(min = 2,max = 10)
    private String title;

    //DTO를 Entity로 변환
    public TodoEntity toEntity(){
       return TodoEntity.builder()
               .title(this.title)
               .build();
    }
}
