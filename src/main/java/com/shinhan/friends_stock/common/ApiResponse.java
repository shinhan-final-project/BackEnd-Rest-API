package com.shinhan.friends_stock.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>(true);
        response.setData(data);
        return response;
    }

    public static ApiResponse<?> error(String message) {
        ApiResponse<?> response = new ApiResponse<>(false);
        response.setMessage(message);
        return response;
    }
}
