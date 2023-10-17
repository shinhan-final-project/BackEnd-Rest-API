package com.shinhan.friends_stock.repository.stock_quiz;

import com.shinhan.friends_stock.domain.entity.InvestItem;
import com.shinhan.friends_stock.domain.entity.StockReturnRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockReturnRateRepository extends JpaRepository<StockReturnRate, Long> {

    Optional<StockReturnRate> findByInvestItemAndYear(InvestItem item, int year);
}
