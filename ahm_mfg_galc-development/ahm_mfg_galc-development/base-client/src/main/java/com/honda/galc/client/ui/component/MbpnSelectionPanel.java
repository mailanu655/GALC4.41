package com.honda.galc.client.ui.component;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JList;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.data.MbpnSpecData;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.ProductTypeSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>MbpnSelectionPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnSelectionPanel description </p>
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
public class MbpnSelectionPanel extends ProductSpecSelectionBase implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	private MbpnSpecData mbpnSpecData = null;
	ProcessPointSelectiontPanel ppSelectionPanel;
	ParentPartNameSelectionPanel ppNameSelectionPanel;
    ProductTypeSelectionPanel ptSelectionPanel;

	
	public MbpnSelectionPanel() {
		super();
        
        AnnotationProcessor.process(this);
        initComponent();
        addSelectionListeners();
	}
	
	public MbpnSelectionPanel(String productType) {
		this();
		mbpnSpecData = new MbpnSpecData(productType); 
		getColumnBoxList().get(MbpnColumns.Main_No.name()).getComponent().setModel(
				new ListModel<String>(mbpnSpecData.getMainNos()));

	}

	private void addSelectionListeners() {
    	                       
		for(MbpnColumns clm : MbpnColumns.values()){
			getPanel(clm.name()).getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	            public void valueChanged(ListSelectionEvent e) {
	                columnValueChanged(e);
	            }
	        });
		}
		
    }

	protected void columnValueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		
		for(MbpnColumns mcls : MbpnColumns.values()){
			if(e.getSource() == getPanel(mcls.name()).getComponent().getSelectionModel())
				handleColumnValueChange(mcls, mcls.next());
				
		}
		
		sendProductSpecSelectingEvent();

	}

	private void handleColumnValueChange(MbpnColumns mclm, MbpnColumns next) {
		
		if(mclm == MbpnColumns.Hes_Color){
			if( getPanel(MbpnColumns.Hes_Color.name()).getComponent().getModel().getSize() != 0)
	        	EventBus.publish(new ProductSpecSelectionEvent(this,SelectionEvent.SELECTED));
			else
				sendProductSpecSelectingEvent();
			return;
		}
		
		String mclmValue = getSelectedValue(mclm.name());
		if(mclmValue != null) {
			Logger.getLogger().info(mclm.name(), " ", mclmValue, " is selected");
			loadNextColumn(mclm, next);
		} else 
			setModel(next.name(),  new ListModel<String>(new ArrayList<String>()));
		
		sendProductSpecSelectingEvent();
	}

	@SuppressWarnings("unchecked")
	private void loadNextColumn(MbpnColumns mclm, MbpnColumns next) {
		
		Class<? extends MbpnSpecData> claz = mbpnSpecData.getClass();
		String methodName = "get" + next.name().replaceAll("_","") +"s";
		Class parameterTypes[] = new Class[mclm.ordinal()+1];
		Object paramObjs[] = new Object[next.ordinal()];
		for(MbpnColumns cl: MbpnColumns.values()){
			if(cl.ordinal() < next.ordinal()){
				if(cl.ordinal() < 2){
					paramObjs[cl.ordinal()] = getSelectedValue(cl.name()); 
					parameterTypes[cl.ordinal()] = String.class;
				} else {
					paramObjs[cl.ordinal()] = getSelectedValues(cl.name());
					parameterTypes[cl.ordinal()] = Object[].class;
				}
			}
		}
		
		try {
			Method method = claz.getMethod(methodName, parameterTypes);
			List<String> listModel = (List<String>)method.invoke(mbpnSpecData, paramObjs);
        	setModel(next.name(),new ListModel<String>(listModel));
        	if(next.ordinal() >= 2)
        		getPanel(next.name()).getComponent().setSelectedIndex(0);
			
		} catch (Exception e) {
			Logger.getLogger().error(e,"Exception: inovke method:" + methodName + " params:" + Arrays.asList(paramObjs));
		}
	}

	private void initComponent() {
		setLayout(new GridLayout(1, 6, 3, 3));

		for(MbpnColumns c : MbpnColumns.values())
			add(createPanel(c, c.ordinal() < 2));
		
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

	}
	
	protected Component createPanel(MbpnColumns clm, boolean singleSelection) {
		LabeledListBox newPanel = getColumnBoxList().get(clm.name());
		if(newPanel == null) {
			newPanel = new LabeledListBox(clm.getColumnName());
			newPanel.getComponent().setName("J" + clm.name().replace("_", "") +"List");
			if(singleSelection)
				newPanel.getComponent().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			getColumnBoxList().put(clm.name(), newPanel);
		}
		return newPanel;
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
	}
	
	public List<String> buildSelectedProductSpecCodes() {
		return mbpnSpecData == null ? new ArrayList<String>() : mbpnSpecData.getProductSpecData(
				getSelectedValue(MbpnColumns.Main_No.name()), 
				getSelectedValue(MbpnColumns.Class_No.name()), 
				getSelectedValues(MbpnColumns.ProtoType_Code.name()),
				getSelectedValues(MbpnColumns.Type_No.name()),
				getSelectedValues(MbpnColumns.Supplementary_No.name()),
				getSelectedValues(MbpnColumns.Target_No.name()),
				getSelectedValues(MbpnColumns.Hes_Color.name()));
	}

	@Override
	public boolean isProductSpecSelected() {
		return getPanel(MbpnColumns.Hes_Color.name()).getComponent().getModel().getSize() != 0;
	}
	
    @EventSubscriber(eventClass=ProcessPointSelectionEvent.class)
    public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
    
    	if(event.isEventFromSource(SelectionEvent.DEPARTMENT_SELECTED, ppSelectionPanel)) {
    		mbpnSpecData = null;
    		getPanel(MbpnColumns.Main_No.name()).getComponent().setModel(new ListModel<String>(new ArrayList<String>()));
    	}else if(event.isEventFromSource(SelectionEvent.PROCESSPOINT_SELECTED, ppSelectionPanel)) {
    		String ppId = ppSelectionPanel.getCurrentProcessPointId();
    		if(ppId == null) return;
    		populateProductSpec(ppSelectionPanel.selectedProductType());
    	} else if(event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, ppSelectionPanel)) {
    		populateProductSpec(ppSelectionPanel.selectedProductType());
    	}
    }
    
    @EventSubscriber(eventClass=ProductTypeSelectionEvent.class)
    public void productTypeSelectedPanelChanged(ProductTypeSelectionEvent event) {
    	if(event.isEventFromSource(SelectionEvent.PRODUCT_TYPE_SELECTED, ptSelectionPanel)) {
    		populateProductSpec(ptSelectionPanel.selectedProductType());
    	}
    }

   protected void populateProductSpec(String productType) {
	   if(productType != null) {
			if(mbpnSpecData == null || !mbpnSpecData.isProductType(productType)) {
				mbpnSpecData = new MbpnSpecData(productType); 
			}
			
			setModel(MbpnColumns.Main_No.name(), new ListModel<String>(mbpnSpecData.getMainNos()));
			if(getPanel(MbpnColumns.Main_No.name()).getComponent().getModel().getSize() > 0) {
    			EventBus.publish(new ProductSpecSelectionEvent(this,SelectionEvent.POPULATED));    		
    	}
    }
   }
   
   public void registerProcessPointSelectionPanel(ProcessPointSelectiontPanel ppSelectionPanel) {
	   
	   this.ppSelectionPanel = ppSelectionPanel;
	   
   }
   
   public void registerParentPartSelectionPanel(ParentPartNameSelectionPanel ppNameSelectionPanel) {
	   this.ppNameSelectionPanel = ppNameSelectionPanel;		   
   }
   
   public void registerProductTypeSelectionPanel(ProductTypeSelectionPanel ptSelectionPanel) {
	   
	   this.ptSelectionPanel = ptSelectionPanel;
	   
   }
   
	public void loadProductSpec(Plant plant) {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setModel(MbpnColumns.Main_No.name(),new ListModel<String>(mbpnSpecData.getMainNos()));
	}
   
   public ApplicationPropertyBean getApplicationPropertyBean(String applicationId) {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class, applicationId);
	}
   
   @Override
   public boolean isUniqueFullSpecSelected() {
	   JList list;
	   Object[] selectedItems;
	   for(MbpnColumns column : MbpnColumns.values()) {
		   list = getPanel(column.name()).getComponent();
		   selectedItems = list.getSelectedValues();

		   if(selectedItems.length != 1) {
			   return false;
		   }

	   }
	   return true;
   }
   
   @Override
   public void clearSelection() {
	   for(MbpnColumns column : MbpnColumns.values()) {
		   getPanel(column.name()).getComponent().getSelectionModel().clearSelection();
	   }
   }
}