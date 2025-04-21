
// InvoiceService.java
package com.example.myinvoice.server.OCR;

import com.example.myinvoice.Entity.*;
import com.example.myinvoice.Entity.DTO.InvoiceResponse;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.exception.enums.ErrorCodeEnum;
import com.example.myinvoice.exception.enums.InvoiceType;
import com.example.myinvoice.exception.UnknownInvoiceTypeException;
import com.example.myinvoice.mapper.*;
        import com.example.myinvoice.util.OcrFieldExtractor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OCRSaveService {

    private final TicketMapper ticketMapper;
    private final InvoiceMapper invoicesMapper;
    private final TrainTicketMapper trainTicketMapper;
    private final AirItineraryMapper airItineraryMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public Object processOcrData(String ocrJson, Long userId, String imageUrl) throws Exception {
        JsonNode rootNode = objectMapper.readTree(ocrJson);
        JsonNode subMsgs = rootNode.get("subMsgs");
        if (subMsgs.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.OCR_DATA_INVALID, "缺少subMsgs数据");
        }

        JsonNode firstMsg = subMsgs.get(0);
        String typeName = firstMsg.get("op").asText();
        InvoiceType type = InvoiceType.fromTypeName(typeName);
        JsonNode dataNode = firstMsg.get("result").get("data");
//        System.out.println("firstMsg"+firstMsg);//submsgs
//        System.out.println("typeName"+typeName );//发票类型
//        System.out.println("type"+type  );//VAT_INVOICE
//        System.out.println("dataNode"+dataNode );//json里面的data部分

        // 保存总表
        Ticket ticket = buildTicket(ocrJson, userId, typeName);
        ticket.setImageUrl(imageUrl);
        ticketMapper.insert(ticket);
        System.out.println(ticket.getId());
        System.out.println(dataNode);
//        String hash = generateHash(ocrJson);

//        // 保存分表
//        switch (type) {
//            case VAT_INVOICE ->
//                    invoicesMapper.insert(OcrFieldExtractor.extractVatInvoice(dataNode, ticket.getId()));
//            case TRAIN_TICKET ->
//                    trainTicketMapper.insert(OcrFieldExtractor.extractTrainTicket(dataNode, ticket.getId()));
//            case AIR_ITINERARY ->
//                    airItineraryMapper.insert(OcrFieldExtractor.extractAirItinerary(dataNode, ticket.getId()));
//            default ->
//                    throw new BusinessException(ErrorCodeEnum.INVOICE_TYPE_UNSUPPORTED);
//        }
        switch (type) {
            case VAT_INVOICE -> {
                // 使用代码块包裹多行操作
                Invoices invoice = OcrFieldExtractor.extractVatInvoice(dataNode, ticket.getId());
                invoicesMapper.insert(invoice);
                // 添加调试日志
                log.info("原始数据节点: {}", dataNode.toPrettyString());
                InvoiceResponse response = OcrFieldExtractor.extractVatInvoiceResponse(dataNode);
                log.info("构建的响应对象: {}", response);

                return response;
            }
            case TRAIN_TICKET -> {
                TrainTicket tickets = OcrFieldExtractor.extractTrainTicket(dataNode, ticket.getId());
                trainTicketMapper.insert(tickets);
                return Map.of("type", "train_ticket");
            }
            case AIR_ITINERARY -> {
                AirItinerary itinerary = OcrFieldExtractor.extractAirItinerary(dataNode, ticket.getId());
                airItineraryMapper.insert(itinerary);
                return Map.of("type", "air_itinerary");
            }
            default -> throw new BusinessException(ErrorCodeEnum.INVOICE_TYPE_UNSUPPORTED);
        }

    }

    private Ticket buildTicket(String ocrJson, Long userId, String type) throws Exception {
        Map<String, Object> ocrData = objectMapper.readValue(ocrJson, new TypeReference<>() {});

        return Ticket.builder()
                .userId(userId)
                .type(type)
                .uniqueHash(generateHash(ocrJson))
                .imageUrl("") // 根据实际需求设置
                .ocrJson(ocrData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .approvalStatus("PENDING")
                .build();
    }

    private String generateHash(String content) {
        // 实现哈希生成逻辑（示例用简单实现）
        return Integer.toHexString(content.hashCode());
    }
}