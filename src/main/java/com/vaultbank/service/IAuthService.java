package com.vaultbank.service;

import com.vaultbank.dto.request.LoginRequest;
import com.vaultbank.dto.request.RegisterRequest;
import com.vaultbank.dto.response.AuthResponse;

public interface IAuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    AuthResponse refrescarToken(String refreshToken);
}
