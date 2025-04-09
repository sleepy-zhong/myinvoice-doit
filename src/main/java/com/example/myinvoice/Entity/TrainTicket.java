package com.example.myinvoice.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@TableName("train_tickets")
public class TrainTicket {
    @TableId("ticket_id")
    private Long ticketId;  // 主键且外键关联tickets.id
    private String ticketNumber;
    private String departureStation;
    private String arrivalStation;
    private String trainNumber;
    private String departureTime;
    private String seatNumber;
    private String fare;
    private String seatType;
    private String passengerInfo;
    private String passengerName;
    private String ticketCode;
    private String saleInfo;
    private String ticketGate;
    private String electronicTicketNumber;
    private String buyerName;
    private String buyerCreditCode;
    private String title;
    private String invoiceDate;
    private String remarks;


    public Map<String, Object> data_findbyuserid() {
        // 返回一个包含重要数据的 Map，可以根据实际需要返回不同的数据
        Map<String, Object> data = new HashMap<>();
        data.put("ticketNumber", ticketNumber);
        data.put("departureStation", departureStation);
        data.put("arrivalStation", arrivalStation);
        // 继续添加其他字段
        return data;
    }
}