package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.storage.service.CarrierManagementService;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "lanes", formBackingObject = StorageRow.class)
@RequestMapping("/lanes")
@Controller
public class LanesController {
     private static final Logger LOG = LoggerFactory.getLogger(LanesController.class);

    @Autowired
    private CarrierManagementService carrierManagementService;
    @Autowired
    CarrierManagementServiceProxy carrierManagementServiceProxy;
}
