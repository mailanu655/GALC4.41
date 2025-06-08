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
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ProductionLot;

/**
 * 
 * <h3>EngineSelectionPanel Class description</h3>
 * <p> EngineSelectionPanel description </p>
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
public class EngineSelectionPanel extends ProductSelectionPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final String selection = "2. Choose Engine Selection";
	
	private JPanel engineSNPanel;
	private JPanel enginesOnlinePanel;
	
	private UpperCaseFieldBean einField;
	
	private JButton einOkButton;
	private JButton einOnLineOkButton;

	
	private static final String[] options = new String[] {
		"Enter Engine Serial Number",
		"Select by Production Date",
		"Engines On Line"
	};
	
	public EngineSelectionPanel(JPanel itemListContainer) {
		
		super(selection,options,itemListContainer);
		
		this.processLocations.add("AE");
		
		createSelectionPanels();
		
		addProductSelectionPanel(engineSNPanel);
		createEngineTablePane(null);
		
		mapHandlers();
		
	}
	
	@SuppressWarnings("unchecked")
	private void mapHandlers() {
		
		for (Enumeration e=selectionGroup.getElements(); e.hasMoreElements(); ) {
	   
			JRadioButton b = (JRadioButton)e.nextElement();
	        b.addActionListener(this);
	        
	    }
		
		einOkButton.addActionListener(this);
		einOnLineOkButton.addActionListener(this);
		productionDateOkButton.addActionListener(this);
		prodLotOkButton.addActionListener(this);
		
	}
	
	private void createSelectionPanels() {
		einOkButton = createOkButton();
		einOnLineOkButton = createOkButton();
		productionDateOkButton = createOkButton();
		
		createEngineSNPanel();
		createProductionDatePanel();
		createEnginesOnlinePanel();
		
	}
	
	private void createEngineSNPanel() {
		
		engineSNPanel = new JPanel(new GridBagLayout());
		
		TitledBorder titledBorder = new TitledBorder("3.Enter Engine Serial");
		titledBorder.setTitleFont(Fonts.DIALOG_BOLD_16);
		
		engineSNPanel.setBorder(titledBorder);
		einField = new UpperCaseFieldBean();
		einField.setColumns(18);
		einField.setFixedLength(true);
		einField.setMaximumLength(12);
		einField.setFont(Fonts.DIALOG_BOLD_20);
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		engineSNPanel.add(einField,c);
		c.gridx = 1;
		c.ipadx = 20;
		c.weightx = 0.1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		engineSNPanel.add(einOkButton,c);
	}
	
	
	private void createEnginesOnlinePanel() {
		
		enginesOnlinePanel = new JPanel(new GridBagLayout());
		
		TitledBorder titledBorder = new TitledBorder("3.Engines Between AE-ON and AE-OFF");
		titledBorder.setTitleFont(Fonts.DIALOG_BOLD_16);
		
		enginesOnlinePanel.setBorder(titledBorder);
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 20;
		enginesOnlinePanel.add(einOnLineOkButton,c);
		
	}
	
	public void doActionPerformed(ActionEvent e) {
		
		if(e.getSource() == einOkButton) engineSelected();
		else if(e.getSource() == einOnLineOkButton) engineOnLineSelected();
		else if(e.getSource() == productionDateOkButton) productionDateSelected();
		else if(e.getSource() == prodLotOkButton) prodLotSelected();
		else {
			JRadioButton button = (JRadioButton)e.getSource();
			int index = getRadioButtonIndex(button.getText());
		
			switch(index) {
				case 0:
					addProductSelectionPanel(engineSNPanel);
					createEngineTablePane(null);
					break;
				case 1:
					addProductSelectionPanel(productionDatePanel);
					createEngineTablePane(null);
					break;
				case 2:
					addProductSelectionPanel(enginesOnlinePanel);
					createEngineTablePane(null);
					break;
			}
		}
	}

	private void engineOnLineSelected() {
		
		String[] lineIds = reprintPropertyBean.getAeLineIdList();
		
		if(lineIds.length == 0){
			setErrorMessage("Please configure AE_LINE_ID_LIST property");
			return;
		}
		
		List<Engine> engines = getDao(EngineDao.class).findAllByLineIds(lineIds);
		
		createEngineTablePane(engines);
	}

	private void engineSelected() {
		
		String ein = einField.getText();
		
		if(ein == null){
			setErrorMessage("Please type in the engine serial number");
			return;
		}
		
		Engine engine = getDao(EngineDao.class).findByKey(ein);
		if(engine == null) {
			setErrorMessage("Incorrect engine serial number");
			return;
		}
		
		List<Engine> engines = new ArrayList<Engine>();
		engines.add(engine);
		createEngineTablePane(engines);
		
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
		
		List<Engine> engines = getDao(EngineDao.class).findAllByProductionLot(prodLot.getProductionLot());
		if(engines.isEmpty()) {
			setErrorMessage("Incorrect production lot number");
			return;
		}
		
		createEngineTablePane(engines);
		
	}
	
	private void createEngineTablePane(List<Engine> engines) {
		
		itemListContainer.removeAll();
		ColumnMappings columnMappings  = ColumnMappings.with("#",""); 
		columnMappings.put("Engine Serial Number","productId");
		ObjectTablePane<Engine> objectTablePane = new ObjectTablePane<Engine>(columnMappings.get(),false,true);
		objectTablePane.setAlignment(SwingConstants.CENTER);
		objectTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		itemListContainer.add(objectTablePane,BorderLayout.CENTER);
		objectTablePane.reloadData(engines);
		this.objectTablePane = objectTablePane;
		
	}

	
}
