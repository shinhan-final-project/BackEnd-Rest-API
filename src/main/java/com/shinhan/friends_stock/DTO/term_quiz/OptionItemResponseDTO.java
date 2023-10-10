package com.shinhan.friends_stock.DTO.term_quiz;

import com.shinhan.friends_stock.domain.entity.TermQuizItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OptionItemResponseDTO {

    private final long id;

    private final String content;

    public static OptionItemResponseDTO of(TermQuizItem item) {
        return new OptionItemResponseDTO(
                item.getId(),
                item.getContent()
        );
    }
}
