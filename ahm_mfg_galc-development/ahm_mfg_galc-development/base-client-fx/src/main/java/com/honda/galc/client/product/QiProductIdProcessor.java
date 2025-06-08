package com.honda.galc.client.product;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.EntryDepartmentEvent;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.ElevatedLoginDialog;
import com.honda.galc.client.ui.ElevatedLoginResult;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.SessionEvent;
import com.honda.galc.client.ui.event.SessionEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiInspectionUtils;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qi.QiStationEntryDepartmentDao;
import com.honda.galc.dao.qi.QiStationUpcPartDao;
import com.honda.galc.dao.qi.QiStationWriteUpDepartmentDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.lcvinbom.PmqaDto;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.lcvinbom.VinBomService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.MultiLineHelper;
import com.honda.galc.util.ProductCheckUtil;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 * 
 * 
 * <h3>ProductIdProcessor Class description</h3>
 * <p> ProductIdProcessor description </p>
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
 * Mar 11, 2014
 *
 *
 */
public class QiProductIdProcessor extends ProductIdProcessor{

	private String entryDepartment;
	private String entryDepartment2;

	private String selectedAssociateId;
	protected String currentAppId = "";
	protected MultiLineHelper multiLineHelper = null;
	
	public QiProductIdProcessor(ProductController productController) {
		super(productController);
		currentAppId = this.getProductController().getProcessPointId();
		multiLineHelper = MultiLineHelper.getInstance(currentAppId);
	}

	public TextField getBaseProductIdField() {
		return getProductController().getView().getInputPane().getProductIdField();
	}

	@Override
	public boolean validateProduct() {
			
		// validate if all the required data is present to go to Defect entry
		// new process point to use, initialize to current station
		ProcessPoint newPP = getDao(ProcessPointDao.class).findById(getProductController().getModel().getApplicationContext().getApplicationId());
		//initialize process point id to current process point
		String processPointToUse = newPP == null ? currentAppId : newPP.getProcessPointId();
		//if multi-line and helper returns a valid process point, use that
		if(multiLineHelper.isMultiLine())  {
			newPP = multiLineHelper.getProcessPointToUse(product);
			if(newPP != null && !newPP.getProcessPointId().trim().equalsIgnoreCase(currentAppId))  {
				processPointToUse = newPP.getProcessPointId().trim();				
			}
			//if newPP is null, set it back to initialized value
			else newPP = getProductController().getModel().getProcessPoint();
		}
		
		//check if valid MBPN product
		if (product.getProductType().equals(ProductType.MBPN)) {
			if (!ServiceFactory.getDao(QiStationUpcPartDao.class).isValidMBPN(processPointToUse, product.getProductId())) {
				getProductController().getView().setErrorMessage("MBPN ID is not valid for this station. Please use NAQ Station Config - MBPN/UPC to setup.");
				return false;
			}
		}
		
		if(!checkAllPreRequisite(product, processPointToUse)){
			return false;
		}
		boolean isLineIdCheckEnabled = PropertyService.getPropertyBean(ProductCheckPropertyBean.class,processPointToUse).isLineIdCheckEnabled();
		boolean isMissingProcessPointCheck = PropertyService.getPropertyBean(ProductCheckPropertyBean.class,processPointToUse).isProductHistoryNotExistCheck();
		String trackingStatus = product.getTrackingStatus();
		String myLineName = trackingStatus;
		if (isLineIdCheckEnabled || isMissingProcessPointCheck) {
			StringBuilder missingHistoryMessage = new StringBuilder();
			boolean isMissingHistory = false;
			ProductCheckUtil productCheckUtil = new ProductCheckUtil();
			productCheckUtil.setProduct(product);
			productCheckUtil.setProcessPoint(newPP);
			if(isMissingProcessPointCheck)  {
				List<String> missingPPList = productCheckUtil.productHistoryNotExistCheck();
				if(missingPPList != null && !missingPPList.isEmpty())  {
					isMissingHistory = true;
					missingHistoryMessage.append("Product History does not exist for: ");
					Iterator<String> slit = missingPPList.listIterator();
					if(slit != null)  {
						if(slit.hasNext())  missingHistoryMessage.append(slit.next());
						while(slit.hasNext())  {
							missingHistoryMessage.append(",").append(slit.next());
						}
					}
				}								
			}
			if(getProductController().getModel().isTrainingMode())  {
				//If station is in Training Mode and VIN already shipped
				if(ProductCheckUtil.isProductShipped(product.getProductId()) 
				&& !showConfirmProductDialog("Product Already Shipped. Do you still want to go ahead?"))  {
					return false;
				}
				//If station is in Training Mode and VIN came from unexpected line
				else if(isLineIdCheckEnabled
						&& !ProductCheckUtil.isProductShipped(product.getProductId()) 
						&& productCheckUtil.invalidPreviousLineCheck() 
						&& !showConfirmProductDialog("Product came from an unexpected line. Do you still want to go ahead?"))  {
					return false;
				}
				else if(isMissingHistory
						&& !showConfirmProductDialog(missingHistoryMessage.toString() + " - Do you still want to go ahead?"))  {
					return false;
				}
			}
			else  {  //station not in training mode
				//from unexpected line
				if (isLineIdCheckEnabled && productCheckUtil.invalidPreviousLineCheck()) {
					if(StringUtils.isEmpty(trackingStatus)){
						getProductController().getView().setErrorMessage("Unexpected Tracking Status", getBaseProductIdField());	
						return false;
					}
					Line myLine=ServiceFactory.getDao(LineDao.class).findByKey(trackingStatus);
					if(myLine != null)   {
						myLineName=myLine.getLineName();
					}
					String msg="Product came from an unexpected line : "+ myLineName;
					getProductController().getView().setErrorMessage(msg, getBaseProductIdField());
					return false;
				}
				else if(isMissingHistory)  {
							getProductController().getView().setErrorMessage(missingHistoryMessage.toString(), getBaseProductIdField());
							return false;
				}				
			}
		}
		
		//check if its next expected product id
		String expectedProductId = getProductController().getModel().getExpectedProductId();
		String scannedProductId = getBaseProductIdField().getText();
		if(getProductController().getModel().getProperty().isCheckExpectedProductId() && !StringUtils.equalsIgnoreCase(expectedProductId, scannedProductId)) {

			String elevatedSecurityGroup = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointToUse).getElevatedSecurityGroup();
			boolean isPasswordRequired = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointToUse).isElevatedUserPasswordRequired();
			String reason = "Assembly sequence number out of sequence - network sign-on required for to set to new sequence number";

			ElevatedLoginResult elevatedLoginResult = ElevatedLoginDialog.login(ClientMainFx.getInstance().getStage(), reason, isPasswordRequired, elevatedSecurityGroup);
			
			if (elevatedLoginResult.isSuccessful()) {
				getProductController().getLogger().info("Elevated User (" + elevatedLoginResult.getUserId() + ") logged in successfully");
			} else {
				getProductController().getLogger().info("Elevated User ("  + elevatedLoginResult.getUserId() + ") failed to log in due to "
						+ elevatedLoginResult.getMessage());
				getProductController().toIdle();
				return false;
			}
		}
		
		String warningTrackingStatus = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointToUse).getWarningTrackingStatus();
        if(StringUtils.isNotBlank(warningTrackingStatus)){
			StringTokenizer st = new StringTokenizer(warningTrackingStatus,Delimiter.COMMA);
			
			String elevatedSecurityGroup = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointToUse).getWarningTrackingStatusElevatedSecurityGroup();
			boolean isPasswordRequired = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointToUse).getWarningTrackingStatusElevatedUserPasswordRequired();
			String reason = "Tracking Status "+trackingStatus+" - "+getDao(LineDao.class).findByKey(trackingStatus).getLineDescription()+
					" for Product ID : "+getBaseProductIdField().getText()+" is not expected. \nContinue processing this product?";
				
			while(st.hasMoreElements()){
				if(StringUtils.equalsIgnoreCase(st.nextToken().trim(), trackingStatus))
				{
					ElevatedLoginResult elevatedLoginResult = ElevatedLoginDialog.login(ClientMainFx.getInstance().getStage(), reason, isPasswordRequired, elevatedSecurityGroup);		
					if (elevatedLoginResult.isSuccessful()) {
						getProductController().getLogger().info("Elevated User (" + elevatedLoginResult.getUserId() + ") logged in successfully");
					} else {
						getProductController().getLogger().info("Elevated User ("  + elevatedLoginResult.getUserId() + ") failed to log in due to "
								+ elevatedLoginResult.getMessage());
						getProductController().toIdle();
						return false;
					}
				}					
			}
        }
        
		getProductController().startProduct(product);

		checkAndCreateLETDefect(product.getProductId());

		EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_INPUT_OK));
        EventBusUtil.publish(new SessionEvent(SessionEventType.SESSION_START));
		return true;
	}
	private void checkAndCreateLETDefect(String inputNumber) {
		QiStationConfiguration qiEntryStationConfigManagement = 
				ServiceFactory.getDao(QiStationConfigurationDao.class)
					.findValueByProcessPointAndPropKey(getProductController().getProcessPointId(), 
							QiEntryStationConfigurationSettings.LET_DEFECT_CREATION.getSettingsName());
		
		// If the setting is configured to "YES", then we need to check for the LET defects and create 
		// the 1st occurring defect in the list and create a defect for it.
		if(qiEntryStationConfigManagement !=null && qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES)) {
			getProductController().getLogger().info("Creating LET Defect if any !");
			PmqaDto pmqaDto = ServiceFactory.getService(VinBomService.class)
					.retrievePmqaData(inputNumber);
			
			if(pmqaDto!=null && pmqaDto.getInspection_passing_flag()==1 
					&& pmqaDto.getDefect_items().size()>0) {
				//check whether the defect is already created for the VIN
				List<String> sortedDefectList = new ArrayList<String>();
				Collections.sort(pmqaDto.getDefect_items());
				sortedDefectList=pmqaDto.getDefect_items();
				String productType = getProductController().getModel().getProductType();
				String firstDefectTypeName = sortedDefectList.get(0).replace('_', ' ');
				
				if(getDao(QiDefectResultDao.class).checkLETDefectExists(inputNumber,firstDefectTypeName).size()>0) {
					getProductController().getLogger().info("1st LET Defect already created for the VIN.");
				}else {
					String processPointId = getProductController().getProcessPointId();
					String createUser = getProductController().getModel().getApplicationContext().getUserId();
					String entryDept = getProductController().getModel().getApplicationContext().getEntryDept();
					QiStationWriteUpDepartment writeUpDept = getDao(QiStationWriteUpDepartmentDao.class).findDefaultWriteUpDeptByProcessPoint(processPointId);
					String writeUpDeptId = writeUpDept.getId().getDivisionId();
					
					// sending the 1st sorted out defect from the defect list
					// create a defect for the above one
					
					boolean isGDPProcessPoint = QiInspectionUtils.isGdpProcessPoint(processPointId); 
					boolean isTrpuProcessPoint = QiInspectionUtils.isTrpuProcessPoint(processPointId); 
					boolean isGlobalGDPEnabled = QiInspectionUtils.isGlobalGdpEnabled() && QiInspectionUtils.isGlobalGdpWriteUpDept(writeUpDept.getId().getDivisionId());
					QiDefectResult qiDefectResult= createLETDefect(
								inputNumber,productType,firstDefectTypeName,processPointId,
								createUser,writeUpDeptId,entryDept,isGDPProcessPoint,isTrpuProcessPoint,isGlobalGDPEnabled
							);
					if(qiDefectResult==null) {
						getProductController().getLogger().info("Error creating LET Defect.");
						getProductController().getView().setErrorMessage(String.format("Error creating LET Defect for:%s",inputNumber), getBaseProductIdField());
						getProductController().getAudioManager().playNGSound();
					}else {
						getProductController().getLogger().info("1st LET Defect created for the VIN.");
					}
				}
			}
		}
		
	}
	
	private QiDefectResult createLETDefect(
			String productId,String productType, String defectTypeName,String processPoint,
			String createUser, String writeUpDept, String entryDept, boolean isGDPProcessPoint,
			boolean isTrpuProcessPoint,boolean isGlobalGDPEnabled) {
			try {
				return getDao(QiDefectResultDao.class).createLETDefect(productId, productType, defectTypeName, processPoint, createUser, writeUpDept, entryDept, isGDPProcessPoint, isTrpuProcessPoint, isGlobalGDPEnabled);		
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
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


	private boolean checkAllPreRequisite(BaseProduct product, String processPointId) {

		boolean isAllDataAvailable = false;
		if (isLegacyDefectEntered(product)) {
			return isAllDataAvailable;
		}
		boolean isProcessProduct = false;
		//When product is scrapped and Assign Actual Problem is Yes, process the scrapped product
		if(checkScrappedProduct(product, processPointId)) {
			if(getAssignActualProblemAfterScrapConfiguration())
				isProcessProduct = true;
		}			
		else isProcessProduct = true;
		
		if (			
				isProcessProduct
				&& isEntryDepartmentConfigured(processPointId)
				&& isWriteUpDepartmentConfigured(processPointId)
				&& isDefectStatusConfigured(processPointId)
				&& isAssociateIdSelected()) {
			isAllDataAvailable = true;
		}
		return isAllDataAvailable;
	
	}
	
	@Subscribe()
	public void onAssociateSelectEvent(EntryDepartmentEvent event) {
		if(null!=event &&  event.getEventType().equals(QiConstant.ASSOCIATE_ID_SELECTED)) {
			selectedAssociateId = (String) event.getTargetObject();
			if(!StringUtils.isBlank(getBaseProductIdField().getText()) && product != null){
				validateProduct();
			} else 
				getProductController().getView().getInputPane().getProductIdField().requestFocus();
		}
	}

	private boolean isAssociateIdSelected() {
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

	private boolean isLegacyDefectEntered(BaseProduct product) {
		boolean isCheckLegacyQics  = PropertyService.getPropertyBean(ApplicationPropertyBean.class).isQicsDefectEnteredCheck();
		if (isCheckLegacyQics&&!getProductController().getModel().isTrainingMode()) {
			int legacyCount = getDao(DefectResultDao.class).getLegacyRowCountByProductId(product.getProductId());
			if (legacyCount>0) {
				getProductController().getView().setErrorMessage("Unable to Enter Defect when Defects were entered in Legacy Qics");
				return true;
			}
		}
		return false;
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

	/**
	 * This method will be used to read station configuration for "Assign Actual Problem After Scrap" feature.
	 * <br>
	 * If no configuration present then it will read default value.
	 * 
	 */
	public boolean getAssignActualProblemAfterScrapConfiguration() {
		boolean isAssignActualProblemAfterScrap;		
		QiStationConfiguration entryStation = getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProductController().getProcessPointId(), "Assign Actual Problem After Scrap");
		if (entryStation != null) {
			isAssignActualProblemAfterScrap = "Yes".equalsIgnoreCase(entryStation.getPropertyValue()) ? true : false;
		} else {
			isAssignActualProblemAfterScrap = QiEntryStationConfigurationSettings.ASSIGN_ACTUAL_PROBLEM_AFTER_SCRAP.getDefaultPropertyValue()
					.equalsIgnoreCase("Yes") ? true : false;
		}
		return isAssignActualProblemAfterScrap;
	}

	private boolean checkScrappedProduct(BaseProduct product, String processPointId) {
		boolean isProductScrapped = false;
		List<QiDefectResult>  defectResults =  getDao(QiDefectResultDao.class).findAllCurrentDefectStatusByProductId(product.getProductId());
		String scrappedDefectDesp = "";
		for (QiDefectResult dto : defectResults) {
			if (DefectStatus.NON_REPAIRABLE.getId() == dto.getCurrentDefectStatus()) {
				isProductScrapped = true;
				scrappedDefectDesp = dto.getPartDefectDesc();
				break;
			}
		}

		if (isProductScrapped) {
			StringBuilder sb = new StringBuilder();
			sb.append("Product Already Scrapped : ").append(scrappedDefectDesp).append(" : ").append(processPointId);
			getProductController().getView().setErrorMessage(sb.toString(), getBaseProductIdField());
			return isProductScrapped;
		}
		return isProductScrapped;
	}

}