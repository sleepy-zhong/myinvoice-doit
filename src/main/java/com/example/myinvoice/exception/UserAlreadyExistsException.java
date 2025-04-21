//package com.example.myinvoice.exception;
//
//// 用户相关异常
//public class UserAlreadyExistsException extends BusinessException {
//    public UserAlreadyExistsException(String message) {
//        super(400100, message);
//    }
//}
//
//// 权限相关异常
//public class PermissionDeniedException extends BusinessException {
//    public PermissionDeniedException() {
//        super(403100, "操作权限不足");
//    }
//}
//
//// 数据校验异常
//public class ValidationException extends BusinessException {
//    public ValidationException(String message) {
//        super(400200, message);
//    }
//}
//
//// 系统级异常
//public class SystemException extends BusinessException {
//    public SystemException(String message) {
//        super(500100, message);
//    }
//}