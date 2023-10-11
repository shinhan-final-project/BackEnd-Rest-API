package com.shinhan.friends_stock.DTO.game;

import com.shinhan.friends_stock.domain.entity.Game;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GameResponseDTO {

    private final long id;

    private final String type;

    private final String characterName;

    private final String characterDesc;

    public static GameResponseDTO of(Game game) {
        return new GameResponseDTO(
                game.getId(),
                game.getType(),
                game.getCharacterName(),
                game.getCharacterDesc()
        );
    }
}
