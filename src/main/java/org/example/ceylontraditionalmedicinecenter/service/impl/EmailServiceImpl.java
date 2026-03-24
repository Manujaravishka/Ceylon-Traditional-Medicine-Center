package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendDoctorRegistrationEmail(String email, String fullName) {
        // No-op placeholder: if JavaMailSender is configured, implement email sending logic here.
        System.out.println("[EMAIL] sendDoctorRegistrationEmail -> " + email + ", " + fullName);
    }
}

