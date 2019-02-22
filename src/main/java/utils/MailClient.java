package utils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class MailClient {

    private Properties props = new Properties();
    private String to;
    private String from;
    private String password;


    public MailClient(String to, String from, String password) {
        this.to = to;
        this.from = from;
        this.password = password;


        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.localhost", "yoursite.com");
    }


    public void send(String subject, String meesageContent) throws MessagingException, UnsupportedEncodingException {
        Session s = Session.getInstance(props, null);
        s.setDebug(true);

        MimeMessage message = new MimeMessage(s);
        InternetAddress from = new InternetAddress(this.from, "automation");
        InternetAddress to = new InternetAddress(this.to);
        message.setSentDate(new Date());
        message.setFrom(from);
        message.addRecipient(Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(meesageContent, "text/plain");
        Transport tr = s.getTransport("smtp");
        tr.connect("smtp.gmail.com", this.from, password);
        message.saveChanges();
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();


    }


}
