package com.honda.galc.client.teamleader.scrap;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.HoldResult;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>UnscrapPanel</code> is ... .
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
 * @created Aug 21, 2013
 */
public class UnscrapPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox productTypeComboBox;
	private JTextField inputNumberTextField;

	private JTextField associateIdTextField;
	private JTextField reasonTextField;

	private ObjectTablePane<HoldResult> holdPane;

	private JButton submitButton;
	private JButton nextButton;

	private UnscrapController controller;

	public UnscrapPanel(TabbedMainWindow mainWindow) {
		super("Unscrap", KeyEvent.VK_U, mainWindow);
		this.controller = new UnscrapController(this);
		initView();
		mapActions();
		initData();
	}

	@Override
	public void onTabSelected() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getInputNumberTextField().requestFocus();
			}
		});
	}

	public void actionPerformed(ActionEvent arg0) {

	}

	protected void initView() {
		setLayout(new MigLayout("", "[max,fill]"));
		add(createInputPanel(), "wrap");
		this.holdPane = createHoldPanel();
		add(getHoldPane(), "height max");
	}

	protected void mapActions() {
		getProductTypeComboBox().addActionListener(getController());
		getInputNumberTextField().addActionListener(getController());
		getInputNumberTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getInputNumberTextField()));

		getAssociateIdTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getAssociateIdTextField()));
		getReasonTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getReasonTextField()));

		getSubmitButton().addActionListener(getController());
		getNextButton().addActionListener(getController());
	}

	protected void initData() {
		List<ProductType> types = getController().getProductTypes();
		for (ProductType type : types) {
			getProductTypeComboBox().addItem(type);
		}
	}

	// === factory methods === //
	protected JPanel createInputPanel() {
		JPanel panel = new JPanel();
		UiFactory factory = UiFactory.create(Fonts.DIALOG_PLAIN_18, Fonts.DIALOG_PLAIN_20, Fonts.DIALOG_PLAIN_18);
		panel.setLayout(new MigLayout("insets 0", "[fill]10[fill]20[fill]10[max,fill]20[]"));
		this.productTypeComboBox = new JComboBox();
		getProductTypeComboBox().setFont(Fonts.DIALOG_PLAIN_30);

		PropertyComboBoxRenderer<ProductType> renderer = new PropertyComboBoxRenderer<ProductType>(ProductType.class, "productName");
		getProductTypeComboBox().setRenderer(renderer);

		this.inputNumberTextField = UiFactory.getInputLarge().createTextField(17, TextFieldState.EDIT);
		getInputNumberTextField().setFont(Fonts.DIALOG_PLAIN_36);

		this.associateIdTextField = factory.createTextField(TextFieldState.DISABLED);
		getAssociateIdTextField().setDocument(new LimitedLengthPlainDocument(11));
		this.reasonTextField = factory.createTextField(TextFieldState.DISABLED);
		getReasonTextField().setDocument(new LimitedLengthPlainDocument(80));

		this.submitButton = factory.createButton("Unscrap", false);
		this.nextButton = factory.createButton("Next", false);
		getSubmitButton().setFont(Fonts.DIALOG_PLAIN_22);
		getNextButton().setFont(getSubmitButton().getFont());

		panel.add(getProductTypeComboBox(), "width 120::");
		panel.add(getInputNumberTextField(), "span 3");
		panel.add(getNextButton(), "width 120!, height 50!, wrap 20");

		panel.add(factory.createLabel("Associate Id", JLabel.RIGHT));
		panel.add(getAssociateIdTextField(), "width 150::");
		panel.add(factory.createLabel("Reason"));
		panel.add(getReasonTextField());
		panel.add(getSubmitButton(), "width 120!, height 50!, wrap");
		return panel;
	}

	protected ObjectTablePane<HoldResult> createHoldPanel() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Hold Type", "id.holdType");
		mapping.put("Hold Reason", "holdReason");
		mapping.put("Hold ID", "holdAssociateNo");
		mapping.put("Hold Name", "holdAssociateName");
		mapping.put("Hold Phone", "holdAssociatePhone");
		mapping.put("Hold Pager", "holdAssociatePager");
		mapping.put("Release Flag", "releaseFlag");
		mapping.put("Release Reason", "releaseReason");
		mapping.put("Release ID", "releaseAssociateNo");
		mapping.put("Release Phone", "releaseAssociatePhone");
		mapping.put("Release Pager", "releaseAssociatePager");
		ObjectTablePane<HoldResult> pane = new ObjectTablePane<HoldResult>(mapping.get(), true, true);
		pane.getTable().setName("holdHistoryPanel");

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (column == 1) {
					if (value instanceof Number) {
						Number num = (Number) value;
						if (0 == num.intValue()) {
							value = String.format("Now(%s)", value);
						} else if (1 == num.intValue()) {
							value = String.format("At Shipping(%s) ", value);
						}
					}
				} else if (column == 7) {
					if (value instanceof Number) {
						Number num = (Number) value;
						if (1 == num.intValue()) {
							value = "Yes";
						} else {
							value = "No";
						}
					}
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};
		pane.setCellRenderer(renderer);
		return pane;
	}

	// === get/set === //
	protected JComboBox getProductTypeComboBox() {
		return productTypeComboBox;
	}

	protected JTextField getInputNumberTextField() {
		return inputNumberTextField;
	}

	protected JTextField getAssociateIdTextField() {
		return associateIdTextField;
	}

	protected JTextField getReasonTextField() {
		return reasonTextField;
	}

	protected ObjectTablePane<HoldResult> getHoldPane() {
		return holdPane;
	}

	protected JButton getSubmitButton() {
		return submitButton;
	}

	protected JButton getNextButton() {
		return nextButton;
	}

	protected UnscrapController getController() {
		return controller;
	}
}
