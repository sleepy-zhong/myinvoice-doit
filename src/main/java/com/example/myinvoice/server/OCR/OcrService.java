package com.example.myinvoice.server.OCR;

import com.example.myinvoice.Entity.OcrResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.slf4j.Logger;

@Service
public class OcrService {
    private static final String OCR_URL = "http://127.0.0.1:8866/predict/chinese_ocr_db_crnn_mobile";
    private static final Logger logger = LoggerFactory.getLogger(OcrService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OcrResponse recognizeText(MultipartFile file) throws IOException {
        // 1. 将图片转为Base64
        String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());

        // 2. 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("images", Collections.singletonList(imageBase64));

        // 3. 创建HTTP请求
        HttpPost httpPost = new HttpPost(OCR_URL);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

        // 4. 执行请求并处理响应
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {

            String responseBody = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("原始OCR响应：{}", responseBody);
                return objectMapper.readValue(responseBody, OcrResponse.class);
                //readValue 是ObjectMapper的核心方法，用于将输入数据（如JSON字符串）转换为指定的Java对象。
                //responseBody：包含JSON数据的字符串（通常来自HTTP响应内容）
                // OcrResponse.class：目标Java类的类型，指定要将JSON转换为什么类型的对象

            } else {
//                logger.log(Level.parse("OCR服务异常: {}"), responseBody);
                logger.error("OCR服务返回错误状态码: {}，内容：{}",
                        response.getStatusLine().getStatusCode(), responseBody);
                throw new RuntimeException("OCR识别失败");

            }
        }
    }

}

//package com.example.myinvoice.server.OCR;
//
//import com.example.myinvoice.Entity.OcrResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.Base64;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//
//@Service
//public class OcrService {
//    private static final String OCR_URL = "http://127.0.0.1:8866/predict/chinese_ocr_db_crnn_mobile";
//    private static final String DATA_FILE_PATH = "src/main/resources/data.json";
//    private static final Logger logger = LoggerFactory.getLogger(OcrService.class);
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public OcrResponse recognizeText(MultipartFile file) throws IOException {
//        String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
//
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("images", Collections.singletonList(imageBase64));
//
//        HttpPost httpPost = new HttpPost(OCR_URL);
//        httpPost.setHeader("Content-Type", "application/json");
//        httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault();
//             CloseableHttpResponse response = httpClient.execute(httpPost)) {
//
//            String responseBody = EntityUtils.toString(response.getEntity());
//            if (response.getStatusLine().getStatusCode() == 200) {
//                logger.info("原始OCR响应：{}", responseBody);
//
//                // 新增：将响应内容追加到文件
//                appendResponseToFile(responseBody);
//
//                return objectMapper.readValue(responseBody, OcrResponse.class);
//            } else {
//                logger.error("OCR服务返回错误状态码: {}，内容：{}",
//                        response.getStatusLine().getStatusCode(), responseBody);
//                throw new RuntimeException("OCR识别失败");
//            }
//        }
//    }
//
//    /**
//     * 将OCR响应内容追加到data.json文件（每行一个JSON对象）
//     * @param responseBody 需要写入的JSON字符串
//     */
//    private void appendResponseToFile(String responseBody) {
//        try {
//            Path path = Paths.get(DATA_FILE_PATH);
//
//            // 确保目录存在
//            if (!Files.exists(path.getParent())) {
//                Files.createDirectories(path.getParent());
//            }
//
//            // 如果文件不存在，则创建；存在则追加
//            if (!Files.exists(path)) {
//                Files.createFile(path);
//            }
//
//            // 写入内容并换行
//            String contentToAppend = responseBody + System.lineSeparator();
//            Files.write(
//                    path,
//                    contentToAppend.getBytes(StandardCharsets.UTF_8),
//                    StandardOpenOption.APPEND
//            );
//
//        } catch (IOException e) {
//            logger.error("无法写入响应到文件: {}", DATA_FILE_PATH, e);
//        }
//    }
//}