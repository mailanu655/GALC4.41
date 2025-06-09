package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.ValidDestination;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "validdestinations", formBackingObject = ValidDestination.class)
@RequestMapping("/validdestinations")
@Controller
public class ValidDestinationController {
}
