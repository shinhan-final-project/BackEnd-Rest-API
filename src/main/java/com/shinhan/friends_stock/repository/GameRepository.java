package com.shinhan.friends_stock.repository;

import com.shinhan.friends_stock.domain.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
