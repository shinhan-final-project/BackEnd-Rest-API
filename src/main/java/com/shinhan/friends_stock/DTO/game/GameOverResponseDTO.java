package com.shinhan.friends_stock.DTO.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GameOverResponseDTO {

    private final boolean isWin;
    private final String reward;
}
