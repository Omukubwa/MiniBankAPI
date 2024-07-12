package com.compulynx.banking.utils;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

@Service
@Slf4j
public class NotificationService {
    @Value("${org.sender}")
    private String from;

    @Value("${org.password}")
    private String pass;

    @Value("${org.host}")
    private String host;

    @Value("${org.port}")
    private String mailport;

    public void sendEmailNotification(String to, String subject, String body, String attach, byte[] attachment) throws MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", mailport);

        //Current Year
        int year = Calendar.getInstance().get(Calendar.YEAR);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        MimeMessage message = new MimeMessage(session);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <main>\n" +
                "        <section>\n" +
                "            <h2>"+subject+"</h2>\n" +
                "            <p>"+body+"</p>\n" +
                "        </section>\n" +
                "    </main>\n" +
                "\n" +
                "    <footer>\n" +
                "        <p>&copy; "+year+" Compulynx. All Rights Reserved</p>\n" +
                "    </footer>\n" +
                "</body>\n" +
                "</html>\n", true);

        DataSource report = null;
        if (attach.equalsIgnoreCase("yes")) {
            report = new ByteArrayDataSource(attachment, "application/octet-stream");
            helper.addAttachment(new SimpleDateFormat("yyyyMMdd").format(new Date()) +".pdf", report);
        }
        Transport.send(message);
    }
}
