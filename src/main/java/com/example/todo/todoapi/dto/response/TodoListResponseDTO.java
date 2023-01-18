package com.example.todo.todoapi.dto.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoListResponseDTO {

    private String error; //에러 발생시 클라이언트에게 전달할 메시지 : 서버에서 따로 전달할 메시지
    private List<TodoDetailResponseDTO> todos;

}
