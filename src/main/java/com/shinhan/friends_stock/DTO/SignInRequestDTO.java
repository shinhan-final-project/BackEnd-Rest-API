package com.shinhan.friends_stock.DTO;


import com.shinhan.friends_stock.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignInRequestDTO {
    private String nickName;
    private String password;
    private Gender gender;
    private int age;
    private int investCareerYear;
}
