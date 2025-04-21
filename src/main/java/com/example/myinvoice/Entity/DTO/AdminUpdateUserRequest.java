package com.example.myinvoice.Entity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminUpdateUserRequest {
    @NotNull
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String department;
    private String role; // 枚举类型


}