/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.util.EngineLoadUtility;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.EngineLoadPropertyBean;
import com.honda.galc.client.datacollection.view.ViewManagerBase;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CommitReady;
import com.honda.galc.device.dataformat.EngineLoadSnValidation;
import com.honda.galc.device.dataformat.ManualOverride;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.TrackingPropertyBean;
import com.honda.galc.service.QicsService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.ProductSpecUtil;

/**
 * @author Subu Kathiresan
 * @date Dec 06, 2011
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date May 04, 2016
 */
public class EngineLoadSnProcessor extends PartSerialNumberProcessor implements DeviceListener, IPartSerialNumberProcessor {

	public static final int STATUS_OK = 1;
	public static final int STATUS_NG = 0;
	public static final int SHORT_AF_ON_SEQ_LENGTH = 3;
	public static final String MESSAGE_ID = "PART_SN"; //Part Serial Number validation failed.
	public static final String ASSIGN_NG = "ENGINE_NOT_ASSIGNED";
	public static final String MANUAL_OVERRIDE_STATUS_KEY = "MANUAL_OVERRIDE_ENABLED";
	public static final String MANIFEST_SUFFIX = "_MNF";
	public static final String BROADCAST_ARG = "ENGINE_ASSIGNMENT";
	
	protected FrameSpec frameSpec = null;
	protected PartSerialNumber partNumber;
	protected Engine engine = null;
	protected Frame frame = null;
	protected FrameDao frameDao = null;
	protected EngineDao engineDao = null;
	protected InProcessProductDao inProcessProductDao = null;
	protected EngineLoadPropertyBean engineLoadPropertyBean;
	protected Boolean isWaiting = false;
	protected EngineLoadAssignNewEngineDialog newEngDialog = null;
	protected HashMap<Integer,Boolean> buttonState;
	protected EngineLoadUtility engineUtil;
	
	public EngineLoadSnProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}

	public void init() {
		installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setMeasurements(new ArrayList<Measurement>());
		processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		this.enableWaitState(false);
		this.registerEiDeviceListener();
		this.getEngineLoadUtility();
	}
	
	public synchronized boolean execute(PartSerialNumber partNumber) {
		Logger.getLogger().info("EngineLoadSnProcessor: Enter execute()");
		
		if (this.getManualOverrideStatus()) return this.enableManualOverride(true);
		
		this.partNumber = partNumber;
		this.frame = (Frame)this.getController().getState().getProduct().getBaseProduct();
		
		try {
			Logger.getLogger().info("Process part:" + partNumber.getPartSn());
			
			if (!this.isWaiting && !validateScannedEngine(partNumber)) {
				this.sendEngineSnValidationDataToPlc(false, frame);
				Logger.getLogger().info("EngineLoadSnProcessor: Exit execute() ng");
				return false;
			} else if (confirmPartSerialNumber(partNumber)) {
				if (this.getEngineLoadPropertyBean().isPlcPersist() && !this.isWaiting) {
					this.enableWaitState(true);
					this.sendEngineSnValidationDataToPlc(true, frame);
					Logger.getLogger().info("EngineLoadSnProcessor: Exit execute() ok");
					return true;
				} else {
					if (!this.updateFrame() || !this.updateEngine()) {
						String errMsg = "Engine " + engine.getProductId() + " was not assigned to frame " + frame.getProductId();
						getController().getFsm().partSnNg(installedPart, ASSIGN_NG, errMsg);
						Logger.getLogger().error(errMsg);
						Logger.getLogger().info("EngineLoadSnProcessor: Exit execute() ng");
						return false;
					}
					processStopShip(frame, engine);
					getController().getFsm().partSnOk(installedPart);
					if (!this.getEngineLoadPropertyBean().isPlcPersist()) 
						this.sendEngineSnValidationDataToPlc(true, frame);
					Logger.getLogger().info("EngineLoadSnProcessor: Exit execute() ok");
					return true;
				}
			}
		} catch (Exception ex) {
			if (ex.getMessage() != null)
				Logger.getLogger().error(ex.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, MESSAGE_ID, ex.getMessage() == null ? "" : ex.getMessage());
		}
		Logger.getLogger().info("EngineLoadSnProcessor: Exit execute() ng");
		return false;
	}

	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		if(partnumber == null || partnumber.getPartSn() == null)
			return false;

		installedPart.setPartSerialNumber(partnumber.getPartSn());
		installedPart.setValidPartSerialNumber(true);
		installedPart.setPartId(getController().getCurrentLotControlRule().getParts().get(0).getId().getPartId());
		return true;
	}
	
	protected boolean updateFrame() {
		try {
			frame.setEngineSerialNo(engine.getProductId());
			frame.setEngineStatus(STATUS_OK);
			frame.setMissionSerialNo(engine.getMissionSerialNo());
			frame.setActualMissionType(engine.getActualMissionType());
			getFrameDao().update(frame);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("PartSerialNumberProcessor: Could not save frame " + frame.getProductId() + " with engine " + engine.getProductId());
		}
		return false;
	}
	
	private boolean updateEngine() {
		try {
			engine.setVin(frame.getProductId());
			if (StringUtils.isNotBlank(engine.getMissionSerialNo()))
				engine.setMissionStatus(STATUS_OK);
			this.updateEngineSpec();
			this.getEngineDao().update(engine);
			this.trackEngine(engine);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("PartSerialNumberProcessor:: Could not save engine " + engine.getProductId() + " for vin " + frame.getProductId());
		}
		return false;
	}
	
	protected void trackEngine(Engine engine) {
		// Engine tracking 		
		String engPPID = this.getEngineLoadPropertyBean().getEngineTrackingPpid();
		if (StringUtils.isBlank(engPPID) || ServiceFactory.getDao(ProcessPointDao.class).findById(engPPID) == null) return;
		TrackingPropertyBean engTrackProp = PropertyService.getPropertyBean(TrackingPropertyBean.class, engPPID);
		getTrackingService().track(engine, engPPID);
		
		// Engine forwarding
		String engFwdPPID = engTrackProp.getTrackingProcessPointIdOnSuccess();
		if (StringUtils.isBlank(engFwdPPID) || ServiceFactory.getDao(ProcessPointDao.class).findById(engFwdPPID) == null) return;
		getTrackingService().track(engine, engFwdPPID);
	}
	
	protected void processStopShip(Frame frame, Engine engine) {
		String message = "";	
		String lastProcPointId = engine.getLastPassingProcessPointId();
		ProcessPoint procPoint = this.context.getAppContext().getProcessPoint();
		if (lastProcPointId != null && lastProcPointId.endsWith(MANIFEST_SUFFIX)) {
			message = "Partial manifest for engine " + engine.getId() + " created at Engine Load";
			if (getEngineLoadPropertyBean().isCreateVinHoldOnNewEngManifest()) createHold(frame, message);
			if (getEngineLoadPropertyBean().isCreateDefectOnNewEngManifest()) createDefect(frame, message); 
		} else if (this.getEngineLoadUtility().isEngineOnHold(engine.getId())) {
			message = "Engine " + engine.getId() + " on hold at Engine Load";
			if (getEngineLoadPropertyBean().isCreateVinHoldOnEngineHold()) createHold(frame, message);
			if (getEngineLoadPropertyBean().isCreateDefectOnEngineHold()) createDefect(frame, message);
		} else if (this.getEngineLoadUtility().isEngineOnExternalHold(engine, procPoint)) {
			message = "Engine " + engine.getId() + " on external hold at Engine Load";
			if (getEngineLoadPropertyBean().isCreateVinHoldOnExtEngineHold()) createHold(frame, message);
			if (getEngineLoadPropertyBean().isCreateDefectOnExtEngineHold()) createDefect(frame, message);
		}
	}
	
	protected void createHold(Frame frame, String message) {
		EngineLoadUtility.holdFrame(frame, this.context.getAppContext().getProcessPoint(),	message);
	}
	
	protected void createDefect(Frame frame, String message) {
		ProductBuildResult buildResult = ProductTypeUtil.FRAME.createBuildResult(
				frame.getProductId(), getEngineLoadPropertyBean().getEngineLoadDefectPartName());
		buildResult.setProcessPointId(this.context.getAppContext().getProcessPoint().getId());
		buildResult.setInstalledPartStatus(InstalledPartStatus.NG);
		buildResult.setDefectStatus(DefectStatus.OUTSTANDING.getId());
		buildResult.setQicsDefect(true);
		
		List<ProductBuildResult> buildResultList = new ArrayList<ProductBuildResult>();
		buildResultList.add(buildResult);
		
		Logger.getLogger().info("Creating NAQ defect for VIN " + frame.getId() + ". Reason: " + message);
		ServiceFactory.getService(QicsService.class).update(
				this.context.getAppContext().getProcessPoint().getId(), this.context.getProductType(), buildResultList);
		
		ServiceFactory.getDao(InstalledPartDao.class).remove(buildResult);
	}
	
	/**
	 * validate the scanned engine before assigning it to the frame
	 * 
	 * @param partNumber
	 */
	protected boolean validateScannedEngine(PartSerialNumber partNumber) {
		try {
			ProductTypeData productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(ProductType.ENGINE.toString());
			//engine SN format check
			if (!productTypeData.isNumberValid(partNumber.getPartSn())) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Invalid Product Id: " + partNumber.getPartSn() + ", length invalid.");
				return false;
			}
			
			//part mask validation
			this.checkPartSerialNumber(partNumber);
			
			engine = getEngineDao().findByKey(partNumber.getPartSn());
			Frame frame=getFrameDao().findByKey( getController().getState().getProduct().getProductId());
			
			if (!validateCurrentProduct(frame)) {
				return false;
			}
		
			//check Engine not found
			if (engine == null && this.getEngineLoadPropertyBean().isEngineCreationAllowed()) {
				this.context.getCurrentViewManager().getMessagePanel().clearErrorMessageArea();
				this.context.getCurrentViewManager().getView().getPartSerialNumber(0).setBackground(Color.BLUE);
				this.newEngDialog = new EngineLoadAssignNewEngineDialog(
					(Frame) getController().getState().getProduct().getBaseProduct(),
					partNumber.getPartSn(), MANIFEST_SUFFIX, context);
				this.newEngDialog.setLocationRelativeTo(null);
				this.newEngDialog.setModal(true);
				this.newEngDialog.setVisible(true);
				if (this.getManualOverrideStatus()) 
					return false;
				engine = this.newEngDialog.getEngine();
				if (engine != null)	partNumber.setPartSn(engine.getId());
			}
			
			if (engine == null) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Engine not found: " + partNumber.getPartSn());
				return false;
			}
		
			//check Engine already assigned
			if (engine.getVin() != null && !engine.getVin().trim().equals("")) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Engine " + engine.getId() + " already assigned to Frame " + engine.getVin().trim());
				return false;
			}
		
			//check Engine type mismatch			
			ProductCheckUtil checkUtil = new ProductCheckUtil(frame, processPointDao.findByKey(context.getProcessPointId()));	
			boolean useAltEngineMto=PropertyService.getPropertyBean(ProductCheckPropertyBean.class, context.getProcessPointId()).isUseAltEngineMto();
			boolean engineTypeCheck = checkUtil.checkEngineTypeForEngineAssignment(frame, engine,useAltEngineMto);
			if (!engineTypeCheck) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Engine type is incorrect. " );
				return false;
			}
			
			if (!this.getEngineLoadPropertyBean().isCreateVinHoldOnEngineHold() && !this.getEngineLoadPropertyBean().isCreateDefectOnEngineHold()) {
				if (this.getEngineLoadUtility().isEngineOnDepartmentHold(partNumber.getPartSn())) {
					getController().getFsm().partSnNg(installedPart, MESSAGE_ID, 
							"Engine " + partNumber.getPartSn() + " on hold. " +
							"Reason: " + this.getEngineLoadUtility().getHoldResult().getHoldReason() + ". " +
							"QSR ID: " + this.getEngineLoadUtility().getHoldResult().getQsrId());
					return false;
				}
			}
			
			if (!checkValidPreviousEngineLine()) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Engine " + partNumber + " is from an invalid Line " + engine.getTrackingStatus());
				return false;
			}
			
			if (!executeInstalledProductChecks()) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Engine " + partNumber + " did not pass all checks.");
				return false;
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			String message = partNumber + " " + (ex.getMessage() == null ? "" : ex.getMessage());
			getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Engine could not be verified: " + message);
			Logger.getLogger().error("Could not check Scanned Engine: " + message);
			return false;
		}
		return true;
	}
	
	/**
	 * update the Engine product spec with expected MTO used in Engine
	 * uncommanization process.
	 * 
	 */

	public void updateEngineSpec() {
		if(this.getEngineLoadPropertyBean().isEngineMtoUpdateEnabled()){
			this.getEngineLoadUtility().updateEngineSpec(getController().getState().getProduct().getProductId(),engine);
		}
	}
	
	protected boolean validateCurrentProduct(Frame frame) {
		
		
		if (frame.getEngineSerialNo() != null && !frame.getEngineSerialNo().trim().equals("")) {
			getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Frame " + frame.getProductId() + " already assigned to Engine " + frame.getEngineSerialNo().trim());
			return false;
		}
		
		String bodyAssemblyLine = ServiceFactory.getDao(ProcessPointDao.class).findByKey(getFrameLinePropertyBean().getAfOnProcessPointId()).getLineId();
		
		if (frame.getTrackingStatus() == null || !frame.getTrackingStatus().equals(bodyAssemblyLine)) {
			getController().getFsm().partSnNg(installedPart, MESSAGE_ID, "Product: " + frame.getProductId() + " is from an invalid Line " + frame.getTrackingStatus());
			return false;
		}
		return true;
	}

	/**
	 * checks if the engine tracking status is valid
	 * 
	 * @return
	 */
	private boolean checkValidPreviousEngineLine() {
		String validPreviousLines = "";
		EngineLoadPropertyBean propertyBean;
		
		try {
			propertyBean = this.getEngineLoadPropertyBean();
		} catch(Exception ex) {
			// can't perform the validation if the property is not accessible, so skip validation
			Logger.getLogger().warn("Could not retrieve EngineLoadPropertyBean: skipping checkValidPreviousEngineLine() " + (ex.getMessage() == null ? "" : ex.getMessage()));					
			return true;
		}
		
		// return true if no previous lines are specified
		if (propertyBean.getValidEngineLoadPreviousLines() == null || 
				propertyBean.getValidEngineLoadPreviousLines().trim().equals(""))
			return true;
		
		// if previous lines are specified in the property, perform the check
		validPreviousLines = propertyBean.getValidEngineLoadPreviousLines();
		boolean isValidPreviousLine = false;
		
		for(String validLine: validPreviousLines.split(",")) {
			if (engine.getTrackingStatus().equals(validLine)) {
				isValidPreviousLine = true;
				break;
			}
		}
		return isValidPreviousLine;
	}
	
	/**
	 * returns the correct FrameSpec for the current product
	 * @return
	 */
	protected FrameSpec getFrameSpec() {
		String productSpecCode = getController().getState().getProductSpecCode();
		if (frameSpec == null || !frameSpec.getProductSpecCode().equals(productSpecCode)) {
			frameSpec = context.getFrameSpec(productSpecCode);
		}
		return frameSpec;
	}
	
	public FrameDao getFrameDao() {
		if (frameDao == null) {
			frameDao = ServiceFactory.getDao(FrameDao.class);
		}
		return frameDao;
	}
	
	public EngineDao getEngineDao() {
		if (engineDao == null) {
			engineDao = ServiceFactory.getDao(EngineDao.class);
		}
		return engineDao;
	}
	
	public InProcessProductDao getInProcessProductDao() {
		if (inProcessProductDao == null) {
			inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		}
		return inProcessProductDao;
	}
	
	public TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}
	
	private boolean executeInstalledProductChecks() {
		boolean result = true;
		String installedProductCheckPoint = getProductCheckPropertyBean().getInstalledProductLastCheckPoint();
		if (StringUtils.isEmpty(installedProductCheckPoint)) {
			installedProductCheckPoint = context.getProcessPointId();
		}
		String[] installedProductCheckTypes = getProductCheckPropertyBean().getInstalledProductCheckTypes();
		Map<String, Object> checkResults = ProductCheckUtil.check(engine,processPointDao.findByKey(installedProductCheckPoint),installedProductCheckTypes);
		List<String> productCheckTypesList = Arrays.asList(installedProductCheckTypes);
		List<String> failedProductCheckList = new ArrayList<String>();
		for (String checkType : productCheckTypesList) {
			if (checkResults.get(checkType) != null) {
				if (checkResults.get(checkType) instanceof Vector && ((Vector) (checkResults.get(checkType))).size() > 0
						|| checkResults.get(checkType) instanceof ArrayList && ((ArrayList) (checkResults.get(checkType))).size() > 0
						|| checkResults.get(checkType) instanceof Boolean && checkResults.get(checkType).equals(Boolean.FALSE)) {
					failedProductCheckList.add(checkType);
				}
			}
		}
		if (failedProductCheckList.size() > 0) {
			result = false;
			StringBuffer msg = new StringBuffer();
			msg.append(engine.getProductId() + " failed the following Product Checks : ");
			for (int i = 0; i < failedProductCheckList.size(); i++) {
				msg.append(failedProductCheckList.get(i));
				if (i != failedProductCheckList.size() - 1) {
					msg.append(", ");
				}
			}
			Logger.getLogger().info(msg.toString());
			MessageDialog.showInfo(context.getFrame(), msg.toString(),"Failed Product Checks");
		}
		return result;
	}

	protected ProductCheckPropertyBean getProductCheckPropertyBean() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class,context.getProcessPointId());
	}	
	
	private void sendEngineSnValidationDataToPlc(Boolean status, Frame frame) {
		Logger.getLogger().info("Sending EngineLoadSnProcessor data to PLC");
		
		EngineLoadSnValidation snValidation = new EngineLoadSnValidation();
		snValidation.setProductId(frame.getProductId());
		snValidation.setShortAfOnSequenceNumber(this.getShortAfOnSequenceNumber());
		snValidation.setFrameYmtoc(frame.getProductSpecCode());
		snValidation.setFrameModelYearCode(ProductSpecUtil.extractModelYearCode(frame.getProductSpecCode()));
		snValidation.setFrameModelCode(ProductSpecUtil.extractModelCode(frame.getProductSpecCode()));
		snValidation.setFrameModelTypeCode(ProductSpecUtil.extractModelTypeCode(frame.getProductSpecCode()));
		snValidation.setExtColCode(ProductSpecUtil.extractExtColorCode(frame.getProductSpecCode()));
		snValidation.setIntColCode(ProductSpecUtil.extractIntColorCode(frame.getProductSpecCode()));
		snValidation.setEngineSerialNo(engine.getProductId());
		snValidation.setEngineFired(engine.getEngineFiringFlag());
		snValidation.setEngineMto(ProductSpecUtil.excludeToModelTypeCode(engine.getProductSpecCode()));
		snValidation.setIsEngineValid(status);
		
		try {
				DeviceManager.getInstance().getEiDevice().send(snValidation);
				Logger.getLogger().info("Sent EngineLoadSnProcessor data to PLC");
		} catch (Exception ex) {
				Logger.getLogger().info("Could not send EngineLoadSnProcessor data to PLC");
				return;
		}
	}
	
	private String getShortAfOnSequenceNumber() {
		int length;
		try {
			length = Integer.parseInt(getEngineLoadPropertyBean().getShortAfOnSeqLength());
		} catch (NumberFormatException e) {
			length = SHORT_AF_ON_SEQ_LENGTH;
		}
		
		Integer seqNum = frame.getShortAfOnSequenceNumber(length);
		if (seqNum == null) 
			return String.format("%" + length + "s", "");
		else 
			return String.format("%" + length + "s", seqNum.toString()).replace(' ', '0');
	}
	
	private EngineLoadPropertyBean getEngineLoadPropertyBean() {
		if (this.engineLoadPropertyBean == null)
			this.engineLoadPropertyBean = PropertyService.getPropertyBean(EngineLoadPropertyBean.class, context.getProcessPointId());
		return this.engineLoadPropertyBean;
	}
	
	private void setManualOverrideStatus(Boolean enable) {
		ComponentStatusId statusId = new ComponentStatusId();
		statusId.setComponentId(this.context.getAppContext().getApplicationId());
		statusId.setStatusKey(MANUAL_OVERRIDE_STATUS_KEY);
		if (enable) {
			ComponentStatus status = new ComponentStatus();
			status.setId(statusId);
			status.setStatusValue(Boolean.TRUE.toString());
			status.setChangeUserId(this.context.getUserId());
			ServiceFactory.getDao(ComponentStatusDao.class).save(status);
		} else {
			ServiceFactory.getDao(ComponentStatusDao.class).removeByKey(statusId);
		}
	}
	
	private Boolean getManualOverrideStatus() {
		Boolean statusValue = false;
		ComponentStatusId statusId = new ComponentStatusId();
		statusId.setComponentId(this.context.getAppContext().getApplicationId());
		statusId.setStatusKey(MANUAL_OVERRIDE_STATUS_KEY);
		ComponentStatus status = ServiceFactory.getDao(ComponentStatusDao.class).findByKey(statusId);
		if (status != null)
			statusValue = Boolean.valueOf(status.getStatusValue());
		return statusValue;
	}
	
	private boolean enableManualOverride(Boolean b) {	
		if(this.newEngDialog != null && this.newEngDialog.isVisible())
			this.newEngDialog.setVisible(false);
		this.isWaiting = false;
		String message = "Manual override";
		this.context.getCurrentViewManager().getView().getTextFieldProdId().setEnabled(!b);
		this.context.getCurrentViewManager().getView().getPartSerialNumber(0).setEnabled(!b);	
		if (b) {
			this.context.getCurrentViewManager().getView().getPartSerialNumber(0).setBackground(Color.RED);
			this.context.getCurrentViewManager().getView().getPartSerialNumber(0).setText(message);
			this.context.getCurrentViewManager().getView().getPartSerialNumber(0).setDisabledTextColor(Color.WHITE);
			this.context.getCurrentViewManager().getMessagePanel().setMessage(message + " enabled");
			this.setButtonState();
			for (JButton button : this.getButtonList())	button.setEnabled(false);
		} else {
			this.context.getCurrentViewManager().getView().getPartSerialNumber(0).setBackground(Color.BLUE);
			this.context.getCurrentViewManager().getMessagePanel().clearErrorMessageArea();
			this.context.getCurrentViewManager().getView().getPartSerialNumber(0).setDisabledTextColor(Color.BLACK);
			this.context.getCurrentViewManager().getView().getPartSerialNumber(0).setText("");
			this.context.getCurrentViewManager().getView().getPartSerialNumber(0).grabFocus();
			for (int i = 0 ; i < this.getButtonList().size(); i++) {
				this.getButtonList().get(i).setEnabled(this.getButtonState().get(i));
			}
		}
		this.context.getCurrentViewManager().getView().repaint();
		Logger.getLogger().info("EngineLoadSnProcessor: Exit execute() " + message + (b?" enabled":" disabled")); 
		return b;
	}
	
	private void enableWaitState(Boolean b) {
		this.isWaiting = b;
		if (b) {
			ViewManagerBase viewManager = this.context.getCurrentViewManager();
			viewManager.getView().getPartSerialNumber(0).setBackground(Color.GREEN);
			viewManager.getView().getPartSerialNumber(0).setText(this.engine.getId());
			viewManager.getView().getPartSerialNumber(0).setEnabled(false);
			viewManager.getMessagePanel().setWarningMessageArea("Processing. Please wait.");
		}
	}
	
	private List<JButton> getButtonList(){
		return this.context.getCurrentViewManager().getView().getButtonList();
	}
	
	private void setButtonState() {
		this.buttonState = new HashMap<Integer,Boolean>();
		List<JButton> buttons = this.getButtonList();
		for (int i = 0; i < buttons.size(); i++) {
			this.buttonState.put(i, buttons.get(i).isEnabled());
		}
	}
	
	private HashMap<Integer, Boolean> getButtonState(){
		return this.buttonState;
	}
	
	private void registerEiDeviceListener() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null && eiDevice.isEnabled()){
			eiDevice.registerDeviceListener(this, getDeviceInputDataList());
		}
	}
	
	private List<IDeviceData> getDeviceInputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new CommitReady());
		list.add(new ManualOverride());
		return list;
	}
	
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		Logger.getLogger().info("PLC signal received");
		if (deviceData instanceof ManualOverride) {
			ManualOverride data = (ManualOverride) deviceData;
			setManualOverrideStatus(data.getValue());
			enableManualOverride(data.getValue());	
		} else if ( deviceData instanceof CommitReady &&
					this.getEngineLoadPropertyBean().isPlcPersist() &&
					this.isWaiting) {
			CommitReady data = (CommitReady)deviceData;
			if (data.getValue()) {
				this.execute(this.partNumber);
			} else {
				this.getController().getFsm().cancel();
				this.getController().execute(this.context.getProcessPointId(), this.frame.getId());
			}
			this.enableWaitState(false);
		}
		return null;
	}
	
	private EngineLoadUtility getEngineLoadUtility() {
		if (this.engineUtil == null) this.engineUtil = new EngineLoadUtility();
		return engineUtil;
	}
}