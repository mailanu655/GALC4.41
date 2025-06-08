package com.honda.galc.client.teamleader.qi.productRecovery;

import static com.honda.galc.service.ServiceFactory.getService;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.Text;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.enumtype.PartValidationStrategy;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.QiHeadlessDefectService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.PartCheckUtil;
import com.honda.galc.util.ProductResultUtil;
import com.honda.galc.util.SubproductUtil;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
/**
 * 
 * <h3>ManualLtCtrResultEnterViewManagerBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLtCtrResultEnterViewManagerBase description </p>
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
 * @author L&T Infotech
 * Aug 28, 2017
 *
 */
public class ManualLtCtrResultEnterViewManagerBase implements IManualLtCtrResultEnterViewManager{
	public static final String REPAIRED = "Repaired";
	private static final String HL_STATUS_COMPELTED = "COMPLETED";
	private static final String REBROADCAST_AT_REPAIR = "REBROADCAST_AT_REPAIR";
	private int torqueIndex = -1;
	private int stringValueIndex = -1;
	protected PartResult partResult;
	protected List<PartResult> resultList;
	private String authorizedUser;
	protected PartSpec partSpec;
	protected Text text =  new Text("");
	protected ApplicationContext appContext;
	protected Application application;
	protected ManualLtCtrResultDialog dialog;
	protected ManualLotControlRepairPanel parent;
	protected boolean partSnNg;
	protected boolean resetScreen;
	protected List<? extends ProductBuildResult> installedPartList;
	
	protected ManualLotControlRepairPropertyBean property;
	private ProductTypeData productTypeData;
	private ProductType productType;
	SubproductUtil subproductUtil;	
	protected BaseProduct product;
	
	
	protected String strategy = null;

	public ManualLtCtrResultEnterViewManagerBase(ApplicationContext appContext, Application application) {
		super();
		this.appContext = appContext;
		this.property = PropertyService.getPropertyBean(ManualLotControlRepairPropertyBean.class,application.getApplicationId());;
		this.application = application;
	}
	
	public ManualLtCtrResultEnterViewManagerBase(ApplicationContext applicationContext,Application application, ProductType currentProductType) {
		super();
		this.appContext = applicationContext;
		this.property = PropertyService.getPropertyBean(ManualLotControlRepairPropertyBean.class,application.getApplicationId());
		this.application = application;
		this.productType = currentProductType;
	}

	protected void validatePartSn() throws TaskException, Exception{
		validatePartSnLotControl();
		validatePartSnStrategy();
		if(getCurrentLotControlRule().isVerify()) validateSubProduct();
	}
	
	private void validateSubProduct() throws Exception {
		PartSerialNumber partnumber = new PartSerialNumber(dialog.getPartSnField().getText());
		LotControlRule rule = getCurrentLotControlRule();
		subproductUtil = new SubproductUtil(partnumber, getCurrentLotControlRule(),partSpec);

		if (subproductUtil.isPartSubproduct()) {
			BaseProduct subProduct = subproductUtil.findSubproduct();
			if (subproductUtil.findSubproduct() == null) {
				dialog.displayErrorMessage("Subproduct SN can not be found.", "Subproduct SN can not be found.");
				return;
			}

			if (!subproductUtil.isValidSpecCode(rule.getPartName().getSubProductType(), subProduct,appContext.getProductTypeData().getProductSpecCodeLabel())) {
				dialog.displayErrorMessage("Spec Code of part does not match expected Spec Code.", "Spec Code of part does not match expected Spec Code.");
				return;
			}

			SubproductPropertyBean subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, getCurrentLotControlRule().getId().getProcessPointId());
			try {
				String installProcessPoint;
				if(!subProductProperty.isUseMainNoFromPartSpec())
					installProcessPoint =subProductProperty.getInstallProcessPointMap().get(rule.getPartName().getSubProductType());
				else{
					installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getMainNo());
				}
				List<String> failedProductCheckList = subproductUtil.performSubProductChecks(rule.getPartName().getSubProductType(), subProduct, installProcessPoint);
				if(failedProductCheckList.size() > 0) {
					StringBuffer msg = new StringBuffer();
					msg.append(subProduct.getProductId() + " failed the following Product Checks : ");
					for (int i = 0; i < failedProductCheckList.size(); i++) {
						msg.append(failedProductCheckList.get(i));
						if (i != failedProductCheckList.size() - 1) {
							msg.append(", ");
						}
					}
					Logger.getLogger().info(msg.toString());
					throw new Exception (msg.toString());
				}
			} catch (Exception e) {
				throw new TaskException(e.getMessage());
			}
		}
	}

	private void validatePartSnStrategy() throws TaskException, Exception{
		try{
			strategy = getCurrentLotControlRule().getStrategy();
		}
		catch(Exception e){
			Logger.getLogger().info(e, "strategy is not defined");
		}
		if(!StringUtils.isEmpty(strategy)){
			Method m = null;
			try{
				m = PartCheckUtil.class.getMethod(getValidationMethodName(strategy), new Class[] { LotControlRule.class, String.class, ProductBuildResult.class });
			}
			catch(Exception e){
				Logger.getLogger().info(e, "validate method is not defined");
			}
			if(m != null){
				String productId = parent.getProductId();
				InstalledPart installedPart = createInstalledPart(null);
				m.invoke(null, getCurrentLotControlRule(), productId, installedPart);
			}
		}
	}
	
	private String getValidationMethodName(String strategyName) { 
		PartValidationStrategy validationStrategy = Enum.valueOf(PartValidationStrategy.class, strategyName); 
		return validationStrategy.getMethodName(); 
	} 
	
	public void confirmMeasurement() {

		try {
			dialog.clearDisplayMessage();
			double doubleToqueVlue = getDoubleTorqueValue(torqueIndex);
			checkTorqueValue(doubleToqueVlue);
			completeTorqueValueCheck();

		} catch(NumberFormatException ex) {
			dialog.getTorqueTextFieldList().get(torqueIndex).setStatus(false);
			String msg = "Invalid torque value. Please set a correct torque value.";
			dialog.displayErrorMessage(msg, msg);
		} catch (BaseException te) {
			dialog.getTorqueTextFieldList().get(torqueIndex).setStatus(false);
		} catch(Exception e){
			Logger.getLogger().error(e, "Error to confirm torque value.");
			dialog.getTorqueTextFieldList().get(torqueIndex).setStatus(false);
		}
	}

	private void completeTorqueValueCheck() {
		dialog.getTorqueTextFieldList().get(torqueIndex).setStatus(true);

		torqueIndex++;
		if(torqueIndex < getCurrentPartMeasurementCount()){
			dialog.getTorqueTextFieldList().get(torqueIndex).setText(new Text(StringUtils.EMPTY));
		}
		else {
			torqueIndex = 0;
			if(getPartSpec().getStringMeasurementSpecs().size() ==0){
				dialog.enableOperationButtons(true);
			}
			else
				prepareStringValueColletion();
		}
	}

	private void checkTorqueValue(double doubleToqueVlue) {
		MeasurementSpec measurementSpec = getCurrentMeasurementSpec(torqueIndex);
		if(measurementSpec == null) return;
		if(measurementSpec.getMaximumLimit() == 0 && measurementSpec.getMinimumLimit() == 0) return; 
		if(doubleToqueVlue > measurementSpec.getMaximumLimit() || doubleToqueVlue < measurementSpec.getMinimumLimit()){
			dialog.displayErrorMessage("Invalid torque value " + doubleToqueVlue + " Max:" + 
					measurementSpec.getMaximumLimit() + " Min:" + measurementSpec.getMinimumLimit(), "Invalid torque value " + doubleToqueVlue + " Max:" + 
							measurementSpec.getMaximumLimit() + " Min:" + measurementSpec.getMinimumLimit());
			throw new TaskException("Invalid torque value " + doubleToqueVlue + " Max:" + 
					measurementSpec.getMaximumLimit() + " Min:" + measurementSpec.getMinimumLimit());
		}
	}

	private MeasurementSpec getCurrentMeasurementSpec(int index) {
		if(getPartSpec().getNumberMeasurementSpecs() == null || getPartSpec().getNumberMeasurementSpecs().size() < (index+1)) return null;
		return getPartSpec().getNumberMeasurementSpecs().get(index);
	}

	private PartSpec getPartSpec() {
		if(partSpec == null && 
				getCurrentLotControlRule().getParts() != null && 
				getCurrentLotControlRule().getParts().size() > 0
		)
			partSpec = getCurrentLotControlRule().getParts().get(0);

		return partSpec;
	}

	private PartSpec getPartSpec(PartResult result) {
		return (result.getLotControlRule().getParts() != null && result.getLotControlRule().getParts().size() > 0) ?
			getCurrentLotControlRule().getParts().get(0) : null;
	}

	private double getDoubleTorqueValue(int index) {
		return new Double(dialog.getTorqueTextFieldList().get(index).getText().trim()).doubleValue();
	}

	public void saveUpdate() {
		try {
			dialog.clearDisplayMessage();
			LoggedButton saveBtn = dialog.getButtonSave();

			if ((isUserauthenticationNeededToSave()
					|| getCurrentLotControlRule().getPartName().isTlConfirm())
					&& !login()) {
				return;
			}

			if(property.isSaveConfirmation() && !confirmUpdate()) return;

			installedPartList = getCollectedBuildResult();
			doSaveUpdate();
			if(resultList == null) {
				doSaveUpdateSubproduct();
			}
			doBroadcast();
			parent.getController().loadProductBuildResultStatus(true);

			Stage stage = (Stage) saveBtn.getScene().getWindow();
			stage.close();

		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to save data into database:" + e.toString());
		}



	}

	private void doBroadcast() {
		String processPointId = getCurrentLotControlRule().getId().getProcessPointId();
		String broadcastDestinations = PropertyService.getProperty(processPointId, REBROADCAST_AT_REPAIR, "");
		
		if (broadcastDestinations != null && broadcastDestinations != "") {
			try {
				Logger.getLogger().info("Beginning broadcast from repair");
				
				List<String> broadcastDestinationList = java.util.Arrays.asList(broadcastDestinations.split(","));
				DataContainer dataContainer = new DefaultDataContainer();

				dataContainer.put(DataContainerTag.PRODUCT_ID, product.getProductId());
				dataContainer.put(DataContainerTag.PRODUCT_TYPE, product.getProductType().toString());
				dataContainer.put(DataContainerTag.PRODUCT, product);
				dataContainer.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());

				for(BroadcastDestination destination: getService(BroadcastDestinationDao.class).findAllByProcessPointId(processPointId, true)) {
					if (broadcastDestinationList.contains(destination.getDestinationId())) {
						Logger.getLogger().info(String.format("Broadcasting %s at %s", destination.getDestinationId(), processPointId));

						getService(BroadcastService.class).broadcast(destination, processPointId, dataContainer);
					}
				}

			} catch (Exception e) {
				
				Logger.getLogger().error(e, "Failed to broadcast from repair: " + e.toString());			
			} finally {
				Logger.getLogger().info("Completed broadcast from repair");
			}
		}
	}

	private void doSaveUpdateSubproduct() throws Exception {
		
		String subProductType = getCurrentLotControlRule().getPartName().getSubProductType();
		
		if (null != subProductType && !subProductType.equalsIgnoreCase("")){
			SubproductPropertyBean subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, getCurrentLotControlRule().getId().getProcessPointId());
			String installProcessPoint="";
			if(!subProductProperty.isUseMainNoFromPartSpec())
				installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getCurrentLotControlRule().getPartName().getSubProductType());
			else{
				installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getMainNo());
			}			
			subproductUtil.performSubproductTracking(subProductType, subproductUtil.findSubproduct(), installProcessPoint,"");
		}
	}

	@SuppressWarnings("unchecked")
	protected void doSaveUpdate() {
		//Generic method - Default for Engine, Frame and Knuckles
		ProductResultUtil.saveAll(appContext.getApplicationId(), (List<InstalledPart>)installedPartList);
		
		//check QI_DEFECT_FLAG from lot control (GAL246TBX)
		//if it is 1, repair NAQ defect
		if (partResult.getLotControlRule().isQicsDefect() && installedPartList.size() > 0) {
			List<String> productIdList = new ArrayList<String>();
			List<String> partNameList = new ArrayList<String>();
			for (InstalledPart installedPart : (List<InstalledPart>)installedPartList) {
				productIdList.add(installedPart.getProductId());
				partNameList.add(installedPart.getPartName());
			}
			
			List<Long> defectRefIds= ServiceFactory.getDao(InstalledPartDao.class).findDefectRefIds(productIdList, partNameList);
			
			List<QiRepairDefectDto> existingRepaired = new ArrayList<QiRepairDefectDto>();
			for (int i = 0; i < defectRefIds.size(); i++) {
				QiRepairDefectDto qiRepairDefectDto = new QiRepairDefectDto();
				qiRepairDefectDto.setExternalSystemKey(defectRefIds.get(i));
				qiRepairDefectDto.setAssociateId(getUser());
				qiRepairDefectDto.setExternalSystemName(QiExternalSystem.LOT_CONTROL.name());
				qiRepairDefectDto.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
				qiRepairDefectDto.setProcessPointId(this.application.getApplicationId());
				existingRepaired.add(qiRepairDefectDto);
			}
			
			if(!existingRepaired.isEmpty())  {
				try {
					ServiceFactory.getService(QiHeadlessDefectService.class).repairDefects(existingRepaired, true);
				} catch (Exception ex) {
					Logger.getLogger().error(ex, "Exception invoking QiHeadlessDefectServiceImpl");
				}		
			}
		}
		
		Logger.getLogger().info("Saved data into database by user:" + getUser() + 
				System.getProperty("line.separator") + installedPartList.get(0));
	}

	protected PartNameDao getPartNameDao() {
		return ServiceFactory.getDao(PartNameDao.class);
	}
	
	protected InstalledPartDao getInstalledPartDao() {
		return ServiceFactory.getDao(InstalledPartDao.class);
	}

	protected String getUser() {
		if(!isUserauthenticationNeededToSave()) return appContext.getUserId();
		else
			return authorizedUser == null ? appContext.getUserId() : authorizedUser;
	}

	protected boolean isUserauthenticationNeededToSave() {
		try {

			String clientStr = property.getClientsNeedAuthenticateUserToSave();
			if (StringUtils.isEmpty(clientStr)) 
				return false;

			String[] clients = clientStr.split(",");
			for (int i = 0; i < clients.length; i++) {
				if (appContext.getTerminal().getHostName().equals(clients[i].trim()))
					return true;
			}

		} catch (Exception e) {
			Logger.getLogger().error(e);
			return false;
		}
		return false;
	}


	public boolean login(){
		authorizedUser = null;
		if(LoginDialog.login() != com.honda.galc.enumtype.LoginStatus.OK) return false;

		if(!ClientMainFx.getInstance().getAccessControlManager().isAuthorized(getAuthorizationGroup() )) {
			MessageDialog.showError(dialog, "You are not authorized to save this result");
			return false;
		}

		authorizedUser = ClientMainFx.getInstance().getAccessControlManager().getUserName();
		Logger.getLogger().info("User:" + authorizedUser + " logged in.");
		return true;

	}


	private String getAuthorizationGroup() {
		return (getCurrentLotControlRule().getPartName().isTlConfirm()) ? 
				property.getTlConfirmAuthorizationGroup() : property.getAuthorizationGroup();
	
	}

	protected List<? extends ProductBuildResult> getCollectedBuildResult() {
		List<InstalledPart> list = new ArrayList<InstalledPart>();
		if(resultList == null) {
			InstalledPart installedPart = createInstalledPart(null);
			
			//restore defect_ref_id for part
			ProductBuildResult pbr = partResult.getBuildResult();
			if (pbr != null) {
				installedPart.setDefectRefId(((InstalledPart)pbr).getDefectRefId()); 
			}
			
			if(!partResult.isHeadLess() || !partResult.isQuickFix())
				getInputPartAndTorqueData(installedPart);

			partResult.setBuildResult(installedPart);
			list.add(installedPart);
		} else {
			for(PartResult result : resultList) {
				InstalledPart installedPart = createInstalledPart(result);
				result.setBuildResult(installedPart);
				list.add(installedPart);
			}
		}
		return list;
	}

	protected void getInputPartAndTorqueData(InstalledPart installedPart) {
		if (dialog.getPartSnField().isVisible())
			installedPart.setPartSerialNumber(dialog.getPartSnField().getText());
		
		if (getCurrentPartMeasurementCount() > 0)
			collectedMeasurementData(installedPart);
		
		if(getStrValueMeasurementSpecs() != null && getStrValueMeasurementSpecs().size() > 0)
			collectStringValueMeasurementData(installedPart);
	}

	protected InstalledPart createInstalledPart(PartResult result) {
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		id.setProductId(parent.getProductId());
		if(result==null){
			id.setPartName(getCurrentLotControlRule().getPartName().getPartName());
			installedPart.setPartId(getPartSpec() == null ? "" : getPartSpec().getId().getPartId());
		}
		else
		{
			id.setPartName(result.getPartName());
			installedPart.setPartId(getPartSpec(result) == null ? "" : getPartSpec(result).getId().getPartId());
		}
		installedPart.setId(id);
		installedPart.setInstalledPartReason(REPAIRED);
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		installedPart.setAssociateNo(getUser());
		installedPart.setPartSerialNumber(dialog.getPartSnField().getText());
		installedPart.setProductType(property.getProductType());
		return installedPart;
	}

	protected LotControlRule getCurrentLotControlRule() {
		return partResult.getLotControlRule();
	}


	private void collectedMeasurementData(InstalledPart installedPart) {
		
		List<Measurement> existingMeasurementList = null;
		if (partResult.getBuildResult() != null ) {
			existingMeasurementList = ((InstalledPart)partResult.getBuildResult()).getMeasurements();
		}
		
		for(int i = 0; i < getCurrentPartMeasurementCount(); i++){
			Measurement measurement = createMeasurement(existingMeasurementList, 
					installedPart, (getNumberMeasurementSpecs() != null && getNumberMeasurementSpecs().size() >=i) ? 
					getNumberMeasurementSpecs().get(i).getId().getMeasurementSeqNum() : i+1);
			measurement.setMeasurementValue(getDoubleTorqueValue(i));  
			installedPart.getMeasurements().add(measurement);  
		}
	}

	private void collectStringValueMeasurementData(InstalledPart installedPart) {

		if(getStrValueMeasurementSpecs() == null || getStrValueMeasurementSpecs().size() == 0) return;
		
		List<Measurement> existingMeasurementList = ((InstalledPart)partResult.getBuildResult()).getMeasurements();

		for(int i = 0; i < getStrValueMeasurementSpecs().size(); i++){

			Measurement measurement = createMeasurement(existingMeasurementList, installedPart, getStrValueMeasurementSpecs().get(i).getId().getMeasurementSeqNum());
			measurement.setMeasurementStringValue(dialog.getCurrentStringValueField(i).getText()); 
			measurement.setMeasurementName(getStrValueMeasurementSpecs().get(i).getMeasurementName()); 
			installedPart.getMeasurements().add(measurement.getId().getMeasurementSequenceNumber()-1,measurement);  
		}

	}
	
	private Measurement createMeasurement(List<Measurement> existingMeasurementList, InstalledPart installedPart, int seqNumber) {
		Measurement measurement = new Measurement();
		MeasurementId id = createMeasurementId(installedPart);
		id.setMeasurementSequenceNumber(seqNumber);
		measurement.setId(id);
		measurement.setMeasurementStatus(MeasurementStatus.OK);
		
		//restore defect_ref_id for measurement
		if (existingMeasurementList != null &&existingMeasurementList.size() >0) {
			for (int j = 0; j < existingMeasurementList.size(); j++) {
				Measurement existingMeasuremnt = existingMeasurementList.get(j);
				if (seqNumber == existingMeasuremnt.getId().getMeasurementSequenceNumber()) {
					measurement.setDefectRefId(existingMeasuremnt.getDefectRefId());
					break;
				}
			}
		}
		
		return measurement;
	}

	private MeasurementId createMeasurementId(InstalledPart installedPart) {
		MeasurementId id = new MeasurementId();
		id.setProductId(installedPart.getId().getProductId());
		id.setPartName(installedPart.getId().getPartName());
		return id;
	}

	protected boolean confirmUpdate() {
		if(property.getDefaultYesButtonToSave()){
			return MessageDialog.confirm(dialog, "Do You Really Want To Update Database ?", true);
		}
		else{
			return MessageDialog.confirm(dialog, "Do You Really Want To Update Database ?");
		}
	}
	
	public void showPartCommentConfirmDialog() {}

	public void confirmPartSn() {
		try {
			dialog.clearDisplayMessage();
			validatePartSn();
			dialog.getPartSnField().setStatus(true);
			partSnNg = false;

			if(getCurrentPartMeasurementCount() > 0)
				prepareForTorqueCollection();
			else{
				dialog.enableOperationButtons(true);
			}

		} catch (BaseException te) {
			Logger.getLogger().info(te.getMessage(), "Failed to confirm part serial number.");
			dialog.getPartSnField().setStatus(false);
			dialog.displayErrorMessage("Failed to confirm part serial number.", te.getMessage());
		} catch(Exception e){
			Logger.getLogger().error(e, "Error to confirm part serial number.");
			dialog.getPartSnField().setStatus(false);
			dialog.displayErrorMessage("Error to confirm part serial number.", e.getMessage());
		}
	}
	
	public void confirmStringValue() {
		try {
			validateStringValue();
			dialog.getStringValueFieldList().get(stringValueIndex).setStatus(true);

			stringValueIndex++;
			if(stringValueIndex < getStringValueCount()){
				dialog.getStringValueFieldList().get(stringValueIndex).setText(new Text(StringUtils.EMPTY));
			}
			else {
				stringValueIndex = 0;
				dialog.enableOperationButtons(true);
			}

		} catch (BaseException te) {
			Logger.getLogger().info(te.getMessage(), "Failed to confirm string value.");
			dialog.getStringValueFieldList().get(stringValueIndex).setStatus(false);
			dialog.displayErrorMessage("Failed to confirm string value.", te.getMessage());
		} catch(Exception e){
			Logger.getLogger().error(e, "Failed to confirm string value.");
			dialog.getStringValueFieldList().get(stringValueIndex).setStatus(false);
			dialog.displayErrorMessage("Failed to confirm string value.", e.getMessage());
		}
	}		
	

	private int getStringValueCount() {
		int retVal = 0;
		if(getPartSpec() != null && getPartSpec().getMeasurementSpecs() != null)  {
			retVal = getPartSpec().getStringMeasurementSpecs().size();
		}
		return retVal;
	}

	private void validateStringValue() {
		dialog.clearDisplayMessage();
		if(dialog.getStringValueFieldList().get(stringValueIndex).getText() == null){
			dialog.displayErrorMessage("Invalid input string.", "Invalid input string.");
			throw new TaskException("Invalid input string.");
		}
	}

	private void prepareForTorqueCollection() {
		torqueIndex = 0;
		dialog.enableTorqueFields(getCurrentPartMeasurementCount());

	}
	
	private void prepareStringValueColletion() {
		stringValueIndex = 0;
		dialog.enableStringValueFields(getStrValueMeasurementSpecs());
		
	}

	protected List<MeasurementSpec> getStrValueMeasurementSpecs() {
		return partResult.getStrValueMeasurements();
	}
	
	protected List<MeasurementSpec> getNumberMeasurementSpecs() {
		return partResult.getNumberMeasurements();
	}

	private int getCurrentPartMeasurementCount() {
		return partResult.getMeasurementCount();
	}

	protected void validatePartSnLotControl() {
		String partSn = dialog.getPartSnField().getText();
		if(getCurrentLotControlRule().isVerify()){
			partSpec = checkPartSnMask(partSn);
			if(partSpec == null) {
				String msg = "failed part serial number verificatino for :" + partSn + " part Masks:" + getPartSnMaskList();
				dialog.displayErrorMessage(msg, "Verification error " + getPartSnMaskList());
				throw new TaskException("Verification error " + getPartSnMaskList());
			} else {
				String parsePartSn = CommonPartUtility.parsePartSerialNumber(partSpec,partSn);
				if (!partSn.equals(parsePartSn)) {
					dialog.getPartSnField().setText(parsePartSn);
					partSn = parsePartSn;
				}
			}
		} else {
			if(getPartSepcs().size() > 0)
				partSpec = getPartSepcs().get(0);
			
		}

		if(getCurrentLotControlRule().isUnique())
			checkDuplicatePart(partSn);
	}
	
	private List<String> getPartSnMaskList() {
		List<String> masks = new ArrayList<String>();
		for(PartSpec spec : getPartSepcs())
			masks.add(CommonPartUtility.parsePartMask(spec.getPartSerialNumberMask()));

		return masks;
	}

	protected List<PartSpec> getPartSepcs() {
		if(getCurrentLotControlRule() == null || getCurrentLotControlRule().getPartName() == null)
		{
			dialog.displayErrorMessage("Exception: missing lot control rule or part name", "Exception: missing lot control rule or part name");
			throw new TaskException("Exception: missing lot control rule or part name");
		} else if(getCurrentLotControlRule().getParts() == null){
			dialog.displayErrorMessage("Exception: missing part spec.", "Exception: missing part spec.");
			throw new TaskException("Exception: missing part spec.");
		}

		return getCurrentLotControlRule().getParts();
	}

	private PartSpec checkPartSnMask(String partSn) {
		int validDays = PropertyService.getPropertyBean(SystemPropertyBean.class,getCurrentLotControlRule().getId().getProcessPointId()).getExpirationDays();
		return CommonPartUtility.verify(partSn, getPartSepcs(),PropertyService.getPartMaskWildcardFormat(),getCurrentLotControlRule().isDateScan(),validDays, product);	
	}

	protected void checkDuplicatePart(String partSn) {
		List<String> partNames = new ArrayList<String>();
		if(partSpec != null)
			partNames = LotControlPartUtil.getPartNamesOfSamePartNumber(partSpec);
		else
			partNames.add(getCurrentLotControlRule().getPartName().getPartName());
		List<InstalledPart> duplicateParts = getInstalledPartDao().findAllByPartNameAndSerialNumber(partNames, partSn);

		if (duplicateParts != null && !duplicateParts.isEmpty()) {
			final String errorMessage;
			java.util.Iterator<InstalledPart> duplicatePartIterator = duplicateParts.iterator();
			StringBuilder errorMessageBuilder = new StringBuilder("Duplicate part # with product");
			errorMessageBuilder.append(duplicateParts.size() > 1 ? "s: " : ": ");
			while (duplicatePartIterator.hasNext()) {
				InstalledPart duplicatePart = duplicatePartIterator.next();
				errorMessageBuilder.append(duplicatePart.getId().getProductId());
				if (duplicatePartIterator.hasNext()) {
					errorMessageBuilder.append(", ");
				}
			}
				errorMessage = errorMessageBuilder.toString();

				throw new TaskException(errorMessage);
		}

	}

	public void loadPartResult() {
		loadInstalledPartResult();
	}

	private void loadInstalledPartResult() {
		if(partResult.isHeadLess() && (partResult.isQuickFix() || partResult.isStatusOnly())){
			loadHeadLessResult();

		} else {
			loadHeadedResult();
		}
	}

	protected void loadHeadedResult() {

		dialog.getPartNameTextArea().setText(getPartNameLabel());// +"   "+partmask);

		if(getCurrentLotControlRule().getSerialNumberScanType() != PartSerialNumberScanType.NONE){
			dialog.setVisibleInstalledPart(true);
			if(partResult.getInstalledPart() != null && !partResult.getInstalledPart().getInstalledPartStatus().equals(InstalledPartStatus.REMOVED)){
				String partmask =  getInstaledPartSpec() != null? CommonPartUtility.parsePartMask(getInstaledPartSpec().getPartSerialNumberMask()):" ";
				dialog.getPartNameTextArea().setText(getPartNameLabel() +" "+partmask);
				text = new Text(partResult.getInstalledPart().getPartSerialNumber());
				dialog.getPartSnField().setText(text);

				if(getCurrentLotControlRule().isVerify()){
					if(!checkInstalledPartSerialNumber()){
						text = new Text(false);
						partSnNg = true;

						if(resetScreen) dialog.getPartSnField().setStatus(false);
						return;
					} 
				} 

				dialog.enableInstalledPart();
				if(!StringUtils.isBlank(partResult.getInstalledPart().getPartSerialNumber()))  {
					dialog.getPartSnField().setStatus(true);
					dialog.getPartSnField().setDisable(true);
				}


			} else {
				LotControlRule lotControlRule =getCurrentLotControlRule();
				String partmask = CommonPartUtility.parsePartMask(lotControlRule.getPartMasks());
				dialog.getPartNameTextArea().setText(getPartNameLabel() +" "+partmask);
				dialog.enableInstalledPart();
				partSnNg = true;
				return;
			}

		} 

		torqueIndex = 0;
		if(getCurrentPartMeasurementCount() > 0){
			if(partResult.getInstalledPart() != null){
				dialog.enableTorqueFields(getCurrentPartMeasurementCount());
				loadTorqueFields();

			} else {
				dialog.enableTorqueFields(getCurrentPartMeasurementCount());
			}
		}

		if(getStringValueCount() > 0){
			stringValueIndex = 0;
			dialog.enableStringValueFields(partResult.getStrValueMeasurements());
			if(partResult.getInstalledPart() != null)
				loadStringValueFields();
		}
	}
	
	private void loadStringValueFields() {
		for(int i = 0; i < partResult.getInstalledPart().getMeasurements().size(); i++){

			Measurement measurement = partResult.getInstalledPart().getMeasurements().get(i);
			if(StringUtils.isEmpty(measurement.getMeasurementName())) continue;
			dialog.getStringValueFieldList().get(i).setText(measurement.getMeasurementStringValue());

			if(dialog.getMeasurementNameList().get(i).getText().equals(measurement.getMeasurementName()))
				Logger.getLogger().error("Load string value:" + i + " MeasurementName:", 
						dialog.getMeasurementNameList().get(i).getText(), " MeasurementName from database:"
						,measurement.getMeasurementName() + measurement.getId().getMeasurementSequenceNumber(), " not equal.");

			if(!measurement.isStatus()){
				text = new Text(measurement.getMeasurementStringValue(), measurement.isStatus());
				stringValueIndex = i;
				return;
			}
		}

	}
	protected boolean checkInstalledPartSerialNumber() {
		PartSpec partSpec = getInstaledPartSpec();

		if(partSpec == null) return false;			

		if(CommonPartUtility.verification(partResult.getInstalledPart().getPartSerialNumber(), 
				partSpec.getPartSerialNumberMask(), PropertyService.getPartMaskWildcardFormat(), product)){
			dialog.getPartSnField().setStatus(true);
			partSnNg = false;
			return true;
		}else {
			dialog.getPartSnField().setStatus(false);
			text = new Text(false);
			partSnNg = true;
			return false;
		}


	}

	private void loadTorqueFields() {
		for(int i = 0; i < partResult.getInstalledPart().getMeasurements().size(); i++){

			Measurement measurement = partResult.getInstalledPart().getMeasurements().get(i);
			if(!StringUtils.isEmpty(measurement.getMeasurementName())) continue;
			DecimalFormat format = new DecimalFormat();
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);
			String measurementValue = format.format(measurement.getMeasurementValue());
			dialog.getTorqueTextFieldList().get(i).setText(measurementValue);
			dialog.getTorqueTextFieldList().get(i).setStatus(measurement.isStatus());

			if(!measurement.isStatus()){
				text = new Text(measurementValue, measurement.isStatus());
				torqueIndex = i;
				return;
			}
		}
	}

	private PartSpec getInstaledPartSpec() {
		InstalledPart installedPart = partResult.getInstalledPart();
		for(PartSpec spec : getPartSepcs()){
			if(spec.getId().getPartName().equals(installedPart.getId().getPartName()) &&
					spec.getId().getPartId().equals(installedPart.getPartId()))
				return spec;
		}

		Logger.getLogger().warn("Failed to find PartSpec for installed part part:" + 
				installedPart.getId().getPartName() + " partId:" + installedPart.getPartId());

		return null;
	}

	protected void loadHeadLessResult() {
		if(resultList != null && resultList.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(resultList.get(0).getPartName());
			for(int i = 1; i < resultList.size(); i++) {
				builder.append(System.getProperty("line.separator"));
				builder.append(resultList.get(i).getPartName());
			}
			dialog.getPartNameTextArea().setText(builder.toString());
		} else {
			dialog.getPartNameTextArea().setText(getPartNameLabel());
		}
		dialog.getHlCompletedField().setText(text);
		dialog.enableHeadLess(partResult.isStatusOnly());
	}
	
	protected String getPartNameLabel() {
		PartName partName = getCurrentLotControlRule().getPartName();
		return partName != null ? partName.getWindowLabel() : getCurrentLotControlRule().getPartNameString();
	}

	protected boolean hasComment() {
		if(partResult.getLotControlRule() == null 
				|| partResult.getLotControlRule().getParts() == null 
				|| partResult.getLotControlRule().getParts().size() ==0)
		return false;
		
		return !StringUtils.isEmpty(partResult.getLotControlRule().getParts().get(0).getComment());
	}

	public void locateFocus() {
		if (partSnNg) {
			((UpperCaseFieldBean)dialog.getPartSnField()).setText(text);
		} else if(torqueIndex > 0) {
			((UpperCaseFieldBean)dialog.getCurrentTorque(torqueIndex)).setText(text);
		} else if(stringValueIndex > 0){
			((UpperCaseFieldBean)dialog.getCurrentStringValueField(stringValueIndex)).setText(text);
		}

	}

	public void addDefaultTorqueValues() {
		try {
			dialog.clearDisplayMessage();
			for (int i = 0; i < getCurrentPartMeasurementCount(); i++) {
				String torqueValueStr = new DecimalFormat("0.00").format(getDefaultTorqueValue(i)).toString();
				Text text = new Text(torqueValueStr, true);
				dialog.getTorqueTextFieldList().get(i).setText(text);
			}

			torqueIndex = 0;
			if(getPartSpec().getStringMeasurementSpecs().size() == 0){
				dialog.enableOperationButtons(true);
			}
			else
				prepareStringValueColletion();

		} catch (Exception e) {
			handleException("An error occured in create Repair method Failed to Create Repair Method Name ", StringUtils.EMPTY, e);
		}
	}
   
    private Double getDefaultTorqueValue(int i) {
		MeasurementSpec currentMeasurementSpec = getCurrentMeasurementSpec(i);
		return (currentMeasurementSpec.getMaximumLimit() + currentMeasurementSpec.getMinimumLimit())/2;
	}
    
    protected int getInstalledPartStatus()
	{
		int iStatus = 0;
		iStatus = dialog.getHlCompletedField().getText().equalsIgnoreCase(HL_STATUS_COMPELTED) ? 1 : 0;
		return iStatus; 
	}

	 /**
     * Save result for Head Less
     *
     */
    public void setHlStatus() {
		dialog.getHlCompletedField().setText(new Text(HL_STATUS_COMPELTED, true));
		dialog.enableOperationButtons(true);

	
	}

	public ProductType getProductType() {
		if (productType == null) {
			productType = ProductType.valueOf(property.getProductType());
		}
		return productType;
	}	
		
	private String getMainNo(){
		return MbpnDef.MAIN_NO.getValue(partSpec.getPartNumber().trim());
	}

	@Override
	public void subScreenOpen(ManualLotControlRepairPanel view,
			List<PartResult> resultList) {
		if(resultList == null || resultList.size() == 0) {
			return;
		}
		this.partSpec = null;
		this.partResult = resultList.get(0);
		this.parent = view;
		this.resultList = resultList.size() > 1 ? resultList : null;
		product = ProductTypeUtil.getProductDao(getProductType()).findBySn(parent.getProductId());
		
		dialog = new ManualLtCtrResultDialog("Enter Result",product,resultList,parent,property,productTypeData,this);
		resetScreenData();
		loadPartResult();	
		dialog.showDialog();
	}
	
	public void resetScreenData() {
		text = new Text("");
		dialog.getHlCompletedField().setText(text);
		dialog.getPartSnField().setText(new Text(StringUtils.trimToEmpty("")));
		for(LengthFieldBean bean : dialog.getTorqueTextFieldList())
			bean.setText(new Text(StringUtils.trimToEmpty("")));
		for(LengthFieldBean bean : dialog.getStringValueFieldList())
			bean.setText(new Text(StringUtils.trimToEmpty("")));
		if (property.isDefaultTorqueValueButtonDisabled())  {
			dialog.setTorqueValueButtonEnabled(false);
		}
		dialog.init();
	}

	@Override
	public void subScreenClose(ActionEvent e) {
		LoggedButton cancelBtn = dialog.getButtonExit();
		Stage stage = (Stage) cancelBtn.getScene().getWindow();
		stage.close();		
	}
	
	/**
	 * This method is used to handle exceptions.
	 * @param loggerMsg
	 * @param errMsg
	 * @param parentScreen
	 * @param e
	 */
	public void handleException(String loggerMsg, String errMsg, Exception e) {
		Logger.getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.DIALOG_ERROR));
	}

	@Override
	public void resetScreen() {
		resetScreen = true;
		dialog.clearDisplayMessage();
		resetScreenData();
		loadInstalledPartResult();
	}
}
