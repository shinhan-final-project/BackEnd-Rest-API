package com.shinhan.friends_stock.repository.stock_quiz;

import com.shinhan.friends_stock.domain.entity.InvestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvestItemRepository extends JpaRepository<InvestItem, Long> {

    @Query(value = "SELECT * FROM stock_friends_dev.invest_item WHERE is_published ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<InvestItem> findRandomInvestItem(boolean condition);
}
