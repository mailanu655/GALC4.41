package com.honda.mfg.stamp.storage.web;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 10:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlarmDefinitionControllerTest {

    @Test
    public void successfullyTestAlarmDefinitionController(){
        AlarmDefinitionController controller = new AlarmDefinitionController();
        assertNotNull(controller.populateALARM_CATEGORY());
        assertNotNull(controller.populateSEVERITY());
    }
}
