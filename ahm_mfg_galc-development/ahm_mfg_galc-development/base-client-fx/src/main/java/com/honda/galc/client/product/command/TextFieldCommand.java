package com.honda.galc.client.product.command;

import java.util.List;

import javafx.scene.control.TextField;

import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.ui.component.TextFieldState;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TextFieldCommand</code> is ... .
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
public class TextFieldCommand {

	private AbstractController<?, ?> controller;
	private TextField textField;
	private String propertyName;
	private List<Command> commands;

	public void execute() {
		if (getTextField() == null || getCommands() == null
				|| getCommands().isEmpty()) {
			return;
		}
		for (Command c : getCommands()) {
			String value = getTextField().getText();
			if (!c.execute(value)) {
				String errorMessage = c.getMessage(getPropertyName());
				getController().addErrorMessage(getTextField(), errorMessage);
				return;
			}
		}
		TextFieldState.READ_ONLY.setState(getTextField());
	}

	public static TextFieldCommand create(AbstractController<?, ?> controller,
		TextField textField, List<Command> commands, String propertyName) {
		TextFieldCommand textFieldCommand = new TextFieldCommand();
		textFieldCommand.setController(controller);
		textFieldCommand.setTextField(textField);
		textFieldCommand.setPropertyName(propertyName);
		textFieldCommand.setCommands(commands);
		return textFieldCommand;
	}

	// === get/set === //
	public AbstractController<?, ?> getController() {
		return controller;
	}

	public void setController(AbstractController<?, ?> controller) {
		this.controller = controller;
	}

	public TextField getTextField() {
		return textField;
	}

	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public List<Command> getCommands() {
		return commands;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}
}
