package com.example.myinvoice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略未知字段
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AliOcrResponse {
    private String algo_version; // 补充缺失字段
    private int count;
    private int height;
    private int width;
    private int orgHeight;       // 补充缺失字段
    private int orgWidth;        // 补充缺失字段
    private List<SubMsg> subMsgs;

    // 补充 sliceRect 结构
    @Data
    public static class SliceRect {
        private int x0;
        private int y0;
        private int x1;
        private int y1;
        private int x2;
        private int y2;
        private int x3;
        private int y3;
    }

    @Data
    public static class SubMsg {
        private int index;
        private String op;
        private Result result;
        private SliceRect sliceRect; // 补充字段
        private String type;         // 如 "机票行程单"、"增值税发票"、"火车票"
    }

    @Data
    public static class Result {
        private String algo_version;
        private int angle;
        private Object data;          // 动态类型，根据op类型解析为不同对象
        private int ftype;
        private int height;
        private int orgHeight;
        private int orgWidth;
        private Map<String, Object> prism_keyValueInfo;
        private SliceRect sliceRect;
        private int width;
        private List<CodeInfo> codes; // 二维码信息（火车票特有）
    }

    // 动态数据容器（根据op类型定义不同子类）
    public static class DataContainer {
        // 公共字段（如有）
    }

    // 机票行程单数据结构
    @Data
    public static class AirItineraryData extends DataContainer {
        private String internationalFlightSign;
        private String serialNumber;
        private String passengerName;
        private String idCardNumber;
        private String endorsement;
        private String fare;
        private String caacDevelopmentFund;
        private String fuelSurcharge;
        private String totalAmount;
        private String ticketNumber;
        private String validationCode;
        private String promptMessage;
        private String insurance;
        private String agentCode;
        private String issueCompany;
        private String issueDate;
        private String pnrCode;
        private String otherTaxes;
        private List<FlightInfo> flights;
    }

    // 火车票数据结构
    @Data
    public static class TrainTicketData extends DataContainer {
        private String ticketNumber;
        private String departureStation;
        private String arrivalStation;
        private String trainNumber;
        private String departureTime;
        private String seatNumber;
        private String fare;
        private String seatType;
        private String passengerInfo;
        private String passengerName;
        private String ticketCode;
        private String saleInfo;
        private String ticketGate;
        private String electronicTicketNumber;
        private String buyerName;
        private String buyerCreditCode;
        private String title;
        private String invoiceDate;
        private String remarks;
    }
    @Data
    public static class InvoiceData{
        private String invoiceCode;
//        private String invoiceNumber;
//        private String invoiceDate;
//        private String checkCode;
//        private String totalAmount;
//        private String sellerName;
//        private String sellerTaxNumber;
//        private List<InvoiceDetail> invoiceDetails;
//        // 根据需要添加其他字段

    }
    // 其他嵌套类（如FlightInfo、KeyValueInfo等需补充）
    @Data
    public static class FlightInfo {
        private String departureStation;
        private String arrivalStation;
        private String carrier;
        private String flightNumber;
        private String cabinClass;
        private String flightDate;
        private String flightTime;
        private String seatClass;
        private String validFromDate;
        private String validToDate;
        private String freeBaggageAllowance;
    }

    @Data
    public static class CodeInfo {
        private String data;
        private List<Point> points;
        private String type;
    }

    @Data
    public static class Point {
        private int x;
        private int y;
    }

//    @Data
//    public  static class KeyValueInfo{
//
//    }
    // 其他原有嵌套类（如InvoiceData等）保持不变
    // ...
}

//package com.example.myinvoice.Entity;
//
//import lombok.Data;
//import java.util.List;
//
//@Data
//public class AliOcrResponse {
//    private DataContent data;
//    private String requestId;
//
//    @Data
//    public static class DataContent {
//        private int count;
//        private int height;
//        private int width;
//        private List<SubMsg> subMsgs;
//    }
//
//    @Data
//    public static class SubMsg {
//        private int index;
//        private String op;
//        private Result result;
//    }
//
//    @Data
//    public static class Result {
//        private InvoiceData data;
//        private List<KeyValueInfo> prism_keyValueInfo;
//    }
//
//    @Data
//    public static class InvoiceData {
//        private String invoiceCode;
//        private String invoiceNumber;
//        private String invoiceDate;
//        private String checkCode;
//        private String totalAmount;
//        private String sellerName;
//        private String sellerTaxNumber;
//        private List<InvoiceDetail> invoiceDetails;
//        // 根据需要添加其他字段
//    }
//
//    @Data
//    public static class KeyValueInfo {
//        private String key;
//        private String value;
//    }
//
//    @Data
//    public static class InvoiceDetail {
//        private String itemName;
//        private String amount;
//        private String taxRate;
//        // 其他字段...
//    }
//}
