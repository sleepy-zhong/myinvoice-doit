package com.example.myinvoice.controller;

import com.example.myinvoice.Entity.DTO.ReviewRequest;
import com.example.myinvoice.server.Invoice.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "票据审核接口")
public class TicketApprovalController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/{ticketId}/review")
    @Operation(summary = "审核票据", description = "Finance角色审核票据为通过或不通过，并记录日志")
    public ResponseEntity<String> reviewTicket(@RequestBody ReviewRequest request, @RequestAttribute("userId") Long reviewerId) {
        boolean success = ticketService.reviewTicket(request.getTicketId(), reviewerId, request.getResult(), request.getReason());
        return success
            ? ResponseEntity.ok("审核成功")
            : ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权限或审核失败");
    }
}
