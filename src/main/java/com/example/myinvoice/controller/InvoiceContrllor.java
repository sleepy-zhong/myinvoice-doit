package com.example.myinvoice.controller;

import com.example.myinvoice.Entity.DTO.ApiResult;
import com.example.myinvoice.server.Invoice.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.exception.enums.ErrorCodeEnum;
@RestController
@RequestMapping("/api/getTicket")
public class InvoiceContrllor {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/user/{userId}")//仅查询当前用户的票据
    @Operation(summary = "仅查询当前用户的票据")
    public ApiResult<List<Map<String, Object>>> getFullTickets(@PathVariable Long userId) {
        List<Map<String, Object>> data = ticketService.getFullTicketsByUserId(userId);//仅匹配invoice类型的票据
        return ApiResult.success(data); // 空数据也视为成功


    }
    @GetMapping("/finace/{userId}")
    @Operation(summary = "仅查询invoice类型,限管理员和财务")
    public ApiResult<List<Map<String, Object>>> financegetTickets(@PathVariable Long userId) {
        List<Map<String, Object>> data = ticketService.getInvoiceforapprove(userId);//仅查询invoice类型，如果查其他类型使用getTicketsforapprove()
        return ApiResult.success(data); // 空数据也视为成功


    }


    @GetMapping("/finace/{userId}/{ticketId}")
    @Operation(summary = "仅查询invoice类型的分表数据,限管理员和财务")
    public ApiResult<Map<String, Object>> getInvoiceDetail(@PathVariable Long userId,
                                                           @PathVariable Long ticketId) {
        Map<String, Object> data = ticketService.getInvoiceDetail(userId,ticketId);
        return ApiResult.success(data); // 空数据也视为成功


    }
    @GetMapping("/ocr/{userId}/{ticketId}")
    @Operation(summary = "仅查询id票据的原始ocr数据，仅管理员和财务")
    public ApiResult<Object> getOcrData(
            @PathVariable Long userId,
            @PathVariable Long ticketId) {

            Object ocrData = ticketService.getOcrDataByTicketId(userId, ticketId);
            return ApiResult.success(ocrData);
    }
    @PutMapping("/resetStatus/{userId}/{ticketId}")
    @Operation(summary = "将票据状态重置为pending")
    public ApiResult<String> resetTicketStatus(
            @PathVariable Long userId,
            @PathVariable Long ticketId) {
        try {
            boolean success = ticketService.resetTicketToPending(userId, ticketId);
            return success ?
                    ApiResult.success("状态重置成功") :
                    ApiResult.error(400, "操作失败");
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        }
    }
    // 在InvoiceContrllor.java中添加以下代码
    @GetMapping("/department/{department}")
    @Operation(summary = "按部门查询发票类型票据（限管理员和财务）")
    public ApiResult<List<Map<String, Object>>> getDepartmentInvoices(
            @PathVariable String department){
        List<Map<String, Object>> data = ticketService.getInvoicesByDepartment(department);
        return ApiResult.success(data);
    }
}
