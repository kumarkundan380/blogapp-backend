package com.blogapp.util;

import com.blogapp.exception.BlogAppException;
import com.blogapp.model.User;
import com.blogapp.model.VerificationToken;
import com.blogapp.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.blogapp.constant.BlogAppConstant.BASE_PATH_USER;
import static com.blogapp.constant.BlogAppConstant.LOCAL_BASE_PATH;
import static com.blogapp.constant.BlogAppConstant.VERIFY_USER;


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

    public void sendEmailWithAttachment(User user) throws UnsupportedEncodingException, MessagingException{
        log.info("sendEmailWithAttachment method invoking");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        String senderName = "BlogApp Portal";
        String subject = "Please verify your Email";
        VerificationToken verificationToken = verificationTokenService.createVerificationToken();
        verificationToken.setUser(user);
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
            FileSystemResource fileSystemResource = new FileSystemResource(ResourceUtils.getFile("classpath:logo/loading.gif"));
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

    private String getContent(User user, String verificationToken) {
        log.info("getContent method invoking");
        String content = "<p>Dear " +
                user.getFirstName() +
                ",<br>" +
                "Please click the link below to verify your registration:<br>" +
                "<div>" +
                "<a href='" +
                LOCAL_BASE_PATH +
                BASE_PATH_USER +
                VERIFY_USER +
                "/" +
                verificationToken +
                "' target='_blank' style='margin-right: 20px;'>" +
                "<div style='height: 20px; width: 100px; padding: 10px 15px; " +
                "background-color: lightgreen; border-radius: 5px; margin-right: 20px; " +
                "display: inline-block; line-height: 20px; text-align: center; " +
                "cursor: pointer; margin-right: 20px;'>" +
                "<b>VERIFY</b>" +
                "</div>" +
                "</a>" +
                "</div> <br>" +
                "<p>This Link will expire in 15 minutes.</p><br>" +
                "Thank you,<br>" +
                "BlogApp</p>";
        log.info("getContent method called");
        return content;
    }
}
