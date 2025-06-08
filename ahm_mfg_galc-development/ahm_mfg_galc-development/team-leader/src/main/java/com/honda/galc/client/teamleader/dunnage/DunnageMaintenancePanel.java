package com.honda.galc.client.teamleader.dunnage;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dunnage.DunnageTablePaneFactory;
import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.NumberInputDialog;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.service.utils.ProductTypeUtil;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DunnageMaintenancePanel</code> is ... .
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
 * @created Apr 24, 2013
 */
public class DunnageMaintenancePanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private JTextField dunnageNumberTextField;
	private JTextField productNumberTextField;
	private JButton clearButton;
	private JButton shipButton;
	private JButton printButton;

	private ObjectTablePane<Map<String, Object>> productPane;
	private JPopupMenu productTablePopupMenu;
	private NumberInputDialog reassignDialog;
	
	private DunnageMaintenanceModel model;

	public DunnageMaintenancePanel(TabbedMainWindow mainWindow) {
		super("Dunnage", KeyEvent.VK_D, mainWindow);
		this.model = new DunnageMaintenanceModel(getMainWindow().getApplicationContext());
		String productName = getMainWindow().getProductType().getProductName();
		if (!isValidConfiguration()) {
			return;
		}
		setScreenName(String.format("%s Dunnage", productName));
		initView();
		mapActions();
		mapValidators();
	}

	protected boolean isValidConfiguration() {
		ProductType productType = getMainWindow().getProductType();
		if (!ProductTypeUtil.isDunnagable(productType)) {
			String msg = String.format("Product type %s does not support Dunnage function", productType);
			getMainWindow().setErrorMessage(msg);
			return false;
		}
		if (getModel().isShippingSupported() && !getModel().isOffConfigured()) {
			String msg = "Shipping is configured but DC OFF property is not set. \nPlease define OFF_PROCESS_POINT_IDS property.";
			getMainWindow().setErrorMessage(msg);
			return false;
		}
		return true;
	}

	@Override
	public void onTabSelected() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (getDunnageNumberTextField() == null) {
					return;
				}
				if (getDunnageNumberTextField().isEnabled()) {
					getDunnageNumberTextField().requestFocus();
				} else if (getProductNumberTextField().isEnabled()) {
					getProductNumberTextField().requestFocus();
				} else if (getClearButton().isEnabled()) {
					getClearButton().requestFocus();
				}
			}
		});
	}

	public void actionPerformed(ActionEvent ae) {

	}

	// === initialization ===//
	protected void initView() {

		productTablePopupMenu = createProductTablePopupMenu();
		setName("DunnageMaintenancePanel");
		StringBuilder sb = new StringBuilder("[]3[350!,fill]5[]3[max,fill]3");
		sb.append("[90!, fill]2");
		sb.append("[90!, fill]");

		if (getModel().isShippingSupported()) {
			sb.append("2[90!, fill]");
		}

		setLayout(new MigLayout("insets 10 5 0 5, gap 0", sb.toString(), "[][]"));

		int productNumberLength = getMainWindow().getProductType().getProductIdLength();
		int dunnageNumberLength = getModel().getProperty().getDunnageNumberLength();
		UiFactory factory = UiFactory.getInfo();
		dunnageNumberTextField = factory.createTextField(dunnageNumberLength, TextFieldState.EDIT);
		productNumberTextField = factory.createTextField(productNumberLength, TextFieldState.EDIT);
		clearButton = factory.createButton("Clear", true);
		shipButton = factory.createButton("Ship", false);
		printButton = factory.createButton("Print", false);
		reassignDialog = createReassignDialog();

		add(factory.createLabel("Dunnage"));
		add(getDunnageNumberTextField(), "grow, width 350!");
		add(factory.createLabel(getMainWindow().getProductType().getProductName()));
		add(getProductNumberTextField());
		add(getClearButton());
		if (getModel().isShippingSupported()) {
			add(getShipButton());
		}
		add(getPrintButton(), "wrap 10");

		productPane = createProductPane();
		add(getProductPane(), new CC().spanX(getComponentCount()).width("max").height("max").wrap("5"));
	}
	
	public void mapActions() {

		DunnageMaintenanceListener dunnageListener = new DunnageMaintenanceListener(this, getModel());

		getDunnageNumberTextField().addActionListener(dunnageListener);
		getProductNumberTextField().addActionListener(dunnageListener);
		getClearButton().addActionListener(dunnageListener);
		getShipButton().addActionListener(dunnageListener);
		getPrintButton().addActionListener(dunnageListener);
		((JMenuItem) getProductTablePopupMenu().getSubElements()[0]).addActionListener(dunnageListener);
		((JMenuItem) getProductTablePopupMenu().getSubElements()[1]).addActionListener(dunnageListener);
		getReassignDialog().getNumberTextField().addActionListener(dunnageListener);
		getReassignDialog().getSubmitButton().addActionListener(dunnageListener);
		getReassignDialog().getCancelButton().addActionListener(dunnageListener);

		MouseListener ml = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				JTextField tf = (JTextField) me.getSource();
				if (TextFieldState.READ_ONLY.isInState(tf)) {
					TextFieldState.EDIT.setState(tf);
					tf.requestFocus();
					super.mouseClicked(me);
				}
			}
		};

		getProductNumberTextField().addMouseListener(ml);

		getDunnageNumberTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getDunnageNumberTextField()));
		getProductNumberTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getProductNumberTextField()) {
			@Override
			protected void processChange() {
				super.processChange();
				getProductPane().getTable().repaint();
			}
		});

		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			public void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					int count = getProductPane().getTable().getSelectedRowCount();
					getProductTablePopupMenu().getSubElements()[0].getComponent().setEnabled(count > 0);
					getProductTablePopupMenu().getSubElements()[1].getComponent().setEnabled(!getModel().isInsertDunnageContext() && count > 0);
					getProductTablePopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		getProductPane().getTable().addMouseListener(mouseAdapter);
	}

	public void mapValidators() {
		getDunnageNumberTextField().putClientProperty("validator", getModel().createDunnageNumberValidator());
		getReassignDialog().getNumberTextField().putClientProperty("validator", getModel().createDunnageNumberValidator());
		getProductNumberTextField().putClientProperty("validator", getModel().createProductNumberValidator());
	}

	// === ui factory methods === //
	protected JPopupMenu createProductTablePopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem remove = new JMenuItem("Remove");
		remove.setName("Remove");
		JMenuItem reassign = new JMenuItem("Reassign");
		reassign.setName("Reassign");
		popup.add(remove);
		popup.add(reassign);
		return popup;
	}

	protected ObjectTablePane<Map<String, Object>> createProductPane() {

		productPane = DunnageTablePaneFactory.createDunnageMaintTablePane(getMainWindow().getProductType(), getModel().getProperty());

		productPane.setCellRenderer(new DunnageMaintTableCellRenderer());
		productPane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return productPane;
	}

	protected NumberInputDialog createReassignDialog() {
		NumberInputDialog dialog = new NumberInputDialog(this, "New Dunnage", getModel().getProperty().getDunnageNumberLength()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void processWindowEvent(WindowEvent e) {
				super.processWindowEvent(e);
				 if (e.getID() == WindowEvent.WINDOW_CLOSING) {
					 getParentPanel().getMainWindow().clearMessage();
				 }
			}
		};
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return dialog;
	}
	
	// === get/set === //
	public JTextField getDunnageNumberTextField() {
		return dunnageNumberTextField;
	}

	public JTextField getProductNumberTextField() {
		return productNumberTextField;
	}

	public ObjectTablePane<Map<String, Object>> getProductPane() {
		return productPane;
	}

	public DunnageMaintenanceModel getModel() {
		return model;
	}

	public JButton getClearButton() {
		return clearButton;
	}

	public JPopupMenu getProductTablePopupMenu() {
		return productTablePopupMenu;
	}

	protected JButton getShipButton() {
		return shipButton;
	}

	protected JButton getPrintButton() {
		return printButton;
	}

	protected NumberInputDialog getReassignDialog() {
		return reassignDialog;
	}
	
	class DunnageMaintTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			String productNumber = null;
			productNumber = getProductNumberTextField().getText();
			productNumber = StringUtils.trim(productNumber);
			Map<String, Object> map = productPane.getItems().get(row);

			ProductType productType = getMainWindow().getProductType();
			if (ProductTypeUtil.isInstanceOf(productType, DieCast.class)) {
				Object dc = table.getValueAt(row, 1);
				Object mc = table.getValueAt(row, 2);

				if (Boolean.TRUE.equals(map.get("shippable"))) {
					setBackGroundColor(productNumber.equals(dc) || productNumber.equals(mc), Color.WHITE);

				} else {
					setBackGroundColor(((productNumber.equals(dc) || productNumber.equals(mc)) && column == 0), Color.YELLOW);
				}

				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			} else {
				Object productId = table.getValueAt(row, 1);
				productId = productId instanceof String ? productId.toString().trim() : productId; 

				if (Boolean.TRUE.equals(map.get("shippable"))) {
					setBackGroundColor(productNumber.equals(productId), Color.WHITE);

				} else {
					setBackGroundColor((productNumber.equals(productId) && column == 0), Color.YELLOW);
				}

				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		}

		private void setBackGroundColor(boolean green, Color color) {
			setBackground(green ? Color.GREEN : color);
		}

	}
}
