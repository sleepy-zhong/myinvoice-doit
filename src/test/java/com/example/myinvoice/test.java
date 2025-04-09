package com.example.myinvoice;

import com.example.myinvoice.Entity.*;
import com.example.myinvoice.server.Invoice.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class test {
    @Autowired
    private UserService userService;

    @Test
    void testListUsers() {
        List<User> users = userService.list();
        System.out.println("直接打印-------------");
        System.out.println("Users: " + users);
        System.out.println("直接打印-------------222222");

        users.forEach(System.out::println);
    }
    @Autowired
    private TicketService ticketService;

    @Test
    void testListTicket() {
        List<Ticket> Tickets = ticketService.list();
        System.out.println("直接打印-------------");
        System.out.println("Tickets: " + Tickets);
        System.out.println("直接打印-------------222222");

        Tickets.forEach(System.out::println);
    }
    @Autowired
    private AirItineraryService airItineraryService;
    @Test
    void  findAirList(){
        List<AirItinerary> AirItinerarys = airItineraryService.list();
        System.out.println("直接打印-------------");
        System.out.println("AirItinerarys: " + AirItinerarys);
        System.out.println("直接打印-------------222222");
    }
    @Autowired
    private InvoiceService invoiceService;
    @Test
    void  InvoiceList(){
        List<Invoices> Invoicess = invoiceService.list();
        System.out.println("直接打印-------------");
        System.out.println("AirItinerarys: " + Invoicess);
        System.out.println("直接打印-------------222222");
    }
    @Autowired
    private TrainTicketService trainTicketService;
    @Test
    void  Trainticketlist(){
        List<TrainTicket> trainTickets = trainTicketService.list();
        System.out.println("直接打印-------------");
        System.out.println("AirItinerarys: " + trainTickets);
        System.out.println("直接打印-------------222222");

    }
    @Autowired
    private TicketReviewLogService ticketReviewLogService;

    @Test
    public void testSelectAllLogs() {
        List<TicketReviewLog> logs = ticketReviewLogService.list();
        System.out.println("logs: " + logs.get(0).getTicketId());

    }
    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> json = new HashMap<>();
        json.put("invoiceCode", "033001700211");
        json.put("invoiceNumber", "56894556");
        json.put("invoiceDate", "2018年01月08日");
        json.put("invoiceAmountPreTax", "66.97");
        json.put("invoiceTax", "0.03");
        json.put("totalAmount", "67.00");

        // invoiceDetails 是一个 List<Map<String, Object>>
        Map<String, Object> detailItem = new HashMap<>();
        detailItem.put("itemName", "劳务费-代驾");
        detailItem.put("quantity", "1");
        detailItem.put("unitPrice", "66.97");
        detailItem.put("amount", "66.97");
        detailItem.put("tax", "0.03");

        List<Map<String, Object>> invoiceDetails = new ArrayList<>();
        invoiceDetails.add(detailItem);

        json.put("invoiceDetails", invoiceDetails);

        // 序列化为 JSON 字符串
        String s = mapper.writeValueAsString(json);
        System.out.println(s);
    }


}