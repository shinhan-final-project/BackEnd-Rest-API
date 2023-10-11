package com.shinhan.friends_stock.controller;

import com.shinhan.friends_stock.DTO.term_quiz.UserAnswerRequsetDTO;
import com.shinhan.friends_stock.DTO.term_quiz.QuestionResponseDTO;
import com.shinhan.friends_stock.DTO.term_quiz.AnswerCheckResponseDTO;
import com.shinhan.friends_stock.DTO.term_quiz.SolutionResponseDTO;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.service.TermQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/term-quiz")
@RequiredArgsConstructor
public class TermQuizController {

    private final TermQuizService termQuizService;

    @GetMapping("/questions/{quizId}")
    public ApiResponse<QuestionResponseDTO> getQuiz(@PathVariable(value = "quizId") long quizId) throws Exception {
        return termQuizService.getQuiz(quizId);
    }

    @PostMapping("/questions/{quizId}/answers/check")
    public ApiResponse<AnswerCheckResponseDTO> checkAnswer(
            @PathVariable(value = "quizId") long quizId,
            @RequestBody UserAnswerRequsetDTO dto
            ) throws Exception {
        return termQuizService.checkAnswer(quizId, dto);
    }

    @GetMapping("/questions/{quizId}/solution")
    public ApiResponse<SolutionResponseDTO> getSolution(
            @PathVariable(value = "quizId") long quizId
    ) throws Exception {
        return termQuizService.getSolution(quizId);
    }
}
