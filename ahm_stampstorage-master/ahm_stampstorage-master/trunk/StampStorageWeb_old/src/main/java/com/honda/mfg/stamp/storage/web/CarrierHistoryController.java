package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.CarrierHistory;
import com.honda.mfg.stamp.conveyor.domain.CarrierHistoryFinderCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "carrierhistories", formBackingObject = CarrierHistory.class)
@RequestMapping("/carrierhistories")
@Controller
public class CarrierHistoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CarrierHistoryController.class);

    private static final String HISTORY_CARRIER_NUMBER_FILTER = "historyCarrierNumberFilter";
    private static final String HISTORY_CURRENT_LOCATION_FILTER = "historyCurrentLocationFilter";
    private static final String HISTORY_DEST_LOCATION_FILTER = "historyDestinationLocationFilter";
    private static final int COOKIE_MAX_AGE = 900;

    private CarrierHistoryFinderCriteria finderCriteria;
}
