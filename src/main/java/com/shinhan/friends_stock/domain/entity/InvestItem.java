package com.shinhan.friends_stock.domain.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="invest_item")
@Getter
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

    @Column(name = "company_info")
    private String companyInfo;

    //주식 고유 코드
    @Column(name = "stock_code")
    @NotNull
    private int stockCode;

    @OneToMany(
            mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StockNewsYear> news;

    //문제로 출제 여부
    @Column(name = "is_published", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    @NotNull
    private boolean isPublished;


    //이 회사가 주식 상장 시작 날짜
    @Column(name = "stock_start_year")
    private Integer stockStartYear;

    //문제 출제자가 설정한 시작 날짜
    @Column(name = "quiz_start_year")
    private Integer quizStartYear;

    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

}