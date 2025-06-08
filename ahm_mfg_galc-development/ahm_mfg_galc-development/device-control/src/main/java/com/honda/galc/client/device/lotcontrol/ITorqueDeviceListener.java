package com.honda.galc.client.device.lotcontrol;

import com.honda.galc.device.IDeviceUser;
import com.honda.galc.openprotocol.model.CommandAccepted;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.openprotocol.model.MultiSpindleResultUpload;

/**
 * @author Subu Kathiresan
 * Feb 22, 2009
 */
public interface ITorqueDeviceListener extends IDeviceUser
{
	public String getId();
	
	public void processLastTighteningResult(String deviceId,LastTighteningResult tResult);
	
	public void handleStatusChange(String deviceId,TorqueDeviceStatusInfo statusInfo);

	public void processMultiSpindleResult(String deviceId,MultiSpindleResultUpload multiSpindleResult);
	
	public void handleCommandAccepted(String deviceId, CommandAccepted commandAccepted);
}
