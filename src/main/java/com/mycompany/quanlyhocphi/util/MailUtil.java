/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlyhocphi.util;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
/**
 *
 * @author basrot
 */
public class MailUtil {
     public static void sendOTP(String toEmail, String otp) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        "qlsv.system@gmail.com",
                        "qwzoqlfezlcodizk"
                    );
                }
            });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("qlsv.system@gmail.com", "QLSV System"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject("[QLSV] OTP khôi phục mật khẩu");
        msg.setText("Mã OTP của bạn là: " + otp + "\nHiệu lực 5 phút.");

        Transport.send(msg);
    }
}
