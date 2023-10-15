package com.shinhan.friends_stock.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "reward_history")
@Getter
@NoArgsConstructor
public class RewardHistory {

    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @Column(name = "reward")
    private String reward;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public RewardHistory(Member member, Game game, String reward) {
        this.member = member;
        this.game = game;
        this.reward = reward;
    }
}
