package com.shinhan.friends_stock.repository.stock_quiz;

import com.shinhan.friends_stock.domain.entity.InvestItem;
import com.shinhan.friends_stock.domain.entity.StockNewsYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockNewsYearRepository extends JpaRepository<StockNewsYear, Long> {

    List<StockNewsYear> findByCompanyAndYear(InvestItem company, int year);
}
