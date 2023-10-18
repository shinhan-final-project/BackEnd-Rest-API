package com.shinhan.friends_stock.domain.entity;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stock_return_rate")
@Getter
public class StockReturnRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long rateId;

    @ManyToOne
    @JoinColumn(name = "stock_item_id")
    private InvestItem investItem;

    @Column(name = "year")
    private Integer year;

    @Column(name = "rate", precision = 4, scale = 2)
    private BigDecimal rate;

    @Column(name = "last_price")
    private Integer lastPrice;
}
