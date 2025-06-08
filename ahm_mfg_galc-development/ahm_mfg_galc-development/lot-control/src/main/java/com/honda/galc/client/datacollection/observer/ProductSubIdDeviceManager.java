package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.device.ProductSubTypeCheckingThread;
import com.honda.galc.client.datacollection.device.Side;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.ei.PlcDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.device.dataformat.SubIdSide;

/**
 * 
 * <h3>ProductSubIdDeviceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSubIdDeviceManager description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jan 24, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 24, 2012
 */

public class ProductSubIdDeviceManager extends LotControlPlcDeviceManager 
implements IProductSubIdDeviceObserver{
	protected PlcDevice plcDevice;
	SubIdSide currentSubId;
	ProductSubTypeCheckingThread typeCheckingThread;
	
	public ProductSubIdDeviceManager(ClientContext context) {
		super(context);
		
	}

	public void checkProductIdSide(ProcessPart part) {
		if(part.getCurrentPartName().contains(property.getProductSubIdLotControlPartName())){

			EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
			if(eiDevice == null || !(eiDevice instanceof PlcDevice) || !isDriverActivate((PlcDevice)eiDevice)){
				Logger.getLogger().error("ERROR: ", "No plc device available.");
				part.exception(new LotControlTaskException("ERROR: Plc is not active when reading product sub Id.", this.getClass().getSimpleName()), false);
				return;
			}

			//wait until we get a right product sub Id is put on
			plcDevice = (PlcDevice)eiDevice;
			waitForTheRightSubIdInThread(part, plcDevice);
		}
	}


	private boolean isDriverActivate(PlcDevice device) {
		String sideDevId = property.getProductSubIdDeviceId();
		return device.getDeviceDriver(sideDevId).isActive();
	}

	private void waitForTheRightSubIdInThread(ProcessPart part, PlcDevice plcDevice) {
		typeCheckingThread = new ProductSubTypeCheckingThread(part, plcDevice, this);
		
		typeCheckingThread.start();
	}


	public void cancel(DataCollectionState state) {
		if(typeCheckingThread != null)
			typeCheckingThread.setStop(true);
		
	}
	

	public void skipPart(DataCollectionState state) {
		stopSideCheckingThread();
		
	}

	public boolean processSideRead(ProcessPart part, SubIdSide read) {
        if(read == null) return false;
		
		read = read.clone();
		if(part.getProduct().getSubId().trim().equals(read.getSide())){
			Logger.getLogger().info("Received correct subId side:" + read.getSide());
			sendRequest(read);
			return true;
		} else if(!read.equals(currentSubId)){
			currentSubId = read.clone();
			Logger.getLogger().info("Product SubId Side changed:" + read.getSide());
			sendRequest(read);
			return false;
		} else
			return false;
	}


	private void sendRequest(final SubIdSide read) {
		Thread t = new Thread(){
			public void run() {
				Side side = Side.getSide(read.getSide());
				
				//don't bother to notify user when the wrongly put knuckle is left
				if(side == null || side == Side.EMPTY){
					if(LotControlAudioManager.isExist())
						LotControlAudioManager.getInstance().stopRepeatedSound();
					
					return;
				}
				if(side == Side.ERROR)
					Logger.getLogger().warn("ERROR:", "Plc error both left side and right side are set.");
					
				DataCollectionController.getInstance().received(new PartSerialNumber(side.toString()));
			}
		};

		t.start();

	}

	public void reset(ProcessProduct product) {
		typeCheckingThread = null;
		
	}


	public void rejectPart(DataCollectionState state) {
		stopSideCheckingThread();
		
	}
	

	private void stopSideCheckingThread() {
		if(typeCheckingThread != null)
			typeCheckingThread.setStop(true);
		
		typeCheckingThread = null;
	}

	
	@Override
	public void cleanUp() {
		stopSideCheckingThread();
		
	}

}


