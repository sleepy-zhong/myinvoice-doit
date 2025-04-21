// InvoiceType.java
package com.example.myinvoice.exception.enums;

import com.example.myinvoice.Entity.*;

public enum InvoiceType {
    VAT_INVOICE("invoice", Invoices.class),
    TRAIN_TICKET("train_ticket", TrainTicket.class),
    AIR_ITINERARY("air_itinerary", AirItinerary.class);

    private final String typeName;
    private final Class<?> entityClass;

    InvoiceType(String typeName, Class<?> entityClass) {
        this.typeName = typeName;
        this.entityClass = entityClass;
    }

    public static InvoiceType fromTypeName(String typeName) {
        for (InvoiceType type : values()) {
            if (type.typeName.equals(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知票据类型: " + typeName);
    }

    // Getters
    public String getTypeName() { return typeName; }
    public Class<?> getEntityClass() { return entityClass; }
}