package com.shinhan.friends_stock.controller;

import com.shinhan.friends_stock.DTO.game.GameOverResponseDTO;
import com.shinhan.friends_stock.DTO.game.GameResponseDTO;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping()
    public ApiResponse<List<GameResponseDTO>> getGames() throws Exception {
        return gameService.getGames();
    }

    @GetMapping("/{gameId}/result")
    public ApiResponse<GameOverResponseDTO> getResult(
            @PathVariable(value = "gameId") long gameId
    ) {
        return gameService.calcGameResult(gameId);
    }
}
