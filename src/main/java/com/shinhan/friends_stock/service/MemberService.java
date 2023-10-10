package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.domain.Gender;
import com.shinhan.friends_stock.domain.entity.Member;
import com.shinhan.friends_stock.jwt.JwtTokenProvider;
import com.shinhan.friends_stock.jwt.TokenInfo;
import com.shinhan.friends_stock.repository.MemberRepository;
import io.jsonwebtoken.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseEntity<TokenInfo> login(String nickName, String password) {
        HttpHeaders headers = new HttpHeaders();
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        if(passwordEncoder.matches(password, memberRepository.findByName(nickName).orElseThrow().getPassword())) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nickName, password);

            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            headers.set("grantType","Bearer");
            headers.set("accessToken",tokenInfo.getAccessToken());
            headers.set("refreshToken",tokenInfo.getRefreshToken());
            return ResponseEntity.ok().headers(headers).build();
        }
        return ResponseEntity.badRequest().build();
    }
    //중복된 이름 있으면 true
    public Boolean checkDuplicateName(String name){
        return memberRepository.findByName(name).isPresent();
    }
    @Transactional
    public ResponseEntity<Long> signin(String nickName, String password, Gender gender, int age, int investCareerYear) {
        if(!checkDuplicateName(nickName)){
            return ResponseEntity.ok().body(
                    memberRepository.save(Member.builder()
                            .nickName(nickName)
                            .password(passwordEncoder.encode(password))
                            .gender(gender)
                            .age(age)
                            .investCareerYear(investCareerYear)
                    .build()).getId());
        }
        return ResponseEntity.badRequest().build();
    }
}
