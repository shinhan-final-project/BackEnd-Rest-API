package com.shinhan.friends_stock.DTO.stock_quiz;

import com.shinhan.friends_stock.domain.entity.StockNewsYear;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class StockNewsYearResponseDTO {

    private final int year;

    private final List<StockNewsResponseDTO> news;

    public static StockNewsYearResponseDTO of(int year, List<StockNewsYear> newsList) {
        return new StockNewsYearResponseDTO(
                year,
                newsList.stream().map(StockNewsResponseDTO::of).toList()
        );
    }
}
