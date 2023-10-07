package com.example.ecommerc.service.impl;


import com.example.ecommerc.entity.Order;
import com.example.ecommerc.entity.User;
import com.example.ecommerc.service.EmailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@AllArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {
    //@Resource
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(Order order, User user) {
        try{
//            MimeMessage message = javaMailSender.createMimeMessage();
//            message.setFrom("minhquy3107@gmail.com");
//            MimeMessageHelper helper = new MimeMessageHelper(message,true);
//            String contextMail = "Thank you for your order. Click on the link to show your order detail: " + "http://localhost:8082/user/order/detail/" + order.getId()
//                    +" \nTên: "+user.getName()
//                    +"\nĐịa chỉ: "+user.getAddress()
//                    +"\nThành tiền: "+order.getTotalPrice() + "VNĐ"
//                    +"\nTrạng thái: "+order.getStatusShipping()
//                    +"\nNgày mua: "+order.getOrderDate();
//            helper.setTo(user.getEmail());
//            helper.setSubject("Email confirm order by " + user.getName());
//            helper.setText(contextMail,true);
//            javaMailSender.send(message);
            String subject = "Email confirm order by " + user.getName();
            String senderName = "ECOMMERCE MBSHOP";
            String mailContent = "Thank you for your order. Click on the link to show your order detail: " + "http://localhost:8080/user/order/detail/" + order.getId()
                    +" \nTên: "+user.getName()
                    +"\nĐịa chỉ: "+user.getAddress()
                    +"\nThành tiền: "+order.getTotalPrice() + "VNĐ"
                    +"\nTrạng thái: "+order.getStatusShipping()
                    +"\nNgày mua: "+order.getOrderDate();
            MimeMessage message = javaMailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom("minhquy3107@gmail.com", senderName);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);
            javaMailSender.send(message);
        } catch(MessagingException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
