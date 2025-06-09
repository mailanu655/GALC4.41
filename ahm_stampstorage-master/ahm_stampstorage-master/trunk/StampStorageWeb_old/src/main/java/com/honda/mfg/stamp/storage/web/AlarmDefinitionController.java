package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.AlarmDefinition;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "alarms", formBackingObject = AlarmDefinition.class)
@RequestMapping("/alarms")
@Controller
public class AlarmDefinitionController {

}
