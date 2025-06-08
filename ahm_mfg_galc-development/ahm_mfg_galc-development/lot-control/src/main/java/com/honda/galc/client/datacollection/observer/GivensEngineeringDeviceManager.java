package com.honda.galc.client.datacollection.observer;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IGivensEngineeringDeviceManager;
import com.honda.galc.client.datacollection.property.GivensEngineeringPropertyBean;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.IDeviceDataInput;
import com.honda.galc.device.dataformat.AbortJob;
import com.honda.galc.device.dataformat.GalcDataString;
import com.honda.galc.device.dataformat.HeartBeat;
import com.honda.galc.device.dataformat.InstructionCode;
import com.honda.galc.device.dataformat.PlcDataString;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;
/**
 * 
 * <h3>GivensEngineeringDeviceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> GivensEngineeringDeviceManager description </p>
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
 * Mar.5, 2019
 *
 */
public class GivensEngineeringDeviceManager extends PlcForceDeviceManager 
implements IGivensEngineeringDeviceManager {
    public static final String ONE ="1";
    public static final String ZERO = "0";
    boolean heartBeatSent = false;
    boolean active = false;
    private GivensEngineeringPropertyBean givensEngineeringPropertyBean;
    private volatile long lastHeartBeatWrite = new Date().getTime();
    private Timer heartbeatTimer = null;
   
    
    enum GivensEngineeringMsg {VIN_ACK("101"), JOB_ACK("102"),FORCE_ACK("103"),ABORT_ACK("104");
        private final String id;
       
        private GivensEngineeringMsg(String id) {
    		this.id = id;
        }
        
        public String getId() {
    		return id;
    	}
     	
    	public static GivensEngineeringMsg getType(String id) {
            for(GivensEngineeringMsg msg : GivensEngineeringMsg.values())
            	if(msg.getId().equals(id))
            		return msg;
            return null;
        }

    }
  
	public GivensEngineeringDeviceManager(ClientContext context) {
		super(context);
		init();
	}

	private void init() {
		active = true;
		
		if(getGivensEngineeringPropertyBean().isHeartbeatEnabled())
			startHeartBeatTimer();
		
	}

	public boolean isActive() {
		return active;
	}

	private void startHeartBeatTimer() {
		TimerTask timerTask = new TimerTask() {
			public void run() {
				while (isActive()) {
					try {
						sendHeartBeat();
						Thread.sleep(getGivensEngineeringPropertyBean().getHeartbeatInterval()*1000);
					} catch (Exception ex) {
						ex.printStackTrace();
						Logger.getLogger().error(ex,"Exception on heartbeat.");
					}
				}

				getHeatbeatTimer().cancel();
				Logger.getLogger().info("Exiting keepAlive() for TorqueSocketDevice ");
			}
		};

		getHeatbeatTimer().schedule(timerTask, 100);
	}
	
	private void sendHeartBeat() {
		String heartbeatData = heartBeatSent ?  ZERO : ONE;
		HeartBeat heartBeat = new HeartBeat();
		heartBeat.setHeartBeat(heartbeatData);
		Logger.getLogger().debug("send heartbeat to device:" + heartbeatData);
		getEiDevice().send(heartBeat);
		heartBeatSent = !heartBeatSent;
		
	}
	
	@Override
	protected List<IDeviceData> getDeviceDataList() {
		
		List<IDeviceData> deviceDataList = super.getDeviceDataList();
		deviceDataList.add(new HeartBeat());
		deviceDataList.add(new GalcDataString());
		
		return deviceDataList;
	}
	


	public GivensEngineeringPropertyBean getGivensEngineeringPropertyBean() {
		if(givensEngineeringPropertyBean==null)
			givensEngineeringPropertyBean=PropertyService.getPropertyBean(GivensEngineeringPropertyBean.class, context.getProcessPointId());
		return givensEngineeringPropertyBean;
	}

	public long getLastHeartBeatWrite() {
		return lastHeartBeatWrite;
	}

	public Timer getHeatbeatTimer() {
		if(heartbeatTimer == null)
			heartbeatTimer = new Timer();
		
		return heartbeatTimer;
	}

	@Override
	public void productIdOk(ProcessProduct state) {
		ProductId productId = new ProductId(state.getProductId());
		Logger.getLogger().info("send product id:", state.getProductId(), "to Device.");
		getEiDevice().send(productId);
		
	}
	
	
	protected void setJobEiDevice(ProcessTorque torque) {
		String jobId = torque.getCurrentLotControlRule().getInstructionCode();
		jobId = StringUtil.padLeft(jobId, 3, '0');
		IDeviceData deviceData = new InstructionCode(torque.getProductId(), jobId);
		Logger.getLogger().info("setJobEiDevice: " + torque.getCurrentLotControlRule().getDeviceId() + 
				"  job#:" + torque.getCurrentLotControlRule().getInstructionCode());
		getEiDevice().send(deviceData );
		
	}
	
	protected void abortJobEiDevice() {
		Logger.getLogger().info("abortJobEiDevice.");
		IDeviceData deviceData = new AbortJob(ONE);
		getEiDevice().send(deviceData );
		
	}
	
	protected void doProcessDeviceData(IDeviceDataInput deviceData) {
		if(deviceData instanceof PlcDataString) {
			PlcDataString plcData = (PlcDataString)deviceData;
			String str = plcData.getPlcDataString();
			Logger.getLogger().info("PLC data string received:" + str);
			if(str.length() < 3) {
				Logger.getLogger().warn("Invalid Message:", str, " received for Givens Engineering protocol.");
			}
			
			if(str.length() > ProductType.FRAME.getProductIdLength()) {
				processForcesPackage(str);
			}
			
			GivensEngineeringMsg gMsg = GivensEngineeringMsg.getType(str.substring(0, 3));
			switch(gMsg) {
			case VIN_ACK: 
				String vinStatus = str.replace(GivensEngineeringMsg.VIN_ACK.getId(), ""); 
				if(vinStatus != null && ZERO.equals(vinStatus.trim()))
				    getController().getFsm().message(new Message("GivensEngineeringPlcValidation", "VIN validation failed on PLC",MessageType.WARN));
				
				Logger.getLogger().info("Received VIN ACK:", vinStatus);
				break;
			case ABORT_ACK:
				Logger.getLogger().info("Received ABORT ACK.");
				break;
			case JOB_ACK:
				String jobStatus = str.replace(GivensEngineeringMsg.JOB_ACK.getId(), ""); 
				Logger.getLogger().info("Received JOB ACK:", jobStatus);
				if(jobStatus != null && ZERO.equals(jobStatus.trim()))
				    getController().getFsm().error(new Message("Job Id validation failed on PLC"));
				
				break;
			default:
				Logger.getLogger().warn("Not supported Message type:" + gMsg);
			}
		}
	}
	
	
	protected void processForcesPackage(String str) {
		super.processForcesPackage(str);
		sendTorqueReceived();
	}


	public DataCollectionController getController() {
		return DataCollectionController.getInstance(context.getAppContext().getApplicationId());
	}

	public void sendTorqueReceived() {
		Logger.getLogger().info("start to send torque received to PLC.");
		IDeviceData deviceData = new GalcDataString(GivensEngineeringMsg.FORCE_ACK.getId());
		getEiDevice().send(deviceData );
		
	}


}
