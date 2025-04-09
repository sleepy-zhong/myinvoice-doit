package com.example.myinvoice.controller;

import com.example.myinvoice.Entity.DTO.RegisterRequest;
import com.example.myinvoice.Entity.User;
import com.example.myinvoice.server.Invoice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("注册成功");
    }


    @GetMapping
    public List<User> listUsers() {
        return userService.list();
    }

    @PostMapping
    public boolean addUser(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.removeById(id);
    }
}
