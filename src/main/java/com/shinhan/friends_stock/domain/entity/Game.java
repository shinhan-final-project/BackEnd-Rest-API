package com.shinhan.friends_stock.domain.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game")
@Getter
public class Game {

    @Id
    @Column(name = "game_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "game_type")
    @NotNull
    private String type;

    @Column(name = "character_name")
    @NotNull
    private String characterName;

    @Column(name = "character_desc")
    private String characterDesc;

    @Column(name = "description")
    private String description;

    @Column(name = "reward")
    private String reward;

    @Column(name = "created_at")
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "game",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RewardHistory> rewardHistoryList;
}
