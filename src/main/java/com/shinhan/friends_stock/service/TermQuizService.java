package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.DTO.term_quiz.UserAnswerRequsetDTO;
import com.shinhan.friends_stock.DTO.term_quiz.QuestionResponseDTO;
import com.shinhan.friends_stock.DTO.term_quiz.AnswerCheckResponseDTO;
import com.shinhan.friends_stock.DTO.term_quiz.SolutionResponseDTO;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.domain.TermQuizInfo;
import com.shinhan.friends_stock.domain.entity.TermQuizItem;
import com.shinhan.friends_stock.domain.entity.TermQuizQuestion;
import com.shinhan.friends_stock.exception.ResourceNotFoundException;
import com.shinhan.friends_stock.exception.ResourceNotPublishedException;
import com.shinhan.friends_stock.repository.term_quiz.TermQuizItemRepository;
import com.shinhan.friends_stock.repository.term_quiz.TermQuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermQuizService {

    private static final int GAME_ID = 1;

    private final TermQuizQuestionRepository termQuizQuestionRepository;
    private final TermQuizItemRepository termQuizItemRepository;

    private final LogService logService;

    public ApiResponse<QuestionResponseDTO> getQuiz() throws Exception {
        try {
            // get from redis
            TermQuizInfo gameInfo = (TermQuizInfo) logService.getGameInfo(GAME_ID);
            long quizId = gameInfo.getCurrentQuizId();

            // get question
            TermQuizQuestion quiz = getPublishedQuizById(quizId);
            return ApiResponse.success(QuestionResponseDTO.of(quiz));
        } catch (ResourceNotFoundException | ResourceNotPublishedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Failed to retrieve term quiz");
        }
    }

    @Transactional
    public ApiResponse<AnswerCheckResponseDTO> checkAnswer(long quizId, UserAnswerRequsetDTO dto) throws Exception {
        try {
            // get from redis
            TermQuizInfo gameInfo = (TermQuizInfo) logService.getGameInfo(GAME_ID);

            // validate
            if (gameInfo.getCurrentQuizId() != quizId) {
                throw new ResourceNotPublishedException("퀴즈 번호를 다시 확인하세요.");
            }
            if (gameInfo.isChecked()) {
                throw new ResourceNotPublishedException("duplicated");
            }

            TermQuizQuestion quiz = getPublishedQuizById(quizId);
            AnswerCheckResponseDTO result = new AnswerCheckResponseDTO(
                    quiz.getId(),
                    quiz.getTerm(),
                    quiz.getAnswerId()
            );
            boolean isCorrect = quiz.getAnswerId() == dto.getUserAnswerId();
            int point = isCorrect ? quiz.getPlusPoint() : quiz.getMinusPoint() * -1;
            result.setCorrect(isCorrect);
            result.setPoint(point);

            // log
            try {
                TermQuizItem item = termQuizItemRepository.findById(dto.getUserAnswerId()).orElseThrow();
                logService.saveLog(gameInfo.getGameId(), quiz, item, isCorrect);
            } catch (Exception e) {
                // Failed to save log
            }

            // set to redis
            gameInfo.setPoint(gameInfo.getPoint() + point);
            gameInfo.setChecked(true);
            logService.saveGameInfo(gameInfo);

            return ApiResponse.success(result);
        } catch (ResourceNotFoundException | ResourceNotPublishedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Failed to check answer of term quiz");
        }
    }

    public ApiResponse<SolutionResponseDTO> getSolution(long quizId) throws Exception {
        try {
            // get from redis
            TermQuizInfo gameInfo = (TermQuizInfo) logService.getGameInfo(1);

            // validate
            if (gameInfo.getCurrentQuizId() != quizId) {
                throw new ResourceNotPublishedException("퀴즈 번호를 다시 확인하세요.");
            }

            TermQuizQuestion quiz = getPublishedQuizById(quizId);
            SolutionResponseDTO result = new SolutionResponseDTO(
                    quiz.getId(),
                    quiz.getTerm(),
                    quiz.getDescription(),
                    quiz.getExplanation()
            );

            // set to redis
            List<Long> quizIdList = gameInfo.getQuizIdList();
            if (gameInfo.getCurrentStage() >= quizIdList.size()) {
                // pass
            } else if (gameInfo.isChecked()){
                gameInfo.setCurrentQuizId(quizIdList.get(gameInfo.getCurrentStage()));
                gameInfo.setCurrentStage(gameInfo.getCurrentStage() + 1);
                gameInfo.setChecked(false);
            }
            logService.saveGameInfo(gameInfo);

            return ApiResponse.success(result);
        } catch (ResourceNotFoundException | ResourceNotPublishedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Failed to get solution of term quiz");
        }
    }

    private TermQuizQuestion getPublishedQuizById(long quizId) {
        TermQuizQuestion quiz = termQuizQuestionRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("문제를 찾을 수 없습니다."));

        if (quiz.isPublished()) {
            return quiz;
        }

        throw new ResourceNotPublishedException("게시되지 않은 문제입니다.");
    }

    public String generateGameInfo() {
        List<TermQuizQuestion> quizQuestions = termQuizQuestionRepository.fetchQuiz(true);
        List<Long> quizIdList = quizQuestions.stream().map(TermQuizQuestion::getId).toList();

        TermQuizInfo gameInfo = new TermQuizInfo();
        gameInfo.setGameId(1);
        gameInfo.setQuizIdList(quizIdList);
        gameInfo.setPoint(0);
        gameInfo.setCurrentStage(1);
        gameInfo.setCurrentQuizId(quizIdList.get(0));
        gameInfo.setChecked(false);

        return logService.initGameInfo(gameInfo);
    }

}
