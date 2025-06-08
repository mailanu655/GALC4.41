package com.honda.galc.service.oif;

import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.IService;

public interface IMissionShippingTransactionService extends IService {
	/**
	 * Description: Sends 50A message for mission products.
	 * 
	 * @param url Destination URL for the message to be sent.
	 * @param product Product being shipped.
	 * @param processPoint Process point to update tracking status.
	 * @return returns true if the shipping transaction can be performed.
	 */
	boolean sendMission50AShippingTransaction(String url, BaseProduct product, String processPoint);
}
