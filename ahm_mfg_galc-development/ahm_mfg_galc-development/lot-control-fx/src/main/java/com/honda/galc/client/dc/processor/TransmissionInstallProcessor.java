package com.honda.galc.client.dc.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.dc.enumtype.InputValidators;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.TransmissionInstallPropertyBean;
import com.honda.galc.client.dc.validator.IPartSnInputValidator;
import com.honda.galc.client.dc.view.TransmissionInstallView;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;

public class TransmissionInstallProcessor extends OperationProcessor {

	private TransmissionInstallView view;
	private TransmissionInstallPropertyBean propertyBean;
	private ClientAudioManager audioManager;
	private String productId;
	public TransmissionInstallProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		productId = getController().getModel().getProductModel().getProductId().trim();
		setPropertyBean(PropertyService.getPropertyBean(TransmissionInstallPropertyBean.class, getController().getProcessPointId()));
		setAudioManager(new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class, getController().getProcessPointId())));
		
	}
	/**
	 * This method is used to check whether current engine is in the right status
	 * @return
	 */
	public boolean isProductPreviousLineValid(){
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(productId);
		productCheckUtil.setProduct(engine);
		productCheckUtil.setProcessPoint(ServiceFactory.getDao(ProcessPointDao.class).findByKey(getController().getProcessPointId().trim()));
		if (productCheckUtil.invalidPreviousLineCheck()) {
			String trackingStatus = engine.getTrackingStatus();
			Line line = ServiceFactory.getDao(LineDao.class).findByKey(trackingStatus);
			String lineName= line == null ? null : line.getLineName();
			String msg="Engine came from an unexpected line : "+lineName;
			getController().addErrorMessage(msg);
			getController().processMessages();
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Get already installed part 
	 * @return
	 */
	public InstalledPart getInstalledPart(){
		InstalledPart part = ServiceFactory.getDao(InstalledPartDao.class).findById(getController().getModel().getProductModel().getProductId(), propertyBean.getMissionAndTypeName());
		return part;
	}
	
	/**
	 * get all the measurements installed under the specified installedPart
	 */
	public Measurement getMeasurement(){
		List<Measurement> measurements = ServiceFactory.getDao(MeasurementDao.class).findAll(getController().getModel().getProductModel().getProductId(), propertyBean.getMissionAndTypeName());
		if(measurements != null && measurements.size() > 0){
			return measurements.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * This method is used to check whether entered mission and type is valid 
	 * and process the result 
	 * @param input
	 */
	public void processInputPart(TextField input){
		if(StringUtils.isBlank(input.getText())){
			getController().addErrorMessage("Part SN can not be empty");
			handleErrorInfo(input);
			return;
		}
		getController().clearMessages();
		String productId = getController().getProductModel().getProductId();
		MCOperationPartRevision partSpec = getOperation().getSelectedPart();
		PartSerialScanData ps = new PartSerialScanData();
		ps.setSerialNumber(input.getText());
		//get corresponding validator and do the validation
		IPartSnInputValidator partSnValidator = (IPartSnInputValidator) InputValidators.getValidator(ps);
		boolean isValid = partSnValidator.validate(productId, (PartSerialScanData) ps, partSpec);
		if(!isValid){
			getController().addErrorMessage(propertyBean.getMissionAndTypeName() + " is invalid.");
			handleErrorInfo(input);
		}else{
			TextFieldState.READ_ONLY.setState(getView().getMissionAndType());
			getAudioManager().playOKSound();
			getController().setFocusComponent(getView().getMissionTorque());
			getController().requestFocus();
			
		}
	}
	
	
	/**
	 * Check entered measurement value of mission and process the check result.
	 * @param input
	 */
	public void collectTorque(TextField input){
		if(StringUtils.isBlank(input.getText())){
			getController().addErrorMessage("Measurement can not be empty.");
			handleErrorInfo(input);
			return;
		}
		getController().clearMessages();
		String measurement = input.getText();
		MCOperationMeasurement mSpec = getOperation().getSelectedPart().getMeasurement(getController().getModel().getCurrentMeasurementIndex());
		double value = Double.parseDouble(measurement);
		if ((value < mSpec.getMinLimit()) || (mSpec.getMaxLimit() < value)){
			getController().addErrorMessage("Measurement value is not within range ("+mSpec.getMinLimit() +","+mSpec.getMaxLimit() + ")");
			handleErrorInfo(input);
		}else{
			TextFieldState.READ_ONLY.setState(getView().getMissionTorque());
			getAudioManager().playOKSound();
			getView().getDoneButton().setDisable(false);
			getController().setFocusComponent(getView().getDoneButton());
			getController().requestFocus();
		}
		
	}
	
	/**
	 * process the error messages 
	 * @param input
	 */
	private void handleErrorInfo(TextField input){
		getController().processMessages();
		input.selectAll();
		getController().setFocusComponent(input);
		getController().requestFocus();
		getAudioManager().playNGSound();
	}
	
	/**
	 * Prepare data that will be stored into dataBase
	 */
	public void saveData(){
		if(!getController().getModel().getCompletedOpsMap().containsKey(getController().getModel().getCurrentOperation())){
			List<InstalledPart> partList = new ArrayList<InstalledPart>();
			InstalledPart missionPart = prepareMissionPart();
			partList.add(missionPart);
			//save mission as installedPart
			ServiceFactory.getDao(InstalledPartDao.class).saveAll(partList);
			//update engine mission serial number and mission type
			saveEngineWithMissionData(missionPart, getController().getModel().getCurrentOperation());
		}
		completeOperation(false);
		
	}
	
	private InstalledPart prepareMissionPart(){
		List<Measurement> missionMeasList = new ArrayList<Measurement>();
		
		InstalledPart missionPart = new InstalledPart();
		
		InstalledPartId missionPartId = new InstalledPartId();
		missionPartId.setProductId(getController().getProductModel().getProductId());
		missionPartId.setPartName(propertyBean.getMissionAndTypeName());
		missionPart.setId(missionPartId);
		
		missionPart.setPartSerialNumber(getView().getMissionAndType().getText());
		missionPart.setProcessPointId(getController().getProcessPointId());
		missionPart.setPartIndex(1);
		missionPart.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
		missionPart.setValidPartSerialNumber(true);
		
		Measurement missionMeas = new Measurement();
		MeasurementId mmid = new MeasurementId();
		mmid.setProductId(getController().getProductModel().getProductId());
		mmid.setPartName(propertyBean.getMissionAndTypeName());
		mmid.setMeasurementSequenceNumber(1);
		missionMeas.setId(mmid);
		
		missionMeas.setMeasurementValue(Double.valueOf(getView().getMissionTorque().getText()).doubleValue());
		missionMeas.setMeasurementStatusId(MeasurementStatus.OK.getId());
		missionMeas.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		missionMeasList.add(missionMeas);
		missionPart.setMeasurements(missionMeasList);
		return missionPart;
	}
	
	/**
	 * get installed measurements of specified installedPart 
	 * @param partName installedPart name
	 * @return 
	 */
	public List<Measurement> getInstalledMeasurements(String partName){
		List<Measurement> measurementList = ServiceFactory.getDao(MeasurementDao.class).findAllOrderBySequence(getController().getProductModel().getProductId(), partName, true);
		return measurementList;
	}
	
	/**
	 * update engine with mission serial number and mission type
	 * @param preparedList
	 * @param structure
	 * @param partName
	 */
	private void saveEngineWithMissionData(InstalledPart mission, MCOperationRevision operation){
		
		if(mission != null && mission.getInstalledPartStatus() == InstalledPartStatus.OK){

			EngineSpecDao specDao = ServiceFactory.getDao(EngineSpecDao.class);
			EngineSpec engineSpec = specDao.findByKey(getController().getModel().getProductModel().getProduct().getProductSpecCode());
			
			String missionType = null;
			String missionSN = null;
			//get mission model type length from defined engineSpec
			int missionModelTypeLength = engineSpec.getMissionModelTypeCode() == null ? 0 : engineSpec.getMissionModelTypeCode().length();
			String missionPart = mission.getPartSerialNumber(); // get whole string of mission SN and mission type
			if(!StringUtils.isEmpty(missionPart)){
				missionType = missionPart.substring(missionPart.length() - missionModelTypeLength);// separate mission type
				missionSN = missionPart.substring(0, missionPart.length() - missionModelTypeLength); // separate mission SN
			}
			
			EngineDao dao = ServiceFactory.getDao(EngineDao.class);
			Engine engine = dao.findByKey(getController().getProductModel().getProductId());
			engine.setActualMissionType(missionType);
			engine.setMissionSerialNo(missionSN);
			engine.setMissionStatus(InstalledPartStatus.OK.getId());
			String ppId = ClientMainFx.getInstance().getApplicationContext().getApplicationId();
			engine.setLastPassingProcessPointId(ppId);
			dao.update(engine); //update current engine with its installed mission information.
			
			Logger.getLogger().info("updated engne:", engine.getId()," missionType:", missionType,
				" mission:", missionSN, " missionStatus:" + engine.getMissionStatus());
		}	
	}
	
	public InstalledPart getInstalledpart(String productId, String partName){
		return ServiceFactory.getDao(InstalledPartDao.class).findById(productId, partName);
	}
	
	public TransmissionInstallView getView() {
		return view;
	}
	public void setView(TransmissionInstallView view) {
		this.view = view;
	}

	public void setPropertyBean(TransmissionInstallPropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}

	public String getProductId() {
		return productId;
	}

	public TransmissionInstallPropertyBean getPropertyBean() {
		return propertyBean;
	}

	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

	public void setAudioManager(ClientAudioManager audioManager) {
		this.audioManager = audioManager;
	}

}
