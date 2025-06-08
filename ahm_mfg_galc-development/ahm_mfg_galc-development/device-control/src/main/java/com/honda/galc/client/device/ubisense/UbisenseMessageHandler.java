package com.honda.galc.client.device.ubisense;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.UbisenseTelegram;
import com.honda.galc.net.MessageHandler;
import com.honda.galc.service.property.PropertyService;

/**
 * Ubisense (ACS) device is a server software communicating with GALC 
 * client. GALC Lot Control client would communicate with Ubisense (ACS) 
 * through a TCP/IP socket connection by registering CLIENT_ID upon launch.
 * 
 * @author Bernard Leong
 * @date Jun 21, 2017
 */
public class UbisenseMessageHandler extends MessageHandler<String>{
	private static String UBISENSE_PROPERTIES = "UBISENSE PROPERTIES";
	private static String	UBISENSE_NUMBER_OF_RETRIES = "UBISENSE NUMBER OF RETRIES";
	private static String	UBISENSE_RETRY_DELAY = "UBISENSE RETRY DELAY"; 
	private UbisenseDevice device;
	private UbisenseTelegram ubisenseMsg;
	private boolean retry = true;
	private int productIdQueryRetryAttempts = 0; 
	private int	maxProductIdQueryRetries = 0;
	private int delayBetweenProductIdRetryAttempts = 0;
	
	/**
	 * Answer an Integer value that is either for the specific <componentId>, UBISENSE_PROPERTIES or the default value of 5 
	 */
	private static Integer getUbisenseProperty(String componentId, String propertyName, int defaultValue) {
		Integer propertyValue = PropertyService.getPropertyInt(componentId, propertyName);
		return propertyValue == null
				? PropertyService.getPropertyInt(UBISENSE_PROPERTIES, propertyName, 5)
				: propertyValue; 
	}
	
	public UbisenseMessageHandler(UbisenseDevice device) {
		this.device = device;
		maxProductIdQueryRetries = getUbisenseProperty(device.getClientId(), UBISENSE_NUMBER_OF_RETRIES, 5);
		delayBetweenProductIdRetryAttempts = getUbisenseProperty(device.getClientId(), UBISENSE_RETRY_DELAY, 5); 
	}

	private Logger getLogger() {
		return device.getLogger();
	}
	
	@Override
	public void processItem(String telegram) {
		ubisenseMsg = new UbisenseTelegram(telegram);
		if (ubisenseMsg.isNotAValidTelegram())
			return;
		if (ubisenseMsg.isReadyTelegram()) {
			// Successful initialization
			connectedToUbisense(true);
			return;
		}

		if (device.isConnected())
			processTelegram(telegram);
		else
			retryInitialization(ubisenseMsg);
	}

	private void resedProductIdQuery() {
		try {
			Thread.sleep(delayBetweenProductIdRetryAttempts*1000);
			getLogger().info("Resending Ubisense query telegram for " + ubisenseMsg.getReceivedProductId());
			device.sendQueryTelegram(ubisenseMsg.getReceivedProductId());
			productIdQueryRetryAttempts++;
		} catch (InterruptedException e) {
			//no-op not an issue
		}
	}
	
	private void processTelegram(String telegram) {
		// Tool update telegram
		if (ubisenseMsg.isStateTelegram()) {
			if (ubisenseMsg.isToolStatusTelegram()) {
				// Change in torque tool status
				device.notifyToolStatusListeners(telegram);
			} else if (ubisenseMsg.isNoSubsequentVinQueryReply()) {
				if (shouldRetryProductIdQuery()) {
					resedProductIdQuery();
				}
			} else if (ubisenseMsg.isSuppliedQueryVinInvalid()){
				getLogger().info("Invalid VIN supplied in query request");
			} else {
				// Ubisense sending the next expected product ID to be processed
				device.notifyProductIdStatusListeners(telegram);
				resetQueryResendAttempts();
			}
		} 
		// Acknowledgment for all telegrams
		device.send(ubisenseMsg.ackTelegram());
	}

	public void resetQueryResendAttempts() {
		productIdQueryRetryAttempts = 0;
	}

	private boolean shouldRetryProductIdQuery() {
		return productIdQueryRetryAttempts <= maxProductIdQueryRetries;
	}
	
	private void retryInitialization(UbisenseTelegram ubisenseMsg) {
		// Initial communication failed and attempting 1 retry only
		if (ubisenseMsg.isResponseTelegram()) {
			if (retry) {
				device.sendInitTelegram();
				retry = false;
			} else 
				// Notify client that initialization to Ubisense failed on retry
				connectedToUbisense(false);
		}
	}
	
	private void connectedToUbisense(boolean connected) {
		device.setEnabled(connected);
		device.setConnected(connected);
		device.notifyToolStatusListeners(connected);
	}
}
