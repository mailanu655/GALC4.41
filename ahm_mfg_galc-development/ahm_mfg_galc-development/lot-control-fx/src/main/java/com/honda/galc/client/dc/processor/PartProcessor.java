package com.honda.galc.client.dc.processor;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * 
 * <h3>PartProcessor Class description</h3>
 * <p> PartProcessor description </p>
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
 * Feb 24, 2014
 *
 *
 */
public class PartProcessor extends AbstractDataCollectionProcessor<PartSerialNumber>implements IPartProcessor{
	
	
	public PartProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	}

	public boolean execute(PartSerialNumber data) {
		return false;
	}

	public void init() {
		
	}

	public IDeviceData processReceived(PartSerialNumber deviceData) {
		if(processReceived(deviceData.getPartSn()))
			getController().getFsm().partSnOk(deviceData.getPartSn());
		return null;
	}
	
	public boolean processReceived(String partSN) {
		try{
			String resultPartSN = validPartSerialNumber(partSN);
			getController().getFsm().partSnOk(resultPartSN);
			return true;
		}catch(TaskException ex) {
			getController().getFsm().partSnNg(partSN, "PART SN", ex.getMessage());
			return false;
		}
	}
	
	public String validPartSerialNumber(String partSN) {
		
		// TODO use checker framework
		return partSN;
	}

	public void registerDeviceListener(DeviceListener listener) {
		// TODO Auto-generated method stub
		
	}
}
