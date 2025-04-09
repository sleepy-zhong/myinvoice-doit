package com.example.myinvoice.server.Invoice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myinvoice.Entity.DTO.RegisterRequest;
import com.example.myinvoice.Entity.DTO.UpdateProfileRequest;
import com.example.myinvoice.Entity.User;

public interface UserService extends IService<User> {
    String generateEmployeeId(String departmentName);
    void register(RegisterRequest request);
    boolean deleteUserByEmployeeId(String employeeId);
    boolean updateProfile(String username, UpdateProfileRequest request);
    boolean AdmindeleteUserByEmployeeId(String employeeId);
}
