package com.example.myinvoice.server.Invoice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myinvoice.Entity.DTO.UserModifyRequest;
import com.example.myinvoice.Entity.Ticket;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface TicketService extends IService<Ticket> {
    List<Map<String, Object>> getFullTicketsByUserId(Long userId);
    boolean reviewTicket(Long ticketId, Long userId, String result, String reason);

    @Transactional(rollbackFor = Exception.class)
    boolean resetTicketToPending(Long userId, Long ticketId);

    // 在TicketServiceImpl.java中实现
    List<Map<String, Object>> getInvoicesByDepartment(String department);

    List<Map<String, Object>> getTicketsforapprove(Long userId);

    List<Map<String, Object>> getInvoiceforapprove(Long userId);

    Object getOcrDataByTicketId(Long userId, Long ticketId);
    boolean userModifyTicket(Long userId, UserModifyRequest request);

    // 获取发票待审核列表，只有发票类型，finance和admin可以访问
    Map<String, Object> getInvoiceDetail(Long userId, Long ticketId);

    List<Map<String, Object>> getTicketOperationDetails(Long ticketId);
}
