// OcrFieldExtractor.java
package com.example.myinvoice.util;

import com.example.myinvoice.Entity.*;
import com.example.myinvoice.Entity.DTO.InvoiceResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OcrFieldExtractor {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Invoices extractVatInvoice(JsonNode dataNode, Long ticketId) throws Exception {
        Invoices invoice = mapper.treeToValue(dataNode, Invoices.class);
        invoice.setTicketId(ticketId);
        return invoice;
    }

    public static TrainTicket extractTrainTicket(JsonNode dataNode, Long ticketId) throws Exception {
        TrainTicket ticket = mapper.treeToValue(dataNode, TrainTicket.class);
        ticket.setTicketId(ticketId);
        return ticket;
    }

    public static AirItinerary extractAirItinerary(JsonNode dataNode, Long ticketId) throws Exception {
        AirItinerary itinerary = mapper.treeToValue(dataNode, AirItinerary.class);
        itinerary.setTicketId(ticketId);
        return itinerary;
    }
    public static InvoiceResponse extractVatInvoiceResponse(JsonNode dataNode) {
        return InvoiceResponse.builder()
                .invoiceCode(getText(dataNode, "invoiceCode")) // 全小写匹配JSON字段
                .invoiceNumber(getText(dataNode, "invoiceNumber"))
                .invoiceDate(getText(dataNode, "invoiceDate"))
                .purchaserTaxNumber(getText(dataNode, "purchaserTaxNumber"))
                .purchaserName(getText(dataNode, "purchaserName"))
                .sellerName(getText(dataNode, "sellerName"))
                .sellerTaxNumber(getText(dataNode, "sellerTaxNumber"))
                .totalAmount(getText(dataNode, "totalAmount"))
                .build();
    }
    private static String getText(JsonNode node, String field) {
        return node.has(field) ? node.get(field).asText() : "";
    }
}