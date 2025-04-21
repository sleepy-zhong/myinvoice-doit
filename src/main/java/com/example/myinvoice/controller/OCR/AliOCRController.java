package com.example.myinvoice.controller.OCR;

import com.example.myinvoice.Entity.DTO.ApiResult;
import com.example.myinvoice.Entity.DTO.InvoiceResponse;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.server.FileStorageService;
import com.example.myinvoice.server.OCR.AliOcrService;
import com.example.myinvoice.server.OCR.OCRSaveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "阿里云识别接口")

public class AliOCRController {

    @Autowired
    private AliOcrService aliOcrService; // 变量名更正为驼峰式

    @Autowired
    private OCRSaveService ocrSaveService; // 修正空格
    @Autowired
    private FileStorageService fileStorageService;

    // 发票识别接口
    @PostMapping("/recognize-invoice")
    public ApiResult<Object> recognizeInvoice(@RequestParam("file") MultipartFile file) {
        try {
            Object result = aliOcrService.processInvoiceRaw(file);
            return ApiResult.success(result);
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiResult.error(500300, "发票识别服务异常");
        }
    }

    // 票据上传处理接口
    @PostMapping("/upload")
    public ApiResult<?> uploadInvoice(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-User-Id") Long userId) {

        log.info("收到上传请求，文件大小：{}", file.getSize());
        log.info("用户ID：{}", userId);

        try {
            String imageUrl =fileStorageService.saveFile(file);

            String ocrJson = aliOcrService.processInvoiceRaw(file);
            Object result = ocrSaveService.processOcrData(ocrJson, userId,imageUrl);
            log.info("处理结果：{}", result);

            // 类型安全检查
            if (result instanceof InvoiceResponse) {
                return ApiResult.success(result);
            } else if (result instanceof Map) {
                return ApiResult.success("票据处理成功（非发票类型）");
            }
            return ApiResult.success("票据处理成功");
        } catch (BusinessException e) {
            return ApiResult.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (IOException e) {
            return ApiResult.error(500301, "文件读写异常");
        } catch (Exception e) {
            log.error("系统异常: ", e);
            return ApiResult.error(500302, "票据处理系统异常");
        }
    }
}