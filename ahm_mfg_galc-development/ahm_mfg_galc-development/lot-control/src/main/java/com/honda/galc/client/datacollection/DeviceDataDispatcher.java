package com.honda.galc.client.datacollection;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.processor.IDataCollectionTaskProcessor;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.ITorqueDeviceListener;
import com.honda.galc.client.device.lotcontrol.TorqueDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CarrierId;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.device.events.PrintDeviceStatusInfo;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.openprotocol.model.CommandAccepted;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;
import com.honda.galc.openprotocol.model.OPCommandError;
import com.honda.galc.openprotocol.model.SpindleStatus;

/**
 * 
 * <h3>DeviceDataDispatcher</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceDataDispatcher description </p>
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
 * @author Paul Chou
 * May 14, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class DeviceDataDispatcher implements DeviceListener, ITorqueDeviceListener, IPrintDeviceListener {

	private DataCollectionController controller;

	public DeviceDataDispatcher() {
		super();
		initialize(ClientMain.getInstance().getLocatedProcessPoint());
	}
	
	public DeviceDataDispatcher(String applicationId) {
		super();
		initialize(applicationId);
	}
	
	public DeviceDataDispatcher(boolean isInitLotControl) {
		super();
		
		if(isInitLotControl)
			initialize(ClientMain.getInstance().getLocatedProcessPoint());
	}
	
	private void initialize(String applicationId) {
		controller = DataCollectionController.getInstance(applicationId);
		registerProcessors(DataCollectionController.getInstance(applicationId).getProcessors().values());
	}

	public IDeviceData received(String clientId, IDeviceData deviceData) {
		Logger.getLogger().info("Receive " + getReceivedLogString(deviceData) + " from device:" + clientId);
		return  (IDeviceData)controller.received(deviceData);
	}

	
	public void registerProcessors(Collection<IDataCollectionTaskProcessor<? extends IDeviceData>> processors){
		for(IDataCollectionTaskProcessor<? extends IDeviceData> p : processors){
			p.registerDeviceListener(this);
		}
		
		//register for all strategies
		for(LotControlRule rule : controller.getClientContext().getAllRules()){
			if(!StringUtils.isEmpty(rule.getStrategy())){

				IDataCollectionTaskProcessor<?> strategy = controller.createProcessor(StrategyType.valueOf(rule.getStrategy()).getCanonicalStrategyClassName());
				strategy.registerDeviceListener(this);
			} 
		}
	}

	protected String getReceivedLogString(IDeviceData deviceData) {
		StringBuilder sb = new StringBuilder();
		sb.append(deviceData.getClass().getSimpleName()).append(":");
		
		if(deviceData instanceof ProductId)
			sb.append(((ProductId)deviceData).getProductId());
		else if(deviceData instanceof PartSerialNumber)
			sb.append(((PartSerialNumber)deviceData).getPartSn());
		else if(deviceData instanceof LastTighteningResult)
			sb.append(((LastTighteningResult)deviceData).getTighteningId());
		else if(deviceData instanceof CarrierId)
			sb.append(((CarrierId)deviceData).getCarrierId());
		
		return sb.toString();
	}
	
	public String getId() {
		return this.getClass().getSimpleName();
	}

	public void handleStatusChange(String deviceId,TorqueDeviceStatusInfo statusInfo) {
		Logger.getLogger().info("Received " + statusInfo.getClass().getSimpleName() + ":" + statusInfo.getMessage());
		String msg = statusInfo.getMessage().replace("{pset}", getInstructionCode());
		controller.getFsm().message(new Message(statusInfo.getDeviceId(), msg,
				(statusInfo.getMessageSeverity() == DeviceMessageSeverity.error ? MessageType.ERROR : MessageType.INFO)));
	}

	public void handleStatusChange(PrintDeviceStatusInfo statusInfo) {
		Logger.getLogger().info("Received " + statusInfo.getClass().getSimpleName() + ":" + statusInfo.getMessage());
		controller.getFsm().message(new Message("Received message from print device driver: ", statusInfo.getMessage(),
				(statusInfo.getMessageSeverity() == DeviceMessageSeverity.error ? MessageType.ERROR : MessageType.INFO)));
	} 
	
	public void processLastTighteningResult(String deviceId,LastTighteningResult tighteningResult) {
		Logger.getLogger().info("Received " +  getReceivedLogString(tighteningResult) + " from torque device driver.");
		
		controller.received(tighteningResult);
	}

	public String getApplicationName() {
		return this.getClass().getSimpleName();
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return null;
	}

	public void processMultiSpindleResult(String deviceId,MultiSpindleResultUpload multiSpindleResult) {
		
		//if any of the status failed then we fail the all the results
		int allAngleStatus = 1;
		int allTorqueStatus = 1;
		
		for(SpindleStatus status : multiSpindleResult.getSpindleStatusList()){
			allAngleStatus = allAngleStatus & status.getAngleStatus();
			allTorqueStatus = allTorqueStatus & status.getTorqueStatus();
		}
		
		for(SpindleStatus status : multiSpindleResult.getSpindleStatusList()){
			LastTighteningResult tighteningResult = createTighteningResult(multiSpindleResult, status, allTorqueStatus, allAngleStatus);
			DataCollectionComplete returnStatus = (DataCollectionComplete)controller.received(tighteningResult);
			
			if(returnStatus.getCompleteFlag().equals("0")){
				Logger.getLogger().warn("Failed on multi-spindle result, spindle number:" + status.getSpindleNumber());
				return;
			}
		}
		
	}

	private LastTighteningResult createTighteningResult(MultiSpindleResultUpload multiSpindleResult, 
			SpindleStatus status, int allTorqueStatus, int allAngleStatus) {
		LastTighteningResult result = new LastTighteningResult();
		
		result.setTighteningId(multiSpindleResult.getSyncTighteningId());
		result.setProductId(multiSpindleResult.getProductId().trim());
		
		//If any of the two torque failed then we would like to fail both of them, even the 1st one
		result.setTighteningStatus(status.getOverallStatus() & multiSpindleResult.getSyncOverallStatus());
		result.setTorque(status.getTorqueResult());
		result.setTorqueStatus(status.getTorqueStatus() & allTorqueStatus);
		result.setAngleStatus(status.getAngleStatus() & allAngleStatus);
		result.setAngle(status.getAngleResult());
		
		return result;
	}

	public void controlGranted(String deviceId) {
		if (DeviceManager.getInstance().getDevice(deviceId) instanceof TorqueSocketDevice){
			if (controller.getState() instanceof ProcessTorque){
				((TorqueSocketDevice) DeviceManager.getInstance().getDevice(deviceId)).enable(getInstructionCode());
			} else {
				((TorqueSocketDevice) DeviceManager.getInstance().getDevice(deviceId)).disable();
			}
		}
	}
	
	private String getInstructionCode(){
		if (controller.getState() instanceof ProcessTorque){
			return ((ProcessTorque)controller.getState()).getCurrentLotControlRule().getInstructionCode();
		}
		return "";
	}
	public void controlRevoked(String deviceId) {}

	@Override
	public void handleCommandAccepted(String deviceId, CommandAccepted commandAccepted) {
		// TODO Auto-generated method stub
		
	}
}
