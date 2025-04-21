package com.example.myinvoice.server.OCR;

import com.aliyun.ocr_api20210707.models.RecognizeMixedInvoicesRequest;
import com.aliyun.ocr_api20210707.models.RecognizeMixedInvoicesResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.exception.enums.ErrorCodeEnum;
import com.example.myinvoice.util.AliClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class AliOcrService {

    //原始输出，未将接口返回的数据转换为对象的方法
    public String processInvoiceRaw(MultipartFile file) throws Exception {
        com.aliyun.ocr_api20210707.Client client = AliClient.createClient();
        InputStream bodyStream = file.getInputStream();
        RecognizeMixedInvoicesRequest request = new RecognizeMixedInvoicesRequest()
                .setBody(bodyStream)
                .setMergePdfPages(true)
                .setPageNo(1);
        RuntimeOptions runtime = new RuntimeOptions();
        RecognizeMixedInvoicesResponse response = client.recognizeMixedInvoicesWithOptions(request, runtime);

        if (response.getStatusCode() == 200) {
            // 直接返回原始JSON字符串
            return response.getBody().getData();
        }
        throw new BusinessException(ErrorCodeEnum.OCR_RECOGNITION_FAILED);

    }

//    private AliOcrResponse parseResponse(String jsonResponse) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(jsonResponse, AliOcrResponse.class);
//    }

    //
//    public AliOcrResponse processInvoice(MultipartFile file) throws Exception {
//        com.aliyun.ocr_api20210707.Client client = AliClient.createClient();
//
////        Client client = Sample.createClient();
//        InputStream bodyStream = file.getInputStream();
//
//        RecognizeMixedInvoicesRequest request = new RecognizeMixedInvoicesRequest()
//                .setBody(bodyStream)
//                .setMergePdfPages(true)
//                .setPageNo(1);
//        RuntimeOptions runtime = new RuntimeOptions();
//        RecognizeMixedInvoicesResponse response = client.recognizeMixedInvoicesWithOptions(request, runtime);
//
//        if (response.getStatusCode() == 200) {
////            System.out.println("原始响应:");
////            System.out.println(Common.toJSONString(response.getBody()));
//            System.out.println("原始响应:222222222222222222222222");
//
//            System.out.println(response.getBody().getData());
//
//
//            return parseResponse(response.getBody().getData());
//
//        }
//        throw new Exception("OCR识别失败，状态码：" + response.getStatusCode());
//    }
}