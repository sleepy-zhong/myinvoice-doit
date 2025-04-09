package com.example.myinvoice.Entity.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String employeeId;
    private String password;
}
