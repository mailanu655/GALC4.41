package com.honda.galc.client.qics.view.fragments;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.ui.component.Fonts;


/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Base for simple button panel.
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
public class ButtonPanel extends JPanel {

	private static final long serialVersionUID = 1L;

  private Collection<ActionId> actionIds;

	private Map<ActionId, JButton> buttons;

	private int elementWidth = 220;
	private int elementHeight = 35;
	private int horizontalSpace = 5;
	private int verticalSpace = 5;

	public ButtonPanel() {
		initialize();
	}

	public ButtonPanel(Collection<ActionId> actionId) {
		this.actionIds = actionId;
		initialize();
	}

	protected void initialize() {
		buttons = new HashMap<ActionId, JButton>();
		if (actionIds == null) {
			actionIds = Arrays.asList(ActionId.values());
		}
		for (ActionId actionId : actionIds) {
			JButton button = createButton();
			button.setName(actionId.name());
			getButtons().put(actionId, button);
			add(button);
		}
		setLayout();
		setSize();
	}

	protected JButton createButton() {
		JButton button = new JButton();
		button.setFont(Fonts.DIALOG_PLAIN_18);
		return button;
	}

	protected Collection<ActionId> getActionIds() {
		return actionIds;
	}

	public Map<ActionId, JButton> getButtons() {
		return buttons;
	}

	public JButton getButton(ActionId actionId) {
		JButton button = getButtons().get(actionId);
		if (button == null) {
			button = createButton();
			button.setName(actionId.name());
			getButtons().put(actionId, button);
		}
		return button;
	}

	public void setSize() {
		setVerticalSize();
	}

	public void setVerticalSize() {
		setSize(getElementWidth(), getComponentCount() * getElementHeight());
	}

	public void setHorizontalSize() {
		setSize(getComponentCount() * getElementWidth(), (getElementHeight()));
	}

	public void setLayout() {
		setVerticalLayout();
	}

	protected void setVerticalLayout() {
		setLayout(new GridLayout(getComponentCount(), 1, getHorizontalSpace(), getVerticalSpace()));
	}

	protected void setHorizontalLayout() {
		setLayout(new GridLayout(1, getComponentCount(), getHorizontalSpace(), getVerticalSpace()));
	}

	public int getElementHeight() {
		return elementHeight;
	}

	public void setElementHeight(int elementHeight) {
		this.elementHeight = elementHeight;
	}

	public int getElementWidth() {
		return elementWidth;
	}

	public void setElementWidth(int elementWidth) {
		this.elementWidth = elementWidth;
	}

	public int getHorizontalSpace() {
		return horizontalSpace;
	}

	public void setHorizontalSpace(int horizontalSpace) {
		this.horizontalSpace = horizontalSpace;
	}

	public int getVerticalSpace() {
		return verticalSpace;
	}

	public void setVerticalSpace(int verticalSpace) {
		this.verticalSpace = verticalSpace;
	}

	public void setButtonsEnabled(boolean enabled) {
		if (getButtons() == null || getButtons().isEmpty()) {
			return;
		}
		for (JButton button : getButtons().values()) {
			button.setEnabled(enabled);
		}
	}
}
