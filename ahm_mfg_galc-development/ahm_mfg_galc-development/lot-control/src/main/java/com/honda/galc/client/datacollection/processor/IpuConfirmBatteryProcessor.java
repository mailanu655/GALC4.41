package com.honda.galc.client.datacollection.processor;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
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
public class IpuConfirmBatteryProcessor extends PartSerialNumberProcessor {

	public IpuConfirmBatteryProcessor(ClientContext context) {
		super(context);
		
	}
	
	public boolean confirmPartSerialNumber(PartSerialNumber partnumber){
		String batterySN = null;
		boolean isOk =false;
		List<InstalledPart> parts = getController().getState().getProduct().getDerivedPartList();
		for (InstalledPart p : parts) {  
			System.out.println("parts"+ p.getPartName());
			if ("BATTERY_SERIAL_NUMBER".equals(p.getPartName().trim()) && p.getPartSerialNumber()!= null) {
				batterySN = (p.getPartSerialNumber().trim());
			}
		}
		if(batterySN!= null)
		isOk = batterySN.trim().equals(partnumber.getPartSn().trim());
		
		if(!isOk) throw new TaskException("Battery SN does not match");
		installedPart.setValidPartSerialNumber(true);
		installedPart.setPartId("A0000");
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setPartSerialNumber(partnumber.getPartSn().trim());
		return isOk;
		
	}



}
