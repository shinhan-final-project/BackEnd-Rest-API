package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.domain.*;
import com.shinhan.friends_stock.domain.entity.*;
import com.shinhan.friends_stock.exception.ResourceNotFoundException;
import com.shinhan.friends_stock.repository.GameRepository;
import com.shinhan.friends_stock.repository.MemberRepository;
import com.shinhan.friends_stock.repository.RewardHistoryRepository;
import com.shinhan.friends_stock.repository.stock_quiz.InvestGameLogRepository;
import com.shinhan.friends_stock.repository.term_quiz.TermQuizLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final TermQuizLogRepository termQuizLogRepository;
    private final InvestGameLogRepository investGameLogRepository;
    private final RewardHistoryRepository rewardHistoryRepository;
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 로그인한 사용자
     * @return
     */
    private UserInfo getUserInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return UserInfo.of(userDetails.getUsername());
    }

    private long getUserId() {
        UserInfo info = getUserInfo();
        return info.getPrimaryKey();
    }

    /**
     * 게임 초기 정보 Redis 에 저장
     * @param gameInfo
     * @return Redis key
     */
    public String initGameInfo(GameInfo gameInfo) {
        UserInfo info = getUserInfo();
        long userId = info.getPrimaryKey();
        gameInfo.setMemberId(userId);

        saveGameInfo(gameInfo);
        return info.getNickname();
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
        } else if (gameId == 2) {
            gameInfo = StockQuizInfo.of(json);
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

    public boolean isDuplicatedReward(long userId, long gameId) {

        List<RewardHistory> history = rewardHistoryRepository.findByMemberId(userId).orElse(null);
        if (history == null || history.size() == 0) return false;

        history = history.stream()
                .filter(h -> h.getGame().getId() == gameId)
                .toList();
        if (history == null || history.size() == 0) return false;

        return true;
    }

    public void saveLog(long gameId, TermQuizQuestion quizQuestion, TermQuizItem item, boolean isCorrect) {
        long userId = getUserId();

        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Member not found")
        );
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new ResourceNotFoundException("Game not found")
        );

        TermQuizLog log = new TermQuizLog(
                member,
                game,
                quizQuestion,
                item,
                isCorrect
        );
        termQuizLogRepository.save(log);
    }

    public void saveInvestLog(long gameId, InvestItem item, int year, int price, InvestmentBehavior action, BigDecimal rate) {
        long userId = getUserId();

        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Member not found")
        );
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new ResourceNotFoundException("Game not found")
        );

        InvestGameLog log = new InvestGameLog(
                member,
                game,
                item,
                year,
                price,
                action,
                rate
        );
        investGameLogRepository.save(log);
    }

    public void saveRewardHistory(long gameId, String reward) {
        long userId = getUserId();
        if (isDuplicatedReward(userId, gameId)) return;

        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Member not found")
        );
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new ResourceNotFoundException("Game not found")
        );

        RewardHistory.RewardHistoryBuilder builder = RewardHistory.builder()
                .member(member)
                .game(game);
        if (reward != null && reward.length() > 0) {
            builder.reward(reward);
        }
        RewardHistory history = builder.build();

        rewardHistoryRepository.save(history);
    }
}
