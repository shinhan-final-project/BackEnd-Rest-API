package com.shinhan.friends_stock.DTO.term_quiz;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SolutionResponseDTO {

    private final long id;

    private final String term;

    private final String description;

    private final String explanation;
}
