package com.example.myinvoice.server.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.*;
import com.example.myinvoice.mapper.*;
import com.example.myinvoice.server.Invoice.TicketService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    @Resource
    private TrainTicketMapper trainTicketMapper;

    @Resource
    private InvoiceMapper invoiceMapper;

    @Resource
    private AirItineraryMapper airItineraryMapper;
    @Resource
    private UserMapper userMapper;

    @Resource
    private TicketReviewLogMapper logMapper;

    @Override
    public List<Map<String, Object>> getFullTicketsByUserId(Long userId) {
        // 查询主表记录
        List<Ticket> tickets = lambdaQuery()
                .eq(Ticket::getUserId, userId)
                .list();

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Ticket ticket : tickets) {
            String type = ticket.getType();  // train / flight / vat
            Long ticketId = ticket.getId();

            Map<String, Object> fullTicket = new HashMap<>();
            fullTicket.put("ticket", ticket.data_findbyuserid());  // 主表信息

            // 查询子表数据
            Map<String, Object> data = null;
            switch (type) {
                case "train_ticket":
                    TrainTicket train = trainTicketMapper.selectById(ticketId);
                    data = train != null ? train.data_findbyuserid() : null;
                    break;
                case "invoice":
                    Invoices vat = invoiceMapper.selectById(ticketId);
                    data = vat != null ? vat.data_findbyuserid() : null;
                    break;
                case "air_itinerary":
                    AirItinerary flight = airItineraryMapper.selectById(ticketId);
                    data = flight != null ? flight.data_findbyuserid() : null;
                    break;
            }

            fullTicket.put("data", data);
            resultList.add(fullTicket);
        }

        return resultList;
    }


    @Override
    public boolean reviewTicket(Long ticketId, Long reviewerId, String result, String reason) {
        // 1. 校验用户角色
        User reviewer = userMapper.selectById(reviewerId);
        if (reviewer == null || !"finance".equals(reviewer.getRole())) {
            return false;
        }

        // 2. 更新票据状态
        Ticket ticket = getById(ticketId);
        if (ticket == null || !"pending".equals(ticket.getApprovalStatus())) {
            return false;
        }

        String status = switch (result.toLowerCase()) {
            case "approved" -> "approved";
            case "rejected" -> "rejected";
            default -> null;
        };

        if (status == null) return false;

        ticket.setApprovalStatus(status);
        updateById(ticket);

        // 3. 插入审核日志
        TicketReviewLog log = new TicketReviewLog();
        log.setTicketId(ticketId);
        log.setReviewerId(reviewerId);
        log.setReviewTime((java.sql.Date) new Date());
        log.setResult("approved".equals(status) ? "通过" : "不通过");
        log.setReason(reason);

        logMapper.insert(log);

        return true;
    }

}