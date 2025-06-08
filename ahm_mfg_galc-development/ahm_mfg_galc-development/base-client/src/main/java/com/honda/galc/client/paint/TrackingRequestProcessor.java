package com.honda.galc.client.paint;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.events.TrackingRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Dec 14, 2012
 */
public class TrackingRequestProcessor extends PlcDataReadyEventProcessorBase implements IPlcDataReadyEventProcessor<TrackingRequest> {

	private String _terminalId;

	public synchronized boolean execute(TrackingRequest deviceData) {
		try {		
			String productId = StringUtils.trimToEmpty(deviceData.getProductId());
			return trackProduct(productId);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error processing Tracking Product " + StringUtils.trimToEmpty(deviceData.getProductId()));
		} finally {
			getBean().put("eqDataReady", new StringBuilder("0"));
		}
		return false;
	}

	public void postPlcWrite(boolean writeSucceeded) {
	}

	public void validate() {
	}

	public String getTerminalId() {
		return _terminalId;
	}

	public void setTerminalId(String terminalId) {
		_terminalId = terminalId;
	}
}
