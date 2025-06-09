package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.AlarmContact;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "alarmcontacts", formBackingObject = AlarmContact.class)
@RequestMapping("/alarmcontacts")
@Controller
public class AlarmContactController {
}
