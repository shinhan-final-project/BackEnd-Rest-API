package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.DTO.game.GameOverResponseDTO;
import com.shinhan.friends_stock.DTO.game.GameResponseDTO;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.domain.GameInfo;
import com.shinhan.friends_stock.domain.entity.Game;
import com.shinhan.friends_stock.exception.ResourceNotFoundException;
import com.shinhan.friends_stock.exception.ResourceNotPublishedException;
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

    private final LogService logService;

    public ApiResponse<List<GameResponseDTO>> getGames() throws Exception {
        try {
            List<Game> games = gameRepository.findAll();
            return ApiResponse.success(games.stream().map(GameResponseDTO::of).toList());
        } catch (Exception e) {
            throw new Exception("Failed to retrieve games");
        }
    }

    public ApiResponse<GameOverResponseDTO> calcGameResult(long gameId) {
        // get from redis
        GameInfo gameInfo = logService.getGameInfo(gameId);
        // TODO 게임 단계 확인
        if (gameInfo.getCurrentStage() < 10) {
            throw new ResourceNotPublishedException("아직 남은 퀴즈가 있어요.");
        }

        // TODO 성공 조건 확인
        boolean isWin = gameInfo.getPoint() >= 100;
        String reward = null;

        if (isWin) {
            Game game = gameRepository.findById(gameId).orElseThrow(
                    () -> new ResourceNotFoundException("게임을 찾을 수 없습니다.")
            );

            reward = game.getReward();
        }

        GameOverResponseDTO result = new GameOverResponseDTO(
                isWin,
                reward
        );

        return ApiResponse.success(result);
    }
}
