package com.logistics.product.application.dto;


import lombok.*;
import org.springframework.http.*;

import java.io.*;

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
