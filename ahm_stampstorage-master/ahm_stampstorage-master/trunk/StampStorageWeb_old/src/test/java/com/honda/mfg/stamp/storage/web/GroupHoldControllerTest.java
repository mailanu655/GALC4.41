package com.honda.mfg.stamp.storage.web;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupHoldControllerTest {

     @Test
    public void successfullyTestGroupHoldController() {
       GroupHoldController controller = new GroupHoldController();
        assertNotNull(controller.populateDEFECT_TYPEs());
        assertNotNull(controller.populateREWORK_METHODs());
    }
}
