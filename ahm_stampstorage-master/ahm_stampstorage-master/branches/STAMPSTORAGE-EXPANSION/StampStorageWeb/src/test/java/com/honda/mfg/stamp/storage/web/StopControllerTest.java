package com.honda.mfg.stamp.storage.web;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class StopControllerTest {

	@Test
	public void successfullyTestStopController() {
		StopController controller = new StopController();
		assertNotNull(controller.populateStopTypes());
		assertNotNull(controller.populateStopAreas());
		assertNotNull(controller.populateStopAvailabilitys());
	}
}
