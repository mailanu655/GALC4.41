package com.honda.mfg.stamp.conveyor.service;

import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 4/11/12
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlertingServiceImplTest {

    @Test
    public void successfullySendEmail(){
        JavaMailSender sender = mock(JavaMailSender.class);
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        AlertingService alertingService = new AlertingServiceImpl(sender, mailMessage, "test");
        alertingService.sendEmail("a@honda.com","message");
    }

     @Test
    public void successfullyHandleMailException(){
        JavaMailSender sender = mock(JavaMailSender.class);
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        doThrow(new MailSendException("exception")).when(sender).send(Matchers.<SimpleMailMessage>any());
        AlertingService alertingService = new AlertingServiceImpl(sender, mailMessage,"test");
        alertingService.sendEmail("a@honda.com","message");
    }

     @Test
    public void successfullyHandleAnyException(){
        JavaMailSender sender = mock(JavaMailSender.class);
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        doThrow(new RuntimeException()).when(sender).send(Matchers.<SimpleMailMessage>any());
        AlertingService alertingService = new AlertingServiceImpl(sender, mailMessage,"test");
        alertingService.sendEmail("a@honda.com","message");
    }
}
