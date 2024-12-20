package com.logistics.order.application.dto;


import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Builder
public class ApiResponse<T> implements Serializable {

    private int statusCode;
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .status("SUCCESS")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.success(message, null);
    }

    public static ApiResponse fail(String message) {
        return ApiResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .data(null)
                .build();
    }

    public static ApiResponse error(String message) {
        return ApiResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(message)
                .data(null)
                .build();
    }

}
