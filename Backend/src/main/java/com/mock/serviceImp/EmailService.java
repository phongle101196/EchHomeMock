package com.mock.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendVerificationEmail(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText("Please click the link below to verify your email:\n\n" +
                "http://localhost:8080/api/v1/auth/verify-email?code=" + verificationCode);
        javaMailSender.send(message);
    }
    public void sendResetPasswordEmail(String to, String resetPasswordLink) {
        // Triển khai logic gửi email chứa liên kết đặt lại mật khẩu đến địa chỉ "to"
        // Ví dụ: sử dụng thư viện JavaMailSender để gửi email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText("Please click the following link to reset your password: " + resetPasswordLink);
        javaMailSender.send(message);
    }
}
