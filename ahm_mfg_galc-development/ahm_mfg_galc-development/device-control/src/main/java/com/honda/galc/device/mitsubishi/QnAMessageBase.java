package com.honda.galc.device.mitsubishi;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceUtil;


/**
 * 
 * <h3>QnAMessageBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnAMessageBase description </p>
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
 * Nov 5, 2010
 *
 */
public class QnAMessageBase {
	protected Logger logger;
	
	protected String getDevice(String device) {
		int firstDigitIndex = DeviceUtil.indexOfFirstDigit(device);
		return device.substring(0, firstDigitIndex);
	}
	
	

}
