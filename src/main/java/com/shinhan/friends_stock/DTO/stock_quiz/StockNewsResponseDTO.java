package com.shinhan.friends_stock.DTO.stock_quiz;

import com.shinhan.friends_stock.domain.entity.StockNewsYear;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StockNewsResponseDTO {

    private final String newsHead;

    private final String newsUrl;

    public static StockNewsResponseDTO of(StockNewsYear data) {
        return new StockNewsResponseDTO(
                data.getTitle(),
                data.getUrl()
        );
    }
}
