package com.honda.mfg.stamp.storage.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.mock;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class WeldScheduleControllerTest {


    @Test
    public void successfullyTestWeldScheduleController() {
        org.springframework.ui.Model model = mock(org.springframework.ui.Model.class);
        WeldScheduleController controller = new WeldScheduleController();
        controller.createForm(model);
    }


}
