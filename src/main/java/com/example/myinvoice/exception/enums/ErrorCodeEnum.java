package com.example.myinvoice.exception.enums;

/**
 * 统一的错误码枚举定义
 */
public enum ErrorCodeEnum {


    // 通用错误
    SYSTEM_ERROR(500000, "系统异常"),
    PARAM_ERROR(400000, "参数错误"),
    NOT_FOUND(404000, "资源未找到"),
    UNAUTHORIZED(401000, "未授权访问"),
    FORBIDDEN(403000, "无权限访问"),

    // 登录相关
    LOGIN_FAILED(400100, "登录失败，用户名或密码错误"),

    // OCR相关
    OCR_FILE_FORMAT_INVALID(400400, "无效文件格式"),
    OCR_SERVICE_ERROR(500401, "图像识别服务异常"),
    OCR_INVOICE_PROCESS_ERROR(500300, "发票识别服务异常"),
    OCR_FILE_IO_ERROR(500301, "文件读写异常"),
    OCR_UPLOAD_PROCESS_ERROR(500302, "票据处理系统异常"),

    // 用户相关
//    PERMISSION_DENIED(403100, "权限不足"),
    USER_ALREADY_EXISTS(400200, "用户已存在"),
    USER_NOT_FOUND(404100, "未找到用户"),
    PHONE_ALREADY_USED(400201, "手机号已被使用"),
    // 新增错误码
    DEPARTMENT_CODE_NOT_FOUND(400300, "部门编码不存在"),
    OCR_DATA_INVALID(400401, "OCR数据格式错误"),
    //ocr识别失败
    OCR_RECOGNITION_FAILED(400400, "OCR识别失败"),
    HASH_CONFLICT(400402, "票据哈希冲突"),
    REVIEWER_INVALID(403100, "审核人身份无效"),
    REVIEW_STATUS_INVALID(400403, "审核状态不合法"),
    USERNAME_ALREADY_EXISTS(400202, "用户名已存在"),
    INVOICE_TYPE_UNSUPPORTED(400404, "不支持的票据类型"),
    DATA_PROCESS_ERROR(500300, "数据解析失败"),
    RESOURCE_NOT_FOUND(404100, "资源未找到"),
    FILE_SAVE_ERROR(500300, "文件保存失败"),
    LOG_INSERT_FAILED(500300, "日志插入失败"),
    TICKET_NOT_FOUND(404100, "票据不存在"),
    PERMISSION_DENIED(403100, "无权操作此票据"),
    INVALID_TICKET_TYPE(400200, "无效的票据类型"),
    OPERATION_FAILED(400200, "操作失败"),
    INVALID_FIELD_VALUE(400300, "字段值不合法"),
    TYPE_MISMATCH(400300, "类型不匹配"),
    TICKET_ALREADY_APPROVED(400200, "票据已通过审核"),
    USER_NOT_EXIST(400200, "用户不存在"),
    UPDATE_FAILED(400200, "更新失败"),
    INVALID_FIELD(400300, "字段不合法");



    private final int code;
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
