package com.honda.galc.client.qics.view.fragments;


import java.util.Arrays;
import java.util.Collection;

import javax.swing.JButton;

import com.honda.galc.client.qics.view.constants.ActionId;
import static com.honda.galc.client.qics.view.constants.ActionId.*;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * UI component with common submit action buttons.
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
public class SubmitButtonsPanel extends ButtonPanel {

	private static final long serialVersionUID = 1L;

	public static final ActionId[] defaultButtonCodes = { DIRECT_PASS, CANCEL, SUBMIT };

	public SubmitButtonsPanel() {
		super(Arrays.asList(defaultButtonCodes));
	}

	public SubmitButtonsPanel(Collection<ActionId> actionIds) {
		super(actionIds);
	}

	public JButton getDirectPassButton() {
		return getButton(DIRECT_PASS);
	}

	public JButton getCancelButton() {
		return getButton(CANCEL);
	}

	public JButton getSubmitButton() {
		return getButton(SUBMIT);
	}

	@Override
	public void setSize() {
		setHorizontalSize();
	}

	@Override
	public void setLayout() {
		setHorizontalLayout();
	}
}
