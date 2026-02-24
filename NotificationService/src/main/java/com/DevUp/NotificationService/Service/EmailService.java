package com.DevUp.NotificationService.Service;

import com.DevUp.NotificationService.Entity.Notification;
import com.DevUp.NotificationService.Listener.AppointmentEvent;
import com.DevUp.NotificationService.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Service
public class EmailService {


    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender mailSender;

//    public void sendEmail(String to, String subject, String text) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(to);
//        msg.setSubject(subject);
//        msg.setText(text);
//
//        mailSender.send(msg);
//    }

    public void sendEmailHtml(String to, String subject, String htmlContent) {
        JavaMailSender javaMailSender;
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true enables HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    public void logNotification(AppointmentEvent event){
        if(notificationRepository.existsByAppointmentId(event.getAppointmentId())){
            return;
        }
        Notification new_notification= new Notification();
        new_notification.setAppointmentId(event.getAppointmentId());
        new_notification.setStatus("PENDING");
        new_notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(new_notification);
    }
}
