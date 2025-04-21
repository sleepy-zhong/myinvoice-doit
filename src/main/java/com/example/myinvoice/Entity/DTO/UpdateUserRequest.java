package com.example.myinvoice.Entity.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private String username;
    private String password;
    private String phone;
}
