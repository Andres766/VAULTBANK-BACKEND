package com.vaultbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String mensaje;
    private Object data;

    public static ApiResponse ok(String mensaje, Object data) {
        return ApiResponse.builder().mensaje(mensaje).data(data).build();
    }

    public static ApiResponse ok(String mensaje) {
        return ApiResponse.builder().mensaje(mensaje).build();
    }
}
