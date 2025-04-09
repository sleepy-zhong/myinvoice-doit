package com.example.myinvoice.server.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.Invoices;
import com.example.myinvoice.mapper.InvoiceMapper;
import com.example.myinvoice.server.Invoice.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoices> implements InvoiceService {}