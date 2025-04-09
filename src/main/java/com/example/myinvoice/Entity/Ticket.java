package com.example.myinvoice.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myinvoice.util.JsonTypeHandler.JsonMapTypeHandler;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@TableName("tickets")
public class Ticket {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    private String type;  // 枚举字段，需自定义处理
    @TableField("unique_hash")
    private String uniqueHash;
    @TableField("image_url")
    private String imageUrl;
    @TableField(value = "ocr_json", typeHandler = JsonMapTypeHandler.class)
    private Map<String, Object> ocrJson;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    @TableField("approval_status")
    private String approvalStatus; // 或使用枚举类型

    public Map<String, Object> data_findbyuserid() {
        Map<String, Object> data = new HashMap<>();
        data.put("TicketId", id); // 将 flights 数据放入 Map
        data.put("type", type);
        data.put("imageurl", imageUrl);
        data.put("createdat", createdAt);
        data.put("updatedAt", updatedAt);
        data.put("approvalStatus", approvalStatus);


        // 继续添加其他字段
        return data;
    }
}