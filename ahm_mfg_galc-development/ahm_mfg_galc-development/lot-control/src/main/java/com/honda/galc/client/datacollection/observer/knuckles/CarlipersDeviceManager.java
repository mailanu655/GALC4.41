package com.honda.galc.client.datacollection.observer.knuckles;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.device.Side;
import com.honda.galc.client.datacollection.observer.ProductSubIdDeviceManager;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.device.ei.PlcDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;


/**
 * 
 * <h3>CarlipersDeviceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CarlipersDeviceManager description </p>
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
 * Feb 24, 2011
 *
 */

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class CarlipersDeviceManager extends ProductSubIdDeviceManager 
implements IKnucklesProductObserver{
	Device carliperSide;
	
	public CarlipersDeviceManager(ClientContext context) {
		super(context);
		
		init();
	}
	
	private void init() {
		plcDevice = getPlcDevice();
		carliperSide = plcDevice.getDevice(getDeviceProperty().getProductSubIdDeviceId().trim());
	}
	
	public PlcDevice getPlcDevice(){
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice == null || !(eiDevice instanceof PlcDevice)){
			Logger.getLogger().error("ERROR: ", "No plc device available.");
			return null;
		} 
		
		plcDevice = (PlcDevice) eiDevice;
		
		return plcDevice;
	}

	public void productIdOk(final ProcessProduct product){
		DeviceFormat format = getDeviceFormat(product);
		
		if(format == null)
			Logger.getLogger().error("ERROR: Failed to retrieve device data format for Carliper side:",
					product.getProduct().getSubId());
		
		try {
			plcDevice.getDeviceDriver(carliperSide).write(format, true);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception: failed to write carliper side:", product.getProduct().getSubId());
		}
		
		Logger.getLogger().info("succeeded to write carliper side:", product.getProduct().getSubId()," to plc.");
	}

	private DeviceFormat getDeviceFormat(ProcessProduct product) {
		Side side = Side.getSide(product.getProduct().getSubId().trim());
		
		for(DeviceFormat format : carliperSide.getDeviceDataFormats()){
			if(format.getTag().equals(side.toString().toUpperCase()))
				return format;
		}
		
		return null;
	}

}
