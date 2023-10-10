package com.shinhan.friends_stock.repository.term_quiz;

import com.shinhan.friends_stock.domain.entity.TermQuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TermQuizQuestionRepository extends JpaRepository<TermQuizQuestion, Long> {

    @Query("SELECT distinct q FROM TermQuizQuestion q LEFT JOIN FETCH q.items i WHERE q.id = :quizId")
    Optional<TermQuizQuestion> findByIdWithOptions(long quizId);
}
