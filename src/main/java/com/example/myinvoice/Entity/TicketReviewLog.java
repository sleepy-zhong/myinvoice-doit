package com.example.myinvoice.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@Data
@TableName("ticket_review_log")
public class TicketReviewLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("ticket_id")
    private Long ticketId;
    @TableField("reviewer_id")
    private Long reviewerId;
    @TableField("review_time")
    private Date reviewTime;
    private String result;  // "通过" 或 "不通过"
    private String reason;
}
