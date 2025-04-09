package com.example.myinvoice.controller.OCR;

import com.example.myinvoice.server.OCR.AliOcrService;
import com.example.myinvoice.server.OCR.OCRSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AliOCRController {

    @Autowired
    private AliOcrService aliOcrService; // 变量名更正为驼峰式

    @Autowired
    private OCRSaveService ocrSaveService; // 修正空格

    @PostMapping("/recognize-invoice")
    public ResponseEntity<?> recognizeInvoice(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(aliOcrService.processInvoiceRaw(file));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadInvoice(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            String ocrJson = aliOcrService.processInvoiceRaw(file);
            ocrSaveService.processOcrData(ocrJson, userId);
            return ResponseEntity.ok("票据处理成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("处理失败: " + e.getMessage());
        }
    }
}