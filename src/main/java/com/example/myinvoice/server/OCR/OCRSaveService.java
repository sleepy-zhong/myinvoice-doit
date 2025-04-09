
// InvoiceService.java
package com.example.myinvoice.server.OCR;

import com.example.myinvoice.Entity.*;
        import com.example.myinvoice.enums.InvoiceType;
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
    public void processOcrData(String ocrJson, Long userId) throws Exception {
        JsonNode rootNode = objectMapper.readTree(ocrJson);
//        System.out.println("打印出rootNode的内如如下————————————————————————————————");
//        System.out.println(rootNode);
        JsonNode subMsgs = rootNode.get("subMsgs");
//        System.out.println("打印出submsgs的内如如下————————————————————————————————");
//        System.out.println(subMsgs);

        if (subMsgs.isEmpty()) {
            throw new IllegalArgumentException("OCR数据缺少subMsgs");
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
        ticketMapper.insert(ticket);
        System.out.println(ticket.getId());
        System.out.println(dataNode);
        // 保存分表
        switch (type) {
            case VAT_INVOICE:
                invoicesMapper.insert(OcrFieldExtractor.extractVatInvoice(dataNode, ticket.getId()));
//                System.out.println(OcrFieldExtractor.extractVatInvoice(dataNode, ticket.getId()));
                break;
            case TRAIN_TICKET:
                trainTicketMapper.insert(OcrFieldExtractor.extractTrainTicket(dataNode, ticket.getId()));
                break;
            case AIR_ITINERARY:
                airItineraryMapper.insert(OcrFieldExtractor.extractAirItinerary(dataNode, ticket.getId()));
                break;
            default:
                throw new UnknownInvoiceTypeException(typeName);
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