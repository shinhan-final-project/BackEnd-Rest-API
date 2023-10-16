package com.shinhan.friends_stock.service;

import com.shinhan.friends_stock.common.ApiResponse;
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
    public ResponseEntity<TokenInfo> login(String nickName, String password) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
            if (passwordEncoder.matches(password, memberRepository.findByName(nickName).orElseThrow().getPassword())) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nickName, password);

                // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
                // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                // 3. 인증 정보를 기반으로 JWT 토큰 생성
                TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

                headers.set("grantType", "Bearer");
                headers.set("accessToken", tokenInfo.getAccessToken());
                headers.set("refreshToken", tokenInfo.getRefreshToken());
                return ResponseEntity.ok().headers(headers).build();
            }
        }
        catch(Exception e){
            throw new Exception("로그인 요청 실패하였습니다.");
        }
        return ResponseEntity.badRequest().build();
    }
    //중복된 이름 있으면 true
    public ApiResponse<Boolean> checkDuplicateName(String name) throws Exception {
        try {
            //중복아니면 true return
            if(memberRepository.findByName(name).isEmpty())
                return ApiResponse.success(true);
        }catch (Exception e){
            throw new Exception("닉네임 중복인지 확인할 수 없습니다.");
        }
        throw new Exception("닉네임 중복입니다.");
    }
    @Transactional
    public ApiResponse<String> signin(String nickName, String password, Gender gender, int age, int investCareerYear) throws Exception {
        try {
            if (checkDuplicateName(nickName).getData()) {
                memberRepository.save(Member.builder()
                        .nickName(nickName)
                        .password(passwordEncoder.encode(password))
                        .gender(gender)
                        .age(age)
                        .investCareerYear(investCareerYear)
                        .build());
                return ApiResponse.success("성공적으로 회원가입이 되었습니다.");
            }
            return (ApiResponse<String>) ApiResponse.error("닉네임 중복입니다.");
        }catch (Exception e){
            throw new Exception("회원가입 요청에 실패하였습니다.");
        }
    }
}
