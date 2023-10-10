package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.DTO.term_quiz.UserAnswerRequsetDTO;
import com.shinhan.friends_stock.DTO.term_quiz.QuestionResponseDTO;
import com.shinhan.friends_stock.DTO.term_quiz.AnswerCheckResponseDTO;
import com.shinhan.friends_stock.DTO.term_quiz.SolutionResponseDTO;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.domain.entity.TermQuizQuestion;
import com.shinhan.friends_stock.exception.ResourceNotFoundException;
import com.shinhan.friends_stock.exception.ResourceNotPublishedException;
import com.shinhan.friends_stock.repository.term_quiz.TermQuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermQuizService {

    private final TermQuizQuestionRepository termQuizQuestionRepository;

    public ApiResponse<QuestionResponseDTO> getQuiz(long quizId) throws Exception {
        try {
            TermQuizQuestion quiz = getPublishedQuizById(quizId);
            return ApiResponse.success(QuestionResponseDTO.of(quiz));
        } catch (ResourceNotFoundException | ResourceNotPublishedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Failed to retrieve term quiz");
        }
    }

    public ApiResponse<AnswerCheckResponseDTO> checkAnswer(long quizId, UserAnswerRequsetDTO dto) throws Exception {
        try {
            TermQuizQuestion quiz = getPublishedQuizById(quizId);
            AnswerCheckResponseDTO result = new AnswerCheckResponseDTO(
                    quiz.getId(),
                    quiz.getTerm(),
                    quiz.getAnswerId()
            );
            boolean isCorrect = quiz.getAnswerId() == dto.getUserAnswerId();
            result.setCorrect(isCorrect);
            result.setPoint(isCorrect ? quiz.getPlusPoint() : quiz.getMinusPoint() * -1);
            return ApiResponse.success(result);
        } catch (ResourceNotFoundException | ResourceNotPublishedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Failed to check answer of term quiz");
        }
    }

    public ApiResponse<SolutionResponseDTO> getSolution(long quizId) throws Exception {
        try {
            TermQuizQuestion quiz = getPublishedQuizById(quizId);
            SolutionResponseDTO result = new SolutionResponseDTO(
                    quiz.getId(),
                    quiz.getTerm(),
                    quiz.getDescription(),
                    quiz.getExplanation()
            );
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

}
