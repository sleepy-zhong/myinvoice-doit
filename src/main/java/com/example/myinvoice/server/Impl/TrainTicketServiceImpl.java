package com.example.myinvoice.server.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.TrainTicket;
import com.example.myinvoice.mapper.TrainTicketMapper;
import com.example.myinvoice.server.Invoice.TrainTicketService;
import org.springframework.stereotype.Service;

@Service
public class TrainTicketServiceImpl extends ServiceImpl<TrainTicketMapper, TrainTicket> implements TrainTicketService {}