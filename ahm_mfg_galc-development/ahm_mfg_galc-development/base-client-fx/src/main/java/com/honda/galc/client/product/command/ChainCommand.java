package com.honda.galc.client.product.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ChainCommand</code> is ... .
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
 * @author L&T Infotech
 */
public class ChainCommand {

	private boolean shortCircuit;
	private String propertyName;
	private List<Command> commands;

	public List<String> execute(String value) {
		List<String> messages = new ArrayList<String>();
		return execute(messages, value);
	}

	public List<String> execute(List<String> messages, String value) {
		if (getCommands() == null || getCommands().isEmpty()) {
			return messages;
		}
		for (Command c : getCommands()) {
			if (!c.execute(value)) {
				String errorMessage = c.getMessage(getPropertyName());
				messages.add(errorMessage);
				if (isShortCircuit()) {
					break;
				}
			}
		}
		return messages;
	}

	public static ChainCommand create(List<Command> commands, String propertyName) {
		ChainCommand chainCommand = new ChainCommand();
		chainCommand.setPropertyName(propertyName);
		chainCommand.setCommands(commands);
		return chainCommand;
	}

	public static ChainCommand create(String propertyName, Command... commands) {
		ChainCommand chainCommand = new ChainCommand();
		chainCommand.setPropertyName(propertyName);
		if (commands != null) {
			List<Command> list = Arrays.asList(commands);
			chainCommand.setCommands(list);
		}
		return chainCommand;
	}

	// === get/set === //
	protected String getPropertyName() {
		return propertyName;
	}

	protected void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	protected List<Command> getCommands() {
		return commands;
	}

	protected void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	protected boolean isShortCircuit() {
		return shortCircuit;
	}

	public void setShortCircuit(boolean shortCircuit) {
		this.shortCircuit = shortCircuit;
	}
}
