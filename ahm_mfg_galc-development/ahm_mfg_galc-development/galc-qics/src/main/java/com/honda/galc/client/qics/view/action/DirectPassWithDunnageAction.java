package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;

import com.honda.galc.client.qics.view.screen.QicsPanel;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>DirectPassWithDunnageAction</code> is ...
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
 * <TD>Apr 7, 2009</TD>
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
public class DirectPassWithDunnageAction extends SubmitWithDunnageAction {

	private static final long serialVersionUID = 1L;

	public DirectPassWithDunnageAction(QicsPanel qicsPanel) {
		super(qicsPanel);
		init();
	}

	protected void init() {
		putValue(Action.NAME, "Direct Pass");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
	}

	@Override
	public void submitAction(ActionEvent e) {
		getQicsController().submitDirectPass();
	}

	@Override
	protected void execute(ActionEvent e) {
		super.execute(e);
	}
}
