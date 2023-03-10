package com.example.todo.userapi.repository;

import com.example.todo.userapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,String> {

    //이메일로 회원 조회
    // select * from tbl_user where email=?
    UserEntity findByEmail(String email);

    //이메일 중복 검사
    // select count(*) from tbl_user where email=?
    // 위의 결과가  0 or 1 인데 , 0이면 조회 안된거라서  false
    //1 이면 true

    // 둘다 같은 기능
    //@Query("select count(*) from userEntity u where  u.email=?1")
    boolean existsByEmail(String email);
}
