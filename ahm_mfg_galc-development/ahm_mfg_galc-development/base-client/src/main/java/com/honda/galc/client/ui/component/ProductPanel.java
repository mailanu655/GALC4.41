package com.honda.galc.client.ui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.honda.galc.client.ui.SwingSpringUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.ProductTypeData;

/**
 * 
 * <h3>ProductPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
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
 * @author Paul Chou
 * Aug 5, 2010
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ProductPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	java.awt.Frame owner;
	JLabel productSpecLabel;
	UpperCaseFieldBean productIdField;
	UpperCaseFieldBean productSpecField;
	String productIdLabelTxt;
	String productSpecLabelTxt;
	int preferreLabelLength = 150;
	int preferreFieldLength = 830;
	int preferedHight = 45;
	int maxProductIdLength = 12;
	protected Font labelFont = new Font("dialog", 0, 18);
	protected Font fieldFont = new Font("dialog", 0, 36);
	private ProductType productType = null;
	private JButton productLookupButton = null;
	JLabel seqLabel;
	UpperCaseFieldBean seqField;
	String seqLabelTxt = "SEQ ";
	

	public ProductPanel(java.awt.Frame owner, ProductTypeData productTypeData) {
		this.owner = owner;
		this.productIdLabelTxt = productTypeData.getProductIdLabel();
		this.productSpecLabelTxt = productTypeData.getProductSpecCodeLabel();
		this.maxProductIdLength = productTypeData.getProductType().getProductIdLength();
		this.productType = productTypeData.getProductType();
		initialize();
	}
	
	public ProductPanel(java.awt.Frame owner, int mbpnMaxProductIdLength, ProductTypeData productTypeData) {
		this.owner = owner;
		this.productIdLabelTxt = productTypeData.getProductIdLabel();
		this.productSpecLabelTxt = productTypeData.getProductSpecCodeLabel();
		this.maxProductIdLength = mbpnMaxProductIdLength;
		this.productType = productTypeData.getProductType();
		initialize();
	}
	
	public ProductType getProductType() {
		return productType;
	}
	
	public ProductPanel(java.awt.Frame owner, String productIdLabelTxt, String productSepcLabelTxt,
			int maxProductIdLength) {
		super();
		this.owner = owner;
		this.productIdLabelTxt = productIdLabelTxt;
		this.productSpecLabelTxt = productSepcLabelTxt;
		this.maxProductIdLength = maxProductIdLength;
		initialize();
	}


	public ProductPanel(java.awt.Frame owner, String productIdLabelTxt, String productSepcLabelTxt,
			int preferreLabelLength, int preferreFieldLength, int preferedHight) {
		super();
		this.owner = owner;
		this.productIdLabelTxt = productIdLabelTxt;
		this.productSpecLabelTxt = productSepcLabelTxt;
		this.preferreLabelLength = preferreLabelLength;
		this.preferreFieldLength = preferreFieldLength;
		this.preferedHight = preferedHight;
		initialize();
	}


	public ProductPanel(java.awt.Frame owner, ProductTypeData productTypeData, int preferredProductIdLength) {
		this.owner = owner;
		this.preferreFieldLength = preferredProductIdLength;
		this.productIdLabelTxt = productTypeData.getProductIdLabel();
		this.productSpecLabelTxt = productTypeData.getProductSpecCodeLabel();
		this.maxProductIdLength = productTypeData.getProductType().getProductIdLength();
		this.productType = productTypeData.getProductType();
		initialize();
		
	}


	private void initialize() {
		setPreferredSize(new Dimension(1024, 120));
		setLayout(new SpringLayout());
		initComponents();
	}


	protected void initComponents() {
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.add(getProductLookupButton());
		panel1.add(getProductIdField());
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		getProductSpecLabel().setLabelFor(getProductSpecField());
		panel2.add(productSpecLabel);
		panel2.add(productSpecField);
		panel2.add(getSeqLabel());
		panel2.add(getSeqField());
		
		add(panel1);
		add(panel2);
			    
	    SwingSpringUtil.makeCompactGrid(this,
                2, 1,        //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
	}

	public JButton getProductLookupButton() {
		if(productLookupButton == null){
			productLookupButton = new JButton(productIdLabelTxt);
			productLookupButton.setFont(labelFont);
			productLookupButton.setName("ProductLookupButton");
			productLookupButton.setMaximumSize(new java.awt.Dimension(125, 50));
			productLookupButton.setPreferredSize(new java.awt.Dimension(125, 50));
			productLookupButton.setBounds(1, 7, 75, 55) ;
			productLookupButton.setMinimumSize(new java.awt.Dimension(125, 50));
			productLookupButton.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
			productLookupButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			productLookupButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(owner, productType.name(),ProductNumberDef.getProductNumberDef(productType).get(0).getName());
					manualProductEntry.setModal(true);
					manualProductEntry.setVisible(true);
					getProductIdField().requestFocus();
					String productId = manualProductEntry.getResultProductId();
					if (!productId.equals("")) {
						getProductIdField().setText(productId);
					}
					return;
				}
			});
		}
		return productLookupButton;
	}
	
	public JLabel getProductSpecLabel() {
		if (productSpecLabel == null) {
			productSpecLabel = new JLabel(productSpecLabelTxt, JLabel.CENTER);
			productSpecLabel.setPreferredSize(new Dimension(preferreLabelLength, preferedHight));
			productSpecLabel.setFont(labelFont);
		}
		return productSpecLabel;
	}
	
	public JLabel getSeqLabel() {
		if (seqLabel == null) {
			seqLabel = new JLabel(seqLabelTxt, JLabel.RIGHT);
			seqLabel.setPreferredSize(new Dimension(100, preferedHight));
			seqLabel.setFont(labelFont);
			seqLabel.setVisible(false);
		}
		return seqLabel;
	}

	public void refresh(){
		getProductIdField().setText(new Text(""));
		getProductSpecField().setText("");
		getSeqField().setText("");
	}	
	
	//Getters & Setters
	public UpperCaseFieldBean getProductIdField() {
		if(productIdField == null){
			
			productIdField = new UpperCaseFieldBean(new DefaultFieldRender());
			productIdField.setName("productIdTextField");
			productIdField.setFont(fieldFont);
			productIdField.setColumns(maxProductIdLength);
			productIdField.setMaximumLength(maxProductIdLength);
			productIdField.setPreferredSize(new Dimension(preferreFieldLength, preferedHight));
		}
		return productIdField;
	}
	
	public void setProductIdField(UpperCaseFieldBean productIdField) {
		this.productIdField = productIdField;
	}
	
	public UpperCaseFieldBean getProductSpecField() {
		if(productSpecField == null){
			productSpecField = new UpperCaseFieldBean();
			productSpecField.setName("prodSpecField");
			productSpecField.setFont(fieldFont);
			productSpecField.setPreferredSize(new Dimension(preferreFieldLength, preferedHight));
			productSpecField.setEditable(false);
			productSpecField.setEnabled(false);
			productSpecField.setSelectionColor(java.awt.SystemColor.controlHighlight);
			productSpecField.setDisabledTextColor(Color.black);
		}
		return productSpecField;
	}
	
	public UpperCaseFieldBean getSeqField() {
		if(seqField == null){
			seqField = new UpperCaseFieldBean();
			seqField.setName("seqField");
			seqField.setFont(fieldFont);
			seqField.setPreferredSize(new Dimension(300, preferedHight));
			seqField.setEditable(false);
			seqField.setEnabled(false);
			seqField.setSelectionColor(java.awt.SystemColor.controlHighlight);
			seqField.setDisabledTextColor(Color.black);
			seqField.setVisible(false);
		}
		return seqField;
	}
	
	public void setProductSpec(UpperCaseFieldBean productSpec) {
		this.productSpecField = productSpec;
	}

	public int getMaxProductIdLength() {
		return maxProductIdLength;
	}

	public void setMaxProductIdLength(int maxProductIdLength) {
		this.maxProductIdLength = maxProductIdLength;
	}
}