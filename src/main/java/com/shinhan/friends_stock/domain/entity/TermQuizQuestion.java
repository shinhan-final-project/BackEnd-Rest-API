package com.shinhan.friends_stock.domain.entity;


import com.sun.istack.NotNull;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "term_quiz")
@Getter
public class TermQuizQuestion {

    @Id
    @Column(name = "quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "term")
    @NotNull
    private String term;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "is_published", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    @NotNull
    @Getter
    private boolean published;

    @Column(name = "answer_item_id")
    private Long answerId;

    @Column(name = "plus_point")
    @NotNull
    private int plusPoint;

    @Column(name = "minus_point")
    @NotNull
    private int minusPoint;

    @Column(name = "created_at")
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @OneToMany(
            mappedBy = "quiz",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TermQuizItem> items;

}
