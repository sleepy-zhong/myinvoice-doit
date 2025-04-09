package com.example.myinvoice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myinvoice.Entity.Invoices;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoices> {}
