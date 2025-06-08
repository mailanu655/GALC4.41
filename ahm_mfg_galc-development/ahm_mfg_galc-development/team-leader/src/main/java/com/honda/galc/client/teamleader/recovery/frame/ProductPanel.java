package com.honda.galc.client.teamleader.recovery.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;

/**
 * 
 * <h3>ProductPanel Class description</h3>
 * <p> ProductPanel description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Dec 23, 2011
 *
 *
 */
public class ProductPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	List<ProductNumberDef> productNumberDefs = new ArrayList<ProductNumberDef>();

	private JLabel dcNumberLabel;
	private JTextField dcNumberTextField;
	private JLabel mcNumberLabel;
	private JTextField mcNumberTextField;

	protected JLabel defectStatusLabel;
	protected JTextField defectStatus;

	private JButton searchDCButton;
	private JButton searchMCButton;
	private JButton refreshButton;
	private JButton nextButton;

	private List<ProductNumberDef> dcNumberDefs;
	private List<ProductNumberDef> mcNumberDefs;
	private ProductRecoveryPanel parentPanel;

	public ProductPanel(ProductRecoveryPanel parentPanel) {
		this.parentPanel = parentPanel;
		initialize();
	}

	protected void initialize() {
		initComponents();
		mapListeners();
		dcNumberDefs = ProductNumberDef.getProductNumberDef(getParentPanel().getProductType(), ProductNumberDef.NumberType.DC);
		mcNumberDefs = ProductNumberDef.getProductNumberDef(getParentPanel().getProductType(), ProductNumberDef.NumberType.MC);
	}
	
	protected void initComponents() {
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder());
		setSize(Utils.getProductInfoPanelWidth(), Utils.getProductInfoPanelHeight());
		setLocation(Utils.getPanelLeftMargin(), Utils.getProductInfoPanelHeight() - Utils.getElementHeight() - 5);

		dcNumberLabel = createLabel("DC Number",Utils.getLeftMargin(), Utils.getTopMargin());
		mcNumberLabel = createLabel("MC Number",Utils.getLeftMargin(), dcNumberLabel.getY() + dcNumberLabel.getHeight() + 5);
		dcNumberTextField = createTextField(Utils.getLeftInputMargin(), dcNumberLabel.getY());
		mcNumberTextField = createTextField(Utils.getLeftInputMargin(), mcNumberLabel.getY());
		dcNumberTextField.requestFocusInWindow();

		defectStatusLabel = createDefectStatusLabel();
		defectStatus = createDefectStatus();

		searchDCButton = Utils.createButton("Search DC",Utils.getButtonMargin(), Utils.getTopMargin());
		refreshButton = Utils.createButton("Refresh",Utils.getButtonMargin(), Utils.getTopMargin());
		searchMCButton = Utils.createButton("Search MC",Utils.getButtonMargin(), searchDCButton.getY() + searchDCButton.getHeight() + 5);
		nextButton = Utils.createButton("Next",Utils.getButtonMargin(), searchDCButton.getY() + searchDCButton.getHeight() + 5);
        
		refreshButton.setVisible(false);
		nextButton.setVisible(false);
		add(dcNumberLabel);
		add(dcNumberTextField);
		add(mcNumberLabel);
		add(mcNumberTextField);
		add(defectStatusLabel);
		add(defectStatus);
		add(searchDCButton);
		add(refreshButton);
		add(searchMCButton);
		add(nextButton);
	}
	
	// === ui elements factory methods === //
	protected JLabel createProductNumberLabel() {
		JLabel label = new JLabel();
		label.setText(getProductName() + " DC Number");
		label.setSize(Utils.getProductInfoLabelWidth(), Utils.getElementHeight());
		label.setLocation(Utils.getLeftMargin(), Utils.getTopMargin());
		label.setFont(Utils.getInputLabelFont());
		return label;
	}
	
	protected JLabel createLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setSize(Utils.getProductInfoLabelWidth(), Utils.getElementHeight());
		label.setLocation(x, y);
		label.setFont(Utils.getInputLabelFont());
		return label;
	}
	
	protected JTextField createTextField(int x, int y){
		JTextField textField = new JTextField();
		textField.setSize(Utils.getNumberFieldWidth(), Utils.getElementHeight());
		textField.setLocation(x,y);
		textField.setFont(Utils.getInputFont());
		textField.setRequestFocusEnabled(true);
		textField.setDocument(new UpperCaseDocument(getProductNumberLength()));
		return textField;
	}


	protected JLabel createDefectStatusLabel() {
		JLabel label = new JLabel();
		label.setText("Defect Status");
		label.setSize(Utils.getProductInfoLabelWidth(), Utils.getElementHeight());
		label.setLocation(Utils.getLeftMargin(), Utils.getProductInfoPanelHeight() - Utils.getElementHeight() - 20);
		label.setFont(Utils.getInputLabelFont());
		return label;
	}

	protected JTextField createDefectStatus() {
		JTextField field = new JTextField();
		field.setText("");
		field.setSize(Utils.getDefectStatusFieldWidth() + 70, Utils.getElementHeight());
		field.setLocation(Utils.getLeftInputMargin(), getDefectStatusLabel().getY());
		field.setFont(Utils.getInputMediumFont());
		field.setRequestFocusEnabled(true);
		field.setHorizontalAlignment(JTextField.CENTER);
		field.setDocument(new LimitedLengthPlainDocument(15));
		field.setFocusable(false);
		field.setEditable(false);
		return field;
	}


	// === get/set === //
	protected String getProductName() {
		return getParentPanel().getProductType().getProductName();
	}

	public int getProductNumberLength() {
		return ProductNumberDef.getMaxLength(getProductNumberDefs());
	}


	protected JLabel getDefectStatusLabel() {
		return defectStatusLabel;
	}

	public JTextField getDefectStatus() {
		return defectStatus;
	}

	protected ProductRecoveryPanel getParentPanel() {
		return parentPanel;
	}

	protected void setParentPanel(ProductRecoveryPanel parentPanel) {
		this.parentPanel = parentPanel;
	}

	// === state control === //
	protected BaseProduct getProduct() {	
		return getParentPanel().getController().getProduct();
	}

	public void setIdleMode() {

		dcNumberTextField.setText("");
		Utils.setTextFieldEditable(dcNumberTextField);
		dcNumberTextField.setFocusable(true);
		mcNumberTextField.setText("");
		Utils.setTextFieldEditable(mcNumberTextField);
		mcNumberTextField.setFocusable(true);
		
		getDefectStatus().setText("");
		Utils.setIdleColors(getDefectStatus());
		nextButton.setVisible(false);
		refreshButton.setVisible(false);
		searchDCButton.setVisible(true);
		searchDCButton.setEnabled(false);
		searchMCButton.setVisible(true);
		searchMCButton.setEnabled(false);
		SwingUtilities.invokeLater(new Runnable() {
			  public void run() {
				  dcNumberTextField.requestFocusInWindow();
			  }
		});
	}

	public void setInputMode() {

		BaseProduct product = getProduct();

		dcNumberTextField.setEditable(false);
		Utils.setInputReadOnlyColors(dcNumberTextField);
		dcNumberTextField.setText(product.getProductId());
		dcNumberTextField.setFocusable(false);
		
		mcNumberTextField.setEditable(false);
		mcNumberTextField.setFocusable(false);
		Utils.setInputReadOnlyColors(mcNumberTextField);
		
		if (product instanceof DieCast) {
			DieCast dieCast = (DieCast) product;
			mcNumberTextField.setText(dieCast.getMcSerialNumber());
		}
		
		if (product.getDefectStatus() == null || product.isRepairedStatus()) {
			Utils.setInputReadOnlyColors(getDefectStatus());
			getDefectStatus().setText("OK");
		} else {
			getDefectStatus().setText(product.getDefectStatus().getName());
			Utils.setErrorColors(getDefectStatus());
		}
		searchDCButton.setVisible(false);
		refreshButton.setVisible(true);
		refreshButton.setEnabled(true);
		searchMCButton.setVisible(false);
		nextButton.setVisible(true);
		nextButton.setEnabled(true);
	}

	protected void setButtonAction(JButton button, Action action, String commandId) {
		button.getInputMap().put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), commandId);
		button.getActionMap().put(commandId, action);
		button.setAction(action);
	}

	protected void setProductNumberValue(BaseProduct product) {
		dcNumberTextField.setText(product.getProductId());
	}

	// === mapping === //
	public void mapListeners() {
		 TextInputChangeListener listener = new TextInputChangeListener(getParentPanel(), dcNumberTextField, searchDCButton);
	     dcNumberTextField.getDocument().addDocumentListener(listener);
	     dcNumberTextField.addCaretListener(listener);
	     
	     TextInputChangeListener listener2 = new TextInputChangeListener(getParentPanel(), mcNumberTextField, searchMCButton);
	     mcNumberTextField.getDocument().addDocumentListener(listener2);
	     mcNumberTextField.addCaretListener(listener2);
	     
	     searchDCButton.addActionListener(this);
	     refreshButton.addActionListener(this);
	     searchMCButton.addActionListener(this);
	     nextButton.addActionListener(this);
	     
	     dcNumberTextField.addActionListener(this);
	     mcNumberTextField.addActionListener(this);
	     
	}

	public List<ProductNumberDef> getProductNumberDefs() {
		return productNumberDefs;
	}

	public void setProductNumberDefs(List<ProductNumberDef> productNumberDefs) {
		this.productNumberDefs = productNumberDefs;
	}

	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == searchDCButton || evt.getSource() == dcNumberTextField) dcNumberInputed();
		else if(evt.getSource() == refreshButton) refreshButtonClicked();
		else if(evt.getSource() == searchMCButton || evt.getSource() == mcNumberTextField) mcNumberInputed();
		else if(evt.getSource() == nextButton) nextButtonClicked();
	}

	private void dcNumberInputed() {
		dcNumberInputed(dcNumberTextField.getText());
	}
	
	private boolean dcNumberInputed(String productId) {
		getController().resetModel();
		if(StringUtils.isEmpty(productId)) return false;

		if (!ProductNumberDef.isNumberValid(productId, dcNumberDefs)) {
			dcNumberTextField.selectAll();
			Utils.setErrorColors(dcNumberTextField);
			parentPanel.getMainWindow().setErrorMessage("Invalid DC number");
			dcNumberTextField.requestFocus();
			return false;
		}

		getController().findProductByDc(productId);
		if (parentPanel.getController().getProduct() == null) {
			dcNumberTextField.selectAll();
			Utils.setErrorColors(dcNumberTextField);
			parentPanel.getMainWindow().setErrorMessage("DC number does not exist!");
			dcNumberTextField.requestFocus();
			return false;
		}

		getParentPanel().resetDataPanelElements();
		getController().findProductBuildResults(getProduct().getProductId(), getParentPanel().getDataPanel().getPartNames());
		parentPanel.setInputMode();
		return true;
	}
	
	private void mcNumberInputed() {
		mcNumberInputed(mcNumberTextField.getText());
	}
	
	private boolean mcNumberInputed(String productId) {
		getController().resetModel();
		if(StringUtils.isEmpty(productId)) return false;

		if (!ProductNumberDef.isNumberValid(productId, mcNumberDefs)) {
			mcNumberTextField.selectAll();
			Utils.setErrorColors(mcNumberTextField);
			parentPanel.getMainWindow().setErrorMessage("Invalid MC number");
			
			mcNumberTextField.requestFocus();
			return false;
		}

		getController().findProductByMc(productId);
		if (parentPanel.getController().getProduct() == null) {
			mcNumberTextField.selectAll();
			Utils.setErrorColors(mcNumberTextField);
			parentPanel.getMainWindow().setErrorMessage("MC number does not exist!");
			mcNumberTextField.requestFocus();
			return false;
		}
		getParentPanel().resetDataPanelElements();
		getController().findProductBuildResults(getProduct().getProductId(), getParentPanel().getDataPanel().getPartNames());
		parentPanel.setInputMode();
		return true;
	}
	
	private void refreshButtonClicked()  {
		dcNumberInputed(getController().getProduct().getProductId());
	}
	
	private void nextButtonClicked(){
		parentPanel.getController().resetModel();
		parentPanel.setIdleMode();
	}
	
	private DataRecoveryController getController() {
		return parentPanel.getController();
	}
	
}
