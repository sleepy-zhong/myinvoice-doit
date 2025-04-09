package com.example.myinvoice.server.login;

import com.example.myinvoice.Entity.DTO.LoginRequest;
import com.example.myinvoice.Entity.DTO.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
