package com.example.myinvoice.server.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.TicketOperationLog;
import com.example.myinvoice.mapper.TicketOperationLogMapper;
import com.example.myinvoice.server.Invoice.TicketReviewLogService;
import org.springframework.stereotype.Service;

@Service
public class TicketReviewLogServiceImpl extends ServiceImpl<TicketOperationLogMapper, TicketOperationLog> implements TicketReviewLogService {}