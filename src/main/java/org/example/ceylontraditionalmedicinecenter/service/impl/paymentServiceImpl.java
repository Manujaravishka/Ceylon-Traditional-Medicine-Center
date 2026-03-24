package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.dto.PaymentDTO;
import org.example.ceylontraditionalmedicinecenter.entity.Payment;
import org.example.ceylontraditionalmedicinecenter.repository.PaymentRepository;
import org.example.ceylontraditionalmedicinecenter.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class paymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean savePayment(PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);
        return true;
    }

    @Override
    public List<Map<String, Object>> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(payment -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", payment.getId());
                    map.put("userEmail", payment.getUserEmail());
                    map.put("amount", payment.getAmount());
                    map.put("paymentDate", payment.getPaymentDate());
                    return map;
                })
                .collect(Collectors.toList());
    }
}

