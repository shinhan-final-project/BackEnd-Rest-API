package com.shinhan.friends_stock.controller;

import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.service.StockQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock-quiz")
@RequiredArgsConstructor
public class StockQuizController {

    private final StockQuizService stockQuizService;

    @PostMapping("/start")
    public ApiResponse<String> startGame() {
        return ApiResponse.success(stockQuizService.generateGameInfo());
    }
}
