package com.example.myinvoice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myinvoice.Entity.User;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper extends BaseMapper<User> {}
