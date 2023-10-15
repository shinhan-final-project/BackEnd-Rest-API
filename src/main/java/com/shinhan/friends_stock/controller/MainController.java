package com.shinhan.friends_stock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.friends_stock.DTO.LogInRequestDTO;
import com.shinhan.friends_stock.DTO.SignInRequestDTO;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.domain.UserInfo;
import com.shinhan.friends_stock.domain.entity.Member;
import com.shinhan.friends_stock.jwt.TokenInfo;
import com.shinhan.friends_stock.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MainController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LogInRequestDTO memberLoginRequestDto) {
        return memberService.login(memberLoginRequestDto.getNickName(), memberLoginRequestDto.getPassword());
    }
    @PostMapping("/signin")
    public ResponseEntity<Long> signin(@RequestBody SignInRequestDTO signInRequestDTO){
        return memberService.signin(signInRequestDTO.getNickName(), signInRequestDTO.getPassword(), signInRequestDTO.getGender(), signInRequestDTO.getAge(), signInRequestDTO.getInvestCareerYear());
    }
    @GetMapping("/test")
    public String test() throws JsonProcessingException {
        return "success";
    }
}
