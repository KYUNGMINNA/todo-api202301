package com.example.todo.todoapi.repository;

import com.example.todo.todoapi.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity,String> {

    //완료되지 않은 할 일들만 조회
    @Query("select t from TodoEntity t where t.done=0") //flase =0 ,true=1
    List<TodoEntity> findNotYetTodos();

    //특정 회원의 할일 목록 조회
    //@Query("select t from TodoEntity t where t.user=:userId")
    //List<TodoEntity> findByUser(@Param("userId") String userId);
    List<TodoEntity> findByUserId(String userId);



}
