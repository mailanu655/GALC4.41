package com.honda.mfg.stamp.storage.web;


import com.honda.mfg.stamp.conveyor.domain.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "stops", formBackingObject = Stop.class)
@RequestMapping("/stops")
@Controller
public class StopController {
     private static final Logger LOG = LoggerFactory.getLogger(StopController.class);
}
