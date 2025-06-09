package com.honda.mfg.stamp.storage.web;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class DieControllerTest {

     @Test
    public void successfullyTestDieController() {
         DieController controller = new DieController();
         assertNotNull(controller.populatePartProductionVolumes());
    }
}
