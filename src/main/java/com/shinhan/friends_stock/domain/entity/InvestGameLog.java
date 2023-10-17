package com.shinhan.friends_stock.domain.entity;

import com.shinhan.friends_stock.domain.InvestmentBehavior;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "invest_game_log")
@Getter
public class InvestGameLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "stock_item_id")
    private InvestItem investItem;

    @Column(name = "selected_year")
    private Integer selectedYear;

    @Column(name = "selected_price")
    private Integer selectedPrice;

    @Column(name = "action", nullable = false)
    private InvestmentBehavior action;

    @Column(name = "result", precision = 4, scale = 2)
    private BigDecimal result;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public InvestGameLog(Member member, Game game, InvestItem item, int year, int price, InvestmentBehavior action, BigDecimal result) {
        this.member = member;
        this.game = game;
        this.investItem = item;
        this.selectedYear = year;
        this.selectedPrice = price;
        this.action = action;
        this.result = result;
    }
}
