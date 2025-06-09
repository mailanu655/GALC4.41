package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "storagerows", formBackingObject = StorageRow.class)
@RequestMapping("/storagerows")
@Controller
public class StorageRowController {
     @Autowired
     CarrierManagementServiceProxy carrierManagementServiceProxy;
}
