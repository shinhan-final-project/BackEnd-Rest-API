package com.shinhan.friends_stock.DTO.term_quiz;

import com.shinhan.friends_stock.domain.entity.TermQuizQuestion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class QuestionResponseDTO {

    private final long id;

    private final String term;

    private final List<OptionItemResponseDTO> items;

    public static QuestionResponseDTO of(TermQuizQuestion quiz) {
        return new QuestionResponseDTO(
                quiz.getId(),
                quiz.getTerm(),
                quiz.getItems().stream().map(OptionItemResponseDTO::of).toList()
        );
    }
}
