package com.shinhan.friends_stock.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfo {
    private final long primaryKey;
    private final String nickName;

    @Override
    public String toString() {
        return "UserInfo{" +
                "primaryKey=" + primaryKey +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
