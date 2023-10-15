package com.shinhan.friends_stock.controller;

import com.shinhan.friends_stock.DTO.LogInRequestDTO;
import com.shinhan.friends_stock.DTO.SignInRequestDTO;
import com.shinhan.friends_stock.jwt.TokenInfo;
import com.shinhan.friends_stock.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/test")
    public String test(){
        return "success";
    }
}
