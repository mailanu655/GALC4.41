package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.WeldSchedule;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "weldschedules", formBackingObject = WeldSchedule.class)
@RequestMapping("/weldschedules")
@Controller
public class WeldScheduleController {
}
