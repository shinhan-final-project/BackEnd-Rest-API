package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.domain.StockQuizInfo;
import com.shinhan.friends_stock.domain.entity.InvestItem;
import com.shinhan.friends_stock.exception.ResourceNotFoundException;
import com.shinhan.friends_stock.repository.stock_quiz.InvestItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockQuizService {

    private static final int GAME_ID = 2;

    private final InvestItemRepository investItemRepository;

    private final LogService logService;

    public String generateGameInfo() {
        /**
         * 1. 종목 -> 시작 연도
         * 2. 결과 (리스트 size = 5)
         */

        InvestItem stock = investItemRepository.findRandomInvestItem(true)
                .orElseThrow(() -> new ResourceNotFoundException("문제가 없습니다."));

        StockQuizInfo gameInfo = new StockQuizInfo();
        gameInfo.setGameId(GAME_ID);
        gameInfo.setStockId(stock.getItemId());
        gameInfo.setPoint(0);
        gameInfo.setCurrentStage(1);
        gameInfo.setChecked(false);
        gameInfo.setResults(new int[5]);

        return logService.initGameInfo(gameInfo);
    }
}
