package com.honda.galc.client.teamlead.structure.delete;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.loader.dto.MeasurementDetailsDto;
import com.honda.galc.client.loader.dto.PartDetailsDto;
import com.honda.galc.client.loader.dto.UnitDetailsDto;
import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.ui.ClientStartUpProgress;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.PddaDetailDto;
import com.honda.galc.dto.StructureDetailsDto;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.MCOrderStructure;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPoint;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPointId;
import com.honda.galc.entity.conf.MCOrderStructureId;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.entity.conf.MCProductStructureForProcessPointId;
import com.honda.galc.entity.conf.MCProductStructureId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.vios.ProductStructureService;
import com.honda.galc.util.KeyValue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

public class StructureDeleteController extends AbstractController<StructureDeleteModel, StructureDeletePanel> implements EventHandler<ActionEvent> {
	private LinkedHashMap<String, UnitDetailsDto> detailsMap;
	private final Logger logger;
	private List<StructureUnitDetailsDto> structureDetailList;
	private BaseMCProductStructure structureCreated = null;
	private String message;
	private List<String> structureAllowedProductTypes = new ArrayList<String>();
	public static final String ALLOW_CREATE_STRUCTURE = "ALLOW_CREATE_STRUCTURE";
	
	public StructureDeleteController(StructureDeleteModel model, StructureDeletePanel view) {
		super(model, view);
		this.logger = view.getLogger();
		structureAllowedProductTypes = PropertyService.getPropertyList(ApplicationConstants.DEFAULT_VIOS, ALLOW_CREATE_STRUCTURE);
		
		if(structureAllowedProductTypes.size()==0) {
			structureAllowedProductTypes.add(ProductType.ENGINE.getProductName().toUpperCase());
			structureAllowedProductTypes.add(ProductType.FRAME.getProductName().toUpperCase());
		}
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			LoggedRadioButton loggedRadioButton = (LoggedRadioButton) actionEvent.getSource();
			if("Lot Number".equals(loggedRadioButton.getText().trim())){
				getView().getLotNumberTextField().setDisable(false);
				getView().getProductIdTextField().setDisable(true);
				getView().getProductIdTextField().settext("");
				getView().getProductIdBtn().setDisable(true);
			} else {
				getView().getLotNumberTextField().setDisable(true);
				getView().getProductIdTextField().setDisable(false);
				getView().getLotNumberTextField().settext("");
				getView().getProductIdBtn().setDisable(false);
			}
			clearPanel();
		}
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			
			if("Search".equals(loggedButton.getText())) searchStructure();
			else if(loggedButton.getText().equals("View")) viewStructureDetails(actionEvent, loggedButton.getId());
			else if(loggedButton.getText().equals("Delete")) deleteStructure(actionEvent, loggedButton.getId());
			else if(loggedButton.getText().equals("Create")) createStructure(actionEvent, loggedButton.getId());
		} 
	}
	

	private void createStructure(ActionEvent actionEvent, String id) {
		StructureDetailsDto detailsDto = getView().getStructureDetailsTablePane().getTable().getItems().get(Integer.valueOf(id));
		String productId = detailsDto.getProductId();
		ClientStartUpProgress progressClient = new ClientStartUpProgress("Structure Creation is in progress");
		progressClient.start();
		progressClient.getDescriptionLabel().setText("Structure Creation for "+productId+" is in progress");
		Task<Void> mainTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				BaseProduct baseProduct = getModel().findBaseProduct(productId, getView().getProductTypeData().getProductType().toString());
				getLogger().info("Structure creation started for product "+productId);
				List<ProcessPoint> processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAllByDivisionIdandByType(getView().getDivisionComboBox().getSelectionModel().getSelectedItem(), ProcessPointType.PartsInstallation);
				structureCreated = null;
				if(processPointList.size()>0 && baseProduct !=null) {
					getLogger().info("Structure creation started for product "+productId+" for division "+processPointList.get(0).getDivisionId());
					try {
						structureCreated = ServiceFactory.getService(ProductStructureService.class).findOrCreateProductStructure(baseProduct,processPointList.get(0), StructureCreateMode.DIVISION_MODE.toString());
						getLogger().info("Structure creation completed successfully for "+productId+" for division "+processPointList.get(0).getDivisionId());
					} catch (Exception e) {
						getLogger().error("Exception occured while creating structure : "+getExceptionLoggerPrints(e));
					}
				}
				return null;
			}
		};

		mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent t) {
				getLogger().error("Structure creation task for "+productId+" is completed");
				if(structureCreated != null) {
					message = "Structure created succesfully for "+productId;;
					searchByProdctId(getView().getProductIdTextField().getText().trim());
				} else {
					message = "Structure Creation Failed for "+productId;
				}
				EventBusUtil.publish(new ProgressEvent(100,	"Structure Creation Process Completed for "+productId));
				MessageDialog.showInfo(getView().getMainWindow().getStage(), message);
			}
		});
		new Thread(mainTask).start();
		
	}
	
	private String getExceptionLoggerPrints(Exception e) {
		StringBuilder builder = new StringBuilder();
		for (StackTraceElement stackTraceElement : e.getStackTrace()) {
			builder.append(stackTraceElement.toString());
			builder.append(System.getProperty("line.separator"));
		}
		return builder.toString();
	}

	@Override
	public void initEventHandlers() {
	
	}
	
	public void loadComboBox() {
		loadPoductTypeComboBox();
		loadDivisionComboBox();
		getView().getProcessPointHBox().setVisible(false);
		if(getStructureCreateMode().equals(StructureCreateMode.PROCESS_POINT_MODE)) {
			addDivisionComboBoxListener();
			getView().getProcessPointHBox().setVisible(true);
		}
	}
	
	private void loadPoductTypeComboBox() {
		getView().getProductTypesComboBox().getItems().clear();
		getView().getProductTypesComboBox().setPromptText("Select");
		List<String> productTypeList = getModel().findAllProductTypes();
		getView().getProductTypesComboBox().getItems().addAll(productTypeList);
		String productType = PropertyService.getPropertyBean(SystemPropertyBean.class).getProductType();
		if(!StringUtils.isEmpty(productType))
			getView().getProductTypesComboBox().getSelectionModel().select(productType);
	}
	
	private void loadDivisionComboBox() {
		getView().getDivisionComboBox().getItems().clear();
		getView().getDivisionComboBox().setPromptText("Select");
		List<Division> divisionList = getModel().findAllDivision();
		for (Division division : divisionList) {
			getView().getDivisionComboBox().getItems().add(division.getDivisionId());
		}
	}
	
	private void addDivisionComboBoxListener() {
		getView().getDivisionComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadProcessPointComboBox(new_val);
			 } 
		});
	}
	
	@SuppressWarnings("unchecked")
	private void loadProcessPointComboBox(String divisionId) {
		getView().getProcessPointComboBox().getItems().clear();
		getView().getProcessPointComboBox().setPromptText("Select");
		List<ProcessPoint> processPoints = getModel().findAllByDivisionId(divisionId);
		for (ProcessPoint pp : processPoints) {
			getView().getProcessPointComboBox().getItems().add(getKeyValue(pp.getProcessPointId(), pp.getProcessPointName() + " - " + pp.getProcessPointId()));
		}
	}
	
	private void searchStructure() {
		clearMessages();
		clearPanel();
		getView().getStructureDetailsTablePane().setData(new ArrayList<StructureDetailsDto>());
		if(validateDivision()){
			displayErrorMessage("Please Select Division");
			return;
		}
		if(getStructureCreateMode().equals(StructureCreateMode.PROCESS_POINT_MODE)) {
			if(validateProcessPoint()){
				displayErrorMessage("Please Select Process Point");
				return;
			}
		}
		if(getView().getProductIdRadioButton().isSelected()) {
			String productId = getView().getProductIdTextField() == null ? "" : getView().getProductIdTextField().getText().toString().trim();
			if(validateProductId(productId)){
				displayErrorMessage("Please Enter product Id");
				return;
			}
			searchByProdctId(productId);
		} else {
			String productionLot = getView().getLotNumberTextField() == null ? "" : getView().getLotNumberTextField().getText().toString().trim();
			if(validateProductionLot(productionLot)){
				displayErrorMessage("Please Enter Production Lot");
				return;
			}
			searchByProductionLot(productionLot);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchByProdctId(String productId) {
		String divisionId = getView().getDivisionComboBox() == null ? "" : getView().getDivisionComboBox().getValue().toString().trim();
		String productType = getView().getProductTypesComboBox() == null ? "" : getView().getProductTypesComboBox().getValue().toString().trim();
		boolean showCreateButton = false;
		if(structureAllowedProductTypes.contains(productType))
			showCreateButton = true;
		
		StructureDetailsDto structureDetailsDto = null;
		List<StructureDetailsDto> structureListByProductId = new ArrayList<StructureDetailsDto>();
		List<? extends BaseProduct> productList = getModel().findByProductId(productId,	productType);
		
		for (BaseProduct baseProduct : productList) {
			structureDetailsDto = null;
			String processPointId = "";
			if(getView().getProcessPointComboBox() != null && getView().getProcessPointComboBox().getSelectionModel().getSelectedItem() != null) {
				KeyValue<String, String> newValue = (KeyValue<String, String>) getView().getProcessPointComboBox().getSelectionModel().getSelectedItem();
				processPointId = newValue.getKey().toString().trim();
			}
			if(getStructureCreateMode().equals(StructureCreateMode.DIVISION_MODE)) {
				MCProductStructureId productStructureId = new MCProductStructureId(baseProduct.getProductId(), divisionId, baseProduct.getProductSpecCode());
				MCProductStructure mcProductStructure = getModel().findById(productStructureId);
				if(mcProductStructure != null) 
					structureDetailsDto = converToDto(baseProduct, mcProductStructure);
			} else {

				MCProductStructureForProcessPointId id = new MCProductStructureForProcessPointId(baseProduct.getProductId(), processPointId, baseProduct.getProductSpecCode());
				MCProductStructureForProcessPoint mcProductStructureForProcessPoint = getModel().findById(id);
				if(mcProductStructureForProcessPoint != null) 
					structureDetailsDto = converToDto(baseProduct, mcProductStructureForProcessPoint);
			}
			if(structureDetailsDto != null && !StringUtils.isBlank(structureDetailsDto.getProductSpecCode())) {
				structureDetailsDto.setStructureCreateFlag(false);
				structureListByProductId.add(structureDetailsDto);
			} else if(showCreateButton){
				getLogger().info("Structure creation is allowed for prodct "+baseProduct.getProductId());
				structureDetailsDto = converToDto(baseProduct, processPointId, divisionId);
				structureDetailsDto.setStructureCreateFlag(true);
				structureListByProductId.add(structureDetailsDto);
			}
			
		}
		
		getView().getStructureDetailsTablePane().setData(FXCollections.observableArrayList(structureListByProductId));
		getView().getStructureDetailsTablePane().getTable().getColumns().get(1).setVisible(true);
	}
	
	private StructureDetailsDto converToDto(BaseProduct baseProduct, String processPoint,String divisionId) {
		StructureDetailsDto detailsDto = new StructureDetailsDto();
		detailsDto.setProductId(baseProduct.getProductId());
		detailsDto.setProcessPointId(processPoint);
		detailsDto.setProductSpecCode(baseProduct.getProductSpecCode());
		detailsDto.setProductionLot(baseProduct.getProductionLot());
		detailsDto.setDivision(divisionId);
		detailsDto.setCreateTimestamp(baseProduct.getCreateTimestamp());
		return detailsDto;
	}

	@SuppressWarnings("unchecked")
	public void searchByProductionLot(String productionLot) {
		String divisionId = getView().getDivisionComboBox() == null ? "" : getView().getDivisionComboBox().getValue().toString().trim();
		String processPointId = "";
		if(getView().getProcessPointComboBox() != null && getView().getProcessPointComboBox().getSelectionModel().getSelectedItem() != null) {
			KeyValue<String, String> newValue = (KeyValue<String, String>) getView().getProcessPointComboBox().getSelectionModel().getSelectedItem();
			processPointId = newValue.getKey().toString().trim();
		}
		
		List<StructureDetailsDto> structureListByOrderNumber = getModel().loadStructureByOrderNumber(productionLot, divisionId, processPointId);
		getView().getStructureDetailsTablePane().setData(FXCollections.observableArrayList(structureListByOrderNumber));
		getView().getStructureDetailsTablePane().getTable().getColumns().get(1).setVisible(false);
	}
	
	public void clearPanel() {
		if(getView().getStructureDetailsTablePane() != null)
			getView().getStructureDetailsTablePane().getTable().getItems().clear();
		if(getView().getRoot() != null) {
			getView().getRoot().getChildren().clear();
			getView().getDetailsTitledPane().setExpanded(false);
			getView().getDetailsTitledPane().setGraphic(null);
			getView().getDetailsTitledPane().setText("Unit Details Panel");
		}
	}
	
	private boolean validateProductId(String poductId){
		return poductId.length() < 2;
	}
	
	private boolean validateProductionLot(String productionLot){
		return productionLot.length() < 2;
	}
	
	private boolean validateDivision(){
		return getView().getDivisionComboBox() != null && getView().getDivisionComboBox().getValue() != null ? false : true;
	}
	
	private boolean validateProcessPoint(){
		return getView().getProcessPointComboBox() != null && getView().getProcessPointComboBox().getSelectionModel().getSelectedItem() != null ? false : true;
	}
	
	public void viewStructureDetails(ActionEvent actionEvent, String index) {
		getView().getStructureDetailsTablePane().getTable().getSelectionModel().select(Integer.valueOf(index));
		
		String operationName = "";
		UnitDetailsDto unitDto = new UnitDetailsDto();
		PartDetailsDto partDto = new PartDetailsDto();
		MeasurementDetailsDto measurementDto = new MeasurementDetailsDto();
		detailsMap = new LinkedHashMap<String, UnitDetailsDto>();
	
		StructureDetailsDto detailsDto = getView().getStructureDetailsTablePane().getTable().getItems().get(Integer.valueOf(index));
		structureDetailList = getModel().loadStructureDetails(detailsDto.getProductId(), detailsDto.getDivision(), detailsDto.getProductionLot(), detailsDto.getProcessPointId(),
				detailsDto.getStructureRevision());
		for (StructureUnitDetailsDto details : structureDetailList) {
			operationName = details.getOperationName();
			if(detailsMap.containsKey(operationName)) {
				unitDto = detailsMap.get(operationName);
				partDto = getPartDetailsDto(details);
				if(partDto != null)	{
					if(unitDto.getPartDetailsList() != null) {
						PartDetailsDto unitPartDto = getUnitpart(unitDto.getPartDetailsList(), partDto);
						if(unitPartDto!=null) {
							measurementDto = getMeasurementDetailsDto(details);
							unitPartDto.getMeasurementDetailsList().add(measurementDto);
						} else {
							measurementDto = getMeasurementDetailsDto(details);
							List<MeasurementDetailsDto> measList = new ArrayList<MeasurementDetailsDto>();
							if(measurementDto.getMin() != 0) {
								measList.add(measurementDto);
								partDto.setMeasurementDetailsList(measList);
							}
							List<PartDetailsDto> partList = unitDto.getPartDetailsList();
							partList.add(partDto);
						}
					}
				}
			} else {
				unitDto = getUnitDetailsDto(details);
				partDto = getPartDetailsDto(details);
				measurementDto = getMeasurementDetailsDto(details);
			
				List<MeasurementDetailsDto> measList = new ArrayList<MeasurementDetailsDto>();
				if(measurementDto.getMin() != 0) {
					measList.add(measurementDto);
					partDto.setMeasurementDetailsList(measList);
				}
				List<PartDetailsDto> partList = new ArrayList<PartDetailsDto>();
				if(!StringUtils.isBlank(partDto.getPartNumber())) {
					partList.add(partDto);
					unitDto.setPartDetailsList(partList);
				}
				detailsMap.put(operationName, unitDto);
			}
		}
		getView().reload(detailsDto.getProductId(), detailsDto.getProductionLot());
		pddaDetailsHeader(detailsDto);
		exportInCsv(detailsDto.getProductId(), detailsDto.getProductionLot());
		getView().getDetailsTitledPane().setExpanded(true);
	}
	
	private PartDetailsDto getUnitpart(List<PartDetailsDto> partList, PartDetailsDto detailsDto){
		for (PartDetailsDto partDetailsDto : partList) {
			if(partDetailsDto.getPartId().equals(detailsDto.getPartId()) && partDetailsDto.getPartRev() == detailsDto.getPartRev()) {
				return partDetailsDto;
			}
		}
		return null;
	}
	
	private UnitDetailsDto getUnitDetailsDto(StructureUnitDetailsDto units) {
		return new UnitDetailsDto(units.getProcessPointId(), units.getProcessPointName(), units.getAsmProcNo(), 
				units.getProcessSeqNum(), units.getOperationName(), units.getOperationSeqNum(), units.getType());
	}
	
	private PartDetailsDto getPartDetailsDto(StructureUnitDetailsDto parts) {
		return new PartDetailsDto(parts.getPartNo(), parts.getPartSectionCode(), 
				parts.getPartItemNo(), parts.getPartType(), parts.getPartMask(),
				parts.getPartId(), parts.getPartRevision());
	}
	
	private MeasurementDetailsDto getMeasurementDetailsDto(StructureUnitDetailsDto meas) {
		return new MeasurementDetailsDto(meas.getMeasurementSeqNum(), meas.getMinLimit(), 
				meas.getMaxLimit(), meas.getDeviceMsg(), meas.getDeviceId());
	}
	
	private void pddaDetailsHeader(StructureDetailsDto detailsDto) {
		List<PddaDetailDto> pddaPlatforms = getModel().findPlatformsByStructureRevision(detailsDto.getStructureRevision());
		if(pddaPlatforms.size() > 0) {
			PddaDetailDto p = (PddaDetailDto)pddaPlatforms.get(0);
			getView().setPddaDetails(p.getPlantLocCode(), p.getDeptCode(), String.valueOf(p.getModelYearDate()).toString(), 
						String.valueOf(p.getProdSchQty()).toString(), p.getProdAsmLineNo(), p.getVehicleModelCode());
		} 
		if(pddaPlatforms.size() > 1) {
			String pddaId = "";
			for (PddaDetailDto o : pddaPlatforms) {
				pddaId +=  "Plant:" + o.getPlantLocCode() + "  Dept:" + o.getDeptCode() + "  MYC:" + o.getModelYearDate()
						+ "  Qty:" + o.getProdSchQty() + "  Line:" + o.getProdAsmLineNo() + "  VMC:" + o.getVehicleModelCode() + "\n";
			}
			MessageDialog.showInfo(ClientMainFx.getInstance().getStage(getApplicationId()), "Structure has more than 1 PDDA platform. Below are PDDA platform id : \n" + pddaId);
		}
	}
	
	public void deleteStructure(ActionEvent actionEvent, String index) {
		StructureDetailsDto detailsDto = getView().getStructureDetailsTablePane().getTable().getItems().get(Integer.valueOf(index));
		if(!MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), 
				"Do you want to delete " + (StringUtils.isBlank(detailsDto.getProductId()) ? "production lot " + detailsDto.getProductionLot() + "?" : "product id " + detailsDto.getProductId() + "?"))){
			return;
		}
		List<String> partNameList = getPartNames(detailsDto);
		if(getStructureCreateMode().equals(StructureCreateMode.DIVISION_MODE)) {
			deleteByDivisionMode(detailsDto);
		} else {
			deleteByProcessPointMode(detailsDto);
		}
		if(getView().getRoot() != null)
			getView().getRoot().getChildren().clear();
		if(getView().getDetailsTitledPane() != null) {
			getView().getDetailsTitledPane().setExpanded(false);
			getView().getDetailsTitledPane().setText("Unit Details Panel");
			getView().getDetailsTitledPane().setGraphic(null);
		}
		getLogger().info("Structure is deleted for VIN: " + detailsDto.getProductId() + ", Rivision: " + 
				detailsDto.getStructureRevision() + " and Production date: " + (new SimpleDateFormat("MM-dd-yyyy")).format(detailsDto.getCreateTimestamp()) + " on " + 
				(new SimpleDateFormat("MM-dd-yyyy")).format(new java.util.Date()));
		deleteInstalledPart(detailsDto, partNameList);
		getView().getStructureDetailsTablePane().getTable().getItems().remove(detailsDto);
		searchStructure();
	}
	
	private List<String> getPartNames(StructureDetailsDto detailsDto) {
		List<String> partNameList = new ArrayList<String>();
		Set<String> partNames = new HashSet<String>();
		List<StructureUnitDetailsDto> unitDetailsDtos = getModel().loadStructureDetails(detailsDto.getProductId(), detailsDto.getDivision(), detailsDto.getProductionLot(), detailsDto.getProcessPointId(),
				detailsDto.getStructureRevision());
		for (StructureUnitDetailsDto structureUnitDetailsDto : unitDetailsDtos) {
			partNames.add(structureUnitDetailsDto.getOperationName());
		}
		partNameList.addAll(partNames);
		return partNameList;
	}
	
	private void deleteByDivisionMode(StructureDetailsDto detailsDto) {
		long productStructureRev = 0;
		long orderStructureRev = 0;
		if(!StringUtils.isBlank(detailsDto.getProductId())) {
			MCProductStructureId productStructureId = new MCProductStructureId(detailsDto.getProductId(), detailsDto.getDivision(), detailsDto.getProductSpecCode());
			MCProductStructure mcProductStructure = getModel().findById(productStructureId);
			if(mcProductStructure != null) {
				productStructureRev = mcProductStructure.getStructureRevision();
				getModel().deleteProductStructure(mcProductStructure);
			}
		} else {
			deleteProductsForProductionLot(detailsDto.getProductionLot(), detailsDto.getDivision(), detailsDto.getProductSpecCode());
		}
		MCOrderStructureId orderStructureId = new MCOrderStructureId(detailsDto.getProductionLot(), detailsDto.getDivision());
		MCOrderStructure mcOrderStructure = getModel().findById(orderStructureId);
		if(mcOrderStructure != null) {
			orderStructureRev = mcOrderStructure.getStructureRevision();		
			getModel().deleteOrderStructure(mcOrderStructure);
		}
		
		if(productStructureRev == orderStructureRev && productStructureRev != 0 && orderStructureRev != 0) {
			List<MCProductStructure> productStructures = getModel().findProductByStructureRevAndDivId(productStructureRev, detailsDto.getDivision());
			List<MCOrderStructure> orderStructures = getModel().findOrderByStructureRevAndDivId(orderStructureRev, detailsDto.getDivision());
			if(productStructures.size() == 0 && orderStructures.size() == 0 ) {
				getModel().deleteStructure(mcOrderStructure.getProductSpecCode(), mcOrderStructure.getId().getDivisionId(), orderStructureRev);
			}
		}
	}
	
	private void deleteByProcessPointMode(StructureDetailsDto detailsDto) {
		long productStructureRev = 0;
		long orderStructureRev = 0;
		if(!StringUtils.isBlank(detailsDto.getProductId())) {
			MCProductStructureForProcessPointId id = new MCProductStructureForProcessPointId(detailsDto.getProductId(), detailsDto.getProcessPointId(), detailsDto.getProductSpecCode());
			MCProductStructureForProcessPoint mcProductStructureForProcessPoint = getModel().findById(id);
			if(mcProductStructureForProcessPoint != null)  {
				productStructureRev = mcProductStructureForProcessPoint.getStructureRevision();
				getModel().deleteProductStructureByProcessPoint(mcProductStructureForProcessPoint);
			}
		} else {
			deleteProductsForProductionLotForProcessPoint(detailsDto.getProductionLot(), detailsDto.getDivision(), detailsDto.getProductSpecCode(), detailsDto.getProcessPointId());
		}
		MCOrderStructureForProcessPointId orderStructureId = new MCOrderStructureForProcessPointId(detailsDto.getProductionLot(), detailsDto.getProcessPointId());
		MCOrderStructureForProcessPoint mcOrderStructure = getModel().findById(orderStructureId);
		if(mcOrderStructure != null) {
			orderStructureRev = mcOrderStructure.getStructureRevision();		
			getModel().deleteOrderStructureByProcessPoint(mcOrderStructure);
		}
		
		if(productStructureRev == orderStructureRev && productStructureRev != 0 && orderStructureRev != 0) {
			List<MCProductStructure> productStructures = getModel().findProductByStructureRevAndDivId(productStructureRev, detailsDto.getDivision());
			List<MCOrderStructure> orderStructures = getModel().findOrderByStructureRevAndDivId(orderStructureRev, detailsDto.getDivision());
			if(productStructures.size() == 0 && orderStructures.size() == 0 ) {
				getModel().deleteStructure(mcOrderStructure.getProductSpecCode(), mcOrderStructure.getDivisionId(), orderStructureRev);
			}
		}
	}
	
	private void deleteProductsForProductionLot(String productionLot, String divisionId, String specCode) {
		String productType = getView().getProductTypesComboBox() == null ? "" : getView().getProductTypesComboBox().getValue().toString().trim();
		List<? extends BaseProduct> productList = getModel().findAllProductsByProductionLot(productionLot, productType);
		List<String> products = new ArrayList<String>();
		String productIds = "";
		List<MCProductStructure> productStructures = new ArrayList<MCProductStructure>();
		for (BaseProduct p : productList) {
			products.add(p.getProductId());
		}
		if(products.size() > 0) {
			productStructures = getModel().findAllByProductIdDivisionAndSpecCode(products, divisionId, specCode);
		}
		for (MCProductStructure p : productStructures) {
			productIds = productIds + p.getId().getProductId() + " ";
		}
		if(productStructures != null && productStructures.size() > 0 && MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), productionLot + " has structures for product ids " + productIds + ". Do you wish to continue?")){
			getModel().deleteAllProductStructure(productStructures);
		}
	}
	
	private void deleteProductsForProductionLotForProcessPoint(String productionLot, String divisionId, String specCode, String processPoint) {
		String productType = getView().getProductTypesComboBox() == null ? "" : getView().getProductTypesComboBox().getValue().toString().trim();
		List<? extends BaseProduct> productList = getModel().findAllProductsByProductionLot(productionLot, productType);
		List<String> products = new ArrayList<String>();
		String productIds = "";
		List<MCProductStructureForProcessPoint> productStructures = new ArrayList<MCProductStructureForProcessPoint>();
		for (BaseProduct p : productList) {
			products.add(p.getProductId());
		}
		if(products.size() > 0) {
			productStructures = getModel().findAllByProductIdDivisionAndSpecCode(products, divisionId, specCode, processPoint);
		}
		for (MCProductStructureForProcessPoint p : productStructures) {
			productIds = productIds + p.getId().getProductId() + " ";
		}
		if(productStructures != null && productStructures.size() > 0 && MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), productionLot + " has structures for product ids " + productIds + ". Do you wish to continue?")){
			getModel().deleteAllProductStructureByProcessPoint(productStructures);
		}
	}
	
	private void deleteInstalledPart(StructureDetailsDto detailsDto, List<String> partNameList) {
		List<String> products = new ArrayList<String>();
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		
		if(StringUtils.isBlank(detailsDto.getProductId())) {
			String productType = getView().getProductTypesComboBox() == null ? "" : getView().getProductTypesComboBox().getValue().toString().trim();
			List<? extends BaseProduct> productList = getModel().findAllProductsByProductionLot(detailsDto.getProductionLot(), productType);
			for (BaseProduct p : productList) {
				products.add(p.getProductId());
			}
		} else {
			products.add(detailsDto.getProductId());
		}
		for (String product : products) {
			installedParts.addAll(getModel().findAllByProductIdAndPartNames(product, partNameList));
		}
		if(installedParts != null && installedParts.size() > 0 && 
				MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), 
						(StringUtils.isBlank(detailsDto.getProductId()) ? detailsDto.getProductionLot() : detailsDto.getProductId()) + " has installed parts . Do you want to delete all installed parts as well?")){
			for (String product : products) {
				getModel().deleteInstalledParts(product, partNameList);
			}
		}
	}
	
	private void exportInCsv(String productId, String porductionLot) {
		final String fileName = StringUtils.isEmpty(productId) ? porductionLot : productId;
		if(getView().getExportInCsvLink() == null)
			return;
		getView().getExportInCsvLink().setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	final FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Folder");
		    	fileChooser.setInitialFileName("Structure_" + fileName);
		    	File dataFile = fileChooser.showSaveDialog(ClientMainFx.getInstance().getStage());
				if(dataFile != null){
                	 Writer writer = null;
                     try {
                    	File file = new File(dataFile.getAbsolutePath() + ".csv.");
                      	writer = new BufferedWriter(new FileWriter(file));
                  		String header = "Process Point Name" + "," + "Process Point Id" + "," + "Asm Proc No" + "," + "Process Seq Num" + "," 
                  				+ "Revision"  + "," + "Operation Name" + "," + "Op Rev" + "," + "operation Desc" + "," 
                  				+ "Op Type"  + "," + "Op Seq No" + "," + "Part Id"  + "," + "Part Revision" + "," 
                  				+ "Part No"  + "," + "Part Description" + "," + "Part Item No" + "," + "Part Section Code" + "," 
                  				+ "Part Type" + "," + "Part Mask"  + "," + "Measurement Seq No" + "," + "Min Limit" + "," 
                  				+ "Max Limit" + "," + "P-Set"  + "," + "Tool" + "," + "Process Point Desc"	+ "\n";
                  		writer.write(header);
                  		for (StructureUnitDetailsDto strDetail : structureDetailList) {
                      	    writer.write(csvWriteLine(strDetail));
						}
                     } catch (Exception ex) {
                    	 getLogger().error("Error occurred while creating CSV file: " + ex.getMessage());
                     }
                     finally {
                         try {
							writer.flush();
							writer.close();
						} catch (IOException ioe) {
							getLogger().error("Error occurred while closing CSV file: " + ioe.getMessage());
						}
                        
                     } 
                }
		    }
		});

	}
	
	private String csvWriteLine(StructureUnitDetailsDto strDetail) {
		return appendDQ(strDetail.getProcessPointName()) + "," + appendDQ(strDetail.getProcessPointId()) + "," + appendDQ(strDetail.getAsmProcNo()) + "," + (strDetail.getProcessSeqNum()) + "," 
  				+ strDetail.getRevision()  + "," + (strDetail.getOperationName()) + "," + (strDetail.getOperationRevision()) + "," + appendDQ(strDetail.getDescription()) + "," 
  				+ appendDQ(strDetail.getType())  + "," + strDetail.getOperationSeqNum() + "," + appendDQ(strDetail.getPartId())  + "," + strDetail.getPartRevision() + "," 
  				+ appendDQ(strDetail.getPartNo())  + "," + appendDQ(strDetail.getPartDesc()) + "," + appendDQ(strDetail.getPartItemNo()) + "," + appendDQ(strDetail.getPartSectionCode()) + "," 
  				+ appendDQ(strDetail.getPartType()) + "," + appendDQ(strDetail.getPartMask())  + "," + strDetail.getMeasurementSeqNum() + "," + strDetail.getMinLimit() + "," 
  				+ strDetail.getMaxLimit() + "," + appendDQ(strDetail.getDeviceMsg())  + "," + appendDQ(strDetail.getDeviceId()) + "," + appendDQ(strDetail.getDescription()) + "\n";
	}
	
	private static String appendDQ(String str) {
	    return "\"" + str + "\"";
	}
	
	public static StructureCreateMode getStructureCreateMode() {
		String structureMode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());
		return structureMode == null ? StructureCreateMode.DIVISION_MODE : StructureCreateMode.get(structureMode);
	}
	
	private StructureDetailsDto converToDto(BaseProduct baseProduct, MCProductStructure productStructure) {
		StructureDetailsDto detailsDto = new StructureDetailsDto();
		detailsDto.setProductId(productStructure.getId().getProductId());
		detailsDto.setProductSpecCode(productStructure.getId().getProductSpecCode());
		detailsDto.setProductionLot(baseProduct.getProductionLot());
		detailsDto.setDivision(productStructure.getId().getDivisionId());
		detailsDto.setStructureRevision(productStructure.getStructureRevision());
		detailsDto.setCreateTimestamp(productStructure.getCreateTimestamp());
		return detailsDto;
	}
	
	private StructureDetailsDto converToDto(BaseProduct baseProduct, MCProductStructureForProcessPoint productStructure) {
		StructureDetailsDto detailsDto = new StructureDetailsDto();
		detailsDto.setProductId(productStructure.getId().getProductId());
		detailsDto.setProcessPointId(productStructure.getId().getProcessPointId());
		detailsDto.setProductSpecCode(productStructure.getId().getProductSpecCode());
		detailsDto.setProductionLot(baseProduct.getProductionLot());
		detailsDto.setDivision(productStructure.getDivisionId());
		detailsDto.setStructureRevision(productStructure.getStructureRevision());
		detailsDto.setCreateTimestamp(productStructure.getCreateTimestamp());
		return detailsDto;
	}
	
	private KeyValue<String, String> getKeyValue(String key, final String value){
		KeyValue<String, String> kv = new KeyValue<String, String>(key, value){
			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return value;
			}
		};
		return kv;
	}
	
	public LinkedHashMap<String, UnitDetailsDto> getDetailsMap() {
		return detailsMap;
	}

	public void setDetailsMap(LinkedHashMap<String, UnitDetailsDto> detailsMap) {
		this.detailsMap = detailsMap;
	}
	public Logger getLogger() { return this.logger; }
}
