package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "carriermes", formBackingObject = CarrierMes.class)
@RequestMapping("/carriermes")
@Controller
public class CarrierMesController {
    private static final Logger LOG = LoggerFactory.getLogger(CarrierMesController.class);

   // @Autowired
   // private CarrierManagementService carrierManagementService;
}
