package com.shinhan.friends_stock.domain.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "term_quiz_log")
@Getter
public class TermQuizLog {

    @Id
    @Column(name = "log_id")
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

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "quiz_id")
    private TermQuizQuestion quizQuestion;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "selected_item_id")
    private TermQuizItem item;

    @Column(name = "is_correct", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    @NotNull
    @Getter
    private boolean correct;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public TermQuizLog(Member member, Game game, TermQuizQuestion quizQuestion, TermQuizItem item, boolean isCorrect) {
        this.member = member;
        this.game = game;
        this.quizQuestion = quizQuestion;
        this.item = item;
        this.correct = isCorrect;
    }
}
