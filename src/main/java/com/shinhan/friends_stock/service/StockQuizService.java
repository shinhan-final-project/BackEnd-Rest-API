package com.shinhan.friends_stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.friends_stock.DTO.stock_quiz.*;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.domain.InvestmentBehavior;
import com.shinhan.friends_stock.domain.StockQuizInfo;
import com.shinhan.friends_stock.domain.entity.InvestItem;
import com.shinhan.friends_stock.domain.entity.StockNewsYear;
import com.shinhan.friends_stock.domain.entity.StockReturnRate;
import com.shinhan.friends_stock.exception.ResourceNotFoundException;
import com.shinhan.friends_stock.exception.ResourceNotPublishedException;
import com.shinhan.friends_stock.repository.stock_quiz.InvestItemRepository;
import com.shinhan.friends_stock.repository.stock_quiz.StockNewsYearRepository;
import com.shinhan.friends_stock.repository.stock_quiz.StockReturnRateRepository;
import com.shinhan.friends_stock.service.LogService;
import com.shinhan.friends_stock.utils.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockQuizService {

    private static final int GAME_ID = 2;
    private static final int PLUS_POINT = 30;
    private static final int MINUS_POINT = -10;

    private final InvestItemRepository investItemRepository;
    private final StockNewsYearRepository stockNewsYearRepository;
    private final StockReturnRateRepository stockReturnRateRepository;

    private final LogService logService;

    private final S3Util s3Util;

    public ApiResponse<CompanyResponseDTO> getCompany() {
        // get from redis
        StockQuizInfo gameInfo = (StockQuizInfo) logService.getGameInfo(GAME_ID);
        long companyId = gameInfo.getCompanyId();

        // get company
        InvestItem company = getPublishedCompany(companyId);

        return ApiResponse.success(CompanyResponseDTO.of(company));
    }

    public ApiResponse<StockPriceYearResponseDTO> getStocks(int year) throws JsonProcessingException {
        // get from redis
        StockQuizInfo gameInfo = (StockQuizInfo) logService.getGameInfo(GAME_ID);
        long companyId = gameInfo.getCompanyId();
        InvestItem company = getPublishedCompany(companyId);
        if (year == 0) {
            year = company.getQuizStartYear();
        }

        // get stocks
        StringBuilder builder = new StringBuilder();
        builder.append(company.getStockCode());
        builder.append("-[");
        builder.append(year);
        builder.append("]");

        String jsonString = s3Util.download(builder.toString());
        try {
            // Jackson ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열을 List<Map> 객체로 변환
            List<Map<String, String>> dataList = objectMapper.readValue(jsonString, List.class);

            List<Integer> price = new ArrayList<>();
            List<String> date = new ArrayList<>();
            for (Map<String, String> data : dataList) {
                date.add(data.get("date"));
                price.add(Integer.valueOf(data.get("price")));
            }

            StockPriceYearResponseDTO result = new StockPriceYearResponseDTO(
                    year,
                    price,
                    date
            );

            // 변환된 데이터 출력
            return ApiResponse.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ApiResponse<StockNewsYearResponseDTO> getNews(int year) {
        // get from redis
        StockQuizInfo gameInfo = (StockQuizInfo) logService.getGameInfo(GAME_ID);
        long companyId = gameInfo.getCompanyId();
        InvestItem company = getPublishedCompany(companyId);
        if (year == 0) {
            year = company.getQuizStartYear();
        }

        // get news
        List<StockNewsYear> newsList = stockNewsYearRepository.findByCompanyAndYear(company, year);
        return ApiResponse.success(StockNewsYearResponseDTO.of(year, newsList));
    }

    @Transactional
    public ApiResponse<StockPredictionResponseDTO> checkAnswer(UserPredictionRequestDTO dto) {

        // get from redis
        StockQuizInfo gameInfo = (StockQuizInfo) logService.getGameInfo(GAME_ID);
        long companyId = gameInfo.getCompanyId();
        InvestItem item = getPublishedCompany(companyId);

        // TODO validate

        StockReturnRate returnRate = stockReturnRateRepository.findByInvestItemAndYear(item, dto.getYear())
                .orElseThrow(() -> new ResourceNotFoundException("수익률을 알 수 없습니다."));

        InvestmentBehavior answer = BigDecimal.ZERO.compareTo(returnRate.getRate()) > 0 ? InvestmentBehavior.BUY : InvestmentBehavior.SELL;
        boolean isCorrect = dto.getUserAnswer().equals(answer);
        int point = isCorrect ? PLUS_POINT : MINUS_POINT;
        StockPredictionResponseDTO result = new StockPredictionResponseDTO(
                companyId,
                dto.getYear(),
                returnRate.getRate(),
                answer,
                isCorrect,
                point
        );

        // TODO get 해당 연 마지막 날 종가
        int stockPrice = 10000;

        // log
        logService.saveInvestLog(gameInfo.getGameId(), item, dto.getYear(), stockPrice, dto.getUserAnswer(), returnRate.getRate());

        return ApiResponse.success(result);
    }

    public String generateGameInfo() {
        /**
         * 1. 종목 -> 시작 연도
         * 2. 결과 (리스트 size = 5)
         */

        InvestItem stock = investItemRepository.findRandomInvestItem(true)
                .orElseThrow(() -> new ResourceNotFoundException("문제가 없습니다."));

        StockQuizInfo gameInfo = new StockQuizInfo();
        gameInfo.setGameId(GAME_ID);
        gameInfo.setCompanyId(stock.getItemId());
        gameInfo.setPoint(0);
        gameInfo.setCurrentStage(1);
        gameInfo.setChecked(false);
        gameInfo.setResults(new int[5]);

        return logService.initGameInfo(gameInfo);
    }

    private InvestItem getPublishedCompany(long companyId) {
        InvestItem company = investItemRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("문제를 찾을 수 없습니다."));

        if (company.isPublished()) {
            return company;
        }

        throw new ResourceNotPublishedException("게시되지 않은 문제입니다.");
    }
}
