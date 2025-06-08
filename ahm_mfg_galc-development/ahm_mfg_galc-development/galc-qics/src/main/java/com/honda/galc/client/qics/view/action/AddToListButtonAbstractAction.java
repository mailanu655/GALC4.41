package com.honda.galc.client.qics.view.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Generic action to add element to JList.
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
public abstract class AddToListButtonAbstractAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private JList list;
	private Component[] inputs;

	public AddToListButtonAbstractAction(JList list, Component input) {
		this.list = list;
		this.inputs = new Component[] { input };
	}

	public AddToListButtonAbstractAction(JList list, Component[] inputs) {
		this.list = list;
		this.inputs = inputs;
	}

	// === abstract api === //
	public abstract Object createListItem();

	public void actionPerformed(ActionEvent e) {

		Object listItem = createListItem();
		
		if (!isValid(listItem)) {
			return;
		}
		
		DefaultListModel model = (DefaultListModel) getList().getModel();

		int ix = getList().getSelectedIndex();
		ix = ix > -1 ? ix + 1 : 0;

		model.insertElementAt(listItem, ix);

		getList().setSelectedIndex(ix);
		getList().ensureIndexIsVisible(ix);

		resetInput();
	}

	// === utility api === //
	protected boolean isValid(Object o) {
		return true;
	}
	
	protected void resetInput() {
		if (inputs == null || inputs.length == 0) {
			return;
		}

		for (Component component : inputs) {
			if (component instanceof JTextField) {
				((JTextField) component).setText("");
				// TODO consider different type of input s/
			}
		}
		getInput().requestFocusInWindow();
	}

	// === get/set === //
	protected JList getList() {
		return list;
	}

	protected Component getInput() {
		return inputs[0];
	}

	protected Component getInput(int ix) {
		return inputs[ix];
	}

	protected int getInputsLength() {
		int length = 0;
		if (inputs != null) {
			length = inputs.length;
		}
		return length;
	}
}
