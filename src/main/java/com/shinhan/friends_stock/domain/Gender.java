package com.shinhan.friends_stock.domain;

import lombok.Getter;

@Getter
public enum Gender {
    Woman("W"),
    Man("M");
    private String gender;
    Gender(String gender) {
        this.gender = gender;
    }
}
