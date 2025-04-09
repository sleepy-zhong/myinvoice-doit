package com.example.myinvoice.server.Invoice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myinvoice.Entity.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketService extends IService<Ticket> {
    List<Map<String, Object>> getFullTicketsByUserId(Long userId);
    boolean reviewTicket(Long ticketId, Long reviewerId, String result, String reason);

}
