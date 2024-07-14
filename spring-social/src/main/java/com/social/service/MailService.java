package com.social.service;

import com.social.model.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    /**
     * Send a verification email to the new user
     *
     * @param user
     * @param userMail
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendVerificationEmail(User user, String userMail)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = userMail;
        String fromAddress = "testjavamail1406@gmail.com";
        String senderName = "NTT Social Media";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Namtayto";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = "http://localhost:5454/api/auth/verification?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }
}
