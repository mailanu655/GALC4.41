package com.honda.galc.client.schedule;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.utils.ProductTypeUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

/**
 * 
 * <h3>AddLotDialog Class description</h3>
 * <p> Add Lot Dialog description </p>
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
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TABLE>
 *  
 */
public class AddLotDialog extends FxDialog implements
EventHandler<ActionEvent> {
	
	private static int STANDARD_KD_LOT_SIZE = 30; 
	private static String SERVICE_LOT_PREFIX = "INT 01";
	
	protected ScheduleMainPanel schedulePanel;
	private static final String resourcePath = "/resource/%s.fxml";
	
	@FXML
    private TitledPane inputLotPane;

    @FXML
    private GridPane inputLotGridPane;

    @FXML
    private SplitPane scheduleMainSplitPane;

    @FXML
    private Label kdLotLabel;

    @FXML
    private Label prodLotLabel;

    @FXML
    private TextField kdLotTxtField;

    @FXML
    private TextField prodLotTxtField;

    @FXML
    private TitledPane serviceLotPane;

    @FXML
    private Slider lotSizeSlider;

    @FXML
    private Label lotSizeLabel;
    
    @FXML
    private CheckBox currentKdLotCheckbox;
    
    @FXML
    private Button addLotButton;
    
    @FXML
    private TextField lotSizeTxtField;
	
	
	private ProductSpecSelectionPanel productSpecPanel;
	
	private ObjectTablePane<PreProductionLot> serviceLotPanel;
	
	private List<PreProductionLot> servicelots;
	
	public AddLotDialog(ScheduleMainPanel schedulePanel) {
		super("Add Lot Dialog", ClientMainFx.getInstance().getStage(), true);
		this.schedulePanel = schedulePanel;
		initComponents();
		mapActions();
		servicelots = findAllServiceLots();
		loadData();
	}

	private void initComponents() {
		productSpecPanel = new ProductSpecSelectionPanel("KNUCKLE");
		inputLotGridPane.add(productSpecPanel, 3, 0, 6, 4);
		serviceLotPanel = createLotPanel();
		lotSizeSlider.setMax(getProperty().getMaxLotSize());
		lotSizeSlider.setMajorTickUnit(getProperty().getLotSizeInterval());
		serviceLotPane.setContent(serviceLotPanel);
	}
	
	private void mapActions() {
		addLotButton.setOnAction(this);
		currentKdLotCheckbox.setOnAction(this);
		lotSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                   lotSizeTxtField.setText(new_val.intValue() + "");
            }
        });
		
		lotSizeTxtField.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (StringUtils.isNotBlank(newValue))
							lotSizeSlider.setValue(Integer.parseInt(newValue));

					}
				});

	}
	
	private ObjectTablePane<PreProductionLot> createLotPanel(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Production Lot", "productionLot")
						  .put("Kd Lot", "kdLotNumber").put("Lot Size", "lotSize").put("Product Spec","productSpecCode");
		ObjectTablePane<PreProductionLot> panel = new ObjectTablePane<PreProductionLot>(columnMappingList);
		panel.setConstrainedResize(true);
		return panel;
	}
	
	private void loadData() {
		
		PreProductionLot lot = findLatestServiceLot(servicelots);
		
		currentKdLotCheckbox.setDisable(!enableCurrentKdLotCheckBox());
		currentKdLotCheckbox.setSelected(enableCurrentKdLotCheckBox());
		kdLotTxtField.setText(findNextKdLotNumber(lot, !currentKdLotCheckbox.isDisabled() && currentKdLotCheckbox.isSelected()));
		prodLotTxtField.setText(findNextProdLotNumber(lot));

		serviceLotPanel.setData(servicelots);
	}
	
	private  boolean enableCurrentKdLotCheckBox() {
		return getTotalLotSize(getLastServiceLots()) > 0 && getTotalLotSize(getLastServiceLots()) < STANDARD_KD_LOT_SIZE;
	}
	
	private int getTotalLotSize(List<PreProductionLot> lots) {
		int size = 0;
		for(PreProductionLot prodLot :lots)	size += prodLot.getLotSize();
		
		return size;
	}
	
	/**
	 * if the last lot of the process location is the service lot, find the lots of the same kd lot
	 * @return
	 */
	private List<PreProductionLot> getLastServiceLots() {
		List<MultiValueObject<PreProductionLot>> upcomingLots = schedulePanel.getUpcomingLotTblPane().getTable().getItems();
		List<PreProductionLot> serviceLots = new ArrayList<PreProductionLot>();
		if(upcomingLots.isEmpty()) return serviceLots;
		PreProductionLot lastLot = upcomingLots.get(upcomingLots.size()-1).getKeyObject();
		if(!isServiceLot(lastLot.getProductionLot())) return serviceLots;
		for (int i = upcomingLots.size() - 1; i >= 0; i--) {
			PreProductionLot preProdLot = upcomingLots.get(i).getKeyObject();
			if(preProdLot.isSameKdLot(lastLot)) serviceLots.add(preProdLot);
			else break;
		}
		return serviceLots;
	}
	
	private String findNextProdLotNumber(PreProductionLot prodLot) {
		String date = (new SimpleDateFormat("yyyyMMdd")).format(new java.util.Date());
		boolean isSameDate = prodLot!= null && date.equalsIgnoreCase(prodLot.getProductionLot().substring(8,16));
		
		int number = 10;
		
		if(isSameDate){
			String sn = prodLot.getProductionLot().substring(16,20);
			number = Integer.parseInt(sn) + 10;
		}

		return getServiceLotPrefix()+ getProcessLocation() + date+StringUtils.leftPad(""+number,4,"0");

	}

	private String getProcessLocation() {
		if(schedulePanel.isProcessLocationSelected()) {
			if(StringUtils.isEmpty(schedulePanel.getDropDownStringValue()))
				return  Arrays.asList(getProperty().getProcessLocation()).get(0);
			else
				return schedulePanel.getDropDownStringValue();
		} else {
			if(StringUtils.isEmpty(schedulePanel.getDropDownStringValue()))
				return  Arrays.asList(getProperty().getProcessLocation()).get(0);
			else
				return schedulePanel.getDropDownStringValue().substring(7,9);
		}
		
	}

	private String getServiceLotPrefix() {
		 return (StringUtils.isEmpty(schedulePanel.getDropDownStringValue())) ? SERVICE_LOT_PREFIX : 
			 SERVICE_LOT_PREFIX.substring(0,3) + " " + getProperty().getAssemblyLineId();
	}
	
	private String findNextKdLotNumber(PreProductionLot prodLot,boolean isSameKdLot) {
		String month = (new SimpleDateFormat("yyyyMM")).format(new java.util.Date());
		boolean isSameMonth = prodLot!= null && month.equalsIgnoreCase(prodLot.getProductionLot().substring(8,14));
		
		int number = 1001;
		if(isSameMonth) {
			int sn1 = Integer.parseInt(prodLot.getKdLot().substring(12,16));
			int sn2 = Integer.parseInt(prodLot.getKdLot().substring(16,18));
			number = isSameKdLot ? sn1*100 + sn2 + 1 : (sn1 + 1) * 100 + 1;
		}
		return SERVICE_LOT_PREFIX.substring(0,3) + " " + getProperty().getAssemblyLineId() + month + StringUtils.leftPad(""+number,6,"0");
	}
	
	private PreProductionLot findLatestServiceLot(List<PreProductionLot> prodLots) {
		PreProductionLot maxLot = null;
		for(PreProductionLot prodLot :prodLots) {
			if(!isServiceLot(prodLot.getProductionLot())) continue;
			if(maxLot == null || prodLot.getProductionLot().compareTo(maxLot.getProductionLot()) > 0) maxLot = prodLot;
		}
		return maxLot;
	}
	
	protected boolean isServiceLot(String prodLotNumber) {
		return !StringUtils.isEmpty(prodLotNumber) && prodLotNumber.startsWith(getServiceLotPrefix());
	}
	
	protected ScheduleClientProperty getProperty() {
		return schedulePanel.getController().getProperties();
	}
	
		
	protected List<PreProductionLot> findAllServiceLots(){
		List<MultiValueObject<PreProductionLot>> upcomingLots = schedulePanel.getUpcomingLotTblPane().getTable().getItems();
		List<PreProductionLot> serviceLots = new ArrayList<PreProductionLot>();
	
		for(MultiValueObject<PreProductionLot> item : upcomingLots) {
			PreProductionLot lot = item.getKeyObject();
			if(lot != null && isServiceLot(lot.getProductionLot()))
					serviceLots.add(lot);
		}
		
		return serviceLots;
	}

		
	public void createServiceLot() {
		if(!productSpecPanel.isProductSpecSelected()){
			MessageDialog.showError(this, "Please Select Product Spec Code");
			return;
		}else {
			List<String> productSpecCodes = productSpecPanel.buildSelectedProductSpecCodes();
			if(productSpecCodes.size()!=1){
				MessageDialog.showError(this, "Please Select only one product spec code");
				return;
			}
			String productSpecCode = productSpecCodes.get(0);
			if(StringUtils.isEmpty(ProductSpec.extractIntColorCode(productSpecCode))){
				MessageDialog.showError(this, "Please Select full product spec code up to interior color code");
				return;
			}
			boolean isOK = MessageDialog.confirm(this, "Are you sure to create a service production lot with lot size " + (int) lotSizeSlider.getValue());
			if(!isOK) return;

			creataProductionLot(productSpecCode);
		}
	}
	
	private void kdLotCheckBoxChanged() {
		List<PreProductionLot> lots = findAllServiceLots();
		PreProductionLot lot = findLatestServiceLot(lots);
		kdLotTxtField.setText(findNextKdLotNumber(lot, !currentKdLotCheckbox.isDisabled() && currentKdLotCheckbox.isSelected()));
	}
	
	private void creataProductionLot(String productSpecCode) {
		ProductionLot lot = new ProductionLot();
		lot.setProductionLot(prodLotTxtField.getText());
		lot.setKdLotNumber(kdLotTxtField.getText());
		lot.setLotSize((int) lotSizeSlider.getValue());
		lot.setProductSpecCode(StringUtils.chomp(productSpecCode, "%"));
		lot.setProductionDate(new Date(new java.util.Date().getTime()));
		lot.setPlanOffDate(lot.getProductionDate());
		if(schedulePanel.isProcessLocationSelected()) {
			lot.setProcessLocation(schedulePanel.getDropDownStringValue());
			lot.setPlanCode(Arrays.asList(getProperty().getPlanCode()).get(0));
		} else {
			lot.setProcessLocation(schedulePanel.getDropDownStringValue().substring(7,9));
			lot.setPlanCode(schedulePanel.getDropDownStringValue());
		}
		lot.setPlantCode(getProperty().getSiteName());
		lot.setLineNo(getProperty().getAssemblyLineId());
		lot.setStartProductId(getStartProductId(lot.getProductSpecCode()));
		saveProductionLot(lot);
		schedulePanel.getController().retrievePreProductionLots();
		loadData();
	}

	private String getStartProductId(String productSpecCode) {
		StringBuilder startProductId = new StringBuilder();
		//temporarily set the start product Id to <sub_id>*<sub_id>
		//this will be override if product will be created.
		BuildAttributeCache bcache = new BuildAttributeCache(BuildAttributeTag.SUB_IDS);
		String subIds = bcache.findAttributeValue(productSpecCode, BuildAttributeTag.SUB_IDS);
		if(!StringUtils.isEmpty(subIds)){
			String[] split = subIds.split(Delimiter.COMMA);
			for(int i = 0; i < split.length; i++){
				if(startProductId.length() > 0) startProductId.append("*");
				startProductId.append(split[i]);
			}
		}
			
		return startProductId.toString();
	}

	public void saveProductionLot(ProductionLot lot) {
		ProductDao<?> productDao = ProductTypeUtil.getProductDao(getProperty().getProductType());
		int count = productDao.createProducts(lot, 
				getProperty().getProductType(), 
				getProperty().getMSLineId(), 
				getProperty().getMSProcessPointId());
		if(!isMbpnProduct(getProperty().getProductType()) && count == 0){
			if(ProductType.KNUCKLE.equals(getProperty().getProductType()))
					MessageDialog.showError(this,"Could not create service lot. Please check PART NUMBER build attribute for selected product spec");
			else {
				MessageDialog.showError(this,"Could not create service lot.");
			}
		}else {
			MessageDialog.showInfo(this, "Service lot is created successfully");
			servicelots.add(lot.derivePreProductionLot());
		}
	}

	protected boolean isMbpnProduct(String productType) {
		ProductType type = ProductType.valueOf(productType);
		return type == ProductType.MBPN || type == ProductType.PLASTICS || type == ProductType.WELD || type == ProductType.BUMPER;
	}

	@Override
	public void handle(ActionEvent arg0) {
		if(arg0.getSource().equals(addLotButton))
			createServiceLot();
		else if(arg0.getSource().equals(currentKdLotCheckbox))
			kdLotCheckBoxChanged();
		
	}
	
	public Logger getLogger() {
		return schedulePanel.getMainWindow().getLogger();
	}
}
