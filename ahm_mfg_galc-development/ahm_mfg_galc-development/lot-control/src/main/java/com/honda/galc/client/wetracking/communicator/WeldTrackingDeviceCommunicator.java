package com.honda.galc.client.wetracking.communicator;


import com.honda.galc.client.events.wetracking.WeldTrackingRequest;
import com.honda.galc.client.events.wetracking.WeldTrackingResponse;
import com.honda.galc.data.DataContainer;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.entity.conf.Application;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>WeldTrackingDeviceCommunicator</code> is interface of Device communicator.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Zhqiang Wang
 */
public interface WeldTrackingDeviceCommunicator {
	
	/**
	 * Start to communicate with Device.
	 *
	 * @param application the application
	 * @param listener the listener
	 */
	public abstract void start(Application application, DeviceListener listener);
	
	/**
	 * Stop the communicate.
	 */
	public abstract void stop();
	
	/**
	 * Process response from GALC service.
	 *
	 * @param nextLotResponse the next lot response
	 * @return true, if successful
	 */
	public abstract boolean processResponse(WeldTrackingResponse nextLotResponse);
	
	/**
	 * Convert DataContainer to weld tracking response.
	 *
	 * @param dc the dc
	 * @param request the request
	 * @return the fins device weld tracking response
	 */
	public abstract WeldTrackingResponse convertToResponse(DataContainer dc, WeldTrackingRequest request);

}
