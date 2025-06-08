package com.honda.galc.client.datacollection.observer;

import static com.honda.galc.common.logging.Logger.getLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.common.util.RuleDataUtil;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.LotControlDevicePropertyBean;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.IDeviceDataInput;
import com.honda.galc.device.IDeviceUser;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.DataCollectionCompleteProductId;
import com.honda.galc.device.exception.DeviceInUseException;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
/**
 * <h3>LotcontrolDataCollectionDeviceManager</h3>
 * <h4>
 * Manage device based action.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class LotControlDeviceManager extends DeviceManagerBase implements IRuleDevice, IDeviceUser 
{
	private static final String PART_STATUS = " part status ";
	protected ClientContext context;
	protected String defaultDeviceId;
	protected TerminalPropertyBean property;
	protected LotControlDevicePropertyBean deviceProperty;
	protected Map<String, Integer> deviceAccessKeys = new HashMap<String, Integer>();
	boolean socketDevice;
	protected Map<String, Device> ruleDeviceMap;
	
	public LotControlDeviceManager(ClientContext context) {
		super();
		this.context = context;
		initialize();
	}

	protected void initialize() {
		if(context != null){
			property = context.getProperty();
			socketDevice = property.isTorqueDataFromDeviceDriver();
		}
		
		registerDeviceData();
	}

	/**
	 * Check if the skip action was performed on Product, part or torque
	 * @param state
	 * @return
	 */
	protected boolean isSkipped(ProcessProduct state) {
		
		if(state.getProduct().isSkipped()) return true;
		
		for(InstalledPart p : state.getProduct().getPartList()){
			if(p.isSkipped()) return true;
		}
		
		return false;
	}


	protected void registerDeviceData() {
		//No need to register if no device defined
		if(DeviceManager.getInstance().getEiDevice() != null)
			DeviceManager.getInstance().getEiDevice().reqisterDeviceData(getDeviceDataList()) ;
	}

	protected List<IDeviceData> getDeviceDataList() {
		List<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new DataCollectionComplete());
		list.add(new DataCollectionCompleteProductId());
		//list.add(new Acknowledgment());
		return list;
	}
	
	

	private StringBuilder getSendToPlcErrorMsg(String ruleMsg) {
		StringBuilder msgBuilder = new StringBuilder("Failed to send ").append(ruleMsg);
		msgBuilder.append(" data to PLC! Would you like to resend?");
		msgBuilder.append(System.lineSeparator()).append("   - Click OK to resend.").append(System.lineSeparator()).append("   - Click Cancel to skip.");
		return msgBuilder;
	}

	public void sendRuleStatus(ProcessPart state) {
		if(!property.isRuleDeviceEnabled()) return;
		
		Map<String, String> deviceForRules = property.getRuleStatusDevice();
		if(deviceForRules == null || deviceForRules.size() == 0) return;
		
		doSendRuleStatus(state);
	}

	private void doSendRuleStatus(ProcessPart state) {
		try {
			String deviceForRule = getRuleStatusDeviceId(state);
			if(StringUtils.isEmpty(deviceForRule)) return;
			
			Device rdev = getRuleDevice(deviceForRule);
			Logger.getLogger().info("send device data for Rule#:" + (state.getCurrentPartIndex()+1) + ":" + deviceForRule);
			getEiDevice().syncSend(rdev, getRuleDataContainer(rdev, state));
		} catch (Exception e) {
			Logger.getLogger().error("Exception on sending device for rule:", e.getMessage());
			int result = JOptionPane.showConfirmDialog(null, getSendToPlcErrorMsg(PART_STATUS).toString(), LotControlConstants.MSG_FAILED_TO_SEND_STATUS_DATA_TO_EI_DEVICE, 
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				getLogger().info(" dialog was presented and the user entered OK to resend." );
				doSendRuleStatus(state);
			} else {
				getLogger().info(" dialog was presented and the user chose to cancel to skip.");
				
			}
		}
	}

	private Device getRuleDevice(String dev) {
		if(ruleDeviceMap == null)
			ruleDeviceMap = new HashMap<String, Device>();
		
		if(ruleDeviceMap.get(dev) == null) {
            Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(dev);
			
			if(device == null){
				Logger.getLogger().error("error: Can't find device - ", dev);				
			}
			
			ruleDeviceMap.put(dev, device);
		}
		
		return ruleDeviceMap.get(dev);
	}

	private DataContainer getRuleDataContainer(Device dev, ProcessPart state) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(dev.getClientId());
		dc.put(TagNames.PROCESS_POINT_ID.name(), context.getProcessPointId());
		dc.put(DataContainerTag.TAG_LIST, dev.getTagNameList());
		
		for(DeviceFormat format: dev.getDeviceDataFormats()) {
			dc.put(format.getTag(), getValueForRule(format.getTag(), state));
		}
		Logger.getLogger().info("device data:" + dc);
		return dc;
	}

	private Object getValueForRule(String tag, ProcessPart state) {
		switch(tag) {		
			case DataContainerTag.PRODUCT_SPEC_CODE:
				return state.getProductSpecCode();
			case DataContainerTag.PRODUCT_ID:
				return state.getStateBean().getProduct().getProductId();
			case DataContainerTag.PART_MASK:
				return RuleDataUtil.getPartMask(state.getCurrentLotControlRule());
			case DataContainerTag.PART_MASKS:
				return RuleDataUtil.getPartMasks(state.getCurrentLotControlRule());
			case DataContainerTag.INSTRUCTION_CODE:
				return state.getCurrentLotControlRule().getInstructionCode();
			case DataContainerTag.TORQUE_COUNT:
				return RuleDataUtil.getTorqueCount(state.getCurrentLotControlRule());
			case DataContainerTag.MIN_LIMIT:
				return RuleDataUtil.getMinLimit(state.getCurrentLotControlRule());
			case DataContainerTag.PART_NUMBER:
				return RuleDataUtil.getPartNumber(state.getCurrentLotControlRule());
			case DataContainerTag.MAX_LIMIT:
				return RuleDataUtil.getMaxLimit(state.getCurrentLotControlRule());
			case DataContainerTag.PART_SERIAL_NUMBER: 
			case DataContainerTag.PART_SN:
				return state.getCurrentInstallPart().getPartSerialNumber();
			case DataContainerTag.STATUS:
				 boolean status = state.getCurrentInstallPart().isValidPartSerialNumber() && !state.getCurrentInstallPart().isSkipped();
				 return status ? DataCollectionComplete.OK : DataCollectionComplete.NG;
			case DataContainerTag.STATUS_OK:
				return DataCollectionComplete.OK;
			default:
				return true;
		}

	}
	


	private String getRuleStatusDeviceId(ProcessPart state) {
		Integer partIndex = state.getCurrentPartIndex()+1;
		return property.getRuleStatusDevice().get(partIndex.toString());
	}

	public void sendStatus(ProcessProduct state) {
		
		//Don't send release code if there is no product
		if(state.getProduct() == null || StringUtils.isEmpty(state.getProductId()))
			return;
		//Don't send data collection status if not configured to 
		if(!property.isSendStatusToPlc() && !property.isSendReleaseCode())
			return;
		
		boolean status = true;
		if(property.isSendStatusToPlc()){
			status = getDataCollectionStatus(state);
		    Logger.getLogger().info("Send to plc data collection complete status:" + status);
		} else {
			Logger.getLogger().info("Send to plc release code " + status);
		}
		
		if(getEiDevice().isConfigured(DataCollectionCompleteProductId.class) && getEiDevice().containOutputDeviceData(DataCollectionCompleteProductId.class)){
			DataCollectionCompleteProductId dccProductId = new DataCollectionCompleteProductId();
			dccProductId.setCompleteFlag(status ? DataCollectionComplete.OK: DataCollectionComplete.NG);
			dccProductId.setProductId(state.getProductId());
			
			getEiDevice().syncSend(dccProductId);
			
		} else {
			if(status) sendDataCollectionComplete();
			else sendDataCollectionIncomplete(state);
		}
	}

	private boolean getDataCollectionStatus(ProcessProduct state) {
		return state.getErrorList().size() == 0 && state.getProduct() != null && !isSkipped(state) && state.getProduct().isValidProductId();
			
	}
	
	public void initEiDevice() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null)
			eiDevice.start();
		
	}

	protected void initTorqueDevice(String applicationId) {
		if(isSocketDevice())
			initTorqueSocketDevice(getCurrentState(applicationId));
		else 
			initTorqueEiDevice();
	}

	private void initTorqueEiDevice() {
		disableTorqueEiDevice();
	}

	protected void disableTorqueEiDevice() {
	}

	private void initTorqueSocketDevice(DataCollectionState state) {
		int count = 0;
		for(IDevice device : DeviceManager.getInstance().getDevices().values()){
			if(device instanceof TorqueSocketDevice && device.isEnabled()){
				initTorqueDevice((TorqueSocketDevice)device);
				defaultDeviceId = device.getId();
				
				//disable torque guns when starting client
				disableTorqueDevice((TorqueSocketDevice)device);
				count++;
			}
		}
		
		//if more than one torque device defined, then don't know which one to default to
		if(count != 1)
			defaultDeviceId = null;
	}

	protected void disableTorqueDevice(TorqueSocketDevice torqueSocketDevice){
		
	}

	private void initTorqueDevice(TorqueSocketDevice device) {
		device.startDevice();
	}

	public String getApplicationName() {
		return this.getClass().getSimpleName();
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return deviceAccessKeys.get(deviceId);
	}
	
	public void setDeviceAccessKey(String deviceId, Integer key) {
		deviceAccessKeys.put(deviceId, key);
	}

	protected IDevice getDevice(String deviceId) throws DeviceInUseException {
		IDevice device = null;
		
		//ignore exclusive lock if this client is a modal client or the parent of a modal client
		boolean modalClient = context.getAppContext().getApplicationPropertyBean().isModal() || context.getAppContext().getApplicationPropertyBean().isModalParent() ? true : false;
		if (modalClient) device = DeviceManager.getInstance().getDevice(deviceId);
		else if ( null != deviceId ) device = DeviceManager.getInstance().getDevice(deviceId, this);
		
		
		if ( device != null && (getDeviceAccessKey(deviceId) == null || getDeviceAccessKey(deviceId) == -1))
			setDeviceAccessKey(deviceId, DeviceManager.getInstance().requestExclusiveAccess(deviceId, this, modalClient).intValue());
			
		return device;
	}

	public void sendDataCollectionIncomplete(DataCollectionState state) {
		if(isSendDataCollectionInComplete(state))
			sendDeviceData(DataCollectionComplete.NG());
	}

	private boolean isSendDataCollectionInComplete(DataCollectionState state) {
		return property.isSendReleaseCode() || 
		(property.isSendStatusToPlc() && state.getProduct() != null &&
				state.getProduct().isValidProductId());
	}


	protected TorqueSocketDevice getTorqueDevice(LotControlRule currentRule) throws DeviceInUseException {
		return (TorqueSocketDevice) getDevice(getDeviceId(currentRule));
	}

	protected String getDeviceId(LotControlRule currentRule) {
		if(!StringUtils.isEmpty(currentRule.getDeviceId())) 
			return currentRule.getDeviceId();
		else 
			return defaultDeviceId;
	}

	protected boolean hasTorque(LotControlRule currentLotControlRule) {
		if(currentLotControlRule.getPartName() == null ||
				currentLotControlRule.getParts() == null ||
				currentLotControlRule.getParts().size() == 0 ||
				currentLotControlRule.getParts().get(0) == null)
			return false;
		
		return currentLotControlRule.getParts().get(0).getMeasurementCount() > 0;
	}

	public boolean isSocketDevice() {
		return socketDevice;
	}
	
	protected void handleException(IDeviceData deviceData, Throwable e) {
		String msg = "Failed to send " + deviceData.getClass().getSimpleName() + " to Ei Device.";
		
		LotControlTaskException exception = getLotControlTaskException(deviceData);
		DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getState().exception(exception, false);
		Logger.getLogger().error(e, msg);
	}

	private LotControlTaskException getLotControlTaskException(IDeviceData deviceData) {
		if(deviceData instanceof DataCollectionComplete){
			return new LotControlTaskException("Failed to send release code to PLC, manully release is required.", 
					LotControlConstants.MSG_FAILED_TO_SEND_STATUS_DATA_TO_EI_DEVICE, MessageType.EMERGENCY);
		} else {
			return new LotControlTaskException("Failed to send " + deviceData.getClass().getSimpleName() + 
					" to Ei Device.", LotControlConstants.MSG_FAILED_TO_SEND_DATA_TO_EI_DEVICE, MessageType.ERROR);
		}
	}

	protected boolean isInstructionCodeSent(DataCollectionState torque, TorqueSocketDevice torqueSocketDevice) {
		return hasTorque(torque.getCurrentLotControlRule()) && torqueSocketDevice.isInstructionCodeSent();
		      
	}

	public TerminalPropertyBean getProperty() {
		return property;
	}
	
	public LotControlDevicePropertyBean getDeviceProperty() {
		if(deviceProperty == null)
			deviceProperty = PropertyService.getPropertyBean(LotControlDevicePropertyBean.class, context.getProcessPointId());
		return deviceProperty;
	}
	
	protected void checkAndDoDelay(TorqueSocketDevice torqueSocketDevice, Action action) {
		if(torqueSocketDevice.getTorqueDevicePropertyBean().isDelayToSendAbortJob() &&
			torqueSocketDevice.getTorqueDevicePropertyBean().getDelayTimeToSendAbortJob() > 0 &&
			action == Action.COMPLETE) {
			try {
				Thread.sleep(torqueSocketDevice.getTorqueDevicePropertyBean().getDelayTimeToSendAbortJob());
			} catch (Exception e) {
				Logger.getLogger().info(e, "DelayToSendAbortJob sleep cycle interrupted");
			}
		}
	}
	
	public void controlGranted(String deviceId) {
		// TODO Auto-generated method stub
		
	}

	public void controlRevoked(String deviceId) {
		// TODO Auto-generated method stub
		
	}
	
	public IDeviceData processDeviceData(IDeviceDataInput deviceData) {return null;};

}
