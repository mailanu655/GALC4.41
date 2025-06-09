package com.honda.mfg.stamp.storage.web;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class CarrierMesControllerTest {

	@Test
	public void successfullyTestCarrierMesController() {
		CarrierMesController controller = new CarrierMesController();
		assertNotNull(controller.populateCarrierStatuses());
		assertNotNull(controller.populatePresses());
	}
}
