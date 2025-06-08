/**
 * 
 */
package com.honda.galc.client.vinstamp;

import com.honda.galc.client.enumtype.FloorStampInfoCodes;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Dec 12, 2012
 */
public abstract class FloorStampBaseProcessor extends PlcDataReadyEventProcessorBase {

	public static String TRACKING_PROCESS_POINT_KEY = "TRACKING_PROCESS_POINT";
	
	private String _infoCode = "";
	private String _infoMessage = "";
	
	private int _infoPriority = -1;
	private String _trackingProcessPoint = "";
	
	public int getInfoPriority() {
		return _infoPriority;
	}
	
	public void setInfoPriority(int infoPriority) {
		_infoPriority = infoPriority;
	}
	
	public void setInfoCode(String infoCode) {
		_infoCode = infoCode;
	}
	
	public String getInfoCode() {
		return _infoCode;
	}
	
	public void setInfoMessage(String infoMessage) {
		_infoMessage = infoMessage;
	}

	public String getInfoMessage() {
		return _infoMessage;
	}
	
	public String getTrackingProcessPoint() {
		if (_trackingProcessPoint.equals("")) {
			_trackingProcessPoint = PropertyService.getProperty(getBean().getTerminalId(), TRACKING_PROCESS_POINT_KEY);
		}
		return _trackingProcessPoint;
	}

	public void setTrackingProcessPoint(String trackingProcessPoint) {
		_trackingProcessPoint = trackingProcessPoint;
	}
	

	public boolean doesVinExist(String vin) {
		Frame frame;
		try {
			frame = getFrameDao().findByKey(vin);
			if (frame != null && frame.getProductId() != null && !frame.getProductId().trim().equals("")) {
				getBean().setProductId(frame.getProductId());
				getBean().setProductSpecCode(frame.getProductSpecCode().trim());
				return true;
			}
		} catch(Exception ex) {}
		return false;
	}
	
	
	// Update the info code and message if the info priority is more significant
	public void updateVinStampInfo(FloorStampInfoCodes vinStampInfo, String vin) {
		if(vinStampInfo.ordinal() > getInfoPriority()) {
			setInfoPriority(vinStampInfo.ordinal());
			setInfoCode("" + vinStampInfo.getInfoCode());
			setInfoMessage(vinStampInfo.getInfoMessage(vin));
		}
	}

}
