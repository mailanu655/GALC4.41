package com.honda.galc.client.teamleader.reprint;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>KnuckleSelectionPanel Class description</h3>
 * <p> KnuckleSelectionPanel description </p>
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
 * Apr 19, 2011
 *
 *
 */
public class KnuckleSelectionPanel extends ProductSelectionPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final String selection = "2. Choose Knuckle Selection";
	
	private JPanel ksnPanel;
	
	
	private JPanel prodLotPanel;
	
	
	private UpperCaseFieldBean ksnField;
	private JButton ksnOkButton;
	
	private UpperCaseFieldBean prodLotField;
	private JButton lotOkButton;

	
	
	private static final String[] options = new String[] {
		"Enter Knuckle Serial Number",
		"Select By Production Date",
		"Select By Production Lot"
	};
	
	public KnuckleSelectionPanel(JPanel itemListContainer) {
		
		super(selection,options,itemListContainer);
		
		this.processLocations.add("KN");
		this.processLocations.add("KR");

		createSelectionPanels();
		
		createKnuckleTablePane(null);
		
		addProductSelectionPanel(ksnPanel);
		
		mapHandlers();
		
	}
	
	@SuppressWarnings("unchecked")
	private void mapHandlers() {
		
		for (Enumeration e=selectionGroup.getElements(); e.hasMoreElements(); ) {
	   
			JRadioButton b = (JRadioButton)e.nextElement();
	        b.addActionListener(this);
	        
	    }
		
		ksnOkButton.addActionListener(this);
		lotOkButton.addActionListener(this);
		productionDateOkButton.addActionListener(this);
		prodLotOkButton.addActionListener(this);
		
	}
	
	private void createSelectionPanels() {
		
		ksnOkButton = createOkButton();
		lotOkButton = createOkButton();
			
		createKsnPanel();
		
		createProductionDatePanel();
		
		createProdLotPanel();
		
	}
	
	private void createKsnPanel() {
		
		ksnPanel = new JPanel(new GridBagLayout());
		
		TitledBorder titledBorder = new TitledBorder("3.Enter Knuckle Serial");
		titledBorder.setTitleFont(Fonts.DIALOG_BOLD_16);
		
		ksnPanel.setBorder(titledBorder);
		ksnField = new UpperCaseFieldBean();
		ksnField.setColumns(18);
		ksnField.setFixedLength(true);
		ksnField.setMaximumLength(17);
		
		ksnField.setFont(Fonts.DIALOG_BOLD_20);
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		ksnPanel.add(ksnField,c);
		c.gridx = 1;
		c.ipadx = 20;
		c.weightx = 0.1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		ksnPanel.add(ksnOkButton,c);
	}
	
	
	
	private void createProdLotPanel() {
		
		prodLotPanel = new JPanel(new GridBagLayout());
		
		TitledBorder titledBorder = new TitledBorder("3.Enter Production Lot Number");
		titledBorder.setTitleFont(Fonts.DIALOG_BOLD_16);
		
		prodLotPanel.setBorder(titledBorder);
		prodLotField = new UpperCaseFieldBean();
		prodLotField.setColumns(18);
		prodLotField.setFixedLength(true);
		prodLotField.setMaximumLength(20);
		prodLotField.setFont(Fonts.DIALOG_BOLD_20);
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		prodLotPanel.add(prodLotField,c);
		c.gridx = 1;
		c.ipadx = 20;
		c.weightx = 0.1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		prodLotPanel.add(lotOkButton,c);
		
	}

	public void doActionPerformed(ActionEvent e) {
		
		if(e.getSource() == ksnOkButton) knuckleSelected();
		else if(e.getSource() == lotOkButton) lotSelected();
		else if(e.getSource() == productionDateOkButton) productionDateSelected();
		else if(e.getSource() == prodLotOkButton) prodLotSelected();
		else {
			JRadioButton button = (JRadioButton)e.getSource();
			int index = getRadioButtonIndex(button.getText());
		
			selectionPanel.removeAll();
			switch(index) {
				case 0:
					addProductSelectionPanel(ksnPanel);
					createKnuckleTablePane(null);
					break;
				case 1:
					addProductSelectionPanel(productionDatePanel);
					createKnuckleTablePane(null);
					break;
				case 2:
					addProductSelectionPanel(prodLotPanel);
					createKnuckleTablePane(null);
					break;
			}
			selectionPanel.updateUI();
		}
	}
	

	private void lotSelected() {
		
		String kdLot = prodLotField.getText();
		if(kdLot == null){
			setErrorMessage("Please type in the production lot number");
			return;
		}
		
		List<SubProduct> knuckles = getDao(SubProductDao.class).findAllByProductionLot(kdLot);
		if(knuckles.isEmpty()) {
			setErrorMessage("Incorrect production lot number");
			return;
		}
		
		createKnuckleTablePane(new SortedArrayList<SubProduct>(knuckles,"getProductId"));
	}

	private void knuckleSelected() {
		
		String ksn = ksnField.getText();
		
		if(ksn == null){
			setErrorMessage("Please type in the knuckle serial number");
			return;
		}
		
		SubProduct knuckle = getDao(SubProductDao.class).findByKey(ksn);
		if(knuckle == null) {
			setErrorMessage("Incorrect knuckle serial number");
			return;
		}
		
		List<SubProduct> knuckles = new ArrayList<SubProduct>();
		knuckles.add(knuckle);
		createKnuckleTablePane(knuckles);
		
	}
	

	private int getRadioButtonIndex(String text) {
		
		int i = 0;
		for(String name : options) {
			if(name.equalsIgnoreCase(text)) return i;
			else i++;
		}
		return 0;
	}

	private void prodLotSelected() {
		
		ProductionLot prodLot = prodLotTablePane.getSelectedItem();
		
		if(prodLot == null) return;
		
		List<SubProduct> knuckles = getDao(SubProductDao.class).findAllByProductionLot(prodLot.getProductionLot());
		if(knuckles.isEmpty()) {
			setErrorMessage("Incorrect production lot number");
			return;
		}
		
		createKnuckleTablePane(knuckles);
		
	}
	
	
	private void createKnuckleTablePane(List<SubProduct> knuckles) {
		
		itemListContainer.removeAll();
		ColumnMappings columnMappings  = ColumnMappings.with("#","");
		columnMappings.put("Knuckle Serial Number","productId");
		columnMappings.put("Sub Id", "subId");
		// disallow unselect and allow sorting
		ObjectTablePane<SubProduct> objectTablePane = new ObjectTablePane<SubProduct>(columnMappings.get(),false,true);
		objectTablePane.setAlignment(SwingConstants.CENTER);
		objectTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		itemListContainer.add(objectTablePane,BorderLayout.CENTER);
		objectTablePane.reloadData(knuckles);
		this.objectTablePane = objectTablePane;
		
	}
}
