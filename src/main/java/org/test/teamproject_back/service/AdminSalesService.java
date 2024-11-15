package org.test.teamproject_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.teamproject_back.dto.request.ReqSearchSalesDto;
import org.test.teamproject_back.dto.response.RespGraphDataDto;
import org.test.teamproject_back.dto.response.RespSalesDto;
import org.test.teamproject_back.dto.response.RespSearchSalesDto;
import org.test.teamproject_back.entity.Payment;
import org.test.teamproject_back.entity.Product;
import org.test.teamproject_back.repository.AdminOrderMapper;
import org.test.teamproject_back.repository.PaymentsMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdminSalesService {

    @Autowired
    private PaymentsMapper paymentsMapper;

    public RespSearchSalesDto getSalesList(ReqSearchSalesDto dto) {
        String paymentStatus = "completed";
        int startIndex = (dto.getPage() - 1) * dto.getLimit();

        return RespSearchSalesDto.builder()
                .paymentList(paymentsMapper.findPaymentList(dto.getLimit(), paymentStatus, startIndex))
                .count(paymentsMapper.findPaymentCount(paymentStatus))
                .build();
    }

    public RespSalesDto getMonthSalesList(String date) {
        String paymentStatus = "completed";
        LocalDate formatDate = LocalDate.parse(date,  DateTimeFormatter.ISO_DATE);

        Long amount = paymentsMapper.findMonthPaymentList(formatDate, paymentStatus.trim())
                .stream()
                .mapToLong(Payment::getAmount)
                .sum();

        return RespSalesDto.builder()
                .paymentList(paymentsMapper.findMonthPaymentList(formatDate, paymentStatus.trim()))
                .amount(amount)
                .build();
    }

    public RespGraphDataDto getGraphData() {
        List<Product> productList = paymentsMapper.getGraphData();
        return RespGraphDataDto.builder()
                .products(productList)
                .build();
    }

}
