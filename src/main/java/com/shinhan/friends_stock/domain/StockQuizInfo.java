package com.shinhan.friends_stock.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockQuizInfo extends GameInfo {

    private long stockId;
    private int[] results;

    public static StockQuizInfo of(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, StockQuizInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
