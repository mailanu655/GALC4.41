package com.honda.galc.client.qics.view.fragments;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.honda.galc.client.qics.config.QicsClientConfig;
import com.honda.galc.client.qics.controller.QicsController;
import com.honda.galc.client.qics.view.dialog.LpdcScrapDialog;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.screen.LpdcIdlePanel;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExceptionalOut;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>SingleProductInputPanel</code>
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
 * <TD>Pankaj Gopal</TD>
 * <TD>Apr 15, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Pankaj Gopal
 */

public class SingleProductInputPanel extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private LpdcIdlePanel lpdcPanel;
	private JLabel inputLabel;
	private JTextField inputField;
	private JPanel buttonPanel;
	private JButton directPassButton;
	private JButton scrapButton;
	private JButton lpdcOnButton;
	private int style = 1; //0 - original e.g. field and button on one row; 1 - new field and button in two rows;

	public final static int DEFAULT_PRODUCT_NUMBER_LENGTH = 17;

	public SingleProductInputPanel(LpdcIdlePanel lpdcPanel, int style) {
		super();
		setLpdcPanel(lpdcPanel);
		this.style = style;
		initialize();
	}

	protected void initialize() {
		setLayout(null);
		add(getInputLabel());
		add(getInputField());
		add(getButtonPanel());
		mapConnections();
		setAssociateNumberCache(getQicsController().createAssociateNumberCache());
		setButtonState(false);
		setIdleState();
	}

	protected void mapConnections() {
		getInputField().addActionListener(this);
		getInputField().addKeyListener(this);
		getDirectPassButton().addActionListener(this);
		getScrapButton().addActionListener(this);
		// getLpdcOnButton().addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		String inputNumber = getInputFieldData();

		if (isEmpty(inputNumber)) {
			return;
		}

		if (!isProductSizeValid(inputNumber)) {
			setErrorMessage("Invalid Product Number");
			return;
		}

		submitForProcessing(inputNumber, e);
	}

	protected void submitForProcessing(String inputNumber, ActionEvent e) {
		
		try {
			getQicsController().submitLpdcProductForProcessing(inputNumber);
		} catch (TaskException te) {
			setErrorMessage(te.getMessage());
			return;
		} catch (Exception ex) {
			setErrorMessage("Product:" + inputNumber);
			getQicsFrame().getLogger().error(ex, "Invalid Product:", inputNumber);
			return;
		}
		
		if (getQicsFrame().displayDelayedMessage()) return;

		BaseProduct product = getQicsController().getProductModel().getProduct();
		
		if (product.isScrapStatus()) {
				setErrorMessage("Product is already scrapped.");
				return;
			}

		if (product.isPreheatScrapStatus()) {
				preheatActionHandler();
				getQicsFrame().displayIdleView();
				setErrorMessage("Product is already preheat scrapped.");
				setPreheatState();
				return;
		}
	
		setValidState();
		setButtonState(true);
		buttonActionHandler(e);
		getLpdcPanel().toggleDirectPassAllButton();
	}

	protected boolean isEmpty(String inputNumber) {
		return (inputNumber == null || inputNumber.length() == 0) ? true : false;
	}

	protected boolean isProductSizeValid(String inputNumber) {
		for(ProductNumberDef def : getQicsController().getProductTypeData().getProductNumberDefs()) {
			if(inputNumber.length() == def.getLength()) {
				return true;
			}
		}
		return false;
	}

	protected boolean isProductValid(String productId) {
		return checkDigit(productId);
	}

	protected boolean checkDigit(String aSerialNumber) {
		if (aSerialNumber.length() < 10)
			return false;
		char cSerialNoChar = aSerialNumber.charAt(aSerialNumber.length() - 1);
		if (cSerialNoChar == '*') {
			return true;
		}
		long lSerialNoChar_sum = 0;
		for (int iIndex = 0; iIndex < aSerialNumber.length() - 1; iIndex++) {
			lSerialNoChar_sum += (long) aSerialNumber.charAt(iIndex);
		}
		if (((lSerialNoChar_sum % 26) + 'A') == cSerialNoChar)
			return true;
		else
			return false;
	}

	public void keyPressed(KeyEvent e) {
		if (isReadyForIdleState()) {
			getQicsFrame().clearMessage();
			getQicsFrame().clearStatusMessage();
			getInputField().setBackground(Color.BLUE);
			getInputField().setForeground(Color.WHITE);
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public JLabel getInputLabel() {
		if (inputLabel == null) {
			inputLabel = new JLabel();
			inputLabel.setFont(new Font("Dialog", Font.BOLD, 40));
			inputLabel.setBounds(0, 0, 30, style == 0 ? 75 : 150);
		}
		return inputLabel;
	}

	public JTextField getInputField() {
		if (inputField == null) {
			inputField = new JTextField();
			inputField.setName("inputField");
			inputField.setFont(new Font("Dialog", Font.BOLD, 38));
			inputField.setDocument(new UpperCaseDocument(DEFAULT_PRODUCT_NUMBER_LENGTH));
			inputField.setBounds(30, 0, 460, 75);
		}
		return inputField;
	}

	public JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel(new GridLayout(1, 3));
			buttonPanel.setName("buttonPanel");
			if(style == 0) 
				buttonPanel.setBounds(490, 0, 511, 74);
			else 
				buttonPanel.setBounds(30, 76, 460, 74);
			
			buttonPanel.add(getDirectPassButton());
			buttonPanel.add(getScrapButton());
			// buttonPanel.add(getLpdcOnButton());
		}
		return buttonPanel;
	}

	public JButton getDirectPassButton() {
		if (directPassButton == null) {
			directPassButton = getButton("DIRECT PASS");
		}
		return directPassButton;
	}

	public JButton getScrapButton() {
		if (scrapButton == null) {
			scrapButton = getButton("SCRAP");
		}
		return scrapButton;
	}

	public JButton getLpdcOnButton() {
		if (lpdcOnButton == null) {
			lpdcOnButton = getButton("LPDC ON");
			lpdcOnButton.setEnabled(false);
		}
		return lpdcOnButton;
	}

	protected JButton getButton(String label) {
		JButton button = new JButton();
		button.setFont(new Font("Dialog", Font.BOLD, 20));
		button.setText(label);
		return button;
	}

	protected String getInputFieldData() {
		return getInputField().getText();
	}

	protected boolean isReadyForIdleState() {
		if (getInputField().getBackground() == Color.RED || getInputField().getBackground() == Color.YELLOW) {
			return true;
		}
		return false;
	}

	protected void setIdleState() {
		getInputField().setBackground(Color.BLUE);
		getInputField().setForeground(Color.WHITE);
		getInputField().setEditable(true);
		getInputField().setText("");
	}

	protected void setValidState() {
		getInputField().setBackground(Color.GREEN);
		getInputField().setForeground(Color.BLACK);
		getInputField().setEditable(false);
	}

	protected void setPreheatState() {
		getInputField().setBackground(Color.YELLOW);
		getInputField().setForeground(Color.BLACK);
		getInputField().selectAll();
	}

	protected void setErrorState() {
		getInputField().setBackground(Color.RED);
		getInputField().setForeground(Color.BLACK);
		getInputField().selectAll();
	}

	protected void setButtonState(boolean state) {
		getDirectPassButton().setEnabled(state);
		getScrapButton().setEnabled(state);
	}

	protected int getDataCompleteValue(JButton button) {
		String buttonName = button.getName();
		if (buttonName.equals("DirectPass_10") || buttonName.equals("Scrap_10")) {
			return 1;
		}
		if (buttonName.equals("DirectPass_11") || buttonName.equals("Scrap_11")) {
			return 2;
		}
		if (buttonName.equals("DirectPass_20") || buttonName.equals("Scrap_20")) {
			return 3;
		}
		return 4;
	}

	protected void buttonActionHandler(ActionEvent e) {
		if (e != null) {
			if (e.getSource() == getDirectPassButton()) {
				directPassButtonHandler();
			}
			if (e.getSource() == getScrapButton()) {
				scrapButtonHandler();
			}
			getQicsFrame().displayIdleView();
		}
	}

	public void directPassButtonHandler() {
		getQicsController().submitDirectPass();
		setIdleState();
		setButtonState(false);
		getQicsFrame().createDataCollectionCompleteDataContainer(getDataCompleteValue(directPassButton));
	}

	public void scrapButtonHandler() {
		LpdcScrapDialog dialog = new LpdcScrapDialog(getQicsFrame(), "Scrap Product", getScrapMessage());
		dialog.loadComboBox(getQicsController().getAssociateNumbers().toArray());
		dialog.setLocationRelativeTo(getQicsFrame());
		dialog.setVisible(true);
		boolean cancelled = dialog.isCancelled();

		if (cancelled) {
			return;
		}

		ExceptionalOut scrap = (ExceptionalOut) dialog.getReturnValue();
		getQicsController().cacheAssociateNumber(scrap.getAssociateNo());

		scrap = setScrapProperties(scrap);
		if (scrap.getExceptionalOutReasonString().equals(DefectStatus.PREHEAT_SCRAP.getName().toUpperCase())) {
			getQicsController().submitPreheat(scrap);
		} else {
			getQicsController().submitScrap(scrap);
		}
		setIdleState();
		setButtonState(false);
		getQicsFrame().createDataCollectionCompleteDataContainer(getDataCompleteValue(scrapButton));
	}

	protected String getScrapMessage() {
		String productId = getQicsController().getProductModel().getInputNumber();
		String productNumberLabel = getQicsController().getProductTypeData().getProductIdLabel();
		String msg = productNumberLabel + ": " + productId + " is being scrapped";
		return msg;
	}

	protected void preheatActionHandler() {
		getQicsController().submitProcessPreheatProduct();
		setButtonState(false);
	}

	protected ExceptionalOut setScrapProperties(ExceptionalOut scrap) {
		String productId = getQicsController().getProductModel().getProduct().getProductId();
		scrap.setProductId(productId);
		scrap.getId().setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		scrap.setProcessPointId(getQicsController().getProcessPointId());
		scrap.setProductionDate(new Date(System.currentTimeMillis()));
		return scrap;
	}

	protected void setErrorMessage(String errorMessageId) {
		getQicsFrame().setErrorMessage(errorMessageId);
		setErrorState();
	}

	protected LpdcIdlePanel getLpdcPanel() {
		return lpdcPanel;
	}

	protected void setLpdcPanel(LpdcIdlePanel panel) {
		this.lpdcPanel = panel;
	}

	protected QicsFrame getQicsFrame() {
		return getLpdcPanel().getQicsFrame();
	}

	protected QicsController getQicsController() {
		return getQicsFrame().getQicsController();
	}

	protected QicsClientConfig getClientConfig() {
		return getQicsController().getClientConfig();
	}

	protected void setAssociateNumberCache(Map<String, String> associateNumberCache) {
		getQicsController().getClientModel().setAssociateNumberCache(associateNumberCache);
		// getQicsController().getAssociateNumberCache = associateNumberCache;
	}
}
