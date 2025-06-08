package com.honda.galc.client.datacollection.strategy;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.control.headless.LotControlRuleManager;
import com.honda.galc.client.datacollection.processor.IDataCollectionTaskProcessor;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PartNameSerialNumber;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.device.dataformat.PlcBoolean;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>MissionInstall</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MissionInstall description </p>
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
 * Mar 8, 2011
 *
 */

public class MissionInstall implements IDataCollectionTaskProcessor<InputData>{
	public static final String MISSING_REQUIRED_PART_ERROR = "MISSING_REQUIRED_PART_ERROR";
	public static final String MISSING_REQUIRED_PART_OK = "MISSING_REQUIRED_PART_OK";
	public static final String SKIP_MISSING_REQUIRED_PART = "SKIP_MISSING_REQUIRED_PART";
	private DataCollectionController controller;
	private ClientContext context;
	private String description;
	private String requiredPartInstallPpid;
	private LotControlRuleManager ruleMgr;
	private boolean missionRecconect;
	
	public MissionInstall(ClientContext context) {
		this.context = context;
		
		initialize();
	}
	
	private void initialize() {
		try {
			requiredPartInstallPpid = PropertyService.getProperty(context.getProcessPointId(), "REQUIRED_PART_INSTALL_PROCESS_POINT");
			controller = DataCollectionController.getInstance();
			description = PropertyService.getProperty(context.getProcessPointId(), "MISSION_RECONNECT_DESCRIPTION");
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to initialize Mission Install strategy.");
		}
	}

	public boolean execute(InputData data) {
		return false;
	}

	public void init() {
	}

	@SuppressWarnings("unchecked")
	public IDeviceData processReceived(IDeviceData deviceData) {
		boolean result = true;
		
		if(deviceData instanceof ProductId){
			result = getProcessor(ProcessProduct.class).execute(deviceData);
			resetMissionStatus();
		} else if(deviceData instanceof PartNameSerialNumber)
			result = processMissingRequiredPart((PartNameSerialNumber) deviceData);
		else if(deviceData instanceof PartSerialNumber)
			result = getProcessor(ProcessPart.class).execute(deviceData);
		else if(deviceData instanceof LastTighteningResult)
			result = getProcessor(ProcessTorque.class).execute(deviceData);
		else if(deviceData instanceof PlcBoolean)
			result = handleMissionReconnectSignal((PlcBoolean)deviceData);
		
	
		return result ? DataCollectionComplete.OK() : DataCollectionComplete.NG();
	}

	private void resetMissionStatus() {
		missionRecconect = false;
	}

	@SuppressWarnings("unchecked")
	private IDataCollectionTaskProcessor getProcessor(Class<?> claz) {
		 IDataCollectionTaskProcessor processor = controller.getProcessor(claz);
		 processor.init();
		 return processor;
	}

	private boolean processMissingRequiredPart(PartNameSerialNumber partNameSn) {
		try {
			
			boolean validatePartSn = getRuleManager().validate(controller.getState().getProductId(), 
					controller.getState().getProductSpecCode(), partNameSn.getPartName(), partNameSn.getPartSn());
			
			//save installed part into database
			saveMissingRequiredPart(partNameSn, InstalledPartStatus.OK);
			
			if(validatePartSn){
				
				controller.getState().getProduct().getMissingRequiredPartList().remove(partNameSn.getPartName());
				controller.getFsm().message(new Message(MISSING_REQUIRED_PART_OK, "Part Mask OK", MessageType.INFO));
				
			}
			
		} catch (TaskException te) {
			saveMissingRequiredPart(partNameSn, InstalledPartStatus.NG);
			controller.getFsm().message(new Message(MISSING_REQUIRED_PART_ERROR, te.getMessage()));
		} catch (Exception e){
			Logger.getLogger().error(e, "exception at processMissingRequiredPart.");
			controller.getFsm().message(new Message("MISSING_REQUIRED_PART_ERROR", e.getMessage()));
		}
		
		return true;
	}

	private void saveMissingRequiredPart(PartNameSerialNumber partNameSn, InstalledPartStatus installedPartStatus) {
		InstalledPart part = createInstalledPart(partNameSn);
		part.setInstalledPartStatus(installedPartStatus);
		InstalledPartDao dao = ServiceFactory.getDao(InstalledPartDao.class);
		dao.save(part);
	}

	private InstalledPart createInstalledPart(PartNameSerialNumber partNameSn) {
		InstalledPart part = new InstalledPart();
		part.setId(new InstalledPartId(controller.getState().getProductId(), partNameSn.getPartName()));
		part.setPartSerialNumber(partNameSn.getPartSn());
		part.setPartId(getRuleManager().getPartSpec() == null ? "": getRuleManager().getPartSpec().getId().getPartId());
		part.setInstalledPartReason("missing required part");
		part.setAssociateNo(context.getUserId());
		part.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		return part;
	}

	private boolean  handleMissionReconnectSignal(PlcBoolean deviceData) {
		
		//this is the Mission reconnect signal, ignore if not in torque collection state
		String msg = "Received Mission Reconnect signal at state:";
		if(controller.getState() instanceof ProcessTorque &&
				controller.getState().getCurrentPartName().equals(getProperty().getMissionPartName())){

			controller.getState().getCurrentInstallPart().setInstalledPartReason(description);
			controller.getState().getCurrentInstallPart().setSkipped(true);//this will signal the complete status to false;
			missionRecconect = true;
			
			
			Logger.getLogger().info(msg, controller.getState().getClass().getSimpleName(),
					" when run down " + (controller.getState().getCurrentTorqueIndex()+1), 
					" torque on part ", controller.getState().getCurrentPartName());
		}else
			Logger.getLogger().warn(msg, controller.getState().getClass().getSimpleName(),
					" signal ignored!");

		return true;
	}

	
	
	public void registerDeviceListener(DeviceListener listener) {
		IDevice eiDevice = DeviceManager.getInstance().getDevice(EiDevice.NAME);
		if(eiDevice != null && eiDevice.isEnabled()){
			((EiDevice)eiDevice).registerDeviceListener(listener, getProcessData());
		}
	}
	

	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new PlcBoolean());
		return list;
	}
	
	public LotControlRuleManager getRuleManager(){
		if(ruleMgr == null)
			ruleMgr = new LotControlRuleManager(context, requiredPartInstallPpid);
		
		return ruleMgr;
	}

	public boolean isMissionRecconect() {
		return missionRecconect;
	}

	public void setMissionRecconect(boolean missionRecconect) {
		this.missionRecconect = missionRecconect;
	}
	
	protected TerminalPropertyBean getProperty(){
		return context.getProperty();
	}
}
