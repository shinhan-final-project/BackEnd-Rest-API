package com.shinhan.friends_stock.DTO.stock_quiz;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class StockPriceYearResponseDTO {

    private final int year;

    private final List<Integer> price;

    private final List<String> date;

    public static StockPriceYearResponseDTO of(int year, List<Integer> price, List<String> date) {
        return new StockPriceYearResponseDTO(
                year,
                price,
                date
        );
    }
}
