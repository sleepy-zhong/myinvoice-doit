package com.example.myinvoice.Entity.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String phone;
    private String department;
    private String role; // "user" / "finance" / "admin"
}
