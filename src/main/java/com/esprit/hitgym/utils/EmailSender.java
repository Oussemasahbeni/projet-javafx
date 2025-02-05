package com.esprit.hitgym.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private String msg;
    private String to;
    private String subject;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void sendEmail(String recipient, String subject, String message) throws MessagingException {
        String from = "hit.gym@gmail.com"; // sender's email address
        String host = "localhost";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "localhost");
        properties.put("mail.smtp.port", "1025");

        Session session =
                Session.getDefaultInstance(
                        properties,
                        new Authenticator() {
                            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        "javatextmail@gmail.com",
                                        "xyjusxlufnvuijsz");
                            }
                        });

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(from));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); // Use recipient, not to
        mimeMessage.setSubject(subject); // Use the passed subject
        mimeMessage.setContent(message, "text/html"); // Set content type to "text/html"
        Transport.send(mimeMessage);

        System.out.println("Email sent successfully");
    }

}
