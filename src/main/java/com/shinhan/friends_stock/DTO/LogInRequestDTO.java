package com.shinhan.friends_stock.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LogInRequestDTO {
    private String nickName;
    private String password;
}
