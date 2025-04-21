package com.example.myinvoice.controller.OCR;

import com.example.myinvoice.Entity.DTO.ApiResult;
import com.example.myinvoice.Entity.OcrResponse;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.exception.enums.ErrorCodeEnum;
import com.example.myinvoice.server.OCR.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/ocr")
@CrossOrigin(origins = "*")
@Tag(name = "paddle识别接口")
public class OCRController {

    @Autowired
    private OcrService ocrService;

    @Operation(summary = "图像识别接口")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "识别成功"),
            @ApiResponse(responseCode = "400400", description = "无效文件格式"),
            @ApiResponse(responseCode = "500401", description = "图像识别服务异常")
    })
    @PostMapping("/recognize")
    public ApiResult<List<Map<String, Object>>> recognizeImage(
            @RequestParam("file") MultipartFile file) throws IOException {
        OcrResponse ocrResponse = ocrService.recognizeText(file);
        List<Map<String, Object>> results = new ArrayList<>();

        Optional.ofNullable(ocrResponse.getResults())
                .ifPresent(pages -> pages.stream()
                        .filter(Objects::nonNull)
                        .map(OcrResponse.OcrPage::getData)
                        .filter(Objects::nonNull)
                        .flatMap(List::stream)
                        .forEach(item -> {
                            Map<String, Object> resultItem = new LinkedHashMap<>();
                            resultItem.put("text", item.getText());
                            resultItem.put("confidence", item.getConfidence());
                            resultItem.put("position", item.getTextBoxPosition());
                            results.add(resultItem);
                        }));

        return ApiResult.success(results);
    }
}