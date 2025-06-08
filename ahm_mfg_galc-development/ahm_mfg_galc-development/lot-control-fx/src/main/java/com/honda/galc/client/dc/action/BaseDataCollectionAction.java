package com.honda.galc.client.dc.action;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.observer.AbstractPersistenceManager;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.property.ManufacturingControlPropertyBean;
import com.honda.galc.client.dc.view.IDataCollectionWidget;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.device.dataformat.MeasurementValue;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.device.dataformat.Torque;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public abstract class BaseDataCollectionAction<T extends InputData> implements IDataCollectionAction<T> {
	
	private Logger logger;
	private MCOperationRevision operation;
	private DataCollectionModel model;
	private AbstractPersistenceManager persistenceManager;
	private static MbpnProductDao mbpnProductDao;

private  IDataCollectionWidget<OperationProcessor> dataCollectionView;

	
	

	public abstract String getCheckPointName();
	
	public void setPersistenceManager(AbstractPersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	public void notifyPartSnOk(PartSerialScanData data, MCOperationRevision operation) {
		DataCollectionResultEventType evType = DataCollectionResultEventType.VALID_PART_SCAN_RECEIVED;
		EventBusUtil.publish(new DataCollectionResultEvent(evType, operation, data));
	}
	
	public void notifyPartSnNg(PartSerialScanData data, MCOperationRevision operation, String message) {
		DataCollectionResultEventType evType = DataCollectionResultEventType.INVALID_PART_SCAN_RECEIVED;
		evType.setMessage(message);
		EventBusUtil.publish(new DataCollectionResultEvent(evType, operation, data));
	}
	
	public void notifyMeasurementOk(MeasurementInputData data, MCOperationRevision operation) {
		Logger.getLogger().check("Data Collection Result Event Type : " + DataCollectionResultEventType.VALID_MEASUREMENT_RECEIVED.getMessage());
		DataCollectionResultEventType evType = DataCollectionResultEventType.VALID_MEASUREMENT_RECEIVED;
		EventBusUtil.publish(new DataCollectionResultEvent(evType, operation, data));
	}

	public void notifyMeasurementNg(MeasurementInputData data, MCOperationRevision operation, String message) {
		Logger.getLogger().check("Received Message String : " + message);
		Logger.getLogger().check("Data Collection Result Event Type : " + DataCollectionResultEventType.INVALID_MEASUREMENT_RECEIVED.getMessage());
		DataCollectionResultEventType evType = DataCollectionResultEventType.INVALID_MEASUREMENT_RECEIVED;
		evType.setMessage(message);
		EventBusUtil.publish(new DataCollectionResultEvent(evType, operation, data));
	}
	
	public MCOperationRevision getOperation() {
		return operation;
	}

	public void setOperation(MCOperationRevision operation) {
		this.operation = operation;
	}
	
	public DataCollectionModel getModel() {
		return model;
	}
	
	public void setModel(DataCollectionModel model) {
		this.model = model;
	}
	
	protected InstalledPart createInstalledPart(DataCollectionModel model, PartSerialScanData partSerialScanData, MCOperationRevision operation, InstalledPartStatus status) {
		
		InstalledPart installedPart = model.getInstalledPartsMap().get(operation.getId().getOperationName());
		String productId = model.getProductModel().getProductId();
		String partName = operation.getId().getOperationName();
		if (installedPart == null) {
			installedPart = new InstalledPart(productId, partName);
		} 
		installedPart.setOperationRevision(operation.getId().getOperationRevision());
		installedPart.setAssociateNo(model.getProductModel().getApplicationContext().getUserId());
		installedPart.setProcessPointId(model.getProductModel().getProcessPointId());
		if (partSerialScanData != null) {
			installedPart.setPartSerialNumber(partSerialScanData.getSerialNumber());
			if(StringUtils.isNotEmpty(partSerialScanData.getOverrrideUser()))
				installedPart.setOverrideAssociateNo(partSerialScanData.getOverrrideUser());
		} 
		installedPart.setInstalledPartStatus(status);
		
		if (operation.getSelectedPart() == null ) { 
			installedPart.setPartId("");
		} else { 
			installedPart.setPartId(operation.getSelectedPart().getId().getPartId());
			installedPart.setPartRevision(operation.getSelectedPart().getId().getPartRevision());
		}
		
		installedPart.setValidPartSerialNumber(true);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		model.getInstalledPartsMap().put(operation.getId().getOperationName(), installedPart);
		return installedPart;
	}
	
	protected Measurement createMeasurement(MeasurementId measurementId, MeasurementStatus status, MeasurementInputData inputData) {
		Measurement measurement = new Measurement(measurementId);
		measurement.setPartId(operation.getSelectedPart().getId().getPartId());
		measurement.setPartRevision(operation.getSelectedPart().getId().getPartRevision());
		measurement.setMeasurementStatus(status);
		if (inputData instanceof Torque) {
			Torque torque = (Torque) inputData;
			measurement.setMeasurementValue(torque.getTorqueValue());
			measurement.setMeasurementAngle(torque.getAngle());
			if (status.equals(MeasurementStatus.OK)) {
				setMeasurementStatus(measurement, torque);
			}
		} else if (inputData instanceof MeasurementValue) {
			MeasurementValue meas = (MeasurementValue) inputData;
			measurement.setMeasurementValue(new Double(meas.getMeasurementValue()));
		}
		return measurement;
	}
	
	protected Measurement createMeasurement(DataCollectionModel model, MeasurementInputData inputData, MCOperationRevision operation, MeasurementStatus status) {
		String productId = model.getProductModel().getProductId();
		String partName = operation.getId().getOperationName();
		
		Measurement measurement = new Measurement(productId, partName, model.getCurrentMeasurementIndex());
		measurement.setPartId(operation.getSelectedPart().getId().getPartId());
		measurement.setPartRevision(operation.getSelectedPart().getId().getPartRevision());
		measurement.setMeasurementStatus(status);
		if (inputData instanceof Torque) {
			Torque torque = (Torque) inputData;
			measurement.setMeasurementValue(torque.getTorqueValue());
			measurement.setMeasurementAngle(torque.getAngle());
			if (status.equals(MeasurementStatus.OK)) {
				setMeasurementStatus(measurement, torque);
			}
		} else if (inputData instanceof MeasurementValue) {
			MeasurementValue meas = (MeasurementValue) inputData;
			measurement.setMeasurementValue(new Double(meas.getMeasurementValue()));
		}
		return measurement;
	}
	
	protected InstalledPart getInstalledPart(DataCollectionModel model, MCOperationRevision operation) {
		return model.getInstalledPartsMap().get(operation.getId().getOperationName());
	}

	public void setMeasurementStatus(Measurement measurement, Torque torque) {
		if (torque.getTighteningStatus() == 2) {
			measurement.setMeasurementStatus(MeasurementStatus.REVERSE);
		} else if ((torque.getTorqueStatus() == 1) &&
			(torque.getAngleStatus() == 1)  &&
			(torque.getTighteningStatus() == 1)) {
			measurement.setMeasurementStatus(MeasurementStatus.OK);
		} else {
			measurement.setMeasurementStatus(MeasurementStatus.NG);
		}
	}
	
	/**
	 * Mark if the current work unit is completed.
	 *
	 * @param model the model
	 * @param operation the current operation
	 */
	public boolean markIfOpCompleted(DataCollectionModel model, MCOperationRevision operation) {
		boolean opCompleted = false;
		if (operation.getSelectedPart() == null) {
			// No part, operation is completed when invoked
			opCompleted = true;
		} else if (!operation.getSelectedPart().hasMeasurements()) {
			// No measurements, when part SN is ok, the operation is completed
			opCompleted = true;
		} else if (getSkippedAndCollectedMeasCount(model, operation) == operation.getSelectedPart().getMeasurements().size()) {
			// when the number of good measurements in the installed part equals part measurement size
			opCompleted = true;
		}
		
		if(opCompleted){
			model.markComplete(operation.getId().getOperationName(), true);
			model.markSkippedPart(operation.getId().getOperationName(), false);
			// Notify the data collection framework, the part is completed
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_COMPLETED_FOR_PART, operation));
		}
		return opCompleted;
	}

	public int getSkippedAndCollectedMeasCount(DataCollectionModel model, MCOperationRevision operation) {
		String operationName = operation.getId().getOperationName();
		int count = model.getGoodMeasurementsCount(operationName);
		
		ArrayList<Integer> skippedMeasurements = model.getSkippedMeasurementsMap().get(operationName);
		if (skippedMeasurements != null) {
			count += skippedMeasurements.size();
		}
		return count;
	}
	
	public void removeFromCompleteOpsMap(DataCollectionEvent event) {
		String operationName = event.getOperation().getId().getOperationName();
		if (getModel().getCompletedOpsMap().containsKey(operationName)) {
			getModel().getCompletedOpsMap().remove(operationName);
		}
	}
	
	public boolean isDataCollectionComplete(InstalledPart installedPart, MCOperationRevision operation) {
		if (operation.getSelectedPart() != null 
				&& !operation.getSelectedPart().getPartMask().equals("")) {
			if (installedPart.getPartSerialNumber().equals("")) {
				return false;
			}
		} 
		
		if (installedPart.getMeasurements().size() < operation.getSelectedPart().getMeasurements().size()) {
			return false;
		}
		return true;
	}
	
	protected void savePart(InstalledPart installedPart, MCOperationRevision operation) {
		getLogger().info(" Saving Part :"+installedPart);
		model.getInstalledPartsMap().put(operation.getId().getOperationName(), installedPart);
		if (getModel().getProductModel().isTrainingMode()) return;
		persistenceManager.saveInstalledPart(installedPart);
	}
	
	protected void rejectPart(InstalledPart installedPart) {
		model.getInstalledPartsMap().remove(installedPart.getId().getPartName());
		persistenceManager.deleteInstalledPart(installedPart.getId());
		for (Measurement meas : installedPart.getMeasurements()) {
			persistenceManager.deleteMeasurement(meas.getId());
		}
		model.getBadMeasurementAttemptsMap().clear();
	}
	
	protected void saveMeasurement(DataCollectionModel model, MCOperationRevision operation, Measurement measurement) {
		InstalledPart installedPart = getInstalledPart(model, operation);
		if (installedPart == null) {
			installedPart = createInstalledPart(model, null, operation, InstalledPartStatus.OK);
			installedPart.getMeasurements().add(measurement);
			savePart(installedPart, operation);
		} else {
			measurement.setPartSerialNumber(installedPart.getPartSerialNumber());
			installedPart.getMeasurements().add(measurement);
			saveMeasurement(measurement);
		}
	}
	
	protected void saveMeasurement(Measurement measurement) {
		getLogger().info("Saving measurement : "+measurement);
		if (getModel().getProductModel().isTrainingMode()) return;
		persistenceManager.saveMeasurement(measurement);		
	}
	
	protected void rejectMeasurement(MeasurementId id) {
		List<Measurement> measurements = model.getInstalledPartsMap().get(id.getPartName()).getMeasurements();
		List<Measurement> tempMeasurements = new ArrayList<Measurement>();
		tempMeasurements.addAll(measurements);
		//int measurementIndexToRemove = -1;
		int i = 0;
		for (Measurement measurement: tempMeasurements) {
			if (measurement.getId().getMeasurementSequenceNumber() == id.getMeasurementSequenceNumber()) {
				measurements.remove(i);
				i--;
			}
			i++;
		}
		/*if (measurementIndexToRemove != -1) {
			measurements.remove(measurementIndexToRemove);
		}*/
		persistenceManager.deleteMeasurement(id);
	}
	
	public void unregisterCheckPoint() {
		CheckPointsRegistry.getInstance().unregister(this);
	}
	
	public boolean executeCheckers(T inputData) {
		// TODO add default implementation
		return true;
	}

	protected void disableTorqueDevices() {
		for (String deviceId : DeviceManager.getInstance().getDeviceNames()) {
			disableTorqueDevice(deviceId);
		}
	}
	
	protected void disableTorqueDevice(String deviceId) {
		if (StringUtils.isNotBlank(deviceId)) {
			IDevice device = DeviceManager.getInstance().getDevice(deviceId);
			if (device instanceof TorqueSocketDevice) {
				((TorqueSocketDevice) device).disable();
				((TorqueSocketDevice) device).setCurrentInstructionCode(null);
			}
		}
	}
	
	protected boolean isOpTypeScan(MCOperationRevision operation) {
		if(operation!=null
				&& (operation.getType().equals(OperationType.GALC_SCAN) 
						|| operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS) 
						|| operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL))) {
			return true;
		}
		return false;
	}
	
	protected boolean isInstalledPartValid(MCOperationRevision operation, InstalledPart installedPart) {
		//return false if operation or installed part is null
		if(installedPart == null || operation == null || operation.getType() == null)
			return false;
		
		//Return false if part serial number is blank for scan units
		if(isOpTypeScan(operation) && StringUtils.isBlank(installedPart.getPartSerialNumber()))
			return false;
		
		return true;
	}
	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}

	
	public IDataCollectionWidget<OperationProcessor> getDataCollectoionView() { 
		           return this.dataCollectionView; 
		       } 
		        
		      public void setDataCollectionView(IDataCollectionWidget<OperationProcessor> dcView) { 
		         this.dataCollectionView = dcView; 
		     } 

		    	public IDataCollectionWidget<OperationProcessor> getView() {
		 		return this.dataCollectionView;
		 	}
		 	
		 	public void setView(IDataCollectionWidget<OperationProcessor> dcView) {
		 		this.dataCollectionView = dcView;
		 	}


	protected ManufacturingControlPropertyBean getManufacturingControlPropertyBean(){
		return PropertyService.getPropertyBean(ManufacturingControlPropertyBean.class, ApplicationContext.getInstance().getApplicationId());
	}
	
	protected boolean isMeasurementValid(Measurement measurement) {
		//if (getModel().getProductModel().isTrainingMode()) return true;
		if(measurement != null && measurement.getId() != null) {
			if(ServiceFactory.getDao(MeasurementDao.class).findByKey(measurement.getId()) != null) {
				return true;
			}
		}
		return false;
	}
	
	protected static MbpnProductDao getMbpnProductDao() {
		if (mbpnProductDao == null) {
			mbpnProductDao = getDao(MbpnProductDao.class);
		}
		return mbpnProductDao;
	}
	

}
