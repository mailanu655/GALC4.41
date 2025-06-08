package com.honda.galc.client.ui.component;

import java.awt.Component;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.entity.product.ProductSpec;

/**
 * 
 * <h3>IProductSpecSelection</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IProductSpecSelection description </p>
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
 * <TD>Jan 26, 2015</TD>
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
 * @since Jan 26, 2015
 */
public abstract class ProductSpecSelectionBase extends JPanel {
	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String, LabeledListBox> columnBoxList = new LinkedHashMap<String, LabeledListBox>();
	
	public abstract List<String> buildSelectedProductSpecCodes();
	public abstract boolean isProductSpecSelected();	
	public abstract boolean isUniqueFullSpecSelected();
	public abstract void clearSelection();

	protected Component createPanel(String name, boolean singleSelection) {
		LabeledListBox newPanel = getColumnBoxList().get(name);
		if(newPanel == null) {
			newPanel = new LabeledListBox(name);
			newPanel.getComponent().setName("J" + name.replace("_", "") +"List");
			if(singleSelection)
				newPanel.getComponent().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			getColumnBoxList().put(name, newPanel);
		}
		return newPanel;
	}

	public LabeledListBox getPanel(String columnName) {
		return getColumnBoxList().get(columnName);
	}

	@SuppressWarnings({ "unchecked"})
	protected void setModel(String columnName, ListModel model){
		getPanel(columnName).getComponent().setModel(model);
	}

	protected String getSelectedValue(String clnName) {
		return (String)getColumnBoxList().get(clnName).getComponent().getSelectedValue();
	}
	
	protected Object[] getSelectedValues(String name) {
		Object[] values = getPanel(name).getComponent().getSelectedValues();
		return values;
	}

	public Collection<LabeledListBox> getColumnBoxsList(){
		return getColumnBoxList().values();
	}

	public void registerProcessPointSelectionPanel(ProcessPointSelectiontPanel processPointPanel) {
		// TODO Auto-generated method stub
		
	}
	
	public void registerParentPartNameSelectionPanel(ParentPartNameSelectionPanel parentPartNameSelectionPanel) {
		// TODO Auto-generated method stub
	}
	
	public void registerProductTypeSelectionPanel(
			ProductTypeSelectionPanel productTypeSelectionPanel) {
		// TODO Auto-generated method stub
		
	}
	
    protected void sendProductSpecSelectingEvent() {
    	EventBus.publish(new ProductSpecSelectionEvent(this,SelectionEvent.SELECTING));
    }
    
    public boolean isNonWildcardValueExist(JList list) {
		for(int i = 0; i < list.getModel().getSize(); i++) {
			if(!ProductSpec.WILDCARD.equals(list.getModel().getElementAt(i).toString().trim())) {
				return true;
			}
		}
		return false;
	}
    
    public LinkedHashMap<String, LabeledListBox> getColumnBoxList() {
		return columnBoxList;
	}

	public void setColumnBoxList(LinkedHashMap<String, LabeledListBox> columnBoxList) {
		this.columnBoxList = columnBoxList;
	}


}