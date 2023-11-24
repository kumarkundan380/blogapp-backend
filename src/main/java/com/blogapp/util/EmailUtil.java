package com.blogapp.util;

import com.blogapp.enums.TokenType;
import com.blogapp.exception.BlogAppException;
import com.blogapp.model.User;
import com.blogapp.model.VerificationToken;
import com.blogapp.service.VerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.blogapp.constant.BlogAppConstant.FORGOT_PASSWORD_PATH;
import static com.blogapp.constant.BlogAppConstant.VERIFY_EMAIL_PATH;


@Component
@Slf4j
public class EmailUtil {


    private final JavaMailSender javaMailSender;


    private final VerificationTokenService verificationTokenService;


    private final String fromEmail;

    @Autowired
    public EmailUtil(JavaMailSender javaMailSender, VerificationTokenService verificationTokenService,@Value("${spring.mail.username}") String fromEmail) {
        this.javaMailSender = javaMailSender;
        this.verificationTokenService = verificationTokenService;
        this.fromEmail = fromEmail;
    }

    public void sendEmail(String toEmail, String body, String subject) {
        log.info("sendEmail method invoking");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        javaMailSender.send(message);
        log.info("sendEmail method called");
    }

    public void sendEmailWithAttachment(User user) throws UnsupportedEncodingException, MessagingException {
        log.info("sendEmailWithAttachment method invoking");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        String senderName = "BlogApp Portal";
        String subject = "Please verify your Email";
        VerificationToken verificationToken = verificationTokenService.createVerificationToken();
        verificationToken.setUser(user);
        verificationToken.setTokenType(TokenType.EMAIL_VERIFICATION);
        log.info("Verification token saved");
        verificationTokenService.saveToken(verificationToken);
        String content = getContent(user,verificationToken.getToken());
        log.info(content);
        messageHelper.setFrom(fromEmail, senderName);
        messageHelper.setTo(user.getUserName());
        messageHelper.setSubject(subject);
        messageHelper.setText(content,true);
        try {
            log.info("attachment fetching");
            FileSystemResource fileSystemResource = new FileSystemResource(ResourceUtils.getFile("classpath:logo/logo.png"));
            messageHelper.addAttachment(Objects.requireNonNull(fileSystemResource.getFilename()), fileSystemResource);
            log.info("sending mail");
            javaMailSender.send(mimeMessage);
            log.info("email sent");
        } catch (FileNotFoundException e) {
            log.error("File not found in given location"+e.getMessage());
            throw new BlogAppException("File not found in given location");
        }
        log.info("sendEmailWithAttachment method called");
    }

    public void sendPasswordResetEmail(User user) throws UnsupportedEncodingException, MessagingException {
        log.info("sendPasswordResetEmail method invoking");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        String senderName = "BlogApp Portal";
        String subject = "Please reset your password";
        VerificationToken verificationToken = verificationTokenService.createVerificationToken();
        verificationToken.setUser(user);
        verificationToken.setTokenType(TokenType.FORGOT_PASSWORD);
        log.info("Verification token saved");
        verificationTokenService.saveToken(verificationToken);
        String content = getPasswordResetContent(user,verificationToken.getToken());
        log.info(content);
        messageHelper.setFrom(fromEmail, senderName);
        messageHelper.setTo(user.getUserName());
        messageHelper.setSubject(subject);
        messageHelper.setText(content,true);
        javaMailSender.send(mimeMessage);
        log.info("email sent");
    }

    private String getContent(User user, String verificationToken) {
        log.info("getContent method invoking");
        String content = "<p>Dear " +
                user.getFirstName() +
                ",</p>" +
                "<p>Please click the link below to verify your Email.</p>" +
                "<a href='" + VERIFY_EMAIL_PATH + "/" +
                verificationToken +
                "'target='_blank' style='color: blue;text-decoration: underline !important;cursor: pointer'>" +
                "Verify your Email Id" +
                "</a>" +
                "<br>" +
                "<br>" +
                "Thank you,<br>" +
                "BlogApp";
        log.info("getContent method called");
        return content;
    }

    private String getPasswordResetContent(User user, String verificationToken) {
        log.info("getPasswordResetContent method invoking");
        String passwordResetContent = "<p>Hello " +
                user.getFirstName() +
                ",</p>" +
                "<p>You have requested to reset your password</p>" +
                "<p>Click the link bellow to change your password</p>" +
                "<a href='" + FORGOT_PASSWORD_PATH + "/" +
                verificationToken +
                "'target='_blank' style='color: blue;text-decoration: underline !important;cursor: pointer'>" +
                "Change my password" +
                "</a>" +
                "<br>" +
                "<p>Ignore this email if you do remember your password, or you have not made the request.</p><br>" +
                "Thank you,<br>" +
                "BlogApp";
        log.info("getPasswordResetContent method called");
        return passwordResetContent;
    }
}
