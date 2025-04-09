package com.example.myinvoice.server.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.AirItinerary;
import com.example.myinvoice.mapper.AirItineraryMapper;
import com.example.myinvoice.server.Invoice.AirItineraryService;
import org.springframework.stereotype.Service;

@Service
public class AirItineraryServiceImpl extends ServiceImpl<AirItineraryMapper, AirItinerary> implements AirItineraryService {}