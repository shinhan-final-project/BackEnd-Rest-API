package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.DTO.game.GameResponseDTO;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.domain.entity.Game;
import com.shinhan.friends_stock.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;

    public ApiResponse<List<GameResponseDTO>> getGames() throws Exception {
        try {
            List<Game> games = gameRepository.findAll();
            return ApiResponse.success(games.stream().map(GameResponseDTO::of).toList());
        } catch (Exception e) {
            throw new Exception("Failed to retrieve games");
        }
    }
}
