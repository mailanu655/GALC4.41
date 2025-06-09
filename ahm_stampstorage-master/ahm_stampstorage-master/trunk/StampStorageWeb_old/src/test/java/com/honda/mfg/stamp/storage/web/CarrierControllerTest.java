package com.honda.mfg.stamp.storage.web;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class CarrierControllerTest {
     @Test
    public void successfullyTestCarrierController() {
          CarrierController controller = new CarrierController();
         assertNotNull(controller.populateCarrierStatuses());
         assertNotNull(controller.populateDefectTypes()) ;
         assertNotNull(controller.populatePresses());
    }
}
