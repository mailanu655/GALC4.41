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
public class DefectControllerTest {

    @Test
    public void successfullyTestDefectController() {
        DefectController defectController = new DefectController();
        assertNotNull(defectController.populateDEFECT_TYPEs());
        assertNotNull(defectController.populateREWORK_METHODs());
    }
}
