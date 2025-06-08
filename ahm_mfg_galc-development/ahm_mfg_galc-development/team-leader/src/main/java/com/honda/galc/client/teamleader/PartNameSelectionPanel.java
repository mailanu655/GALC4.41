package com.honda.galc.client.teamleader;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.entity.product.PartName;

/**
 * 
 * <h3>PartNameSelectionPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartNameSelectionPanel description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Nov 19, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Nov 19, 2013
 */
public class PartNameSelectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected TablePane partSelectionPanel;
	private PartNameTableModel partNameTableModel;
	protected LabeledUpperCaseTextField partNameFilterTextField;
	private List<PartName> items;
	private PartName currentSelection;
	
	public PartNameSelectionPanel(int width, int height, Dimension preferredSize) {
		init(width, height, preferredSize);
	}


	private void init(int width, int height, Dimension preferredSize) {
		setSize(width, height);
		setPreferredSize(preferredSize);
		setMaximumSize(preferredSize);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		initComponents();
		initConnections();
	}

	private void initComponents() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(getPartSelectionPanel());
		this.add(getPartNameFilterTextField());
		
	}
	
	private void initConnections() {
	
		// Listen for changes in the text
		getPartNameFilterTextField().getComponent().getDocument().addDocumentListener(new DocumentListener() {
		  public void changedUpdate(DocumentEvent e) {
			  filterPartName();
		  }
		  public void removeUpdate(DocumentEvent e) {
			  filterPartName();
		  }
		  public void insertUpdate(DocumentEvent e) {
			  filterPartName();
		  }
		});
		
	}


	public TablePane getPartSelectionPanel() {
		if(partSelectionPanel == null){
			partSelectionPanel = new TablePane("Part Name",true);
		}
		return partSelectionPanel;
	}
	
	public PartNameTableModel getPartNameTableModel() {
		return partNameTableModel;
	}

	public LabeledUpperCaseTextField getPartNameFilterTextField() {
		if(partNameFilterTextField == null){
			partNameFilterTextField = new LabeledUpperCaseTextField("Part Filter:", true);
			partNameFilterTextField.getComponent().setColumns(32);
			partNameFilterTextField.setPreferredWidth((int)this.getSize().getWidth()/2);
			partNameFilterTextField.setPreferredHeight(15);
			partNameFilterTextField.setMaxHight(15);
			partNameFilterTextField.setInsets(1, 2, 1, 2);

		}
		return partNameFilterTextField;
	}


	private void filterPartName() {
		if(StringUtils.isEmpty(getPartNameFilterTextField().getComponent().getText())){
			partNameTableModel = new PartNameTableModel(partSelectionPanel.getTable(),items,true);
			partNameTableModel.pack();
		} else {
			
			PartName selectedItem = partNameTableModel == null ? null :partNameTableModel.getSelectedItem();
			
			List<PartName> filteredList = new ArrayList<PartName>();
			if (items != null)
				for(PartName pn :items)
					if(pn.getPartName().contains(getPartNameFilterTextField().getComponent().getText()))
						filteredList.add(pn);
			
			partNameTableModel = new PartNameTableModel(partSelectionPanel.getTable(),filteredList,true);
			
			if(filteredList.size() == 1) selectedItem = filteredList.get(0);
			if(!filteredList.contains(selectedItem)) selectedItem = null;
			
			if(selectedItem == null || !selectedItem.equals(currentSelection)) partNameTableModel.selectItem(selectedItem);
			
			partNameTableModel.pack();
			
			currentSelection = partNameTableModel.getSelectedItem();
			
		}
	}
	
	public boolean isPartNameSelected() {
		return (partNameTableModel.getSelectedItem() != null);
	}


	public void update(List<PartName> partNames) {
		items = partNames;
		filterPartName();
		
	}
}
