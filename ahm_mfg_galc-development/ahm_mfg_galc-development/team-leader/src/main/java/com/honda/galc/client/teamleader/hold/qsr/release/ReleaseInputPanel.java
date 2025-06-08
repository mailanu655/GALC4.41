package com.honda.galc.client.teamleader.hold.qsr.release;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.teamleader.hold.InputPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.PropertyPatternComboBoxRenderer;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.Qsr;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ReleaseInputPanel</code> is ... .
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
public class ReleaseInputPanel extends InputPanel {

	private static final long serialVersionUID = 1L;

	private LabeledComboBox qsrElement;
	private LabeledComboBox holdTypeElement;
	private JButton commandButton;

	public ReleaseInputPanel(ReleasePanel parentPanel) {
		super(parentPanel);
	}

	protected void initView() {
		super.initView();
		this.holdTypeElement = createHoldAccessTypeElement();
		this.qsrElement = createQsrElement(getHoldTypeElement());
		this.commandButton = createSelectButton(getQsrElement());
		remove(getProductTypeElement());
		setLayout(new MigLayout("insets 3", "[fill]10[fill]10[fill]10[fill]10[al right]", ""));
		add(getProductTypeElement(), "width 200::");
		add(getHoldTypeElement(), "width 300::");
		add(getQsrElement(), "width 300::");
		add(getCommandButton(), "wrap");
	}

	protected void init() {
		super.initView();
	}

	// === factory methods === //
	protected LabeledComboBox createQsrElement(Component base) {
		LabeledComboBox element = new LabeledComboBox("QSR#", true);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.getComponent().setRenderer(new PropertyPatternComboBoxRenderer<Qsr>(Qsr.class, "%s-%s-[Resp Dpt: %s]", 
				"name", "description","responsibleDepartment") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value instanceof Qsr) {
					Qsr qsr = (Qsr) value;
					setToolTipText(qsr.getDescription());
				}
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		});
		return element;
	}
	
	protected LabeledComboBox createHoldAccessTypeElement() {
		LabeledComboBox element = new LabeledComboBox("Hold Access Type ", true);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.getComponent().setRenderer(new PropertyPatternComboBoxRenderer<HoldAccessType>(HoldAccessType.class, "%s","description") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value instanceof HoldAccessType) {
					HoldAccessType holdAccessType = (HoldAccessType) value;
					setToolTipText(holdAccessType.getDescription());
				}
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		});
		return element;
	}

	protected JButton createSelectButton(Component base) {
		JButton button = new JButton("Select");
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_BOLD_16);
		button.setLocation(base.getX() + base.getWidth(), base.getY() + 5);
		return button;
	}

	// === get/set === //
	public JButton getCommandButton() {
		return commandButton;
	}

	public LabeledComboBox getQsrElement() {
		return qsrElement;
	}

	public LabeledComboBox getHoldTypeElement() {
		return holdTypeElement;
	}
	
	public JComboBox getHoldTypeComboBox() {
		return getHoldTypeElement().getComponent();
	}
	
	public JComboBox getQsrComboBox() {
		return getQsrElement().getComponent();
	}
}