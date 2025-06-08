package com.honda.galc.client.teamleader.hold.qsr.release.vinseq;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.util.List;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.ui.component.Calendar;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.HoldAccessTypeDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;


import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.LDAPService;



public class ReleaseBySeqPanel extends ReleasePanel {

	private static final long serialVersionUID = 1L;

	private ProductTypeData productTypeData;
	private ProductSpecData productSpecData;
	private String dateSelected = "";

	public ReleaseBySeqPanel(QsrMaintenanceFrame mainWindow) {
		super(mainWindow);
		this.setScreenName("Release By Seq");
	}

	// === init methods === //
	protected void initView() {
		super.initView();
		getSplitPanel().setDividerLocation(200);
		getInputPanel().setInputEnabled(false);
	}
		
	private void populateSpec(JList list, List<String> values) {
		 ComboBoxModel<String> model = new ComboBoxModel<String>(values);
		 list.setModel(model);
		 list.setSelectedIndex(-1);
	}
	
	public void loadProductSpecs() {
		ProductType productType = getProductType();
		productSpecData= new ProductSpecData(productType.name());
		productSpecData.loadProductSpec();
	}
	
	protected PropertiesMapping putQsrColumnsMapping(PropertiesMapping mapping) {	
		mapping.put("Product Status", "holdResult.releaseFlag", createQsrReleaseFlagFormat());
		mapping.put("Hld Timestamp", "holdResult.createTimestamp");
		mapping.put("Hld Reason", "holdResult.holdReason");
		mapping.put("QSR ID", "holdResult.qsrId");
		mapping.put("Hld Assoc Name", "holdResult.holdAssociateName");
		mapping.put("Rls Timestamp", "holdResult.releaseTimestamp");
		mapping.put("Rls Assoc Name", "holdResult.releaseAssociateName");
		mapping.put("Rls Assoc Phone", "holdResult.releaseAssociatePhone");

		return mapping;
	}

	protected PropertiesMapping createEngineColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("EIN", "product.productId");
		mapping.put("VIN", "product.vin");
		mapping.put("Production Lot", "product.productionLot");
		mapping.put("KD Lot", "product.kdLotNumber");
		mapping.put("Last Process", "lastProcessPointName");
		mapping.put("Last Process Time Stamp", "updateTimestamp");
		

		return mapping;
	}

	// === factory methods === //
	@Override
	protected ReleaseBySeqInputPanel createInputPanel() {
		ReleaseBySeqInputPanel panel = new ReleaseBySeqInputPanel(this);
		return panel;
	}

	public ProductTypeData getProductTypeData(){
		if(getProductType() != null) {
			for(ProductTypeData type : window.getApplicationContext().getProductTypeDataList()){
				if(type.getProductTypeName().equals(getProductType().name())){
					productTypeData = type;
					break;
				}
			}
		}
	
	return productTypeData;
	}
	protected void mapActions() {
		super.mapActions();

		
		getInputPanel().getVinButton().addActionListener(this);
		getInputPanel().getClearButton().addActionListener(this);
		getInputPanel().getProductInput().addActionListener(this);
		getInputPanel().getProductTypeComboBox().addActionListener(new BaseListener<ReleasePanel>(this) {
			@Override
			public void executeActionPerformed(ActionEvent e) {
				getInputPanel().resetInput();
				getInputPanel().setInputEnabled(true);
				getInputPanel().getCommandButton().setEnabled(true);
				getInputPanel().getVinButton().setText(getProductTypeData().getProductIdLabel());
				getInputPanel().getQsrComboBox().removeAllItems();
				ProductType productType = getProductType();
				if (productType != null) {
					getInputPanel().getHoldTypeElement().getComponent().setModel(new DefaultComboBoxModel(new Vector<HoldAccessType>(getHoldAccessTypes())));
					getInputPanel().getHoldTypeElement().getComponent().setSelectedIndex(-1);
					if(productType.equals(ProductType.FRAME)) {
						getInputPanel().showFilterInput(true);
					}else {
						getInputPanel().showFilterInput(false);
					}
				}
			}
		});
		getInputPanel().getHoldTypeComboBox().addItemListener(new BaseListener<ReleasePanel>(this) {
			@Override
			public void executeItemStateChanged(ItemEvent e) {
				getView().getProductPanel().removeData();
				if (ItemEvent.DESELECTED == e.getStateChange()) {
					getView().getInputPanel().getCommandButton().setEnabled(false);
				} else if (ItemEvent.SELECTED == e.getStateChange()) {
					getView().getInputPanel().getCommandButton().setEnabled(true);
					getInputPanel().setInputEnabled(true);
				}
				loadProductSpecs();
				populateSpec(getInputPanel().getModelYearListElement().getComponent(), productSpecData.getModelYearCodes());
				
			}
		});
		
		getInputPanel().getModelYearListElement().getComponent().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(getInputPanel().getModelYearListElement().getComponent().getSelectedValues()!= null) {
					String year = (String)getInputPanel().getModelYearListElement().getComponent().getSelectedValue();
					if(StringUtils.isNotEmpty(year))
					populateSpec(getInputPanel().getModelCodeListElement().getComponent(), productSpecData.getModelCodes(year));
				}
				getInputPanel().getCommandButton().setEnabled(enableCommandButton());
				
			}
			
		});
		
		getInputPanel().getModelCodeListElement().getComponent().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(getInputPanel().getModelCodeListElement().getComponent().getSelectedValues()!= null) {
					String year = (String)getInputPanel().getModelYearListElement().getComponent().getSelectedValue();
					String modelCode = (String)getInputPanel().getModelCodeListElement().getComponent().getSelectedValue();
					if(StringUtils.isNotEmpty(year) && StringUtils.isNotEmpty(modelCode))
					populateSpec(getInputPanel().getModelTypeListElement().getComponent(), productSpecData.getModelTypeCodes(year, modelCode));
				}
				getInputPanel().getCommandButton().setEnabled(enableCommandButton());
				
			}
			
		});
		getInputPanel().getStartTimeButton().addActionListener(this);
		getInputPanel().getEndTimeButton().addActionListener(this);
		
		getInputPanel().getStartVinSeqInput().addActionListener(this);
		getInputPanel().getEndVinSeqInput().addActionListener(this);
	}
	


	@Override
	public ReleaseBySeqInputPanel getInputPanel() {
		return (ReleaseBySeqInputPanel) super.getInputPanel();
	}

	public void actionPerformed(ActionEvent e) {
		 if(e.getSource().equals(getInputPanel().getVinButton())){
			String selectedProductId = selectProductFromDialog();
			if (!StringUtils.isBlank(selectedProductId)){
				getInputPanel().getProductInput().setText(selectedProductId);
				getInputPanel().getProductInput().postActionEvent();
				getInputPanel().getCommandButton().setEnabled(true);
			}
			getInputPanel().getProductInput().requestFocusInWindow();
			
		}else if(e.getSource().equals(getInputPanel().getClearButton())){
			getInputPanel().resetInput();
			getInputPanel().setInputEnabled(true);
			getInputPanel().getHoldTypeElement().getComponent().setSelectedIndex(-1);
			getInputPanel().getQsrElement().getComponent().setSelectedIndex(-1);
			getProductPanel().removeData();
		}else if(e.getSource().equals(getInputPanel().getStartTimeButton())){
				selectDateTime();
				getInputPanel().getStartTimeInput().setText(!StringUtils.isBlank(dateSelected) ? dateSelected: getInputPanel().getStartTimeInput().getText());
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				try {
					date = format.parse(dateSelected);
					getInputPanel().getStartTimeInput().setValue(date);
				} catch (ParseException e1) {
					Logger.getLogger(getApplicationId()).error(Arrays.toString(e1.getStackTrace()));
				}
				getInputPanel().getCommandButton().setEnabled(enableCommandButton());
				
			}
			else if(e.getSource().equals(getInputPanel().getEndTimeButton())){
				selectDateTime();
				getInputPanel().getEndTimeInput().setText(!StringUtils.isBlank(dateSelected) ? dateSelected : getInputPanel().getEndTimeInput().getText());
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				try {
					date = format.parse(dateSelected);
					getInputPanel().getEndTimeInput().setValue(date);
				} catch (ParseException e1) {
					Logger.getLogger(getApplicationId()).error(Arrays.toString(e1.getStackTrace()));
				}
				
				getInputPanel().getCommandButton().setEnabled(enableCommandButton());
			}else if(e.getSource().equals(getInputPanel().getEndVinSeqInput())||e.getSource().equals(getInputPanel().getStartVinSeqInput())||e.getSource().equals(getInputPanel().getProductInput())){
				getInputPanel().getCommandButton().setEnabled(enableCommandButton());
			}else {
				getInputPanel().getCommandButton().setEnabled(enableCommandButton());
			}
		
	}
	
	public String selectDateTime(){
		Calendar calendar = new Calendar();
		calendar.setModal(true);
		calendar.setVisible(true);
		dateSelected = calendar.getFinalTextDate();
		return dateSelected; 
	}
	
	public String selectProductFromDialog(){
		String productId = "";
		ProductType productType = getProductType();
		ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(getMainWindow(), productType.toString(), ProductNumberDef.getProductNumberDef(productType).get(0).getName());
		manualProductEntry.setModal(true);
		manualProductEntry.setVisible(true);
		productId = manualProductEntry.getResultProductId();
		return productId;
	}
	
	protected PropertiesMapping createFrameColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("VIN", "product.productId");
		mapping.put("Short Vin", "product.shortVin");
		mapping.put("YMTOC", "product.productSpecCode");
		mapping.put("Production Lot", "product.productionLot");
		mapping.put("AF ON Seq.", "product.afOnSequenceNumber");
		mapping.put("KD Lot", "product.kdLotNumber");
		mapping.put("EIN", "product.engineSerialNo");
		mapping.put("Last Process", "lastProcessPointName");
		mapping.put("Last Process Time Stamp", "updateTimestamp");
		return mapping;
	}
	
	private boolean enableCommandButton() {
		String startDate = getInputPanel().getStartTimeInput().getText();
		String endDate = getInputPanel().getEndTimeInput().getText();
		if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			return true;
		}
		String startSeq = getInputPanel().getStartVinSeqInput().getText();
		String endSeq = getInputPanel().getEndVinSeqInput().getText();
		if (!StringUtils.isEmpty(startSeq) && !StringUtils.isEmpty(endSeq)) {
			return true;
		}
		if(getInputPanel().getQsrComboBox().getSelectedItem()!= null) {
			return true;
		}
		String productId = getInputPanel().getProductInput().getText();
		if(!StringUtils.isEmpty(productId)) {
			return true;
		}
		return false;
	}
}
