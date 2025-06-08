package com.honda.galc.client.product.validator;

import java.text.MessageFormat;

import com.honda.galc.client.product.command.Command;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>AbstractValidator</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public abstract class AbstractValidator implements Command {

	private String propertyMessageTemplate = "Invalid {0}";
	private String detailedMessageTemplate;

	public String getMessage(String propertyName) {
		return MessageFormat.format(getMessageTemplate(), propertyName);
	}

	public String getDetailedMessage(String propertyName) {
		return MessageFormat.format(getDetailedMessageTemplate(), propertyName);
	}

	protected String getMessageTemplate() {
		return String.format("%s - %s", getPropertyMessageTemplate(), getDetailedMessageTemplate());
	}

	protected String getPropertyMessageTemplate() {
		return propertyMessageTemplate;
	}

	protected void setPropertyMessageTemplate(String propertyMessageTemplate) {
		this.propertyMessageTemplate = propertyMessageTemplate;
	}

	protected String getDetailedMessageTemplate() {
		return detailedMessageTemplate;
	}

	protected void setDetailedMessageTemplate(String detailedMessageTemplate) {
		this.detailedMessageTemplate = detailedMessageTemplate;
	}
}
