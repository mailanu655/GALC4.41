package com.honda.galc.client.qics.view.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Generic action to remove element from JList.
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
public class RemoveFromListButtonAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;

	private JList list;

	public RemoveFromListButtonAction(JList list) {
		this.list = list;
	}

	public void actionPerformed(ActionEvent e) {
		DefaultListModel model = (DefaultListModel) getList().getModel();
		int ix = getList().getSelectedIndex();
		model.remove(ix);
		if (model.size() == 0) {
			Object source = e.getSource();
			if (source instanceof Component) {
				((Component) source).setEnabled(false);
			}
		}
	}
	
	protected JList getList() {
		return list;
	}
}
