package com.example.todo.todoapi.controller;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.service.TodoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/api/todos")
@RequiredArgsConstructor
//CORS 허용 설정
//@CrossOrigin  나중에 배포 할 때에도 클라이언트 앱의 URL을 적어야 함 !! -- @CrossOrigin(origins="")으로 특정 URL만 가능
@CrossOrigin
public class TodoApiController {
    private final TodoService todoService;

    //할 일 등록 요청
    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId,@Validated @RequestBody TodoCreateRequestDTO requestDTO
            , BindingResult result){ //검증 에러 받음

        if (result.hasErrors()){
            log.warn("DTO 검증 에러 발생 : {}",result.getFieldError());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            TodoListResponseDTO responseDTO = todoService.create(requestDTO,userId);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }
    }

    //할 일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,@PathVariable("id") String todoId){
        log.info("/api/todos/{} DELETE request! ",todoId);
        if (todoId ==null || todoId.equals("")){
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO.builder().error("ID를 전달해 주세요"));
        }
        try {
            TodoListResponseDTO responseDTO = todoService.delete(todoId,userId);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }
    }


    // 할 일 목록요청 (GET)
    @GetMapping                                //인증완료처리시 등록했던 값을 넣어줌 : 이프로젝트에서는 Filter에서 이 작업을 진행했음
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
        log.info("/api/todos GET request!");

        TodoListResponseDTO responseDTO = todoService.retrieve(userId);

        return ResponseEntity.ok().body(responseDTO);
    }

    @RequestMapping(
            value = "/{id}"
            , method = {RequestMethod.PUT, RequestMethod.PATCH} //put, patch 둘다 허용 -->일부만 수정하더라도 수정하지 않는 기존의 값도 보내야 함
    )
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,
            @PathVariable("id") String todoId
            , @Validated @RequestBody TodoModifyRequestDTO requestDTO
            , BindingResult result
            , HttpServletRequest request //put인지 patch인지 알아 보기 위해
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }

        log.info("/api/todos/{} {} request", todoId, request.getMethod());
        log.info("modifying dto : {}", requestDTO);

        try {
            TodoListResponseDTO responseDTO = todoService.update(todoId, requestDTO,userId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }
    }



}
