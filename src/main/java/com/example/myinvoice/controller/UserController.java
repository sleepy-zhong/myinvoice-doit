package com.example.myinvoice.controller;

import com.aliyun.credentials.utils.StringUtils;
import com.example.myinvoice.Entity.DTO.*;
import com.example.myinvoice.Entity.User;
import com.example.myinvoice.server.Invoice.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.exception.enums.ErrorCodeEnum;
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // 用户注册
    @PostMapping("/register")
    public ApiResult<String> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return ApiResult.success("用户注册成功");
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResult.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(),
                    ErrorCodeEnum.SYSTEM_ERROR.getMessage());
        }
    }
    @GetMapping("/{id}")
    @Operation(summary = "根据用户ID获取用户信息")
    public ApiResult<UserResponse> getUserById(@PathVariable Long id) {
        try {
            UserResponse response = userService.getUserById(id);
            return ApiResult.success(response);
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResult.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(),
                    ErrorCodeEnum.SYSTEM_ERROR.getMessage());
        }
    }
        // 获取用户列表
    @GetMapping
    public ApiResult<List<User>> listUsers() {
        try {
            return ApiResult.success(userService.list());
        } catch (Exception e) {
            return ApiResult.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(),
                    ErrorCodeEnum.SYSTEM_ERROR.getMessage());
        }
    }

    // 删除用户（普通用户版）
    @DeleteMapping("/delete/{employeeId}")
    @Operation(summary = "删除用户（普通用户版）,只能删除没有票据的用户")
    public ApiResult<String> deleteUser_try(@PathVariable String employeeId) {
        try {
            boolean success = userService.deleteUserByEmployeeId(employeeId);
            return success
                    ? ApiResult.success("用户删除成功")
                    : ApiResult.error(ErrorCodeEnum.NOT_FOUND.getCode(),
                    ErrorCodeEnum.NOT_FOUND.getMessage());
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResult.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(),
                    ErrorCodeEnum.SYSTEM_ERROR.getMessage());
        }
    }

    // 管理员删除用户
    @DeleteMapping("/AdmindeleteUserByEmployeeId/{employeeId}")
    @Operation(summary = "管理员删除用户，有票据关联的用户也会被删除，同步删除票据")
    public ApiResult<String> deleteUser(@PathVariable String employeeId) {
        try {
            return userService.AdmindeleteUserByEmployeeId(employeeId)
                    ? ApiResult.success("删除成功")
                    : ApiResult.error(ErrorCodeEnum.NOT_FOUND.getCode(),
                    ErrorCodeEnum.NOT_FOUND.getMessage());
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResult.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(),
                    ErrorCodeEnum.SYSTEM_ERROR.getMessage());
        }
    }

    // 更新用户资料
    // 管理员更新接口
    @PutMapping("/admin/users/{id}")
    @Operation(summary = "管理员更新用户信息，可修改所有字段")
    public ApiResult<String> adminUpdateUser(
            @PathVariable Long id,
            @RequestBody @Valid AdminUpdateUserRequest request
    ) {
        try {
            request.setId(id);
            boolean success = userService.adminUpdateUser(request);
            return success ?
                    ApiResult.success("用户信息更新成功") :
                    ApiResult.error(400, "用户名或手机号已存在");
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResult.error(500, "系统错误，更新失败");
        }
    }

    // 普通用户更新接口（移除身份绑定）
    @PutMapping("/user/profile/{id}")
    @Operation(summary = "更新用户信息，可修改除身份绑定外的所有字段")
    public ApiResult<String> userUpdateInfo(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        try {
            userService.updateUserInfo(id, request);
            return ApiResult.success("资料更新成功");
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息，便于开发调试
            return ApiResult.error(500, "系统错误，更新失败");
        }
    }


}
