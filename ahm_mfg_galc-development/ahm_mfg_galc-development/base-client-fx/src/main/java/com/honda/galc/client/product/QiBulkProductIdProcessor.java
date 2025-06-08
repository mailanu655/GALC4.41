package com.honda.galc.client.product;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.enumtype.ProductStatus;
import com.honda.galc.client.product.mvc.BulkProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.schedule.EntryDepartmentEvent;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProductProcessEvent;
import com.honda.galc.client.ui.event.SessionEvent;
import com.honda.galc.client.ui.event.SessionEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qi.QiStationEntryDepartmentDao;
import com.honda.galc.dao.qi.QiStationUpcPartDao;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.KickoutStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.MultiLineHelper;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.StringUtil;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class QiBulkProductIdProcessor extends BulkProductIdProcessor {

	private String entryDepartment;
	private String entryDepartment2;

	private String selectedAssociateId;
	protected String currentAppId = "";
	protected MultiLineHelper multiLineHelper = null;
	private Map<String, Division> divisionMap = new HashMap<String, Division>();
	private Map<String, Line> lineMap = new HashMap<String, Line>();
	private Map<String, ProcessPoint> processPointMap = new HashMap<String, ProcessPoint>();
	

	public QiBulkProductIdProcessor(BulkProductController productIdController) {
		super(productIdController);
		currentAppId = this.getProductController().getProcessPointId();
		multiLineHelper = MultiLineHelper.getInstance(currentAppId);
	}

	@Override
	public void validateProduct() {
		try {
			getDefectResults();
			ProductModel productModel = new ProductModel();

			// validate if all the required data is present to go to Defect entry
			// new process point to use, initialize to current station
			ProcessPoint newPP = getDao(ProcessPointDao.class).findById(getProductController().getModel().getApplicationContext().getApplicationId());
			//initialize process point id to current process point
			String processPointToUse = newPP == null ? currentAppId : newPP.getProcessPointId();
			//if multi-line and helper returns a valid process point, use that
			if(multiLineHelper.isMultiLine())  {
				newPP = multiLineHelper.getProcessPointToUse(productSearchResults.get(0).getProduct());
				if(newPP != null && !newPP.getProcessPointId().trim().equalsIgnoreCase(currentAppId))  {
					processPointToUse = newPP.getProcessPointId().trim();				
				}
				//if newPP is null, set it back to initialized value
				else newPP = getProductController().getModel().getProcessPoint();
			}

			if(!checkAllPreRequisite(processPointToUse)) {
				return;
			}
			
			ProductSearchResult.setCurrentWorkingProcessPointId(processPointToUse); //used to check previous defect entry dept
			
			for(ProductSearchResult result : productSearchResults) {
				List<QiRepairResultDto> defectList = result.getDefectResults();
				for(QiRepairResultDto defect : defectList) {
					updateDefectStatus(result, defect);
					if(result.getDefectStatus() != null && result.getDefectStatus().getId() == DefectStatus.NON_REPAIRABLE.getId())
						break;
				}
				BaseProduct currentProduct = result.getProduct();
				//check if valid MBPN product
				if(currentProduct !=null && currentProduct.getProductSpecCode() != null) {
					if (currentProduct.getProductType().equals(ProductType.MBPN)) {
						if (!ServiceFactory.getDao(QiStationUpcPartDao.class).isValidMBPN(processPointToUse, currentProduct.getProductId())) {
							getProductController().getView().setErrorMessage("MBPN ID is not valid for this station. Please use NAQ Station Config - MBPN/UPC to setup.");
							return;
						}
					}

					boolean isLineIdCheckEnabled = PropertyService.getPropertyBean(ProductCheckPropertyBean.class,processPointToUse).isLineIdCheckEnabled();
					if (isLineIdCheckEnabled) {
						ProductCheckUtil productCheckUtil = new ProductCheckUtil();
						productCheckUtil.setProduct(currentProduct);
						productCheckUtil.setProcessPoint(newPP);
						//If station is in Training Mode and VIN already shipped
						if(getProductController().getModel().isTrainingMode() 
								&& ProductCheckUtil.isProductShipped(currentProduct.getProductId()) 
								&& !showConfirmProductDialog("Product Already Shipped. Do you still want to go ahead?")) return;
						//If station is in Training Mode and VIN came from unexpected line
						else if(getProductController().getModel().isTrainingMode() 
								&& !ProductCheckUtil.isProductShipped(currentProduct.getProductId()) 
								&& productCheckUtil.invalidPreviousLineCheck() 
								&& !showConfirmProductDialog("Product came from an unexpected line. Do you still want to go ahead?")) return;
						//If station is not in training mode and came from unexpected line
						else if (productCheckUtil.invalidPreviousLineCheck() && !getProductController().getModel().isTrainingMode()) {
							String trackingStatus = currentProduct.getTrackingStatus();
							if(!StringUtils.isEmpty(trackingStatus)){
								String myLineName = trackingStatus;
								Line myLine = getLine(trackingStatus);
								if(myLine != null)   {
									myLineName=myLine.getLineName();
								}
								result.setErrorMessage("Product came from an unexpected line : "+ myLineName);
							} else {
								result.setErrorMessage("Tracking status is null");

							}
							result.setProductStatus(ProductStatus.INVALID);
						} 
					}
				} else {
					result.setErrorMessage("Product does not exist");
					result.setProductStatus(ProductStatus.NOT_EXIST);			
				}
			}

			productModel.setProductList(productSearchResults);

			EventBusUtil.publish(new ProductEvent(productModel, ProductEventType.PRODUCT_INPUT_OK));

		} catch(Exception e) {
			getProductController().getView().setErrorMessage("Error occured while validating products");
			getProductController().getLogger().error(e, "Error Occured while validating products " + e.getStackTrace());
		}	
	}

	private boolean isStationConfigured(String processPointToUse) {
		return(isEntryDepartmentConfigured(processPointToUse)
				&& isWriteUpDepartmentConfigured(processPointToUse)
				&& isDefectStatusConfigured(processPointToUse));
	}

	/**
	 * Method to create and show confirm dialog with user defined message.
	 * @param msg
	 * @return boolean
	 */
	private boolean showConfirmProductDialog(String msg) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(msg);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
			return false;
		}		
	}
	public TextField getBaseProductIdField() {
		return getProductController().getView().getInputPane().getProductIdField();
	}

	private boolean checkAllPreRequisite(String processPointToUse) {
		return isStationConfigured(processPointToUse) && isAssociateIdSelected();
	}

	private void getDefectResults() {
		int resultSize = productSearchResults.size();
		List<String> productIdList = new ArrayList<String>();
		int currentLocation = 0;
		List<QiRepairResultDto> defectResults = new ArrayList<QiRepairResultDto>();

		while(resultSize > 0) {
			int batchSize = getBatchSize(resultSize);
			resultSize -= batchSize;

			for(int y = currentLocation, x = batchSize; x > 0; x--, y++) {
				if(productSearchResults.get(y).getProduct() != null) {
					productIdList.add(productSearchResults.get(y).getProduct().getProductId());
				}
			}
			if(!productIdList.isEmpty()) {
				defectResults.addAll(getDao(QiRepairResultDao.class).findAllDefectsByProductIdsNoImage(productIdList));
			}
			productIdList.clear();
			currentLocation += batchSize;

		}
		
		if(getProductController().getModel().isTrainingMode())
			defectResults = updateParentDefectResultsForTrainingMode(defectResults);
		ProductSearchResult.setDefectResults(defectResults);
	}
	
	/**
	 * This method is to Add/Update/Delete the searched main/parent defect results with the cached data for Training Mode
	 * @param defectResults
	 * @return
	 */
	private List<QiRepairResultDto> updateParentDefectResultsForTrainingMode(List<QiRepairResultDto> defectResults){
		Map<QiRepairResultDto, Integer> cachedDefectsForTM = this.getProductController().getModel().getCachedDefectsForTraingMode();
		if(cachedDefectsForTM != null && cachedDefectsForTM.size() > 0) {	
			for(Map.Entry<QiRepairResultDto, Integer> entry : cachedDefectsForTM.entrySet()) {
				QiRepairResultDto cachedDefect = (QiRepairResultDto) entry.getKey();
				Integer type = entry.getValue();
				if(type == QiConstant.ADD_PARENT_DEFECT_FOR_TM) {
					defectResults.add(cachedDefect);
				}else if(type == QiConstant.DELETE_PARENT_DEFECT_FOR_TM) {
					if(defectResults != null && defectResults.size() > 0)
						defectResults.remove(cachedDefect);
				}else if(type == QiConstant.UPDATE_PARENT_DEFECT_FOR_TM){
					if(defectResults != null && defectResults.size() > 0) {
						for(QiRepairResultDto defect : defectResults) {
							if(defect.getDefectResultId() == cachedDefect.getDefectResultId()) {
								defectResults.remove(defect);
								defectResults.add(cachedDefect);
								break;
							}
						}
					}
				}
			}
		}
			
		return defectResults;
	}

	private void updateDefectStatus(ProductSearchResult result, QiRepairResultDto qiRepairResultDto) {
		
		short defectStatus = qiRepairResultDto.getCurrentDefectStatus();
		boolean isScrap = DefectStatus.NON_REPAIRABLE.getId() == defectStatus;
		boolean hasKickout = qiRepairResultDto.getKickoutId() > 0;
		boolean isKOOutstanding = hasKickout && (qiRepairResultDto.getKickoutStatus() == KickoutStatus.OUTSTANDING.getId());
		
		//set defect status
		if (isScrap) {
			result.setDefectStatus(DefectStatus.getType(defectStatus));
			result.setErrorMessage("Product is scrapped");	
		} else if (DefectStatus.OUTSTANDING.getId() == defectStatus
				|| DefectStatus.NOT_REPAIRED.getId() == defectStatus
				|| DefectStatus.NOT_FIXED.getId() == defectStatus) {
			result.setDefectStatus(DefectStatus.getType(defectStatus));
		} else if(result.getDefectStatus() == null) {
			result.setDefectStatus(DefectStatus.getType(defectStatus));
		}
		
		//set kickout if it has kickout
		if(isKOOutstanding && (result.getLastKickoutId() == 0 || qiRepairResultDto.getKickoutId() < result.getLastKickoutId()))  {
			//fetch kickout process point name
			String koProcessPointName = "";
			koProcessPointName = getKickoutProcessPoint(qiRepairResultDto);
			if(StringUtils.isBlank(koProcessPointName))  koProcessPointName = "";
			//set process point name if defect is non-repairable or ko is outstanding)
			qiRepairResultDto.setKickoutProcessPointName(koProcessPointName);
			updateKickoutLocation(result, qiRepairResultDto, koProcessPointName);			
			result.setLastKickoutId(qiRepairResultDto.getKickoutId());
		}
	}

	private void updateKickoutLocation(ProductSearchResult result, QiRepairResultDto qiRepairResultDto, String kickoutProcessPointName) {
		if(kickoutProcessPointName != null) {
			updateProductKickoutSequence(qiRepairResultDto, result);
		}
	}

	/**
	 * @param qiRepairResultDto : QiRepairResultDto
	 * @return processPointName : String
	 * get kickout process point name if kickout exists
	 * returns the process point name (kickout location name) or ""
	 */
	private String getKickoutProcessPoint(QiRepairResultDto qiRepairResultDto) {
		long kickoutId = qiRepairResultDto.getKickoutId();
		String koProcessPointId = qiRepairResultDto.getKickoutProcessPointId();
		String processPointName = "";

		if(kickoutId > 0 && !StringUtils.isBlank(koProcessPointId)) {	
			ProcessPoint processPoint = processPointMap.get(StringUtils.trim(koProcessPointId));	
			if(processPoint == null) {
				processPoint = getProductController().getModel().findProcessPoint(koProcessPointId);
				processPointName = processPoint.getProcessPointName();
			}else {
				processPointName = processPoint.getProcessPointName();
			}
		}
		if(StringUtils.isBlank(processPointName))  processPointName = "";
		return processPointName;
	}
	
	private void updateProductKickoutSequence(QiRepairResultDto qiRepairResultDto, ProductSearchResult result) {
		Division division = getDivision(qiRepairResultDto.getKickoutDivisionId());
		Line line = getLine(qiRepairResultDto.getKickoutLineId());
		ProcessPoint processPoint = getProcessPoint(qiRepairResultDto.getKickoutProcessPointId());
		
		if(division != null && (division.getSequenceNumber() < result.getKickoutDivisionSequence())) {
			setKickoutLocation(result, division, line, processPoint);
		} else if(line != null && (line.getLineSequenceNumber() < result.getKickoutLineSequence())) {
			setKickoutLocation(result, division, line, processPoint);
		} else if(processPoint != null && (processPoint.getSequenceNumber() < result.getKickoutProcessPointSequence())) {
			setKickoutLocation(result, division, line, processPoint);
		}
		
	}

	private void setKickoutLocation(ProductSearchResult result, Division division, Line line,
			ProcessPoint processPoint) {
		if(division != null) result.setKickoutDivisionSequence(division.getSequenceNumber());
		if(line != null) result.setKickoutLineSequence(line.getLineSequenceNumber());
		if(processPoint != null)  {
			result.setKickoutProcessPointSequence(processPoint.getSequenceNumber());
			result.setKickoutProcessPointName(processPoint.getProcessPointName());
		}
	}
		
	private boolean isEntryDepartmentConfigured(String processPointId) {
		boolean retVal = true;
		StringBuilder sb = new StringBuilder();
		String myEntryDept = "";
		boolean isSameLine = true;

		try {
			List<QiStationEntryDepartment> deptList = getDao(QiStationEntryDepartmentDao.class)
					.findAllEntryDeptInfoByProcessPoint(processPointId);
			if (deptList == null || deptList.isEmpty()) {
				sb.append("Entry Department not configured for Process point :").append(processPointId);
				getProductController().getView().setErrorMessage(sb.toString(), getBaseProductIdField());
				return false;
			}
			isSameLine = multiLineHelper.isSameStation(processPointId);
			if(isSameLine)  {
				myEntryDept = entryDepartment;
			}
			else  {
				myEntryDept = entryDepartment2;
			}
			if (StringUtils.isBlank(myEntryDept)) {
				QiStationEntryDepartment qiStationEntryDepartment = getDao(QiStationEntryDepartmentDao.class)
						.findDefaultEntryDeptByProcessPoint(processPointId);
				if (qiStationEntryDepartment != null) {
					if(isSameLine)  {
						entryDepartment = qiStationEntryDepartment.getId().getDivisionId();
					}
					else  {
						entryDepartment2 = qiStationEntryDepartment.getId().getDivisionId();
					}
				}
				else { //entry department was not selected and there is no default set
					sb.append("Please Select an Entry Department.");
					getProductController().getView().setErrorMessage(sb.toString(), getBaseProductIdField());
					retVal = false;
				}
			} 
		} catch (Exception ex) {
			sb.append("Error occured validating (is) entry dept configured: ").append(processPointId)
			.append("-").append(ex.getMessage());
			getProductController().getView().setErrorMessage(sb.toString(), getBaseProductIdField());
			retVal = false;
		}
		return retVal;
	}

	private boolean isWriteUpDepartmentConfigured(String processPointId) {
		boolean retVal = true;
		StringBuilder sb = new StringBuilder();
		try {
			List<String> writeUpDeptLists =  getDao(QiStationWriteUpDepartmentDao.class).findAllWriteUpDeptByProcessPoint(processPointId);
			if(writeUpDeptLists == null || writeUpDeptLists.isEmpty()){
				sb.append("Write up department not configured for Process point :").append(processPointId);
				getProductController().getView().setErrorMessage(sb.toString(), getBaseProductIdField());
				retVal = false;
			}
		} catch (Exception ex) {
			sb.append("Error occured validating write-up department for: ").append(processPointId)
			.append("-").append(ex.getMessage());
			getProductController().getView().setErrorMessage(sb.toString(), getBaseProductIdField());
			retVal = false;
		}
		return retVal;
	}

	private boolean isDefectStatusConfigured(String processPointId) {
		boolean retVal = true;
		StringBuilder sb = new StringBuilder();
		try {
			QiStationConfiguration qiEntryStationConfigManagement = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(processPointId, QiConstant.ENTRY_STATION_AVAILABLE_DEFECT_STATUS);
			if(qiEntryStationConfigManagement == null){
				sb.append("Defect Status not configured for Process Point  :").append(processPointId);
				getProductController().getView().setErrorMessage(sb.toString(), getBaseProductIdField());
				retVal = false;
			}
		} catch (Exception ex) {
			sb.append("Error occured validating defect status for: ").append(processPointId)
			.append("-").append(ex.getMessage());
			getProductController().getView().setErrorMessage(sb.toString(), getBaseProductIdField());
			retVal = false;
		}
		return retVal;
	}

	@Override
	public boolean isAssociateIdSelected() {
		QiStationConfiguration qiEntryStationConfigManagement = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProductController().getProcessPointId(),QiEntryStationConfigurationSettings.GATHER_ASSOCIATE_ID.getSettingsName());
		if((qiEntryStationConfigManagement != null && !qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.NO))
				|| (qiEntryStationConfigManagement == null && !QiEntryStationConfigurationSettings.GATHER_ASSOCIATE_ID.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.NO))) {
			if(StringUtils.isEmpty(selectedAssociateId) || selectedAssociateId.equalsIgnoreCase(QiConstant.NEW)) {
				getProductController().getView().setErrorMessage("Please select Associate Id ",getBaseProductIdField());
				return false;
			}
		}
		return true;
	}

	//=== events === //
	@Subscribe()
	public void onEntryDepartmentSelectEvent(EntryDepartmentEvent event) {
		if(null!=event &&  event.getEventType().equals(QiConstant.ENTRY_DEPT_SELECTED)) {
			entryDepartment = (String) event.getTargetObject();
		}
	}

	@Subscribe()
	public void onEntryDepartment2SelectEvent(EntryDepartmentEvent event) {
		if(null!=event &&  event.getEventType().equals(QiConstant.ENTRY_DEPT2_SELECTED)) {
			entryDepartment2 = (String) event.getTargetObject();
		}
	}

	@Subscribe()
	public void onAssociateSelectEvent(EntryDepartmentEvent event) {
		if(null!=event &&  event.getEventType().equals(QiConstant.ASSOCIATE_ID_SELECTED)) {
			selectedAssociateId = (String) event.getTargetObject();
		}
	}

	@Subscribe
	public void onProductEvent(ProductEvent event) {
		if(event != null) {
			if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_OK)) {
				getBaseProductIdField().setText(null);
				getProductController().getView().setTestFieldState(getBaseProductIdField(), TextFieldState.EDIT);
			} else if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_NG)) {
				getProductController().getView().setErrorMessage("Product: " + (String) event.getTargetObject() + " already scanned.", getBaseProductIdField());
			}
		}
	}
	
	@Subscribe
	public void onProductProcessEvent(ProductProcessEvent event) {
		if(event.getProductModel().getProductList() != null &&!event.getProductModel().getProductList().isEmpty()) {
			if(isAssociateIdSelected()) {
				BulkProductController bulkProductController = (BulkProductController) getProductController();
				//Add the defect status to the products for check if products processed from scrapped table
				List<ProductSearchResult> results = event.getProductModel().getProductList();
				List<BaseProduct> products = new ArrayList<BaseProduct>(results.size());
				if(results != null && results.size() > 0 ) {
					for(ProductSearchResult result : results) {
						BaseProduct product = result.getProduct();
						if(result.getDefectStatus() != null)
							product.setDefectStatus(result.getDefectStatus());					
						products.add(product);
					}	
					bulkProductController.startProduct(products);
					EventBusUtil.publish(new SessionEvent(SessionEventType.SESSION_START));
				}
			} 
		}
	}
	
	protected Division getDivision(String divisionId) {
		Division division = null;
		if(StringUtil.isNullOrEmpty(divisionId)) return null;
		division = divisionMap.get(StringUtils.trim(divisionId));
		if(division == null) {
			division = getProductController().getModel().findDivision(divisionId);
			divisionMap.put(StringUtils.trim(divisionId), division);
		}
		return division;
	}
	
	protected Line getLine(String lineId) {
		Line line = null;
		if(StringUtil.isNullOrEmpty(lineId)) return null;
		line = lineMap.get(StringUtils.trim(lineId));
		if(line == null) {
			line = getProductController().getModel().findLine(lineId);
			lineMap.put(StringUtils.trim(lineId), line);
		}
		return line;
	}
	
	protected ProcessPoint getProcessPoint(String processPointId) {
		ProcessPoint processPoint = null;
		if(StringUtil.isNullOrEmpty(processPointId)) return null;
		processPoint = processPointMap.get(StringUtils.trim(processPointId));
		if(processPoint == null) {
			processPoint = getProductController().getModel().findProcessPoint(processPointId);
			processPointMap.put(StringUtils.trim(processPointId), processPoint);
		}
		return processPoint;
	}
}
