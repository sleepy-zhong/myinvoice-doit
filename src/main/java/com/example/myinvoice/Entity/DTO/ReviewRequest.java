package com.example.myinvoice.Entity.DTO;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long ticketId;
    private String result; // "approved" 或 "rejected"
    private String reason; // 可选
}
