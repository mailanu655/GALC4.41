package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.storage.service.CarrierManagementService;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "carrierreleases", formBackingObject = CarrierRelease.class)
@RequestMapping("/carrierreleases")
@Controller
public class CarrierReleaseController {

     @Autowired
     CarrierManagementService carrierManagementService;
     @Autowired
     CarrierManagementServiceProxy carrierManagementServiceProxy;
}
