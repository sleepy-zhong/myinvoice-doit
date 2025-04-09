package com.example.myinvoice.controller;

import com.example.myinvoice.server.Invoice.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/invoicefull_by_userid")
public class InvoiceContrllor {

    @Autowired
    private TicketService ticketService;
    @GetMapping("/user/{userId}")

    public List<Map<String, Object>> getFullTickets(@PathVariable Long userId) {
        return ticketService.getFullTicketsByUserId(userId);
    }


}
