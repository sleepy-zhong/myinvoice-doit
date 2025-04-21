package com.example.myinvoice.Entity.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

// InvoiceResponse.java

@Data
@Builder
public class InvoiceResponse {
    @JsonProperty("invoiceCode")
    private String invoiceCode;

    @JsonProperty("invoiceNumber")
    private String invoiceNumber;

    @JsonProperty("invoiceDate")
    private String invoiceDate;

    @JsonProperty("purchaserTaxNumber")
    private String purchaserTaxNumber;

    @JsonProperty("purchaserName")
    private String purchaserName;

    @JsonProperty("sellerName")
    private String sellerName;

    @JsonProperty("sellerTaxNumber")
    private String sellerTaxNumber;

    @JsonProperty("totalAmount")
    private String totalAmount;
}