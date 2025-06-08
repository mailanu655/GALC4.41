package com.honda.galc.client.teamlead.ltCtrRepair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.fx.ManualLotControlRepairPanel;
import com.honda.galc.client.teamleader.fx.ManualLotControlViewMessageDialog;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.OperationType;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.entity.conf.MCProductStructureForProcessPointId;
import com.honda.galc.entity.conf.MCProductStructureId;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ObjectComparator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ManualLotControlRepairController {

	private List<ProductBuildResult> productBuildResults;
	private ProductType productType;
	private MainWindow mainWin;
	private ApplicationContext appContext;

	private ManualLotControlRepairPanel view;
	private ManualLotControlRepairPropertyBean property;
	private List<LotControlRule> rules = new ArrayList<LotControlRule>();
	private List<Division> divisionList = new ArrayList<Division>();
	private List<ProcessPoint> processPointList=new ArrayList<ProcessPoint>();
	private List<String> operationTypeList=new ArrayList<String>();
	private List<MCOperationRevision> operations = new ArrayList<MCOperationRevision>();
	private List<String> specialityScreenList=new ArrayList<String>();
	private List<String> toolTypeList=new ArrayList<String>();
	private List<String> operationStatusList=new ArrayList<String>();
	//selected filters
	private Division selectedDivision;
	private List<String> selectedProcesspoint;
	private Map<String, ProcessPoint> processPointMap;
	private boolean isSelectedDivisionChanged;
	private boolean isSelectedProcessPointChanged;
	private List<String> selectedOpType;
	private boolean stopShipOnly;
	private boolean nonGALCPartsOnly;
	private boolean requiredPartsOnly;
	private String selectedScreen;
	private String selectedTool;
	private String selectedOpStatus;
	private String selectedUnitNumberOrDesc;
	private String selectedSerialNumber;
	private String selectedPartMask;
	private String selectedPset;
	private String productTypeName;
	
	private Set<RequiredPart> requiredPartsList = new HashSet<RequiredPart>();
	private boolean hasProductIdChanged = false;
	private List<Object[]> rulesAndPpIds;
	private BaseProductSpec currentSpecCode ;
	private List<PartResult> partResults;
	
	private static final String OPERATION_STATUS_COMPLETE ="COMPLETE";
	
	
	public boolean isHasProductIdChanged() {
		return hasProductIdChanged;
	}

	public void setHasProductIdChanged(boolean hasProductIdChanged) {
		this.hasProductIdChanged = hasProductIdChanged;
	}

	public ManualLotControlRepairController(MainWindow mainWin, ManualLotControlRepairPanel repairPanel) {
		this.mainWin = mainWin;
		this.appContext = mainWin.getApplicationContext();
		this.view = repairPanel;

		initialize();
	}

	private void initialize() {
		productType = view.getCurrentProductType();
		property = view.getProperty();
		divisionList = ServiceFactory.getDao(DivisionDao.class).findAll();
		processPointList =ServiceFactory.getDao(ProcessPointDao.class).findAll();
		Collections.sort(processPointList,new ObjectComparator<ProcessPoint>("processPointName"));
		operationTypeList = FXCollections.observableArrayList(ManualLotCtrRepairUtil.getOperationTypes());
		specialityScreenList = FXCollections.observableArrayList(ManualLotCtrRepairUtil.getSpecialityScreens());
		toolTypeList = FXCollections.observableArrayList(ManualLotCtrRepairUtil.getToolTypes());
		operationStatusList = Arrays.asList(new String(),"COMPLETE","INCOMPLETE");
	}
	

	public ProductType getProductType() {
		productType = ProductType.valueOf(productTypeName);
		if (productType == null) {
			productType = ProductType.valueOf(property.getProductType());
		}
		return productType;
	}

	public BaseProduct checkProductOnServer(String vin) {
		try {
			productTypeName=(String) this.view.getProductTypeComboBox().getSelectionModel().getSelectedItem();
			return ProductTypeUtil.getTypeUtil(getProductType()).findProduct(vin);
		} catch (Exception e) {
			String msg = "failed to load " + getProductType().name() + ": " + vin;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}

	}
	
	public void refreshPartData(String productId, PartResult partResult){
		MCOperationRevision operation = getOperation(partResult);
		LotControlRule lotControlRule = null;
		if(operation == null){
			lotControlRule = getRule(partResult.getPartName());
		}
		
		List<String> partNames = new ArrayList<String>();
		partNames.add(partResult.getPartName());
		
		List<InstalledPart> updatedParts = (List<InstalledPart>) ServiceFactory.getDao(InstalledPartDao.class).findAllByProductIdAndPartNames(productId, partNames);
		//removing build result in case of rejection of installed part
		if(updatedParts.isEmpty()){
			productBuildResults.remove(getBuildResuiltByPartName(partResult.getPartName()));
		}
			
		boolean hasBuildResult = false;
		for(ProductBuildResult existingResult:  productBuildResults){
			InstalledPart installedPart = (InstalledPart)existingResult;
			if(installedPart.getId().getPartName().equalsIgnoreCase(partResult.getPartName())){
				hasBuildResult = true;
				if(updatedParts != null && !updatedParts.isEmpty()){
					installedPart.setInstalledPartStatus(updatedParts.get(0).getInstalledPartStatus());
					installedPart.setPartSerialNumber(updatedParts.get(0).getPartSerialNumber());
					installedPart.setPartId(updatedParts.get(0).getPartId());
					if((operation!= null && (ManualLotCtrRepairUtil.isMeasOnlyOperation(operation) || ManualLotCtrRepairUtil.hasScanMeasurementPart(operation)))
							|| (lotControlRule != null &&  (lotControlRule.getParts().get(0).getMeasurementSpecs().size() >0))
							|| (installedPart.getInstalledPartReason().equalsIgnoreCase(ApplicationConstants.MANUAL_INSERT))){
							setMeasurement(productId, installedPart);
					}
				}else{ //remove result scenario
					List<Measurement> currentMeasurements = installedPart.getMeasurements();
					installedPart.getMeasurements().removeAll(currentMeasurements);
					installedPart.setPartSerialNumber("");
					installedPart.setInstalledPartStatus(InstalledPartStatus.NC);					
				}
				break;
				}
		}
		//If the part does't have installedPart record before, add now.
		if(!hasBuildResult && updatedParts != null && !updatedParts.isEmpty()){
		
			List<Measurement> measurements = (List<Measurement>) ServiceFactory.getDao(MeasurementDao.class).findAllByProductIdAndPartNames(productId, partNames);
			updatedParts.get(0).setMeasurements(measurements);
			productBuildResults.add(updatedParts.get(0)); 
		}
		loadProductBuildResults();
	}
	
	private void getSelectedFilters(){
		selectedDivision = this.view.getDivisionComboBox().getSelectionModel().getSelectedItem();
		selectedProcesspoint = this.view.getProcessPointComboBox().getSelectionModel().getSelectedItems();
		
		processPointMap = new HashMap<String, ProcessPoint>();
		for (String processpoint : selectedProcesspoint) {
			processPointMap.put(processpoint.substring(0, processpoint.indexOf("-")), view.getProcessPointMap().get(processpoint));
		}
		
		selectedOpType = this.view.getOperationTypeComboBox().getSelectionModel().getSelectedItems();
		stopShipOnly = this.view.getStopShipCheckBox().isSelected();
		nonGALCPartsOnly = this.view.getNonGALCPartsCheckBox().isSelected();
		requiredPartsOnly = this.view.getRequiredPartsCheckBox().isSelected();
		selectedOpStatus = this.view.getOperationStatusType().getSelectionModel().getSelectedItem();
		selectedScreen = this.view.getSpecalityScreen().getSelectionModel().getSelectedItem();
		selectedTool = this.view.getToolType().getSelectionModel().getSelectedItem();
		selectedUnitNumberOrDesc = this.view.getUnitNumbOrDesc().getText();
		selectedSerialNumber= this.view.getSerialNumber().getText();
		selectedPartMask = this.view.getPartMask().getText();
		selectedPset = this.view.getPsetValue().getText();
	}
	
	public void loadProductBuildResults(BaseProduct product) {
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
		//get selected filters from view
		getSelectedFilters();
		if(hasProductIdChanged || productBuildResults == null || productBuildResults.isEmpty())
		{
			if(hasProductIdChanged)	currentSpecCode = loadProductSpec(product);
			List<InstalledPart> buildResults = (List<InstalledPart>) ProductTypeUtil.getTypeUtil(getProductType())
					.getProductBuildResultDao().findAllByProductId(product.getProductId());
			
			productBuildResults = new ArrayList<ProductBuildResult>();
			if(buildResults != null && !buildResults.isEmpty()){
				buildResults = ServiceFactory.getDao(MeasurementDao.class).findMeasurementsForInstalledParts(buildResults);
				productBuildResults.addAll(buildResults);
			}
		}
		
		if(nonGALCPartsOnly)	loadLotControlRules();
		if(!nonGALCPartsOnly && !requiredPartsOnly)	loadOperations(product);
		if(requiredPartsOnly)	loadRequiredParts();
		loadProductBuildResults();
		
		if (productBuildResults != null && productBuildResults.size() > 0) {
			this.view.getAddResult().setDisable(false);
			this.view.getRemoveResult().setDisable(false);
			this.view.getRemoveAllButton().setDisable(false);
			this.view.getRefresh().setDisable(false);
			this.view.getShowHistory().setDisable(false);
			this.view.getDoneNextProduct().setDisable(false);
			this.view.getInsertResultBtn().setDisable(false);
		}
		hasProductIdChanged = false;
		isSelectedDivisionChanged = false;
		isSelectedProcessPointChanged = false;
	}
	
	private void applyOpStatusFilter(String actual, Object part, List filteredObjects){
		boolean isComplete = false;
		if(part instanceof MCOperationRevision){
			MCOperationRevision mcOperationRevision = (MCOperationRevision)part ;
			isComplete = isOperationComplete(actual, mcOperationRevision);
		}else if(part instanceof LotControlRule){
			LotControlRule lotControlRule = (LotControlRule)part ;
			isComplete = isOperationComplete(actual, lotControlRule);
		}else if(part instanceof RequiredPart){
			RequiredPart requiredPart = (RequiredPart)part ;
			LotControlRule lotControlRule = getRule(requiredPart.getId().getPartName());
			isComplete = isOperationComplete(actual, lotControlRule);
		}
		if(OPERATION_STATUS_COMPLETE.equalsIgnoreCase(selectedOpStatus)){
			if(isComplete){
				if(!filteredObjects.contains(part)) filteredObjects.add(part);
			}else{
				if(filteredObjects.contains(part)) filteredObjects.remove(part);
			}
		}else{
			if(isComplete) {
				if(filteredObjects.contains(part)) filteredObjects.remove(part);
			}else{
				if(!filteredObjects.contains(part)) filteredObjects.add(part);
			}
		}
	}
	
	
	private List<MCOperationRevision> filterOperations() {
		if(!bNeedsFiltering()){
			return operations;
		}
		if(nonGALCPartsOnly || requiredPartsOnly) return null;
		
		List<MCOperationRevision> masterList = new ArrayList<MCOperationRevision>();
		//If there is division, process point and/or stop ship selected, pick the filtered list.
		List<MCOperationRevision> filteredOpsByDivisionProcessPointAndStopShip = filterByDivisionProcessPointAndStopShip();
		if(filteredOpsByDivisionProcessPointAndStopShip != null && filteredOpsByDivisionProcessPointAndStopShip.size() > 0){
			masterList  = filteredOpsByDivisionProcessPointAndStopShip;
		}else{
			masterList = operations;
		}
		if(!bNeedsSubFiltering()) return masterList;
	
		
		List<MCOperationRevision> filteredOperations = new ArrayList<MCOperationRevision>();
		//Operation Type
		filterOpType(masterList,filteredOperations);
		//Opeartion Status
		filterOpStatus(masterList,filteredOperations);
		//Specality screen
		filterSpecalityScreen(masterList,filteredOperations);
		//Tool Type
		filterToolType(masterList,filteredOperations);
		//Part Mask filter
		filterPartMask(masterList,filteredOperations);
		//Part Desc filter
		filterPartDesc(masterList,filteredOperations);
		//serial number
		filterSerialNumber(masterList,filteredOperations);
		//Pset value
		filterPSetValue(masterList,filteredOperations);


		 
		return filteredOperations;
	}
	private void filterOpType(List<MCOperationRevision> masterList,List<MCOperationRevision> filteredOperations){

				for (MCOperationRevision operation : masterList) {
					for (String operationType : selectedOpType) {
						if(!StringUtils.isEmpty(operationType)){
							if(operation.getType() != null)  {
								if(operationType.equalsIgnoreCase(operation.getType().name())){
									if(!filteredOperations.contains(operation))
										filteredOperations.add(operation);
								}
							}
						}
					}
				}
	}
	private void filterOpStatus(List<MCOperationRevision> masterList,List<MCOperationRevision> filteredOperations){
		if(!StringUtils.isEmpty(selectedOpStatus) ){
			//Use the filtered ops if not empty
			List<MCOperationRevision> tmpFilteredOperations = null;
			if(filteredOperations.isEmpty() && selectedOpType.isEmpty()) 
				tmpFilteredOperations = new ArrayList<MCOperationRevision>(masterList);
			else tmpFilteredOperations = new ArrayList<MCOperationRevision>(filteredOperations);
			for(MCOperationRevision operation : tmpFilteredOperations) {
				applyOpStatusFilter(operation.getId().getOperationName(), operation,filteredOperations);
			}
		}
	}
	private void filterSpecalityScreen(List<MCOperationRevision> masterList,List<MCOperationRevision> filteredOperations){
		if(!StringUtils.isEmpty(selectedScreen)){
			String[] screen = selectedScreen.split("/");
			String view = screen[0];
			String processor = screen[1];
			List<MCOperationRevision> tmpFilteredOperations = null;
			if(filteredOperations.isEmpty()  
					&& selectedOpType.isEmpty()
					&& StringUtils.isEmpty(selectedOpStatus) ) tmpFilteredOperations = new ArrayList<MCOperationRevision>(masterList);
			else tmpFilteredOperations = new ArrayList<MCOperationRevision>(filteredOperations);
			for(MCOperationRevision operation : tmpFilteredOperations) {
				if(!StringUtils.isEmpty(operation.getView()) && !StringUtils.isEmpty(operation.getProcessor()))
				{
						if(operation.getView().endsWith(view) && operation.getProcessor().endsWith(processor)){
							if(!filteredOperations.contains(operation))	filteredOperations.add(operation);
						}else{
							if(filteredOperations.contains(operation))	filteredOperations.remove(operation);
						}
					}
			}
			
		}
	}
	private void filterToolType(List<MCOperationRevision> masterList,List<MCOperationRevision> filteredOperations){
		if(!StringUtils.isEmpty(selectedTool)){
			List<MCOperationRevision> tmpFilteredOperations = null;
			if(filteredOperations.isEmpty()
					&& selectedOpType.isEmpty()
					&& StringUtils.isEmpty(selectedOpStatus)
					&& StringUtils.isEmpty(selectedScreen)) tmpFilteredOperations = new ArrayList<MCOperationRevision>(masterList);
			else tmpFilteredOperations = new ArrayList<MCOperationRevision>(filteredOperations);
			for(MCOperationRevision operation : tmpFilteredOperations) {
				if(ManualLotCtrRepairUtil.isMeasOnlyOperation(operation) || ManualLotCtrRepairUtil.hasScanMeasurementPart(operation))
				{
					for(MCOperationPartRevision part : operation.getParts()){
						if(PartType.MFG.equals(part.getPartType())) 
							applyFilter(selectedTool, part.getDeviceId(), operation,filteredOperations,false);
					}
				}else{
					//For all the other types remove the operation if exits
					applyFilter(selectedTool, null, operation,filteredOperations,false);
				}
				
			}
		}
	}
	private void filterPartMask(List<MCOperationRevision> masterList,List<MCOperationRevision> filteredOperations){
		if(!StringUtils.isEmpty(selectedPartMask)){
			List<MCOperationRevision> tmpFilteredOperations = null;
			if(filteredOperations.isEmpty()
					&& selectedOpType.isEmpty()
					&& StringUtils.isEmpty(selectedOpStatus)
					&& StringUtils.isEmpty(selectedScreen)
					&& StringUtils.isEmpty(selectedTool)) tmpFilteredOperations = new ArrayList<MCOperationRevision>(masterList);
			else tmpFilteredOperations = new ArrayList<MCOperationRevision>(filteredOperations);
			for(MCOperationRevision operation : tmpFilteredOperations) {
				if(!ManualLotCtrRepairUtil.isInstructionOrAutoCompleteOperation(operation)){
					for(MCOperationPartRevision part :operation.getParts()){
						//Add operation only for 'MFG' parts and if the operation doesn't exist in the filtered operations
						if(PartType.MFG.equals(part.getPartType()))
							applyFilter(selectedPartMask, part.getPartMask(), operation, filteredOperations,false);
					}
				}else{
					applyFilter(selectedPartMask, null, operation, filteredOperations,false);
				}
				
			}
		}
	}
	private void filterPartDesc(List<MCOperationRevision> masterList,List<MCOperationRevision> filteredOperations){
		if(!StringUtils.isEmpty(selectedUnitNumberOrDesc)){
			List<MCOperationRevision> tmpFilteredOperations = null;
			if(filteredOperations.isEmpty()
					&& selectedOpType.isEmpty()
					&& StringUtils.isEmpty(selectedOpStatus)
					&& StringUtils.isEmpty(selectedScreen)
					&& StringUtils.isEmpty(selectedTool)
					&& StringUtils.isEmpty(selectedPartMask)) tmpFilteredOperations = new ArrayList<MCOperationRevision>(masterList);
			else tmpFilteredOperations = new ArrayList<MCOperationRevision>(filteredOperations);
			for(MCOperationRevision operation : tmpFilteredOperations) {
				String tmpUnitNumberOrDec = operation.getId().getOperationName()+" "+operation.getDescription();
				applyFilter(selectedUnitNumberOrDesc, tmpUnitNumberOrDec, operation, filteredOperations,true);
			}

		}
	}
	
	private void filterSerialNumber(List<MCOperationRevision> masterList,List<MCOperationRevision> filteredOperations){
		if(!StringUtils.isEmpty(selectedSerialNumber)){
			List<MCOperationRevision> tmpFilteredOperations = null;
			if(filteredOperations.isEmpty()
					&& selectedOpType.isEmpty()
					&& StringUtils.isEmpty(selectedOpStatus)
					&& StringUtils.isEmpty(selectedScreen)
					&& StringUtils.isEmpty(selectedTool)
					&& StringUtils.isEmpty(selectedPartMask)
					&& StringUtils.isEmpty(selectedUnitNumberOrDesc)) tmpFilteredOperations = new ArrayList<MCOperationRevision>(masterList);
			else tmpFilteredOperations = new ArrayList<MCOperationRevision>(filteredOperations);
			for(MCOperationRevision operation : tmpFilteredOperations) {
				if(ManualLotCtrRepairUtil.hasScanPart(operation))
				{
					for (ProductBuildResult buildResult : productBuildResults) {
						if (buildResult.getPartName().equalsIgnoreCase(operation.getId().getOperationName())) 
							applyFilter(selectedSerialNumber,buildResult.getPartSerialNumber() ,operation,filteredOperations,false);
					}
				}else{
					applyFilter(selectedSerialNumber,null ,operation,filteredOperations,false);
				}
			}
		}
	}
	
	private void filterPSetValue(List<MCOperationRevision> masterList,List<MCOperationRevision> filteredOperations){
		
		if(!StringUtils.isEmpty(selectedPset)){
			List<MCOperationRevision> tmpFilteredOperations = null;
			if(filteredOperations.isEmpty()
					&& selectedOpType.isEmpty()
					&& StringUtils.isEmpty(selectedOpStatus)
					&& StringUtils.isEmpty(selectedScreen)
					&& StringUtils.isEmpty(selectedTool)
					&& StringUtils.isEmpty(selectedPartMask)
					&& StringUtils.isEmpty(selectedUnitNumberOrDesc)
					&& StringUtils.isEmpty(selectedSerialNumber))tmpFilteredOperations = new ArrayList<MCOperationRevision>(masterList);
			else tmpFilteredOperations = new ArrayList<MCOperationRevision>(filteredOperations);
			for(MCOperationRevision operation : tmpFilteredOperations) {
				for(MCOperationPartRevision part: operation.getParts()) 
					 applyFilter(selectedPset, part.getDeviceMsg(), operation, filteredOperations,false);
			}
		}
		
	}
	
	private List<LotControlRule> filterLotControlRules(){
		List<LotControlRule> filterRules = new ArrayList<LotControlRule>();
		if(!bNeedsSubFiltering()) return rules;
		if(rules != null && !rules.isEmpty()){
			if(!StringUtils.isEmpty(selectedUnitNumberOrDesc)){
				for(LotControlRule rule : rules){
					applyFilter(selectedUnitNumberOrDesc,rule.getPartNameString(), rule,filterRules,true);
				}	
			}
				
			if(!StringUtils.isEmpty(selectedPartMask)){
				List<LotControlRule> tmpFilteredRules = null;
				if(filterRules.isEmpty()) tmpFilteredRules = new ArrayList<LotControlRule>(rules);
				else tmpFilteredRules = new ArrayList<LotControlRule>(filterRules);
				for(LotControlRule rule : tmpFilteredRules) {
					if(!StringUtils.isEmpty(rule.getPartMasks()) && rule.getPartMasks().contains(selectedPartMask))
						applyFilter(selectedPartMask,rule.getPartNameString(), rule,filterRules,false);
					
				}
			}
				

				if(!StringUtils.isEmpty(selectedOpStatus)){
					List<LotControlRule> tmpFilteredRules = null;
					if(filterRules.isEmpty()) tmpFilteredRules = new ArrayList<LotControlRule>(rules);
					else tmpFilteredRules = new ArrayList<LotControlRule>(filterRules);
					for(LotControlRule rule : tmpFilteredRules) {
						applyOpStatusFilter(rule.getPartNameString(), rule,filterRules);
					}
				}
			}
		return filterRules;
	}
	
	private List<RequiredPart> filterRequiredParts(){
		List<RequiredPart> sourceList = Arrays.asList(requiredPartsList.toArray(new RequiredPart[requiredPartsList.size()]));
		if(!bNeedsSubFiltering()) return sourceList;
		
		List<RequiredPart> filterRequiredParts = new ArrayList<RequiredPart>();
		if(requiredPartsList != null && !requiredPartsList.isEmpty()){
			
				if(!StringUtils.isEmpty(selectedUnitNumberOrDesc)){
					for(RequiredPart part : sourceList){
						applyFilter(selectedUnitNumberOrDesc,part.getId().getPartName(), part,filterRequiredParts,true);
					}
				}
				

				if(!StringUtils.isEmpty(selectedOpStatus) && !"".equalsIgnoreCase(selectedOpStatus)){
					List<RequiredPart> tmpFilteredParts = null;
					if(filterRequiredParts.isEmpty()) tmpFilteredParts = new ArrayList<RequiredPart>(sourceList);
					else tmpFilteredParts = new ArrayList<RequiredPart>(filterRequiredParts);
					for(RequiredPart part : tmpFilteredParts) {
						applyOpStatusFilter(part.getId().getPartName(), part,filterRequiredParts);
					}
				}
			}

		return filterRequiredParts;
	}

	private List<MCOperationRevision> filterByDivisionProcessPointAndStopShip(){
		List<ProcessPoint> filteredProcessPointList = new ArrayList<ProcessPoint>();
		
		for (ProcessPoint processPoint : processPointList) {
			if (selectedProcesspoint != null && !selectedProcesspoint.isEmpty()) {
				ProcessPoint processPointSelected = processPointMap.get(processPoint);
				if (processPointSelected!= null && processPoint.getId().equalsIgnoreCase(processPointSelected.getId())
						&& processPoint.getLineId().equalsIgnoreCase(processPointSelected.getLineId())
						&& processPoint.getDivisionId().equalsIgnoreCase(processPointSelected.getDivisionId())) {
					filteredProcessPointList.add(processPoint);
				}
			} else {
				if (selectedDivision != null && selectedDivision != null) {
					if (processPoint.getDivisionId().equalsIgnoreCase(selectedDivision.getDivisionId())) {
						filteredProcessPointList.add(processPoint);
					}
				}
			}
		}
		List<MCOperationRevision> filteredOperations = new ArrayList<MCOperationRevision>();
		if (filteredProcessPointList.size() == 0) {
			if (stopShipOnly) {
				for (MCOperationRevision opr : operations) {
					if (opr.getCheck() == 1) {
						filteredOperations.add(opr);
					}
				}
			}
		} else {
			for (ProcessPoint processPoint : filteredProcessPointList) {
				for (MCOperationRevision operation : operations) {

					if (operation.getStructure().getId().getProcessPointId().equalsIgnoreCase(processPoint.getProcessPointId())
							&& operation.getStructure().getId().getDivisionId().equalsIgnoreCase(processPoint.getDivisionId())) {
						if (stopShipOnly) {
							if (operation.getCheck() == 1) {
								filteredOperations.add(operation);
							}
						} else {
							filteredOperations.add(operation);
						}
					}
				}
			}
		}
		
		return filteredOperations;
	}
	
	private boolean bNeedsFiltering(){
		return ((selectedDivision != null && selectedDivision.getId() != null) ||
				(selectedProcesspoint != null && !selectedProcesspoint.isEmpty()) ||
				bNeedsSubFiltering() ||
				stopShipOnly ||
				nonGALCPartsOnly ||
				requiredPartsOnly );
				
				
	}
	private boolean bNeedsSubFiltering(){
		return (!selectedOpType.isEmpty()||
		StringUtils.isNotEmpty(selectedOpStatus) ||
		StringUtils.isNotEmpty(selectedScreen)  ||
		StringUtils.isNotEmpty(selectedTool) ||
		StringUtils.isNotEmpty(selectedUnitNumberOrDesc) ||
		StringUtils.isNotEmpty(selectedPartMask) ||
		StringUtils.isNotEmpty(selectedUnitNumberOrDesc) ||
		StringUtils.isNotEmpty(selectedSerialNumber) ||
		StringUtils.isNotEmpty(selectedPset)); 
	}
	
	private void applyFilter(String selected, String actual, Object obj, List filterList, boolean bUnitNoOrDesc){
		if(!StringUtils.isEmpty(actual)){
			if(bUnitNoOrDesc){
				if(actual.toUpperCase().contains(selected.toUpperCase())){
					if(!filterList.contains(obj))	filterList.add(obj);
				}else{
					if(filterList.contains(obj))	filterList.remove(obj);
				}
			}else{
				if(actual.equalsIgnoreCase(selected)){
					if(!filterList.contains(obj))	filterList.add(obj);
				}else{
					if(filterList.contains(obj))	filterList.remove(obj);
				}
			}
		}else{
			if(filterList.contains(obj))	filterList.remove(obj);
		}
	}
	protected void loadProductBuildResults() {
		//filter
		partResults = new ArrayList<PartResult>();
		boolean operationCompleted = false;

		if(!nonGALCPartsOnly && !requiredPartsOnly){
			List<MCOperationRevision> filteredOperations = filterOperations();
			for (MCOperationRevision operation : filteredOperations) {
				operationCompleted = false;
				for (ProductBuildResult buildResult : productBuildResults) {
					if (buildResult.getPartName().equalsIgnoreCase(operation.getId().getOperationName())) {
						PartResult partResult = new PartResult();
						partResult.setBuildResult(buildResult);
						partResult.setLotControlRule(null);
						partResult.setPartName(buildResult.getPartName());
						partResult.setPartDesc(operation.getDescription());
						partResult.setStopShipOnly(operation.getCheck() == 1?true:false);
						partResult.setOperationType(operation.getType().name());
						partResults.add(partResult);
						operationCompleted = true;
						break;
					}
				}
				if (!operationCompleted) {
					PartResult partResult = new PartResult();
					partResult.setPartName(operation.getId().getOperationName());
					partResult.setPartDesc(operation.getDescription());
					partResult.setOperationType(operation.getType().name());
					partResult.setStopShipOnly(operation.getCheck() == 1?true:false);
					partResults.add(partResult);
				}
			}
			for (ProductBuildResult buildResult : productBuildResults) {
				if (buildResult.getInstalledPartReason().equalsIgnoreCase(ApplicationConstants.MANUAL_INSERT)) {
					PartResult partResult = new PartResult();
					partResult.setBuildResult(buildResult);
					partResult.setLotControlRule(null);
					partResult.setPartName(buildResult.getPartName());
					partResult.setPartDesc(null);
					partResult.setStopShipOnly(false);
					partResult.setOperationType(null);
					partResults.add(partResult);
				}
			}
			
			partResults = ManualLotCtrRepairUtil.getUniqueArrayList(partResults);
		}
		if(nonGALCPartsOnly){
			List<LotControlRule> filterLotControlRules = filterLotControlRules();
			
			List<RequiredPart> filterRequiredParts = filterRequiredParts();
			for (LotControlRule rule : filterLotControlRules) {
				if(!requiredPartsOnly){
					if(rule.getSerialNumberScanType() != PartSerialNumberScanType.NONE || rule.getParts().get(0).getMeasurementSpecs().size() >0 ){
						operationCompleted = false;
						for (ProductBuildResult buildResult : productBuildResults) {
							if (buildResult.getPartName().equals(rule.getId().getPartName())) {
								setPartResult(rule,buildResult);
								operationCompleted = true;
								break;
							}
						}
						if (!operationCompleted) {
							setPartResult(rule,null);
						}
					}
				}else{
					for (RequiredPart requiredPart : filterRequiredParts) {
						if(rule.getPartName().getPartName().equals(requiredPart.getId().getPartName())){
							operationCompleted = false;
							for (ProductBuildResult buildResult : productBuildResults) {
								if (buildResult.getPartName().equals(requiredPart.getId().getPartName())) {
									setPartResult(rule,buildResult);
									operationCompleted = true;
									break;
								}
							}
							if (!operationCompleted) {
								setPartResult(rule,null);
							}
						}
					}
				}
			}
		}

		this.view.getPartStatusTableView().getItems().clear();
		this.view.getPartStatusTableView().setItems(FXCollections.observableArrayList(partResults));

	}
	protected BaseProductSpec loadProductSpec(BaseProduct product) {
		if(hasProductIdChanged){
			BaseProductSpecDao productSpecDao = ProductTypeUtil.getProductSpecDao(getProductType());
			if ((product instanceof MbpnProduct) )
				 currentSpecCode = (Mbpn) productSpecDao.findByProductSpecCode(getProductSpecCode(product), getProductType().name());
			else currentSpecCode = (ProductSpec) productSpecDao.findByProductSpecCode(getProductSpecCode(product), getProductType().name());
		}
		
		return currentSpecCode;

	}

	private String getProductSpecCode(BaseProduct product) {

		return (product instanceof MbpnProduct) ? ((MbpnProduct) product).getCurrentProductSpecCode()
				: product.getProductSpecCode();
	}

	private void loadLotControlRules() {
		if(hasProductIdChanged  || isSelectedDivisionChanged || isSelectedProcessPointChanged || rulesAndPpIds == null || rulesAndPpIds.isEmpty() ){
			LotControlRuleId ruleId;
			rules = new  ArrayList<LotControlRule>();
			if (this.view.getProduct() instanceof MbpnProduct)
				ruleId = new LotControlRuleId((Mbpn)loadProductSpec(this.view.getProduct()));
			else ruleId = new LotControlRuleId((ProductSpec)loadProductSpec(this.view.getProduct()));
			LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
	
			rulesAndPpIds = lotControlRuleDao.findAllRulesAndProcessPoints(ruleId, getProductType().name());
			
			for (Object[] objects : rulesAndPpIds) {
				LotControlRule r = (LotControlRule) objects[0];
				ProcessPoint pp = (ProcessPoint) objects[1];
				if (selectedDivision!= null && selectedDivision.getId() != null &&pp != null && !pp.getDivisionId().equalsIgnoreCase(selectedDivision.getDivisionId()))
					continue;// filter out division

			
				
				if (selectedProcesspoint != null && !selectedProcesspoint.isEmpty()) {
					ProcessPoint processPointSelected = processPointMap.get(pp.getId());
					if (processPointSelected!= null && !pp.getId().equalsIgnoreCase(processPointSelected.getId()))
							continue;
					
				}
				rules.add(r);
			}
		}
		
	}

	public void removePartResult(String productId,PartResult partResult) {
		if (partResult.getBuildResult() == null) {
			MessageDialog.showInfo(ClientMainFx.getInstance().getStage(), "No data to remove.");

			return;
		}

		if (!MessageDialog.confirm(ClientMainFx.getInstance().getStage(), "Confirm remove data ?"))
			return;

		try {
			removeInstalledPart(partResult);
		} catch (Exception e) {
			MessageDialog.showError(ClientMainFx.getInstance().getStage(), "Failed to delete data:" + e.getMessage());
		}
		
		refreshPartData(productId,partResult);	

	}
	
	public void removeAllPartResult(String productId, List<PartResult> partResultList) {
		boolean isOk = true;
		boolean isDataAvailable = false;
		if (!MessageDialog.confirm(ClientMainFx.getInstance().getStage(), "Confirm remove all data?")) {
			return;
		}
		for(PartResult partResult : partResultList) {
			if (partResult.getBuildResult() != null) {
				isDataAvailable = true;
				try {
					removeInstalledPart(partResult);
				} catch (Exception e) {
					isOk = false;
					Logger.getLogger().error(new LogRecord("Failed to delete data:" + e.getMessage()));
				}
				refreshPartData(productId,partResult);
			}
		}
		if(!isOk) {
			MessageDialog.showError(ClientMainFx.getInstance().getStage(), "An error occured while deleting all data. Please try again or contact support team.");
		} else if(!isDataAvailable) {
			MessageDialog.showError(ClientMainFx.getInstance().getStage(), "There is no data to remove.");
		}
	}

	protected void removeInstalledPart(PartResult partResult) {
		if (partResult.getInstalledPart() == null || partResult.getInstalledPart().getMeasurements() == null)
			return;

		removeMeasurementData(partResult);

		removeInstalledPartData(partResult);

		Logger.getLogger().info("Installed Part Result was removed by user:" + appContext.getUserId()
				+ System.getProperty("line.separator") + partResult.getInstalledPart());

	}

	protected void removeInstalledPartData(PartResult result) {
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		installedPartDao.remove(result.getInstalledPart());

		result.setBuildResult(null);
	}

	protected void removeMeasurementData(PartResult result) {
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		for (Measurement measurement : result.getInstalledPart().getMeasurements()) {
			measurementDao.remove(measurement);
		}
	}

	public MCOperationRevision getOperation(PartResult partResult) {

		if (operations.size() > 0) {

			MCOperationRevision tempOperation = null;
			for (MCOperationRevision op : operations) {
				if (partResult.getPartName().equalsIgnoreCase(op.getId().getOperationName())) {
					tempOperation = op;
					break;
				}
			}
			return tempOperation;
		}
		return null;
	}

	public List<Division> getDivisionList() {
		return divisionList;
	}

	public List<ProcessPoint> getProcessPointList() {
		return processPointList;
	}

	public void setDivisionList(List<Division> divisionList) {
		this.divisionList = divisionList;
	}

	public void setProcessPointList(List<ProcessPoint> processPointList) {
		this.processPointList = processPointList;
	}

	
	public List<String> getOperationTypeList() {
		return operationTypeList;
	}

	public void setOperationTypeList(List<String> operationTypeList) {
		this.operationTypeList = operationTypeList;
	}
	
	

	public List<String> getSpecialityScreenList() {
		return specialityScreenList;
	}

	public void setSpecialityScreenList(List<String> specialityScreenList) {
		this.specialityScreenList = specialityScreenList;
	}

	public List<String> getToolTypeList() {
		return toolTypeList;
	}

	public void setToolTypeList(List<String> toolTypeList) {
		this.toolTypeList = toolTypeList;
	}

	public List<String> getOperationStatusList() {
		return operationStatusList;
	}

	public void setOperationStatusList(List<String> operationStatusList) {
		this.operationStatusList = operationStatusList;
	}

	public List<ProcessPoint> getProcessPointListByDivision(Division division) {
		this.processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAllByDivision(division);
		return this.processPointList;
	}
	
	public List<ProcessPoint> getAllProcessPoint() {
		this.processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAll();
		return this.processPointList;
	}

	public List<ProcessPoint> getProcessPointListByLine(Line line) {
		this.processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAllByLine(line);
		return this.processPointList;
	}
	public boolean isSelectedDivisionChanged() {
		return isSelectedDivisionChanged;
	}

	public void setSelectedDivisionChanged(boolean isSelectedDivisionChanged) {
		this.isSelectedDivisionChanged = isSelectedDivisionChanged;
	}

	public boolean isSelectedProcessPointChanged() {
		return isSelectedProcessPointChanged;
	}

	public void setSelectedProcessPointChanged(boolean isSelectedProcessPointChanged) {
		this.isSelectedProcessPointChanged = isSelectedProcessPointChanged;
	}
	
	private Map<String,List<MCOperationPartRevision>> getPartsListForProceePoint(Map<String,List<MCOperationPartRevision>> partsMap,String specCode,long structureRev,String processPointId ){
		List<MCOperationPartRevision> operationPartsList = ServiceFactory
				.getDao(MCOperationPartRevisionDao.class).findAllParts(specCode,
						structureRev, processPointId);
		if(operationPartsList != null && !operationPartsList.isEmpty())	partsMap.put(processPointId, operationPartsList);
		
		return partsMap;
	}

	public List<MCOperationRevision> loadOperations(BaseProduct product) {
		if(!hasProductIdChanged  && operations != null && !operations.isEmpty() && !isSelectedDivisionChanged && !isSelectedProcessPointChanged) return operations;
		Map<String, MCOperationRevision> operationsMap = new HashMap<String,MCOperationRevision>();
		List<MCStructure> mcStructures  = new ArrayList<MCStructure>() ;
		String productSpecCode = null;
		long  structureRev = 0  ; 
		for (Division division : divisionList) {
			if (selectedDivision != null && selectedDivision.getId() != null && !selectedDivision.equals(division))
				continue;
			//when structure create mode is process point mode, structure revision is fetched from MCProductStructureForProcessPoint 
			if(getStructureCreateMode().equals(StructureCreateMode.PROCESS_POINT_MODE)){
				if(selectedProcesspoint != null  && !selectedProcesspoint.isEmpty()){
					
					for (String processPoint : selectedProcesspoint) {
						
						MCProductStructureForProcessPoint productStructProcessPoint = ServiceFactory.getDao(MCProductStructureForProcessPointDao.class).findByKey(
								new MCProductStructureForProcessPointId(product.getProductId(),
										processPoint.substring(0,selectedProcesspoint.indexOf("-")), currentSpecCode.getProductSpecCode()));
						if(productStructProcessPoint!=null){
							productSpecCode = productStructProcessPoint.getId().getProductSpecCode();
							structureRev = productStructProcessPoint.getStructureRevision();
						}
						
					}

				}else{
					EventBusUtil.publish(new StatusMessageEvent("Please select process point as Structure Create Mode is Process Point Mode", StatusMessageEventType.ERROR));
					return null;
				}
			}else{
				MCProductStructure productStruct = ServiceFactory.getDao(MCProductStructureDao.class).findByKey(
						new MCProductStructureId(product.getProductId(), division.getDivisionId(), currentSpecCode.getProductSpecCode()));
				if(productStruct!=null){
					productSpecCode = productStruct.getId().getProductSpecCode();
					structureRev = productStruct.getStructureRevision();
				}
			}
			if(productSpecCode != null )
				mcStructures = ServiceFactory.getDao(MCStructureDao.class)
										.findAllByStructureForProdSpecCodeAndRevision(productSpecCode,structureRev);
			Map<String,List<MCOperationPartRevision>> partsMap = new HashMap<String,List<MCOperationPartRevision>>() ;
			for(MCStructure mcstructure : mcStructures)
			{
				if(selectedProcesspoint != null && !selectedProcesspoint.isEmpty()){
					ProcessPoint processPoint = processPointMap.get(mcstructure.getId().getProcessPointId());
					if (processPoint!= null  && processPoint.getProcessPointId().equalsIgnoreCase(mcstructure.getId().getProcessPointId())){
						if(!partsMap.containsKey(mcstructure.getId().getProcessPointId())) {
							partsMap = getPartsListForProceePoint(partsMap,productSpecCode,structureRev,mcstructure.getId().getProcessPointId() );
						}
					}
				}else{
					if(!partsMap.containsKey(mcstructure.getId().getProcessPointId())) {
						partsMap = getPartsListForProceePoint(partsMap,productSpecCode,structureRev,mcstructure.getId().getProcessPointId() );
					}
				}
			}
			for (MCStructure mcstructure : mcStructures) {
				
				ProcessPoint processPointSelected = null;
				if(selectedProcesspoint != null && !selectedProcesspoint.isEmpty())
					processPointSelected = processPointMap.get(mcstructure.getId().getProcessPointId());
						
				if (processPointSelected != null) {
					if(processPointSelected.getProcessPointId()!= null 
						&& !processPointSelected.getProcessPointId().equalsIgnoreCase(mcstructure.getId().getProcessPointId()))
					continue;
				
				MCOperationRevision tempOperation  = null;
				if (!operationsMap.containsKey(mcstructure.getId().getOperationName())) tempOperation = mcstructure.getMcOperationRevision();
				else tempOperation = operationsMap.get(mcstructure.getId().getOperationName()+"-"+mcstructure.getId().getProcessPointId());
				
				if(tempOperation != null){
					
					tempOperation.setStructure(mcstructure);
					
					List<MCOperationPartRevision> parts = partsMap.get(mcstructure.getId().getProcessPointId());
					if(parts != null && !parts.isEmpty())
					{
						for (MCOperationPartRevision part : parts) {
							if(mcstructure.getId().getOperationName().equalsIgnoreCase(part.getId().getOperationName())
									&& mcstructure.getId().getPartRevision()== part.getId().getPartRevision()
									&& mcstructure.getId().getPartId().equals(part.getId().getPartId()))
							{
								//For instruction, add reference part
								if(ManualLotCtrRepairUtil.isInstructionOrAutoCompleteOperation(tempOperation)){
									tempOperation.getParts().add(part);
									break;	
								}else{ //All other part types, add all the MFG parts
									if(PartType.MFG.equals(part.getPartType())){
										tempOperation.getParts().add(part);
									}
								}
									
							}
						}
					}
					if (!operationsMap.containsKey(mcstructure.getId().getOperationName()+"-"+mcstructure.getId().getProcessPointId())) 
						operationsMap.put(mcstructure.getId().getOperationName()+"-"+mcstructure.getId().getProcessPointId(),tempOperation);
				}
			   }
			}
		}
		operations = new ArrayList<MCOperationRevision>(operationsMap.values());
		return operations;
	}
	
	private void loadRequiredParts() {
		if(hasProductIdChanged || isSelectedDivisionChanged || isSelectedProcessPointChanged || view.getRequiredPartsCheckBox().isSelected())
		{
			if((selectedDivision != null && selectedDivision.getId() != null) 
					|| (selectedProcesspoint != null && !selectedProcesspoint.isEmpty())){
				List<ProcessPoint> selectedProcessPoints = new ArrayList<ProcessPoint>();
				if(selectedProcesspoint != null)  {
					for (String processPointString : selectedProcesspoint) {
						selectedProcessPoints.add(view.getProcessPointMap().get(processPointString.substring(0, processPointString.indexOf("-"))));
					}
				}else if(selectedDivision != null) {
					for (ProcessPoint processPoint : processPointList) {
						if (processPoint.getDivisionId().equalsIgnoreCase(selectedDivision.getDivisionId())) {
							selectedProcessPoints.add(processPoint);
						}
					}
				}
				addRequiredParts(selectedProcessPoints);
			}else{
				addRequiredParts(processPointList);
					
				}
		}

	}
	private void addRequiredParts(List<ProcessPoint> selectedProcessPoints){
		for (ProcessPoint processPoint : processPointList) {
			List<RequiredPart> requiredparts = ServiceFactory.getDao(RequiredPartDao.class)
				.findAllByProcessPointAndProdSpec(processPoint.getId(), currentSpecCode);
			this.requiredPartsList.addAll(requiredparts);
		}
	}

	public void setFilterCriteriaText() {
		
		if(bNeedsFiltering()){
			List<String> selectedProcessPointFilter =  new ArrayList<>();
			List<String> selectedOpTypeFilter =  new ArrayList<>();
			
			StringBuffer text = new StringBuffer();
			if(selectedDivision != null && selectedDivision.getId() != null){
				text.append("Div = "+selectedDivision.getId());
			}

			if(StringUtils.isNotEmpty(selectedOpStatus)){
				if(text.length() >0) text.append(", ");
				text.append("Op Status = "+selectedOpStatus);
			}
			if(StringUtils.isNotEmpty(selectedScreen)){
				if(text.length() >0) text.append(", ");
				text.append("Specality Screen = "+selectedScreen);
			}
			if(StringUtils.isNotEmpty(selectedSerialNumber)){
				if(text.length() >0) text.append(", ");
				text.append("Serial number = "+selectedSerialNumber);
			}
			if(StringUtils.isNotEmpty(selectedPartMask)){
				if(text.length() >0) text.append(", ");
				text.append("Part Mask = "+selectedPartMask);
			}
			if(StringUtils.isNotEmpty(selectedUnitNumberOrDesc)){
				if(text.length() >0) text.append(", ");
				text.append("Unit Number/Desc Mask = "+selectedUnitNumberOrDesc);
			}
			if(StringUtils.isNotEmpty(selectedPset)){
				if(text.length() >0) text.append(", ");
				text.append("Pset = "+selectedPset);
			}
			if(StringUtils.isNotEmpty(selectedTool)){
				if(text.length() >0) text.append(", ");
				text.append("Tool Type = "+selectedTool);
			}
			if(stopShipOnly){
				if(text.length() >0) text.append(", ");
				text.append("Stop Ship Only = "+stopShipOnly);
			}
			if(nonGALCPartsOnly){
				if(text.length() >0) text.append(", ");
				text.append("Non-GALC Parts Only = "+nonGALCPartsOnly);
			}
			if(requiredPartsOnly){
				if(text.length() >0) text.append(", ");
				text.append("Required Parts Only = "+requiredPartsOnly);
			}

			this.view.getFilterPane().setText("");
    		BorderPane bPane = new BorderPane();
    		HBox  hboxFilter= new HBox();
    		hboxFilter.getChildren().addAll(new Label("Filter Criteria  "+text.toString()), new Label("  "));
  			if(selectedProcesspoint != null && !selectedProcesspoint.isEmpty()){
  				selectedProcessPointFilter.addAll(selectedProcesspoint);
  				Button processPointButton = createFilterButton("Selected PP",true, selectedProcessPointFilter, "Selected Process Points");
				
  				hboxFilter.getChildren().add(processPointButton);
  			}
  			
  			if(!selectedOpType.isEmpty()){
  				selectedOpTypeFilter.addAll(selectedOpType);
  				Button opTypeButton = createFilterButton("Selected OpType", false, selectedOpTypeFilter, "Selected Operation Types");
  				hboxFilter.getChildren().add(opTypeButton);
  			}
  			
  			bPane.setLeft(hboxFilter);
			this.view.getFilterPane().setGraphic(bPane);
		}else{
			this.view.getFilterPane().setText("Filter Criteria  ");
		}
		
	}

	private Button createFilterButton(String name,boolean isProcessPoint, List<String> data, String title) {
		Button processPointButton = UiFactory.createButton(name,true);
		processPointButton.setPrefSize(110, 15);
		processPointButton.setPadding(new Insets(0, 20, 0, 20));
		processPointButton.setStyle("-fx-font: 10 arial;-fx-font-weight:bold;");
		processPointButton.setDisable(false);
		processPointButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				ManualLotControlViewMessageDialog dialog = 
						new ManualLotControlViewMessageDialog(view.getMainWindow(), 
								title, view.getMainWindow().getStage(), isProcessPoint, data);
				dialog.showDialog();
			}
		});
		return processPointButton;
	}

	/**
	 * This method adds the require part check box listener.
	 */
	public void addRequiredPartChechBoxListener() {
		view.getRequiredPartsCheckBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue){
		    		view.getNonGALCPartsCheckBox().setSelected(true);
		    		view.getStopShipCheckBox().setSelected(false);
		    	}
		    }
		});
	}
	
	/**
	 * This method adds the Non GALC part check box listener.
	 */
	public void addNonGalcPartChechBoxListener() {
		view.getNonGALCPartsCheckBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(!newValue){
		    		view.getRequiredPartsCheckBox().setSelected(false);
		    	}else{
		    		view.getStopShipCheckBox().setSelected(false);
		    	}
		    }
		});
	}
	
	/**
	 * This method adds the stop ship check box listener.
	 */
	public void addStopShipListener() {
		view.getStopShipCheckBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue){
		    		view.getRequiredPartsCheckBox().setSelected(false);
		    		view.getNonGALCPartsCheckBox().setSelected(false);
		    	}
		    }
		});
	}

	/**
	 * This method fetches the structure create mode 
	 */
	public  StructureCreateMode getStructureCreateMode() {
		String structureMode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());
		return structureMode == null ? StructureCreateMode.DIVISION_MODE : StructureCreateMode.get(structureMode);
	}
	
	/**
	 * This method sets part result data
	 * @param LotControlRule
	 * @param ProductBuildResult
	 */
	private void setPartResult(LotControlRule rule,ProductBuildResult buildResult) {
		PartResult partResult = new PartResult();
		if(buildResult!=null){
			partResult.setBuildResult(buildResult);
			partResult.setPartName(buildResult.getPartName());
			partResult.setLotControlRule(rule);
		}else{
			partResult.setLotControlRule(rule);
			partResult.setPartName(rule.getId().getPartName());
		}
		partResults.add(partResult);
	}
	
	
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
	

	/**
	 * This method sets measurements for installed part data
	 * @param productId
	 * @param installedPart
	 */
	private void setMeasurement(String productId , InstalledPart installedPart) {
			List<Measurement> measurements = ServiceFactory.getDao(MeasurementDao.class).findAll(productId,
					installedPart.getPartName());
			List<Measurement> currentMeasurements = installedPart.getMeasurements();
			installedPart.getMeasurements().removeAll(currentMeasurements);
			installedPart.getMeasurements().addAll(measurements);
	}
	

	/**
	 * This method checks if operation is Complete 
	 * @param actual
	 * @param mcOperationRevision
	 * @return boolean
	 */
	private boolean isOperationComplete(String actual , MCOperationRevision mcOperationRevision) {
		for (ProductBuildResult buildResult : productBuildResults) {
			if (buildResult.getPartName().equalsIgnoreCase(actual) && buildResult.getInstalledPartStatus().equals(InstalledPartStatus.OK)) {
				
				if(mcOperationRevision.getType().equals(OperationType.GALC_MEAS_MANUAL) || 
						mcOperationRevision.getType().equals(OperationType.GALC_MEAS) ||
						mcOperationRevision.getType().equals(OperationType.GALC_SCAN_WITH_MEAS)||
						mcOperationRevision.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL)){
					
					InstalledPart installedPart = (InstalledPart)buildResult;
					for(MCOperationPartRevision  mcOperationPartRev : mcOperationRevision.getParts()){
						if (installedPart.getPartId().equalsIgnoreCase(mcOperationPartRev.getId().getPartId())){
							if(installedPart.getMeasurements().size() == mcOperationPartRev.getMeasurements().size()){
								return true;
							}
						}
					}
				}else{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This method checks if operation is Complete for non GALC part data
	 * @param actual
	 * @param lotControlRule
	 * @return boolean
	 */
	private boolean isOperationComplete(String actual ,  LotControlRule lotControlRule) {
		for (ProductBuildResult buildResult : productBuildResults) {
			if (buildResult.getPartName().equalsIgnoreCase(actual) && buildResult.getInstalledPartStatus().equals(InstalledPartStatus.OK)) {
				InstalledPart installedPart = (InstalledPart)buildResult;
					for(PartSpec  partSpec : lotControlRule.getParts()){
						if (installedPart.getPartId().equalsIgnoreCase(partSpec.getId().getPartId())){
							if(installedPart.getMeasurements().size() == partSpec.getMeasurementSpecs().size()){
								return true;
							}
						}
					}
				}
			}
		return false;
	}
	
	/**
	 * This method returns lot control rule by part name
	 * @param partName
	 * @return LotControlRule
	 */
	public LotControlRule getRule(String partName) {
		if (rules.size() > 0) {
			LotControlRule lotControlRule = null;
			for (LotControlRule rule : rules) {
				if (partName.equalsIgnoreCase(rule.getPartName().getPartName())) {
					lotControlRule = rule;
					break;
				}
			}
			return lotControlRule;
		}
		return null;
	}
	
	/**
	 * This method returns build result by part name
	 * @param partName
	 * @return ProductBuildResult
	 */
	public ProductBuildResult getBuildResuiltByPartName(String partName) {
		for(ProductBuildResult buildResult :productBuildResults){
			if(buildResult.getPartName().equalsIgnoreCase(partName)){
				return buildResult;
			}
		}
		return null;
	}
	
}

