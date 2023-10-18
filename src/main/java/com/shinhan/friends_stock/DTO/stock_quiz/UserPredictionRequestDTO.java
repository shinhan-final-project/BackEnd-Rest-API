package com.shinhan.friends_stock.DTO.stock_quiz;

import com.shinhan.friends_stock.domain.InvestmentBehavior;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPredictionRequestDTO {

    private int year;
    private InvestmentBehavior userAnswer;
}
