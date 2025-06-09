package com.honda.mfg.stamp.storage.web;


import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@RooWebScaffold(path = "ordermgrs", formBackingObject = OrderMgr.class)
@RequestMapping("/ordermgrs")
@Controller
public class OrderMgrController {
     private static final Logger LOG = LoggerFactory.getLogger(OrderMgrController.class);

    @ModelAttribute("stops")
    public Collection<Stop> populateStops() {
        return Stop.findAllStops();
    }
}
