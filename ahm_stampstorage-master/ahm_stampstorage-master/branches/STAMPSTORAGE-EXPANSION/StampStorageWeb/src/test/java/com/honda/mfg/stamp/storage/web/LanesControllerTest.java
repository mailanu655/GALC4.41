package com.honda.mfg.stamp.storage.web;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class LanesControllerTest {

	@Test
	public void successfullyTestLanesController() {
		LanesController lanesController = new LanesController();
		assertNotNull(lanesController.populateCarrierStatuses());
		assertNotNull(lanesController.populatePresses());
		assertNotNull(lanesController.populateStorageAreas());
	}
}
