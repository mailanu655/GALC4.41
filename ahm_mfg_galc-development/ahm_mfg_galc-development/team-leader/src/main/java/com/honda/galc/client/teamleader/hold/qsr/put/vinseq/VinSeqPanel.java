package com.honda.galc.client.teamleader.hold.qsr.put.vinseq;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.client.ui.component.Calendar;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.report.TableReport;
import com.honda.galc.service.ServiceFactory;

public class VinSeqPanel extends HoldProductPanel {

	private static final long serialVersionUID = 1L;

	private Action selectProductsByVinSeqAction;
	private Action clearAction;

	private ProductTypeData productTypeData;
	
	private String dateSelected = "";
	
	private ProductSpecData productSpecData;

	public VinSeqPanel(QsrMaintenanceFrame mainWindow) {
		super("Hold By Product Sequence", KeyEvent.VK_H, mainWindow);
	}

	@Override
	protected void initView() {
		super.initView();
		getSplitPanel().setDividerLocation(150);
		setSelectProductAction(new SelectProductsByVinSeqAction(this));
		setClearAction(new ClearVinInputAction(this));
	}
	
	protected void initModel() {
		List<String> lines = Arrays.asList(Config.getProperty().getPlanCodes());
		getInputPanel().getLineElement().getComponent().setModel(new DefaultComboBoxModel(new Vector<String>(lines)));
		if(lines.size() == 1)getInputPanel().getLineElement().getComponent().setSelectedIndex(0);
		else getInputPanel().getLineElement().getComponent().setSelectedIndex(-1);
	}
	
	private void populateSpec(JList list, List<String> values) {
		 ComboBoxModel<String> model = new ComboBoxModel<String>(values);
		 list.setModel(model);
		 list.setSelectedIndex(-1);
	}
	
	public void loadProductSpecs() {
		productSpecData= new ProductSpecData(getProductType().getProductName());
		productSpecData.loadProductSpec();
	}
	
	@Override
	protected void defineProductTypeColumnsMapping() {
		super.defineProductTypeColumnsMapping();
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
		mapping.put("Current Status","product.trackingStatus");
		return mapping;
	}

	// === mappings === //
	protected void mapActions() {
		super.mapActions();
		
		getInputPanel().getProductTypeComboBox().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ProductType productType = getProductType();
				if (productType != null) {
					getInputPanel().resetInput();
					getInputPanel().showFilterInput(true);
					getInputPanel().setInputEnabled(true);
					getInputPanel().getCommandButton().setEnabled(true);
					getInputPanel().getVinButton().setText(getProductTypeData().getProductIdLabel());
					if(getProductType().equals(ProductType.FRAME)) {
						loadProductSpecs();
						populateSpec(getInputPanel().getModelYearListElement().getComponent(), productSpecData.getModelYearCodes());
					}else {
						getInputPanel().showFilterInput(false);
					}
					
				}
				
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
			
			}
			
		});
		
		getInputPanel().getModelCodeListElement().getComponent().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
			
				if(getInputPanel().getModelCodeListElement().getComponent().getSelectedValues()!= null) {
					String year = (String)getInputPanel().getModelYearListElement().getComponent().getSelectedValue();
					String modelCode = (String)getInputPanel().getModelCodeListElement().getComponent().getSelectedValue();
					if(StringUtils.isNotEmpty(year) && (StringUtils.isNotEmpty(modelCode))) {
						populateSpec(getInputPanel().getModelTypeListElement().getComponent(), productSpecData.getModelTypeCodes(year, modelCode));
						populateSpec(getInputPanel().getModelDestListElement().getComponent(), getDestinations(year, modelCode));
					}
					
					
				}
			}
			
		});
		
		getInputPanel().getCommandButton().setAction(getSelectProductAction());
		getInputPanel().getClearButton().setAction(getClearAction());
		getInputPanel().getCommandButton().setEnabled(false);
		getInputPanel().getVinButton().addActionListener(this);
		getInputPanel().getLineElement().getComponent().addActionListener(this);
	
	
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

	public String selectDateTime(){
		Calendar calendar = new Calendar();
		calendar.setModal(true);
		calendar.setVisible(true);
		dateSelected = calendar.getFinalTextDate();
		return dateSelected; 
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getInputPanel().getLineElement().getComponent())) {
			boolean enabled = false;
			if(getInputPanel().getLineElement().getComponent().getSelectedItem()!= null) {
				enabled = true;
			}
			getInputPanel().setInputEnabled(enabled);
		}else if(e.getSource().equals(getInputPanel().getVinButton())){
			String selectedProductId = selectProductFromDialog();
			if (!StringUtils.isBlank(selectedProductId)){
				getInputPanel().getProductInput().setText(selectedProductId);
				getInputPanel().getProductInput().postActionEvent();
				getInputPanel().getCommandButton().setEnabled(true);
			}
			getInputPanel().getProductInput().requestFocusInWindow();
			
		}
		else if(e.getSource().equals(getInputPanel().getModelCodeListElement())) {
			boolean enabled = false;
			
			if(getInputPanel().getModelCodeListElement().getComponent().getSelectedValues()!= null) {
				enabled = true;
			}
	
			getInputPanel().setInputEnabled(enabled);
			getInputPanel().getCommandButton().setEnabled(true);
		
		}
		
	}
	
	// === factory methods === //
	@Override
	public VinSeqInputPanel createInputPanel() {
		VinSeqInputPanel panel = new VinSeqInputPanel(this);
		panel.setInputEnabled(false);
		return panel;
	}

	@Override
	public TableReport getReport() {
		TableReport report = super.getReport();
		Division division = getDivision();
		String fileName = String.format("QSR-HOLD-%s.xlsx", division.getDivisionId());
		String sheetName = String.format("QSR-HOLD-%s", getDivision().getDivisionId());
		String headerLine = String.format("QSR-HOLD-%s-%s", division.getDivisionId(), getProductType());
		report.setFileName(fileName);
		report.setTitle(headerLine);
		report.setReportName(sheetName);
		return report;
	}

	@Override
	public void defineReports() {
		super.defineReports();
		for (TableReport report : getReports().values()) {
			report.addColumn("history.actualTimestamp", "Time", 7000);
		}
	}

	@Override
	public VinSeqInputPanel getInputPanel() {
		return (VinSeqInputPanel) super.getInputPanel();
	}

	public Action getClearAction() {
		return clearAction;
	}

	public void setClearAction(Action clearAction) {
		this.clearAction = clearAction;
	}

	public Action getSelectProductAction() {
		return selectProductsByVinSeqAction;
	}

	public void setSelectProductAction(Action selectProductAction) {
		this.selectProductsByVinSeqAction = selectProductAction;
	}
	
	public ProductTypeData getProductTypeData(){
			for(ProductTypeData type : window.getApplicationContext().getProductTypeDataList()){
				if(type.getProductTypeName().equals(getProductType().name())){
					productTypeData = type;
					break;
				}
			}
		
		return productTypeData;
	}
	
	public List<String> getDestinations(String modelYearCode, String modelCode){
		List<String> destinations = new ArrayList<String>();
		 Set<String> salesModelTypeCodes = new HashSet<String>();
		List<FrameSpec> frameSpecs = ServiceFactory.getDao(FrameSpecDao.class).findAllByYMTOCWildCard(modelYearCode, modelCode, "*", "*", "*", "*");
		for(FrameSpec productSpec: frameSpecs) {
			if(productSpec.getSalesModelTypeCode() != null)salesModelTypeCodes.add(productSpec.getSalesModelTypeCode());
				
		}
		destinations.addAll(salesModelTypeCodes);
		Collections.sort(destinations);
		return destinations;
	}
	
}
