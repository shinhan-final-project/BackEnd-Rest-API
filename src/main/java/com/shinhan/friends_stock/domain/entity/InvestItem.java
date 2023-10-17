package com.shinhan.friends_stock.domain.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="invest_item")
@Getter
@EntityListeners(AuditingEntityListener.class) // 추가
public class InvestItem {
    //퀴즈 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    //회사 이름
    @Column(name = "company_name")
    @NotNull
    private String companyName;

    //시가 총액
    @Column(name = "company_value")
    private int companyValue;

    //주식 고유 코드
    @Column(name = "stock_code")
    @NotNull
    private int stockCode;

    //문제로 출제 여부
    @Column(name = "is_published", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    @NotNull
    private boolean isPublished;


    //이 회사가 주식 상장 시작 날짜
    @Column(name = "stock_start_year")
    private LocalDateTime stockStartYear;

    //문제 출제자가 설정한 시작 날짜
    @Column(name = "quiz_start_year")
    private LocalDateTime quizStartYear;

    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

}