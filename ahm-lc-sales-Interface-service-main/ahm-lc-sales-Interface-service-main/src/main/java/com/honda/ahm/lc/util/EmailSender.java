package com.honda.ahm.lc.util;

import org.springframework.stereotype.Service;

import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailSender {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private JavaMailSender mailSender;
	
	 @Autowired
	    private PropertyUtil propertyUtil;

    public void sendEmail(String messageId, List<String> errorList) {
        SimpleMailMessage message = new SimpleMailMessage();
     
        message.setFrom(propertyUtil.getEmailFrom());
        message.setTo(propertyUtil.getEmailTo());
        message.setSubject(propertyUtil.getEmailSubject());
            
        StringBuffer msg = new StringBuffer();
        msg.append(messageId);
        for(String error : errorList) {
			msg.append(error).append("\n");
		}
        message.setText(msg.toString());
        logger.info("Sending email: From: " + message.getFrom() + " To: " + String.join(", ", message.getTo()) + " Subject: " + message.getSubject() + " Message Content: " + msg.toString());
        mailSender.send(message);
    }
}
