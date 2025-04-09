package com.example.myinvoice.server.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.TicketReviewLog;
import com.example.myinvoice.mapper.TicketReviewLogMapper;
import com.example.myinvoice.server.Invoice.TicketReviewLogService;
import org.springframework.stereotype.Service;

@Service
public class TicketReviewLogServiceImpl extends ServiceImpl<TicketReviewLogMapper, TicketReviewLog> implements TicketReviewLogService {}