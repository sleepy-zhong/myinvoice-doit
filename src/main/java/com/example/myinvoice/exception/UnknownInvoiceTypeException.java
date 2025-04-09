// UnknownInvoiceTypeException.java
package com.example.myinvoice.exception;

public class UnknownInvoiceTypeException extends RuntimeException {
    private final String type;

    public UnknownInvoiceTypeException(String type) {
        super("未知票据类型: " + type);
        this.type = type;
    }

    public String getType() { return type; }
}