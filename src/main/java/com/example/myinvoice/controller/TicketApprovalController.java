package com.example.myinvoice.controller;

import com.example.myinvoice.Entity.DTO.ApiResult;
import com.example.myinvoice.Entity.DTO.ReviewRequest;
import com.example.myinvoice.Entity.DTO.UserModifyRequest;
import com.example.myinvoice.server.Invoice.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.exception.enums.ErrorCodeEnum;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "票据审核接口")
public class TicketApprovalController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/review/{userId}")
    @Operation(summary = "票据审核,只能处理状态为peddling的票据，处理为rejected，approved")
    public ApiResult<String> reviewTicket(
            @RequestBody ReviewRequest request,
            @PathVariable("userId") Long userId
    ) {
        try {
            boolean success = ticketService.reviewTicket(
                    request.getTicketId(),
                    userId,
                    request.getResult(),
                    request.getReason()
            );

            return success
                    ? ApiResult.success("审核成功")
                    : ApiResult.error(ErrorCodeEnum.REVIEW_STATUS_INVALID.getCode(),
                    ErrorCodeEnum.REVIEW_STATUS_INVALID.getMessage());
        }  catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResult.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(),
                    ErrorCodeEnum.SYSTEM_ERROR.getMessage());
        }
    }
    @PutMapping("/user-modify/{userId}")
    @Operation(summary = "修改票据信息")
    public ApiResult<String> userModifyTicket(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody UserModifyRequest request
    ) {
        boolean success = ticketService.userModifyTicket(userId, request);
        return success ?
                ApiResult.success("修改成功") :
                ApiResult.error(ErrorCodeEnum.OPERATION_FAILED.getCode(),
                        ErrorCodeEnum.OPERATION_FAILED.getMessage());
    }

    @GetMapping("/operations/{ticketId}")
    @Operation(summary = "获取票据操作记录",
            description = "根据票据ID查询详细操作记录，包含操作时间、类型、原因及操作人信息")
    public ApiResult<List<Map<String, Object>>> getTicketOperations(
            @PathVariable("ticketId") Long ticketId
    ) {
        try {
            List<Map<String, Object>> operations = ticketService.getTicketOperationDetails(ticketId);
            return ApiResult.success(operations);
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResult.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(),
                    "获取操作记录失败：" + e.getMessage());
        }
    }

}
