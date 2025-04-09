package com.example.myinvoice.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myinvoice.util.JsonTypeHandler.JsonListMapTypeHandler;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@TableName("air_itineraries")
public class AirItinerary {
    @TableId(value = "ticket_id",type = IdType.INPUT)
    private Long ticketId;  // 主键且外键关联tickets.id
    private String internationalFlightSign;
    private String serialNumber;
    private String passengerName;
    private String idCardNumber;
    private String endorsement;
    private String fare;
    private String caacDevelopmentFund;
    private String fuelSurcharge;
    private String totalAmount;
    private String ticketNumber;
    private String validationCode;
    private String promptMessage;
    private String insurance;
    private String agentCode;
    private String issueCompany;
    private String issueDate;
    private String pnrCode;
    private String otherTaxes;
    @TableField(typeHandler = JsonListMapTypeHandler.class)
    private List<Map<String, Object>> flights;  // JSON字段


    // 添加 getData() 方法
    public Map<String, Object> data_findbyuserid() {
        Map<String, Object> data = new HashMap<>();
        data.put("flights", flights); // 将 flights 数据放入 Map
        // 继续添加其他字段
        return data;
    }
}