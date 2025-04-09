package com.example.myinvoice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myinvoice.Entity.AirItinerary;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
@Mapper  // 必须添加
public interface AirItineraryMapper extends BaseMapper<AirItinerary> {}