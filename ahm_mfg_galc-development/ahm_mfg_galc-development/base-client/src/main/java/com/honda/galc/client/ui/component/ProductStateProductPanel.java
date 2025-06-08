package com.honda.galc.client.ui.component;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.honda.galc.client.ui.SwingSpringUtil;
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
 * @author Rakesh
 * March 22, 2017
 *
 */

public class ProductStateProductPanel extends ProductPanel {
	private static final long serialVersionUID = 1L;
	private UpperCaseFieldBean statusText;
	

	public ProductStateProductPanel(java.awt.Frame owner, ProductTypeData productTypeData) {
		super(owner, productTypeData);
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
		panel2.add(getStatusPane());
		panel2.add(getSeqLabel());
		panel2.add(getSeqField());
		
		add(panel1);
		add(panel2);
			    
	    SwingSpringUtil.makeCompactGrid(this,
                2, 1,        //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
	}
	
	private Box getStatusPane() {
		Box statusBox = new Box(BoxLayout.X_AXIS);
		JLabel statusLabel = new JLabel("Status: ");
		statusLabel.setFont(labelFont);
		statusLabel.setBorder(new EmptyBorder(0,10,0,5));
		statusBox.add(statusLabel);
		statusBox.add(getStatusText());

		return statusBox;
	}

	public UpperCaseFieldBean getStatusText() {
		if(statusText == null) {
			statusText = new UpperCaseFieldBean();
			statusText.setName("statusText");
			statusText.setFont(fieldFont);
			statusText.setHorizontalAlignment(JTextField.CENTER);
			statusText.setEditable(false);
			statusText.setEnabled(false);
			statusText.setPreferredSize(new Dimension(70, preferedHight));
			statusText.setMaximumSize(new java.awt.Dimension(70, preferedHight));
			statusText.setSelectionColor(java.awt.SystemColor.controlHighlight);
			statusText.setDisabledTextColor(Color.black);
		}
		return statusText;
	}
}