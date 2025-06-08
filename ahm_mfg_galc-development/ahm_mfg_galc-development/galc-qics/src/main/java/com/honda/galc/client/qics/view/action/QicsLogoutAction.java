package com.honda.galc.client.qics.view.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.qics.view.frame.QicsFrame;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>QicsLogoutAction</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Sep 16, 2009</TD>
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
public class QicsLogoutAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private QicsFrame qicsFrame;

	public QicsLogoutAction(QicsFrame qicsFrame) {
		this.qicsFrame = qicsFrame;
		putValue(Action.NAME, "Logout");
	}

	public void actionPerformed(ActionEvent e) {
		try {
			beforeExecute(e);
			execute(e);

		} finally {
			afterExecute(e);
		}
	}

	protected void beforeExecute(ActionEvent e) {
		getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	protected void execute(ActionEvent e) {
		try {
			if (getQicsFrame().isApplicationInProcessExistCheck()) {
				return;
			}
			getQicsFrame().logon();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void afterExecute(ActionEvent e) {
		getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public QicsFrame getQicsFrame() {
		return qicsFrame;
	}
}
