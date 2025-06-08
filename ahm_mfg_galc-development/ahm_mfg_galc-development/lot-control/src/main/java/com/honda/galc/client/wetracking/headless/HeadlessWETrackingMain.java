package com.honda.galc.client.wetracking.headless;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.lang.reflect.Constructor;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.events.wetracking.WeldOnNextLotRequest;
import com.honda.galc.client.events.wetracking.WeldOnProductCompleteRequest;
import com.honda.galc.client.events.wetracking.WeldTrackingResponse;
import com.honda.galc.client.headless.IHeadlessMain;
import com.honda.galc.client.wetracking.communicator.WeldTrackingDeviceCommunicator;
import com.honda.galc.data.DataContainer;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.WETrackingNextLotService;
import com.honda.galc.service.WETrackingProductCompleteService;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HeadlessWETrackingMain</code> is an headless application response to communicate with GALC and PLC device.
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
public class HeadlessWETrackingMain implements IHeadlessMain, DeviceListener {

	private WeldTrackingDeviceCommunicator _communicator;
	private Application application;

	/**
	 * Gets the communicator base on DB property configuration "COMMUNICATOR_CLASS".
	 *
	 * @return the communicator
	 */
	private WeldTrackingDeviceCommunicator getCommunicator() {
		if (_communicator == null) {
			try {
				String clazzName = PropertyService.getProperty(application.getApplicationId(), "COMMUNICATOR_CLASS");
				Constructor<?> c = Class.forName(clazzName).getDeclaredConstructor();
				_communicator = (WeldTrackingDeviceCommunicator)c.newInstance();
			} catch(Exception e) {
				getLogger().error("Bad COMMUNICATOR_CLASS configuration.");
			}
		}
		return _communicator;
	}

	public HeadlessWETrackingMain() {
		super();
	}

	public void initialize(ApplicationContext appContext,
			Application application) {
		this.application = application;
		getCommunicator().start(application, this);
	}


	public IDeviceData received(String clientId, IDeviceData event) {
		DataContainer dc; //hold response from WETrackingService
		if (event instanceof WeldOnNextLotRequest) {
			WETrackingNextLotService nextLotService = ServiceFactory.getService(WETrackingNextLotService.class);
			WeldOnNextLotRequest request = (WeldOnNextLotRequest) event;
			dc = nextLotService.execute(request.convertToDataContainer());
			WeldTrackingResponse response = getCommunicator().convertToResponse(dc, request);
			if(getCommunicator().processResponse(response)) {
				nextLotService.confirmSend(dc);
			}
		} else if (event instanceof WeldOnProductCompleteRequest) {
			WETrackingProductCompleteService productCompleteService = ServiceFactory.getService(WETrackingProductCompleteService.class);
			WeldOnProductCompleteRequest request = (WeldOnProductCompleteRequest) event;
			dc = productCompleteService.execute(request.convertToDataContainer());
			WeldTrackingResponse response = getCommunicator().convertToResponse(dc, request);
			getCommunicator().processResponse(response);
		} else {
			getLogger().error("Illegal argument type.");
		}
		return null;
	}

}
