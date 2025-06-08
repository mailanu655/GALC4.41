package com.honda.galc.client.teamleader.mbpn;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;

import net.miginfocom.swing.MigLayout;

public class MbpnProductChangePanel extends TabbedPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int TEXT_FIELD_WIDTH = 150;
	private static final int TEXT_FIELD_HEIGHT = 35;

	private JTextField productIdTxtField;
	private LabeledTextField currentSpecCodeTxtField;
	private LabeledTextField orderNoTxtField;
	private LabeledTextField trackingStatusTxtField;
	private LabeledTextField lastProcessPointTxtField;

	private LabeledComboBox newSpecCodeComboBox;

	private JButton productIdBtn;
	private JButton updateBtn;
	private JButton cancelBtn;

	public MbpnProductChangePanel(TabbedMainWindow mainWindow) {
		super("MBPN Spec Change", KeyEvent.VK_M, mainWindow);
		initView();
		new MbpnProductChangeController(mainWindow, this);
	}

	@Override
	public void onTabSelected() {
		if (isInitialized) {
			return;
		}

	}

	public void initView() {
		try {
			initComponents();
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			handleException(e);
		}
	}

	protected void initComponents() {
		setLayout(new MigLayout("insets 20, align center", "[fill]"));

		//add product ID button and text field
		add(getProductIdBtn(), "span, split 2, w 100!, h 40!, aligny top");
		add(getProductIdTxtField(), "wrap 25, h 40!");

		//add text fields
		add(getCurrentSpecCodeTxtField());
		add(getOrderNoTxtField(), "wrap");
		add(getTrackingStatusTxtField());
		add(getLastProcessPointTxtField(), "wrap 50");

		//line between product info and new spec code field
		add(new JSeparator(), "span, wrap 25");

		add(getNewSpecCodeComboBox(), "span, wrap 50");

		//add buttons
		add(getUpdateBtn(), "w 250!, align center");
		add(getCancelBtn(), "w 250!, align center");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

	public JTextField getProductIdTxtField() {
		if (productIdTxtField == null) {
			productIdTxtField = new JTextField();
			productIdTxtField.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 24));
			productIdTxtField.setDocument(new AsciiDocument(17));
			productIdTxtField.setEditable(false);
			productIdTxtField.setBackground(Color.blue);
			productIdTxtField.setForeground(Color.white);
		}
		return productIdTxtField;
	}

	public LabeledTextField getCurrentSpecCodeTxtField() {
		if (currentSpecCodeTxtField == null) {
			currentSpecCodeTxtField = new LabeledTextField("Current Spec Code", false);
			currentSpecCodeTxtField.getComponent().setName("CurrentSpecCodeTextField");
			currentSpecCodeTxtField.getComponent().setPreferredSize(new Dimension(TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT));
			currentSpecCodeTxtField.getComponent().setEditable(false);
		}
		return currentSpecCodeTxtField;
	}

	public LabeledTextField getOrderNoTxtField() {
		if (orderNoTxtField == null) {
			orderNoTxtField = new LabeledTextField("Current Order No", false);
			orderNoTxtField.getComponent().setName("ProdLotTextField");
			orderNoTxtField.getComponent().setPreferredSize(new Dimension(TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT));
			orderNoTxtField.getComponent().setEditable(false);
		}
		return orderNoTxtField;
	}

	public LabeledTextField getTrackingStatusTxtField() {
		if (trackingStatusTxtField == null) {
			trackingStatusTxtField = new LabeledTextField("Tracking Status", false);
			trackingStatusTxtField.getComponent().setName("TrackingStatusTextField");
			trackingStatusTxtField.getComponent().setPreferredSize(new Dimension(TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT));
			trackingStatusTxtField.getComponent().setEditable(false);
		}
		return trackingStatusTxtField;
	}

	public LabeledTextField getLastProcessPointTxtField() {
		if (lastProcessPointTxtField == null) {
			lastProcessPointTxtField = new LabeledTextField("Last Passing Process Point", false);
			lastProcessPointTxtField.getComponent().setName("LastProcessPointTextField");
			lastProcessPointTxtField.getComponent().setPreferredSize(new Dimension(TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT));
			lastProcessPointTxtField.getComponent().setEditable(false);
		}
		return lastProcessPointTxtField;
	}

	public LabeledComboBox getNewSpecCodeComboBox() {
		if (newSpecCodeComboBox == null) {
			newSpecCodeComboBox = new LabeledComboBox("Replacement Spec Code", false);
			newSpecCodeComboBox.getComponent().setName("NewSpecCodeComboBox");
			newSpecCodeComboBox.getComponent().addItem("Select");
			newSpecCodeComboBox.getComponent().setSelectedIndex(0);
			newSpecCodeComboBox.getComponent().setEnabled(false);
		}
		return newSpecCodeComboBox;
	}

	public JButton getProductIdBtn() {
		if (productIdBtn == null) {
			productIdBtn = new JButton("Product ID");
			productIdBtn.setName("getProductIdBtn");
		}
		return productIdBtn;
	}

	public JButton getUpdateBtn() {
		if (updateBtn == null) {
			updateBtn = new JButton("Update");
			updateBtn.setName("getUpdateBtn");
			updateBtn.setEnabled(false);
		}
		return updateBtn;
	}

	public JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new JButton("Cancel");
			cancelBtn.setName("getCancelBtn");
			cancelBtn.setEnabled(false);
		}
		return cancelBtn;
	}

}