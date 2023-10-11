package com.shinhan.friends_stock.DTO.term_quiz;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AnswerCheckResponseDTO {

    private final long id;  // 퀴즈 id

    private final String term;  // 용어

    private final long answerId;   // 정답 id

    private boolean isCorrect; // 정답여부

    private int point;    // 호감도 증감 값

}
