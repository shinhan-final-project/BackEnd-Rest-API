package com.shinhan.friends_stock.DTO.stock_quiz;

import com.shinhan.friends_stock.domain.InvestmentBehavior;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class StockPredictionResponseDTO {

    private final long id;  // 종목

    private final int year; // 연도

    private final BigDecimal stockRate; // 주가 증감율

    private final InvestmentBehavior answer;    // 정답

    private final boolean correct;  // 정답여부

    private final int point;    // 호감도 증감 값
}
