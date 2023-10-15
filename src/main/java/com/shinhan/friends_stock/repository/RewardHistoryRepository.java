package com.shinhan.friends_stock.repository;

import com.shinhan.friends_stock.domain.entity.RewardHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {

    Optional<List<RewardHistory>> findByMemberId(long memberId);
}
