package com.example.myinvoice.Entity.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String employeeId;
    private String department;
    private String role;
    private String phone;
    private LocalDateTime createdAt;
}