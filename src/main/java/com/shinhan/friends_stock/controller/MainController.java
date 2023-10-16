package com.shinhan.friends_stock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinhan.friends_stock.DTO.LogInRequestDTO;
import com.shinhan.friends_stock.DTO.SignInRequestDTO;
import com.shinhan.friends_stock.common.ApiResponse;
import com.shinhan.friends_stock.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MainController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LogInRequestDTO memberLoginRequestDto) throws Exception {
        return memberService.login(memberLoginRequestDto.getNickName(), memberLoginRequestDto.getPassword());
    }
    @PostMapping("/signin")
    public ApiResponse<String> signin(@RequestBody SignInRequestDTO signInRequestDTO) throws Exception {
        return memberService.signin(signInRequestDTO.getNickName(), signInRequestDTO.getPassword(), signInRequestDTO.getGender(), signInRequestDTO.getAge(), signInRequestDTO.getInvestCareerYear());
    }
    @GetMapping("/duplicate/{nickName}")
    public ApiResponse<Boolean> checkDuplicateName(@PathVariable("nickName") String nickName) throws Exception {
        return memberService.checkDuplicateName(nickName);
    }
    @GetMapping("/test")
    public String test() throws JsonProcessingException {
        return "success";
    }
}
