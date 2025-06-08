package com.honda.galc.client.codebroadcast;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.mail.MailContext;
import com.honda.galc.mail.MailSender;
import com.honda.galc.property.SmtpMailPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class CodeBroadcastEmailHandler {

	public enum CodeBroadcastEmailNotificationLevel {
		NONE(0), ERROR(1), WARNING(2), MESSAGE(3);
		private final int level;
		private CodeBroadcastEmailNotificationLevel(final int level) {
			this.level = level;
		}
		public int getLevel() {
			return this.level;
		}
		public boolean isSendEmailForLevel(final CodeBroadcastEmailNotificationLevel emailNotificationLevel) {
			return this.level <= emailNotificationLevel.getLevel();
		}
		public static CodeBroadcastEmailNotificationLevel getLevelForName(final String name) {
			if (StringUtils.isEmpty(name)) { return NONE; }
			if (name.equalsIgnoreCase(ERROR.name())) { return ERROR; }
			if (name.equalsIgnoreCase(WARNING.name())) { return WARNING; }
			if (name.equalsIgnoreCase(MESSAGE.name())) { return MESSAGE; }
			return NONE;
		}
	}

	private final CodeBroadcastPropertyBean propertyBean;
	private final SmtpMailPropertyBean mailPropertyBean;

	public CodeBroadcastEmailHandler(final CodeBroadcastPropertyBean propertyBean, final String applicationId) {
		this.propertyBean = propertyBean;
		this.mailPropertyBean = PropertyService.getPropertyBean(SmtpMailPropertyBean.class, applicationId);
	}

	public boolean isSendEmail(final CodeBroadcastEmailNotificationLevel emailNotificationLevel) {
		return emailNotificationLevel.isSendEmailForLevel(CodeBroadcastEmailNotificationLevel.getLevelForName(this.propertyBean.getEmailNotificationLevel()));
	}

	public void sendEmail(final String message) {
		sendEmail(message, null);
	}
	public void sendEmail(final String message, final String messageTitle) {
		if (StringUtils.isEmpty(this.mailPropertyBean.getRecipients())) {
			return;
		}
		try {
			final MailContext mailContext = new MailContext();

			final StringBuilder messageBuilder = new StringBuilder();
			final String messageTemplate = this.mailPropertyBean.getMessage().replace("\\n", "\n");
			if (StringUtils.isNotEmpty(messageTemplate)) {
				messageBuilder.append(messageTemplate);
				messageBuilder.append("\r\n");
			}
			if (StringUtils.isNotEmpty(messageTitle)) {
				messageBuilder.append("[");
				messageBuilder.append(messageTitle);
				messageBuilder.append("] ");
			}
			messageBuilder.append(message);

			mailContext.setMessage(messageBuilder.toString());
			mailContext.setSubject(this.mailPropertyBean.getSubject());
			mailContext.setHost(this.mailPropertyBean.getHost());
			mailContext.setSender(this.mailPropertyBean.getSender());
			mailContext.setRecipients(this.mailPropertyBean.getRecipients());
			mailContext.setTimeout(this.mailPropertyBean.getConnectionTimeout() * 1000);

			MailSender.sendAsync(mailContext); 
		} catch(Exception e) {
			Logger.getLogger().error(e, "Unable to send " + this.mailPropertyBean.getSubject() + " email: " + message );
		}
	}
}
