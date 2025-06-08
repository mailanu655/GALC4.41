package com.honda.galc.client.ui.component;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.ui.event.ParentPartNameSelectionEvent;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.ProductTypeSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;


/**
 * 
 * <h3>ProductSpecSelectionPanel Class description</h3>
 * <p> ProductSpecSelectionPanel description </p>
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
 * Apr 13, 2012
 *
 *
 */
@SuppressWarnings("serial")
public class ProductSpecSelectionPanel extends ProductSpecSelectionBase implements ListSelectionListener{
	public enum YmtocColumns{Year,Model, Model_Type,Option,Ext_Color,Int_Color}
    private ProductSpecData productSpecData= null;
    
    ProcessPointSelectiontPanel ppSelectionPanel;
    ProductTypeSelectionPanel ptSelectionPanel;
    ParentPartNameSelectionPanel ppNameSelectionPanel;
    
    
    public ProductSpecSelectionPanel() {
    	super();
        
        AnnotationProcessor.process(this);
        initComponent();
        addSelectionListeners();
    }   
    
    public ProductSpecSelectionPanel(String productType, ProductSpecData productSpecData) {
    	this();
    	this.productSpecData = productSpecData;
    	getColumnBoxList().get(YmtocColumns.Year.name()).getComponent().setModel(
				new ListModel<String>(productSpecData.getModelYearCodes()));
    }
    
    public ProductSpecSelectionPanel(String productType) {
    	this(productType, new ProductSpecData(productType));
    }
    
    public void initComponent(){
       
        setLayout(new GridLayout(1, 6, 3, 3));
        
    	for(YmtocColumns c : YmtocColumns.values())
			add(createPanel(c.name(), c.ordinal() < 2));
    	
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  
    }
    
	private void addSelectionListeners() {
    	                       
		getPanel(YmtocColumns.Year.name()).getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                modelYearCodeChanged(e);
            }
        });
        
		getPanel(YmtocColumns.Model.name()).getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                modelCodeChanged(e);
            }
        });
        
		getPanel(YmtocColumns.Model_Type.name()).getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                modelTypeCodeChanged(e);
            }
        });
        
		getPanel(YmtocColumns.Option.name()).getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                modelOptionCodeChanged(e);
            }
        });
        
		getPanel(YmtocColumns.Ext_Color.name()).getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                modelExtColorCodeChanged(e);
            }
        });
        
		getPanel(YmtocColumns.Int_Color.name()).getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                modelIntColorCodeChanged(e);
            }
        });
    }
    
    public void modelYearCodeChanged(ListSelectionEvent e){
    	if(e.getValueIsAdjusting()) return;
        String modelYearCode = getSelectedValue(YmtocColumns.Year.name());
        if (modelYearCode!=null)
        {
        	Logger.getLogger().info("Model Year Code "+modelYearCode+" is selected");
        	setModel(YmtocColumns.Model.name(),new ListModel<String>(productSpecData.getModelCodes(modelYearCode)));
        } else 
            setModel(YmtocColumns.Model.name(), new ListModel<String>(new ArrayList<String>()));
        
        sendProductSpecSelectingEvent();
    }
    
 
	public void modelCodeChanged(ListSelectionEvent e){
    	if(e.getValueIsAdjusting()) return;
        String modelYearCode = getSelectedValue(YmtocColumns.Year.name());
        String modelCode = getSelectedValue(YmtocColumns.Model.name());
        if(modelCode!=null) {
        	Logger.getLogger().info("Model Code "+modelCode+" is selected");
        	setModel(YmtocColumns.Model_Type.name(),new ListModel<String>(productSpecData.getModelTypeCodes(modelYearCode,modelCode)));
        } else
            setModel(YmtocColumns.Model_Type.name(),  new ListModel<String>(new ArrayList<String>()));

        sendProductSpecSelectingEvent();
    }
    
    public void modelTypeCodeChanged(ListSelectionEvent e){
    	if(e.getValueIsAdjusting()) return;
    	  String modelYearCode = getSelectedValue(YmtocColumns.Year.name());
          String modelCode = getSelectedValue(YmtocColumns.Model.name());
          Object[] modelTypeCodes = getSelectedValues(YmtocColumns.Model_Type.name());
        if(modelTypeCodes.length>0){
        	for(Object code : modelTypeCodes){
          		Logger.getLogger().check("Model Type Code "+ code.toString() +" is selected");
          	}
        }
        
        if(modelTypeCodes.length == 0) setModel(YmtocColumns.Option.name(),  new ListModel<String>(new ArrayList<String>()));
        else  setModel(YmtocColumns.Option.name(), new ListModel<String>(productSpecData.getModelOptionCodes(modelYearCode, modelCode, modelTypeCodes)));
        getPanel(YmtocColumns.Option.name()).getComponent().setSelectedIndex(0);
    }

    public void modelOptionCodeChanged(ListSelectionEvent e){
    	if(e.getValueIsAdjusting()) return;
    	String modelYearCode = getSelectedValue(YmtocColumns.Year.name());
        String modelCode = getSelectedValue(YmtocColumns.Model.name());
        Object[] modelTypeCodes = getSelectedValues(YmtocColumns.Model_Type.name());
        Object[] modelOptionCodes =  getSelectedValues(YmtocColumns.Option.name());
        if(modelOptionCodes.length>0){
        	for(Object code: modelOptionCodes){
        		Logger.getLogger().check("Model Option Code " + code.toString() + " is selected");
        	}
        }        
        
        if(modelOptionCodes.length == 0) setModel(YmtocColumns.Ext_Color.name(),  new ListModel<String>(new ArrayList<String>()));
        else setModel(YmtocColumns.Ext_Color.name(),new ListModel<String>(productSpecData.getModelExtColorCodes(modelYearCode,modelCode,modelTypeCodes,modelOptionCodes)));
		getPanel(YmtocColumns.Ext_Color.name()).getComponent().setSelectedIndex(0);
    }
    
    public void modelExtColorCodeChanged(ListSelectionEvent e){
    	if(e.getValueIsAdjusting()) return;
    	String modelYearCode = getSelectedValue(YmtocColumns.Year.name());
        String modelCode = getSelectedValue(YmtocColumns.Model.name());
        Object[] modelTypeCodes = getSelectedValues(YmtocColumns.Model_Type.name());
        Object[] modelOptionCodes =  getSelectedValues(YmtocColumns.Option.name());
        Object[] exitColorCodes = getSelectedValues(YmtocColumns.Ext_Color.name());
        if(exitColorCodes.length>0){
        	for(Object code : exitColorCodes){
        		Logger.getLogger().check("Exterior Color " + code.toString() + " is selected");
        	}
        }
        
        if(exitColorCodes.length == 0) setModel(YmtocColumns.Int_Color.name(),new ListModel<String>(new ArrayList<String>()));
        else setModel(YmtocColumns.Int_Color.name(), new ListModel<String>(productSpecData.getModelIntColorCodes(modelYearCode,modelCode,modelTypeCodes,modelOptionCodes,exitColorCodes)));
        getPanel(YmtocColumns.Int_Color.name()).getComponent().setSelectedIndex(0);
    }
   
    public void modelIntColorCodeChanged(ListSelectionEvent e) {
        
    	if(e.getValueIsAdjusting()) return;
    	if( getPanel(YmtocColumns.Int_Color.name()).getComponent().getModel().getSize() != 0){
    		Object[] intColorCodes = getSelectedValues(YmtocColumns.Int_Color.name());
    		for(Object code : intColorCodes){
        		Logger.getLogger().check("Interior Color " + code.toString() + " is selected");
    		}    		
        	EventBus.publish(new ProductSpecSelectionEvent(this,SelectionEvent.SELECTED));
    	}else sendProductSpecSelectingEvent();

    }
    
    public String getSelectedModelYearCode() {
    	return getSelectedValue(YmtocColumns.Year.name());
    }
    
    public String getSelectedModelCode() {
    	return getSelectedValue(YmtocColumns.Model.name());
    }
    
    public String getSelectedModelTypeCode() {
    	return getSelectedValue(YmtocColumns.Model_Type.name());
    }
    
    public String getSelectedModelOptionCode() {
    	return getSelectedValue(YmtocColumns.Option.name());
    }
    
    public String getSelectedExtColorCode() {
    	return getSelectedValue(YmtocColumns.Ext_Color.name());
    }
    
    public String getSelectedIntColorCode() {
    	return getSelectedValue(YmtocColumns.Int_Color.name());
    }
    
    public Object[] getSelectedModelTypeCodes() {
    	return getPanel(YmtocColumns.Model_Type.name()).getComponent().getSelectedValues();
    }
    
    public Object[] getSelectedModelOptionCodes() {
    	return getPanel(YmtocColumns.Option.name()).getComponent().getSelectedValues();
    }
    
    public Object[] getSelectedExtColorCodes() {
    	return getPanel(YmtocColumns.Ext_Color.name()).getComponent().getSelectedValues();
    }
    
    public Object[] getSelectedIntColorCodes() {
    	return getPanel(YmtocColumns.Int_Color.name()).getComponent().getSelectedValues();
    }
    
    public List<EngineSpec> getSelectedEngineSpecData() {
    	String modelYearCode = getSelectedValue(YmtocColumns.Year.name());
        String modelCode = getSelectedValue(YmtocColumns.Model.name());
        Object[] modelTypeCodes = getSelectedValues(YmtocColumns.Model_Type.name());
        Object[] modelOptionCodes =  getSelectedValues(YmtocColumns.Option.name());
        return productSpecData.getProductSpecData(modelYearCode, modelCode, modelTypeCodes,modelOptionCodes);
    }
    
    public boolean isProductSpecSelected() {
    	return getPanel(YmtocColumns.Int_Color.name()).getComponent().getModel().getSize() != 0;
    }
    
    
	public List<String> buildSelectedProductSpecCodes() {
		return productSpecData == null ? new ArrayList<String>() : productSpecData.getProductSpecData(getSelectedModelYearCode(), 
				getSelectedModelCode(), getSelectedModelTypeCodes(), 
				getSelectedModelOptionCodes(), getSelectedExtColorCodes(),
				getSelectedIntColorCodes());
	}
	
	public boolean isUniqueFullSpecSelected() {
    	JList list;
    	Object[] selectedItems;
    	for(YmtocColumns column : YmtocColumns.values()) {
    		list = getPanel(column.name()).getComponent();
        	selectedItems = list.getSelectedValues();

        	if(selectedItems.length != 1) {
        		return false;
        	}
        	
        	if(ProductSpec.WILDCARD.equals(selectedItems[0].toString().trim()) && isNonWildcardValueExist(list)) {
        		return false;
        	}
    	}
    	return true;
    } 
	
	@Override
	public void clearSelection() {
		for(YmtocColumns column : YmtocColumns.values()) {
			getPanel(column.name()).getComponent().getSelectionModel().clearSelection();
		}
	}
    
    @EventSubscriber(eventClass=ProcessPointSelectionEvent.class)
    public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
    
    	if(event.isEventFromSource(SelectionEvent.DEPARTMENT_SELECTED, ppSelectionPanel)) {
    		productSpecData = null;
    		getPanel(YmtocColumns.Year.name()).getComponent().setModel(new ListModel<String>(new ArrayList<String>()));
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
    
    @EventSubscriber(eventClass = ParentPartNameSelectionEvent.class)
    public void parentPartNamePChanged(ParentPartNameSelectionEvent event) {
    	if(event.isEventFromSource(SelectionEvent.PARENT_PART_SELECTED, ppNameSelectionPanel)) {
    		populateProductSpec(ppNameSelectionPanel.getProductType());
    	}
    }

   protected void populateProductSpec(String productType) {
	   if(productType != null) {
			if(productSpecData == null || !productSpecData.isProductType(productType)) {
    			productSpecData = new ProductSpecData(productType); 
			}
			setModel(YmtocColumns.Year.name(), new ListModel<String>(productSpecData.getModelYearCodes()));
			if(getPanel(YmtocColumns.Year.name()).getComponent().getModel().getSize() > 0) {
    			EventBus.publish(new ProductSpecSelectionEvent(this,SelectionEvent.POPULATED));
    		    
    	}
    }
   }
   
   public void registerProcessPointSelectionPanel(ProcessPointSelectiontPanel ppSelectionPanel) {
   		this.ppSelectionPanel = ppSelectionPanel;
   }
    
   public void registerParentPartNameSelectionPanel(ParentPartNameSelectionPanel parentPartNameSelectionPanel) {
   		this.ppNameSelectionPanel = parentPartNameSelectionPanel;
   }
   
   public void registerProductTypeSelectionPanel(ProductTypeSelectionPanel ptSelectionPanel) {
	   
	   this.ptSelectionPanel = ptSelectionPanel;
	   
   }
   
   
   public ApplicationPropertyBean getApplicationPropertyBean(String applicationId) {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class, applicationId);
	}
   
   public void loadProductSpec(Plant plant) {
	   
	   setCursor(new Cursor(Cursor.WAIT_CURSOR));
	   
//	   productSpecData.loadProductSpec(plant);
	   
	   setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	   
	  setModel(YmtocColumns.Year.name(),new ListModel<String>(productSpecData.getModelYearCodes()));
		
   }
   

	public void valueChanged(ListSelectionEvent e) {
	}
    
}

