package com.honda.galc.client.qics.view.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Generic action to clear input elements on screen.
 * on qics defect repair panel.
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
public class GenericClearScreenAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private Component[] components;

	public GenericClearScreenAction(Component[] components) {
		this.components = components;
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
	}

	public void actionPerformed(ActionEvent e) {

		if (components == null || components.length == 0) {
			return;
		}

		for (Component component : components) {

			if (component == null) {
				continue;
			}
			if (component instanceof JTextField) {
				resetComponent((JTextField) component);
			} else if (component instanceof JList) {
				resetComponent((JList) component);
			}
		}
	}

	protected void resetComponent(JTextField component) {
		component.setText("");
	}

	protected void resetComponent(JList component) {
		component.clearSelection();
	}
}
