package com.example.myinvoice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myinvoice.Entity.User;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

        @Select("SELECT * FROM users WHERE username = #{username}")
        User findByUsername(String username);


}
