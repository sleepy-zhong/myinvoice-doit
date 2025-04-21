package com.example.myinvoice.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@Data
@TableName("ticket_operation_log")
public class TicketOperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("ticket_id")
    private Long ticketId;
    @TableField("operation_id")
    private Long operationId;
    @TableField("operation_time")
    private Date operationTime;
    private String operation;  // "通过" 或 "不通过"
    private String reason;
}
