package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierFinderCriteria;
import com.honda.mfg.stamp.storage.service.CarrierManagementService;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "carriers", formBackingObject = Carrier.class)
@RequestMapping("/carriers")
@Controller
public class CarrierController {

    private static final Logger LOG = LoggerFactory.getLogger(CarrierController.class);

    private static final String CARRIER_NUMBER_FILTER = "carrierNumberFilter";
    private static final String CARRIER_STATUS_FILTER = "carrierStatusFilter";
    private static final String CURRENT_LOCATION_FILTER = "currentLocationFilter";
    private static final String DEST_LOCATION_FILTER = "destinationLocationFilter";
    private static final String ORIGIN_LOCATION_FILTER = "originationLocationFilter";
    private static final String DIE_ID_FILTER = "dieFilter";
    private static final String PROD_RUN_FILTER = "prodRunFilter";
    private static final int COOKIE_MAX_AGE = 900;

    @Autowired
    CarrierManagementService carrierManagementService;
    @Autowired
    CarrierManagementServiceProxy carrierManagementServiceProxy;
    private Carrier previousInspectedCarrier;
    private Carrier previousReworkedCarrier;

    private CarrierFinderCriteria finderCriteria;
}
