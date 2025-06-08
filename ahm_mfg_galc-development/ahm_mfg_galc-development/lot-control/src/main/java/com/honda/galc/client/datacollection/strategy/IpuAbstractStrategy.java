package com.honda.galc.client.datacollection.strategy;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.LotControlIpuPersistenceManager;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.InstalledPart;
/**
 * 
 * <h3>IpuAbstractStrategy Class description</h3>
 * <p> IpuAbstractStrategy description </p>
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
 * @author Jeffray Huang<br>
 * Nov 29, 2011
 *
 *
 */
public abstract class IpuAbstractStrategy extends PartSerialNumberProcessor {

	public IpuAbstractStrategy(ClientContext context) {
		super(context);
		
	}
	
	public boolean confirmPartSerialNumber(PartSerialNumber partnumber){
		
		List<InstalledPart> parts = parseIpuData(partnumber.getPartSn());
		boolean isOk = parts != null && !parts.isEmpty();
		getDBManager().addParts(parts);
		if(!isOk) throw new TaskException("Invalid Part Serial Number");
		installedPart.setValidPartSerialNumber(true);
		installedPart.setPartSerialNumber("OK");
		return isOk;
		
	}


	public void registerDeviceListener(DeviceListener listener) {
		
	}
	
	protected LotControlIpuPersistenceManager getDBManager() {
		return (LotControlIpuPersistenceManager)context.getDbManager();
	}
	
	public abstract List<InstalledPart> parseIpuData(String dataStr);

}
