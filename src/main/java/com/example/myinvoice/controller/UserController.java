package com.example.myinvoice.controller;

import com.example.myinvoice.Entity.DTO.RegisterRequest;
import com.example.myinvoice.Entity.DTO.UpdateProfileRequest;
import com.example.myinvoice.Entity.User;
import com.example.myinvoice.server.Invoice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return new ResponseEntity<>("用户注册成功", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("UserController addUser 错误: " + e.getMessage()); // 添加日志
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public List<User> listUsers() {
        return userService.list();
    }
    @DeleteMapping("/users/{employeeId}")
    public ResponseEntity<String> deleteUser_try(@PathVariable String employeeId) {
        try {
            boolean success = userService.deleteUserByEmployeeId(employeeId);
            if (success) {
                return ResponseEntity.ok("用户删除成功");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到对应工号的用户");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("参数错误: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // 打印堆栈信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除用户失败，请稍后再试");
        }
    }

    @DeleteMapping("/AdmindeleteUserByEmployeeId/{employeeId}")
    public ResponseEntity<?> deleteUser(@PathVariable String employeeId) {
        boolean deleted = userService.AdmindeleteUserByEmployeeId(employeeId);
        return deleted ? ResponseEntity.ok("删除成功") : ResponseEntity.badRequest().body("未找到用户");
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request, Principal principal) {
        String username = principal.getName(); // 当前登录用户名
        boolean success = userService.updateProfile(username, request);
        return success ? ResponseEntity.ok("更新成功") : ResponseEntity.badRequest().body("手机号已被使用");
    }

}
