package sku.sw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.*;


@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
@Getter
@Setter
@Builder // 객체를 생성, 초기화하는 빌더 메소드 생성
@Entity // 데이터베이스의 테이블과 매핑
@Table(name = "Schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false, length = 50)
    private String subject;
    
    @Column(nullable = true, length = 1000)
    private String memo;
}
