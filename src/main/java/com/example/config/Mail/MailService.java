package com.example.config.Mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    public void sendSimpleMail(String to, String subject, String text) {
        if (to == null || to.trim().isEmpty()) {
            log.warn("sendSimpleMail aborted: recipient email is null or empty");
            return;
        }
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    public void sendMimeMail(String to, String subject, String htmlBody,
                             Map<String, byte[]> attachments) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        if (attachments != null) {
            for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
                helper.addAttachment(entry.getKey(), new ByteArrayResource(entry.getValue()));
            }
        }

        mailSender.send(message);
    }
}
