package com.shinhan.friends_stock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.friends_stock.DTO.stock_quiz.*;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.service.StockQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-quiz")
@RequiredArgsConstructor
public class StockQuizController {

    private final StockQuizService stockQuizService;

    @PostMapping("/start")
    public ApiResponse<String> startGame() {
        return ApiResponse.success(stockQuizService.generateGameInfo());
    }

    @GetMapping("/companies/question")
    public ApiResponse<CompanyResponseDTO> getCompany() {
        return stockQuizService.getCompany();
    }

    @GetMapping("/companies/stocks")
    public ApiResponse<StockPriceYearResponseDTO> getStocks(
            @RequestParam(name = "year", defaultValue = "0") int year
    ) throws JsonProcessingException {
        return stockQuizService.getStocks(year);
    }

    @GetMapping("/companies/news")
    public ApiResponse<StockNewsYearResponseDTO> getNews(
            @RequestParam(name = "year", defaultValue = "0") int year
    ) {
        return stockQuizService.getNews(year);
    }

    @PostMapping("/answers/check")
    public ApiResponse<StockPredictionResponseDTO> checkAnswer(
            @RequestBody UserPredictionRequestDTO dto
            ) {
        return stockQuizService.checkAnswer(dto);
    }
}
