package com.shinhan.friends_stock.domain.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "stock_news_year")
@Getter
public class StockNewsYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId;

    @ManyToOne
    @JoinColumn(name = "stock_item_id", nullable = false)
    private InvestItem company;

    @Column(name = "year")
    private Integer year;

    @Column(name = "title")
    private String title;

    @Column(name = "url", length = 2000)
    private String url;
}
