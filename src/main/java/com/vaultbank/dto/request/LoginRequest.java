package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    /** Puede ser el email o el nombre de usuario. */
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
