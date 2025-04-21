package com.example.myinvoice.Entity.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class OperationLogDetailDTO {
    private Long logId;          // 日志ID
    private Long ticketId;       // 票据ID
    private String operation;    // 操作类型
    private Date operationTime;  // 操作时间
    private String reason;       // 操作原因
    private UserInfo userInfo;   // 操作用户详细信息

    @Data
    public static class UserInfo {
        private String username;
        private String employeeId;
        private String department;
        private String phone;
        private String role;
    }
}