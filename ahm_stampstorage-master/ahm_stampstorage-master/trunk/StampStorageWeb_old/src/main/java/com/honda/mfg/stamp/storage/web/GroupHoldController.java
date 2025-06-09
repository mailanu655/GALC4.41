package com.honda.mfg.stamp.storage.web;


import com.honda.mfg.stamp.conveyor.domain.GroupHoldFinderCriteria;
import com.honda.mfg.stamp.storage.service.CarrierManagementService;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 2/1/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
@RooWebScaffold(path = "groupholds", formBackingObject = GroupHoldFinderCriteria.class)
@RequestMapping("/groupholds")
@Controller
public class GroupHoldController {
     private static final Logger LOG = LoggerFactory.getLogger(GroupHoldController.class);


    private static final String HOLD_STATUS_FILTER="statusFilter";
    private static final String HOLD_ROW_FILTER = "rowFilter";
    private static final String HOLD_NUMBER_AFTER_FILTER = "numberAfterFilter";
    private static final String HOLD_NUMBER_BEFORE_FILTER = "numberBeforeFilter";
    private static final String HOLD_RUN_NUMBER_FILTER = "prodRunNumberFilter";
    private static final String HOLD_RUN_DATE_FILTER = "prodRunDateFilter";
    private static final String HOLD_DEFECT_COUNT = "defectCount";
    private static final String HOLD_DEFECT = "defect_";
    private static final String HOLD_ROW_PRODRUN_FILTER = "rowAndProdRun";
    private static final String HOLD_ROBOT_FILTER = "robot";
    private static final int COOKIE_MAX_AGE =900;


    @Autowired
    CarrierManagementService carrierManagementService;
    @Autowired
    CarrierManagementServiceProxy carrierManagementServiceProxy;

}
