package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.service.EmailService;
import org.springframework.stereotype.Service;

// @Service annotation used here.
@Service
public class EmailServiceImpl implements EmailService {

    // Indicates this method overrides a method from a superclass or interface.
    @Override
    public void sendDoctorRegistrationEmail(String email, String fullName) {
        // No-op placeholder: if JavaMailSender is configured, implement email sending logic here.
        System.out.println("[EMAIL] sendDoctorRegistrationEmail -> " + email + ", " + fullName);
    }
}

