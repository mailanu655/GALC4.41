package com.honda.galc.client.product.controller.listener;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;


/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ActionAdapter</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
public class ActionAdapter<T extends JPanel> extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private BaseListener<T> command;

	public ActionAdapter(String text, int keyEvent, BaseListener<T> command) {
		this.command = command;
		putValue(NAME, text);
		putValue(MNEMONIC_KEY, keyEvent);
	}

	public void actionPerformed(ActionEvent e) {
		getCommand().actionPerformed(e);
	}

	protected BaseListener<T> getCommand() {
		return command;
	}

	public T getView() {
		return getCommand().getView();
	}
}
