package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.domain.GameInfo;
import com.shinhan.friends_stock.domain.TermQuizInfo;
import com.shinhan.friends_stock.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 로그인한 사용자 PK
     * @return
     */
    private long getUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserInfo userInfo = UserInfo.of(userDetails.getUsername());

        return userInfo.getPrimaryKey();
    }

    /**
     * 게임 초기 정보 Redis 에 저장
     * @param gameInfo
     * @return Redis key
     */
    public String initGameInfo(GameInfo gameInfo) {
        long userId = getUserId();
        gameInfo.setMemberId(userId);

        return saveGameInfo(gameInfo);
    }

    /**
     * Redis 에서 진행 중인 게임 정보 조회
     * @param gameId
     * @return
     */
    public GameInfo getGameInfo(long gameId) {
        String key = generateKey(getUserId(), gameId);

        String json = redisTemplate.opsForValue().get(key);
        GameInfo gameInfo;
        if (gameId == 1) {
            gameInfo = TermQuizInfo.of(json);
        } else {
            gameInfo = GameInfo.of(json);
        }
        return gameInfo;
    }

    /**
     * Redis 에 저장된 게임 정보 업데이트
     * @param gameInfo
     */
    public String saveGameInfo(GameInfo gameInfo) {
        long userId = gameInfo.getMemberId();
        if (userId < 1) userId = getUserId();

        String key = generateKey(userId, gameInfo.getGameId());
        redisTemplate.opsForValue().set(key, gameInfo.toString());
        return key;
    }

    /**
     * Redis key 생성
     * @param userId
     * @param gameId
     * @return Redis key
     */
    private String generateKey(long userId, long gameId) {
        return "user:" + userId + ":game:" + gameId + ":info";
    }
}
