package org.example.ceylontraditionalmedicinecenter.service;

import org.example.ceylontraditionalmedicinecenter.dto.PaymentDTO;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    boolean savePayment(PaymentDTO paymentDTO);

    List<Map<String, Object>> getAllPayments();
}
