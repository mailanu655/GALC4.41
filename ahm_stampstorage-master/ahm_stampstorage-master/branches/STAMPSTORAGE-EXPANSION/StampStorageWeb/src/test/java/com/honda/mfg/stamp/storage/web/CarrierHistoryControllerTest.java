package com.honda.mfg.stamp.storage.web;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CarrierHistoryControllerTest {
	@Test
	public void successfullyTestCarrierHistoryController() {
		CarrierHistoryController controller = new CarrierHistoryController();
		org.springframework.ui.Model model = mock(org.springframework.ui.Model.class);

		controller.createForm(model);
	}
}
