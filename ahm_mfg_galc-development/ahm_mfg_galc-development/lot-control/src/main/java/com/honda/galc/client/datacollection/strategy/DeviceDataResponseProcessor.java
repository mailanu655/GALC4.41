package com.honda.galc.client.datacollection.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.IDataCollectionTaskProcessor;
import com.honda.galc.client.datacollection.processor.ProcessorBase;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DeviceDataResult;
import com.honda.galc.device.dataformat.DevicePartDataResult;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.DeviceResult;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;

public class DeviceDataResponseProcessor extends ProcessorBase implements IDataCollectionTaskProcessor<IDeviceData> {

	protected InstalledPart installedPart = null;

	public DeviceDataResponseProcessor(ClientContext context) {
		super(context);
	}

	public void init() {
		installedPart = new InstalledPart();
		installedPart.setAssociateNo(getAssociateNo());
		installedPart.setMeasurements(new ArrayList<Measurement>());
		installedPart.setProcessPointId(context.getProcessPointId());
		installedPart.setProductType(context.getProperty().getProductType());
	}

	private String getAssociateNo() {
		return context.getUserId();
	}

	public void registerDeviceListener(DeviceListener listener) {
		registerListener(EiDevice.NAME, listener, getDeviceData());
	}

	protected List<IDeviceData> getDeviceData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new DeviceDataResult());
		list.add(new DevicePartDataResult());
		return list;
	}

	public boolean execute(IDeviceData data) {
		return false;
	}

	public IDeviceData processReceived(IDeviceData deviceData) {

		String msg = " Part Install result not good";
		String value = "-1";
		
		DeviceResult partInstallResult = null;
		if(deviceData instanceof DevicePartDataResult){
			installedPart.setPartId(getController().getState().getCurrentLotControlRulePartList().get(0).getId().getPartId());
			partInstallResult = (DevicePartDataResult) deviceData;
			String errMsg = ((DevicePartDataResult) deviceData).getErrorMessage();
			msg = StringUtils.isEmpty(errMsg)?msg:errMsg;
			
			String partValue = ((DevicePartDataResult) deviceData).getPartValue();
			value = StringUtils.isEmpty(partValue)?value:partValue;
		}else if (deviceData instanceof DeviceDataResult) {
			installedPart.setPartId(getController().getState().getCurrentLotControlRulePartList().get(0).getId().getPartId());
			partInstallResult = (DeviceDataResult) deviceData;
		}
			
		if(partInstallResult != null){
			
            if(partInstallResult.isOk()) {
                value = value.equalsIgnoreCase("-1")? "1":value;
                updateInstalledPart(value, false, true);
                getController().getFsm().partSnOk(installedPart);
                return DataCollectionComplete.OK();
            }else {
                if(!partInstallResult.isSkipOperation()) {
                    updateInstalledPart(value, false, false);
                    getController().getFsm().partSnNg(installedPart, "PART_SN",msg);
                    return DataCollectionComplete.NG();                                
                }
                else {
                    updateInstalledPart(value, true, false);
                    getController().getFsm().skipPart();                     
                }                          
            }	            
		}    

		return deviceData;
	}
	
	private void updateInstalledPart(String partSerialNumber, boolean isSkipped, boolean isValid){
		installedPart.setValidPartSerialNumber(isValid);
		installedPart.setPartSerialNumber(partSerialNumber);
		installedPart.setSkipped(isSkipped);
	}
 
}
