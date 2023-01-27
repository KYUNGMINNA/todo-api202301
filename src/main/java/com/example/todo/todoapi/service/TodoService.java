package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.TodoEntity;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    //할 일 목록 조회
    @Transactional
    public TodoListResponseDTO retrieve(String userId){
        List<TodoEntity> entityList = todoRepository.findByUserId(userId);

        List<TodoDetailResponseDTO> dtoList = entityList.stream()
                                    .map(te -> new TodoDetailResponseDTO(te))
                                    .collect(Collectors.toList());

        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }
    //할 일 등록
    public TodoListResponseDTO create(final TodoCreateRequestDTO createRequestDTO,final String userId) throws RuntimeException{


        TodoEntity todo = createRequestDTO.toEntity();
        // todo.setUserId(userRepository.findBYId(userId)) --TodoCreateRequestDTO 의 필드는 title만 있는데 ,
        // 실제 save할 때는 Entity형태로 있어서 User에 값을 채워야하는데 ,JPA 사용하려면 Reposotiry를 호출해야
        // 하는 일이 발생함 --> 최적화 이슈가 발생하게 됨
        todo.setUserId(userId);


        todoRepository.save(todo);






        log.info("할 일이 저장되었씁니다. 제목 : {} ",createRequestDTO.getTitle());

        return retrieve(userId);
    }


    //할 일 수정 ( 제목 , 할 일 완료 여부)
    public TodoListResponseDTO update(final String id,final TodoModifyRequestDTO modifyRequestDTO,String userId) throws RuntimeException{
        Optional<TodoEntity> targetEntity = todoRepository.findById(id);
        targetEntity.ifPresent(entity -> {
            entity.setTitle(modifyRequestDTO.getTitle());
            entity.setDone(modifyRequestDTO.isDone());
            todoRepository.save(entity);
        });
        return retrieve(userId);
    }

    //할 일 삭제
    public TodoListResponseDTO delete(final String id,String userId){
        try {
            todoRepository.deleteById(id);
        } catch (Exception e) {
            log.error("id가 존재하지 않아 삭제에 실패하였습니다.- ID : {} ,err : {}"
                    ,id,e.getMessage());
            throw new RuntimeException("id가 존재하지 않아 삭제에 실패하였습니다.");
        }
        return retrieve(userId);

    }



}
