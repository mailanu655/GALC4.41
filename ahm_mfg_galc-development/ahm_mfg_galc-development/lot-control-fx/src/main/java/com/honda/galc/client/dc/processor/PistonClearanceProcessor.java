package com.honda.galc.client.dc.processor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextField;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.PistonClearancePropertyBean;
import com.honda.galc.client.dc.validator.PistonClearanceValidator;
import com.honda.galc.client.dc.view.PistonClearanceView;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPart;
import com.honda.galc.entity.conf.MCOperationPartId;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
/*   
* @author Jiamei Li<br>
* Jul 9, 2014
*
*/
public class PistonClearanceProcessor extends OperationProcessor implements IOperationProcessor{

	private PistonClearanceView view;
	private Map<Integer, Double> pistonMeasValues;
	private Map<Integer, Double> clearanceMeasValues;
	private PistonClearancePropertyBean propertyBean;
	private String productId;
	private ClientAudioManager audioManager;
	private PistonClearanceValidator validator;
	private boolean isEditable = false;
	private List<MCOperationMeasurement> clearanceMeasList;
	private int inProcessIndex;
	private boolean isCompleted = false;
	public PistonClearanceProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		pistonMeasValues = new HashMap<Integer, Double>();
		clearanceMeasValues = new HashMap<Integer, Double>();
		productId = getController().getModel().getProductModel().getProductId().trim();
		setPropertyBean(PropertyService.getPropertyBean(PistonClearancePropertyBean.class, getController().getProcessPointId()));
		setAudioManager(new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class, getController().getProcessPointId())));
		clearanceMeasList = getClearanceMCOperationMeasurementList();
	}
	
	/**
	 * check whether cylinder bore has already been installed before piston installation.
	 * 
	 */
	public boolean isCylinderBoreInstalled(){
		InstalledPart bore = getInstalledpart(getProductId(),propertyBean.getInstalledCylinderBoreName());
		List<Measurement> measurements = getMeasurements(getProductId(), propertyBean.getInstalledCylinderBoreName());
		if(bore != null && measurements != null && measurements.size() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * get installed measurements of specified installedPart 
	 * @param partName installedPart name
	 * @return 
	 */
	public List<Measurement> getInstalledMeasurements(String partName){
		List<Measurement> measurementList = ServiceFactory.getDao(MeasurementDao.class).findAllOrderBySequence(getProductId(), partName, true);
		return measurementList;
	}
	
	/**
	 * process entered piston measurements and clearance value
	 * 1. piston measurement value must be less than its corresponding bore measurement value.
	 * 2. clearance = bore - piston, clearance is equal or greater than  0.125 but equal or less than 0.175. 
	 * clearance validation is executed in validator
	 * @param input
	 */
	public void processInputValue(TextField input){
		getController().clearMessages();
		getValidator().validate(input);
		if(getController().isErrorExists()){
			handleWithErrorInfo(input);
		}else{
			Integer key = getKey(getView().getPistonMeasurements(), input);
			int index = 0;
			if(key != null){
				index = key.intValue();
			}
			this.inProcessIndex = index + 1;
			double pistonValue = Double.valueOf(input.getText()).doubleValue();
			double boreValue = 0;
			//get corresponding bore value
			if(index < getView().getBoreMeasurements().size()){ 
				boreValue = Double.valueOf(getView().getBoreMeasurements().get(index)).doubleValue();
			}
			//calculate clearance value, and show the value as float.
			float clearance = getClearanceValue(boreValue, pistonValue);
			getController().clearMessage();
			//check if piston entry is equal or greater than bore size
			if(clearance <= 0){
				getController().addErrorMessage("Piston size can not be equal or greater than Bore size.");
				handleWithErrorInfo(input);
				TextFieldState.ERROR.setState(getView().getPistonMeasurements().get(key));
				TextFieldState.READ_ONLY.setState(getView().getClearanceMeasurements().get(key));
				getView().getClearanceMeasurements().get(key).setText("");
				return;
			}
			getView().getClearanceMeasurements().get(index).setText(String.valueOf(clearance));
			getValidator().validate(getView().getClearanceMeasurements().get(index));//validate clearance range
			if(!getController().isErrorExists()){
				 pistonMeasValues.put(key, Double.valueOf(pistonValue));
				 clearanceMeasValues.put(key, Double.valueOf(clearance));
				if(index < getView().getBoreMeasurements().size() - 1){
					if(isPistonCompleted()){
						focusDoneButton();
						return;
					}
					getController().setFocusComponent(getView().getPistonMeasurements().get(index + 1));
					getController().requestFocus();
				}else{
					if(isPistonCompleted()){
						focusDoneButton();
					}
				}
				getAudioManager().playOKSound();
			}else{
				getController().clearMessage();
				TextFieldState.EDIT.setState(getView().getPistonMeasurements().get(index));
				handleWithErrorInfo(getView().getPistonMeasurements().get(index));
			}
		}
	}
	
	private void handleWithErrorInfo(TextField input){
		getController().processMessages();
		input.selectAll();
		getController().setFocusComponent(input);
		getController().requestFocus();
		getAudioManager().playNGSound();
		if(this.isEditable) getView().getDoneButton().setDisable(true);//if error occurs in editable status, done button should be disabled
	}
	
	private void focusDoneButton(){
		getView().getDoneButton().setDisable(false);
		getController().setFocusComponent(getView().getDoneButton());
		getController().requestFocus();
	}
	public boolean isPistonCompleted(){
		if(this.pistonMeasValues.size() < getView().getBoreMeasurements().size()){
			return false;
		}
		return true;
	}
	public void changeToEditableState(){
		if(getView().getPistonMeasurements() != null && getView().getPistonMeasurements().size() > 0){
			for(TextField tf : getView().getPistonMeasurements().values()){
				TextFieldState.EDIT.setState(tf);
			}
		}
		this.isEditable = true;
		getController().setFocusComponent(getView().getPistonMeasurements().get(0));//set default focus to the first piston textField
		getController().requestFocus();
	}
	
	/**
	 * calculate clearance value 
	 * clearance = boreValue - pistonValue
	 * @param boreValue
	 * @param pistonValue
	 * @return  float value 
	 */
	private float getClearanceValue(double boreValue, double pistonValue){
		BigDecimal bdPiston = new BigDecimal(pistonValue);
		BigDecimal bdBore = new BigDecimal(boreValue);
		
		return bdBore.subtract(bdPiston).floatValue();
	}
	
	/**
	 * get maximum measurement value
	 * @return
	 */
	public Double getMaxMeasurementValue(){
		if(this.getClearanceMeasList() == null && this.getClearanceMeasList().size() ==0 ){
			return null;
		}
		for(MCOperationMeasurement meas : this.getClearanceMeasList()){
			if(meas.getId().getMeasurementSeqNum() == this.inProcessIndex){
				return meas.getMaxLimit();
			}
		}
		return null;
	}
	
	/**
	 * get minimum measurement value
	 * @return
	 */
	public Double getMinMeasurementValue(){
		if(this.getClearanceMeasList() == null && this.getClearanceMeasList().size() ==0 ){
			return null;
		}
		for(MCOperationMeasurement meas : this.getClearanceMeasList()){
			if(meas.getId().getMeasurementSeqNum() == this.inProcessIndex){
				return meas.getMinLimit();
			}
		}
		return null;
	}
	
	private List<MCOperationMeasurement> getClearanceMCOperationMeasurementList(){
		String operationName = getController().getModel().getCurrentOperation().getId().getOperationName();
		String partId = getController().getModel().getCurrentOperation().getSelectedPart().getId().getPartId();
		MCOperationPartId id = new MCOperationPartId();
		id.setOperationName(operationName);
		id.setPartId(partId);
		MCOperationPart part = ServiceFactory.getDao(MCOperationPartDao.class).findByKey(id);
		int partRev = part == null ? 0 : part.getPartRevision();
		
		return ServiceFactory.getDao(MCOperationMeasurementDao.class).findAllMeasurementForOperationPartAndPartRevision(operationName, partId, partRev);
	}
	
	
	/**
	 * save piston and clearance measurements
	 * save operation as an installedPart
	 */
	public void saveData(){
		if(!getController().getModel().getCompletedOpsMap().containsKey(getController().getModel().getCurrentOperation())){
			List<InstalledPart> partList = new ArrayList<InstalledPart>();
			prepareOperationPart(partList);
			this.preparePiston(partList);
			this.prepareClearance(partList);
			//save installed part 'Piston sub assembly','PISTON','CLEARANCE' and measurements under 'PISTON' and  'CLEARANCE'
			ServiceFactory.getDao(InstalledPartDao.class).saveAll(partList);
			if(getView().getEditButton() != null){
				getView().getEditButton().setDisable(true);
			}
		}
		completeOperation(false);
	}
	
	/**
	 * prepare operation as an installedPart for saving
	 * @param partList
	 */
	private void prepareOperationPart(List<InstalledPart> partList){
		InstalledPart operationPart = new InstalledPart();
		InstalledPartId operationPartId = new InstalledPartId();
		operationPartId.setPartName(getController().getModel().getCurrentOperation().getId().getOperationName());
		operationPartId.setProductId(getController().getProductModel().getProductId());
		operationPart.setId(operationPartId);
		operationPart.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
		operationPart.setProcessPointId(getController().getProcessPointId());
		operationPart.setValidPartSerialNumber(true);
		partList.add(operationPart);
	}
	
	/**
	 * prepare piston installePpart and measurement
	 * @param partList
	 */
	private void preparePiston(List<InstalledPart> partList){
		List<Measurement> pistonMeasList = new ArrayList<Measurement>();
		
		InstalledPart piston = new InstalledPart();
		
		InstalledPartId pistonId = new InstalledPartId();
		pistonId.setProductId(getProductId());
		pistonId.setPartName(propertyBean.getPistonName());
		piston.setId(pistonId);
		
		piston.setProcessPointId(getController().getProcessPointId());
		piston.setPartIndex(1);
		piston.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
		piston.setValidPartSerialNumber(true);
		
		for(int i =0 ; i < pistonMeasValues.size(); i ++){
			Measurement pistonMeas = new Measurement();
			MeasurementId pmid = new MeasurementId();
			pmid.setProductId(getProductId());
			pmid.setPartName(propertyBean.getPistonName());
			pmid.setMeasurementSequenceNumber(i +1);
			pistonMeas.setId(pmid);
			
			pistonMeas.setMeasurementValue(pistonMeasValues.get(i).doubleValue());
			pistonMeas.setMeasurementStatusId(MeasurementStatus.OK.getId());
			pistonMeas.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			pistonMeasList.add(pistonMeas);
		}
		piston.setMeasurements(pistonMeasList);
		partList.add(piston);
	}
	
	/**
	 * prepare clearance installedPart and measurement
	 * @param partList
	 */
	private void prepareClearance(List<InstalledPart> partList){
		
		List<Measurement> clearanceList = new ArrayList<Measurement>();
		InstalledPart clearance = new InstalledPart();
		InstalledPartId clearanceId = new InstalledPartId();
		clearanceId.setProductId(getProductId());
		clearanceId.setPartName(propertyBean.getClearanceName());
		clearance.setId(clearanceId);
		
		clearance.setProcessPointId(getController().getProcessPointId());
		clearance.setPartIndex(1);
		clearance.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
		clearance.setValidPartSerialNumber(true);
		
		for(int index =0; index < this.clearanceMeasValues.size(); index ++){
			Measurement clearanceMeas = new Measurement();
			MeasurementId clMeasId = new MeasurementId();
			clMeasId.setProductId(getProductId());
			clMeasId.setPartName(propertyBean.getClearanceName());
			clMeasId.setMeasurementSequenceNumber(index + 1);
			clearanceMeas.setId(clMeasId);
			
			clearanceMeas.setMeasurementValue(clearanceMeasValues.get(index).doubleValue());
			clearanceMeas.setMeasurementStatusId(MeasurementStatus.OK.getId());
			clearanceMeas.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			clearanceList.add(clearanceMeas);
		}
		clearance.setMeasurements(clearanceList);
		
		partList.add(clearance);
	}
	public Integer getKey(Map<Integer, TextField> textFields,
			TextField textField) {
		if (textFields == null || textField == null) {
			return null;
		}
		for (Integer i : textFields.keySet()) {
			if (textField.equals(textFields.get(i))) {
				return i;
			}
		}
		return null;
	}
	
	public InstalledPart getInstalledpart(String productId, String partName){
		return ServiceFactory.getDao(InstalledPartDao.class).findById(productId, partName);
	}
	private List<Measurement> getMeasurements(String productId, String partName){
		return ServiceFactory.getDao(MeasurementDao.class).findAll(productId, partName);
	}
	
	public MCOperationPartRevision getMCOperationPartRevision(){
		MCOperationPartRevisionId id = new MCOperationPartRevisionId();
		id.setOperationName("PISTON");
		id.setPartId("A000");
		id.setPartRevision(1);
		return ServiceFactory.getDao(MCOperationPartRevisionDao.class).findByKey(id);
	}
	public void setPropertyBean(PistonClearancePropertyBean propertyBean){
		this.propertyBean = propertyBean;
	}
	
	public void setView(PistonClearanceView view) {
		this.view = view;
	}

	public PistonClearanceView getView() {
		return view;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setAudioManager(ClientAudioManager audioManager){
		this.audioManager = audioManager;
	}
	public PistonClearancePropertyBean getPropertyBean() {
		return propertyBean;
	}

	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

	public PistonClearanceValidator getValidator() {
		return validator;
	}

	public void setValidator(PistonClearanceValidator validator) {
		this.validator = validator;
	}

	public List<MCOperationMeasurement> getClearanceMeasList() {
		return clearanceMeasList;
	}

	public Map<Integer, Double> getPistonMeasValues() {
		return pistonMeasValues;
	}

	public Map<Integer, Double> getClearanceMeasValues() {
		return clearanceMeasValues;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	
}
