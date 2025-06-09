package com.honda.mfg.stamp.storage.web;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlarmContactControllerTest {

	@Test
	public void successfullyTestAlarmContactController() {
		AlarmContactController controller = new AlarmContactController();
		assertNotNull(controller.populateContactTypes());
	}
}
