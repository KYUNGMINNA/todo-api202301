package com.example.todo.todoapi.entity;

//일정관리 프로그램

import com.example.todo.userapi.entity.UserEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "todoId")
@Builder
@Entity
@Table(name = "tbl_todo")
public class TodoEntity {
    //절대 중복이 없는 랜덤 문자열로 pk값이 처리 됨
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "uuid")
    private String todoId;

    @Column(nullable = false,length = 30)
    private String title; //제목

    private boolean done; //일정 완료 여부

    @CreationTimestamp
    private LocalDateTime createDate; //등록 시간

    //회원과 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    //연관관계 설정은 했지만 INSERT,UPDATE시에는 이 객체를 활용하지 않겠다 : inserttable,updateable -->성능 최적화를 위해서
    @JoinColumn(name = "user_id",insertable = false,updatable = false) // 성능 최적화
    private UserEntity user;

    //위의 insert와 update 할 때 사용할 변수 생성
    //할 일 추가 ,수정시 사용할 외래키
    @Column(name = "user_id")
    private String userId;
}
