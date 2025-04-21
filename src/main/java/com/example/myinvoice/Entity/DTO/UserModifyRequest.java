package com.example.myinvoice.Entity.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class UserModifyRequest {
    @NotNull
    private Long ticketId;
    @NotBlank
    private String operation; // 操作类型（如 "modify"）
    private String reason;    // 修改原因
    private Map<String, Object> updateFields; // 需要修改的字段键值对
}