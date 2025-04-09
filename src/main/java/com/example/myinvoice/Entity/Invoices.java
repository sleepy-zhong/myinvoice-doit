package com.example.myinvoice.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.myinvoice.util.JsonTypeHandler.JsonListMapTypeHandler;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@TableName("invoices")
public class Invoices {
    @TableId("ticket_id")
    private Long ticketId;  // 主键且外键关联tickets.id
    private String invoiceCode;
    private String invoiceNumber;
    private String printedInvoiceCode;
    private String printedInvoiceNumber;
    private String invoiceDate;
    private String machineCode;
    private String checkCode;
    private String purchaserName;
    private String purchaserTaxNumber;
    private String purchaserContactInfo;
    private String purchaserBankAccountInfo;
    private String passwordArea;
    private String invoiceAmountPreTax;
    private String invoiceTax;
    private String totalAmountInWords;
    private String totalAmount;
    private String sellerName;
    private String sellerTaxNumber;
    private String sellerContactInfo;
    private String sellerBankAccountInfo;
    private String recipient;
    private String reviewer;
    private String drawer;
    private String remarks;
    private String title;
    private String formType;
    private String invoiceType;
    private String specialTag;
    @TableField(typeHandler = JsonListMapTypeHandler.class)
//    private Map<String, Object> invoiceDetails;  // JSON字段
    private List<Map<String, Object>> invoiceDetails;  // JSON字段

    public Map<String, Object> data_findbyuserid() {
        Map<String, Object> data = new HashMap<>();
        data.put("invoiceDetails", invoiceDetails); // 将 invoiceDetails 数据放入 Map
        // 继续添加其他字段
        return data;
    }

}