package com.example.myinvoice;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.myinvoice.mapper")
public class MyInvoiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyInvoiceApplication.class, args);
    }

}
