package com.example.myinvoice.controller;

import com.example.myinvoice.Entity.DTO.ApiResult;
import com.example.myinvoice.Entity.DTO.OperationLogDetailDTO;
import com.example.myinvoice.server.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Tag(name = "日志接口")
public class LogController {

    private final LogService logService;

    @GetMapping("/operation/{userId}")
    @Operation(summary = "获取操作日志（仅管理员）")
    public ApiResult<List<OperationLogDetailDTO>> getOperationLogs(
//        @RequestAttribute("userId") Long userId
            @PathVariable Long userId
    ) {
        return logService.getOperationLogs(userId);
    }
}