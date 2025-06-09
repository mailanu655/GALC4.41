// MessageSinkInterface.java

package com.honda.mfg.stamp.storage.service.utils;

import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.service.StorageStateUpdateService;

public interface MessageSinkInterface {
	void put(Object objectSource, Message object) throws Exception;

	void setCarrierManager(StorageStateUpdateService carrierManager);

	StorageStateUpdateService getCarrierManager();

}
