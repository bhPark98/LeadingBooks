package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    // 이메일 전송
    @Override
    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createMailForm(toEmail, title, text);
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new CustomException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }
    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createMailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
