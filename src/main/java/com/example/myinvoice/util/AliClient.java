package com.example.myinvoice.util;

public class AliClient {

    public static com.aliyun.ocr_api20210707.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId("LTAI5tH13A3Pjs58p29yrVQC")
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret("HCAzsKZNle8mzH1loWy2BZWAqBioYZ");
        config.endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";
        return new com.aliyun.ocr_api20210707.Client(config);
    }
//
//    public static void main(String[] args_) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
//        com.aliyun.ocr_api20210707.Client client = Sample.createClient();
//        // 需要安装额外的依赖库，直接点击下载完整工程即可看到所有依赖。
//        java.io.InputStream bodyStream = com.aliyun.darabonba.stream.Client.readFromFilePath("src/main/resources/static/images/002.png");
//        com.aliyun.ocr_api20210707.models.RecognizeMixedInvoicesRequest recognizeMixedInvoicesRequest = new com.aliyun.ocr_api20210707.models.RecognizeMixedInvoicesRequest()
//                .setBody(bodyStream)
//                .setMergePdfPages(true)
//                .setPageNo(1);
//        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
//        try {
//            // 复制代码运行请自行打印 API 的返回值
//            com.aliyun.ocr_api20210707.models.RecognizeMixedInvoicesResponse response = client.recognizeMixedInvoicesWithOptions(recognizeMixedInvoicesRequest, runtime);
//            if (response.getStatusCode() == 200) {
//                // 打印原始JSON响应
//                System.out.println("原始响应:");
//                System.out.println(Common.toJSONString(response.getBody()));
//                //把json数据转换成对象（Object）JSONtoObject
//
//
//
//            } else {
//                System.out.println("请求失败，状态码: " + response.getStatusCode());
//            }
//        } catch (TeaException error) {
//            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
//            // 错误 message
//            System.out.println(error.getMessage());
//            // 诊断地址
//            System.out.println(error.getData().get("Recommend"));
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        } catch (Exception _error) {
//            TeaException error = new TeaException(_error.getMessage(), _error);
//            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
//            // 错误 message
//            System.out.println(error.getMessage());
//            // 诊断地址
//            System.out.println(error.getData().get("Recommend"));
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        }
//    }
}