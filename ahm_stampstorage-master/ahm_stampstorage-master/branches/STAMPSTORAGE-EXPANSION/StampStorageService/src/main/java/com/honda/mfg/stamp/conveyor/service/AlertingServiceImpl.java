package com.honda.mfg.stamp.conveyor.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 4/4/12 Time: 10:52 AM To
 * change this template use File | Settings | File Templates.
 */
public class AlertingServiceImpl implements AlertingServices {

	private JavaMailSender mailSender;
	private SimpleMailMessage simpleMailMessage;
	private String env;

	public AlertingServiceImpl(JavaMailSender mailSender, SimpleMailMessage simpleMailMessage, String envname) {
		this.mailSender = mailSender;
		this.simpleMailMessage = simpleMailMessage;
		this.env = envname;
	}

	@Override
	public void sendEmail(String emailAddress, String message) {
		SimpleMailMessage msg = new SimpleMailMessage(simpleMailMessage);
		msg.setTo(emailAddress);
		msg.setText(message);
		msg.setSubject(simpleMailMessage.getSubject() + "---" + this.env);
		try {
			mailSender.send(msg);
		} catch (MailSendException ex) {
			ex.printStackTrace();
		}
	}
}
