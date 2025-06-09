package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.AuditErrorLog;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "auditerrorlogs", formBackingObject = AuditErrorLog.class)
@RequestMapping("/auditerrorlogs")
@Controller
public class AuditErrorLogController {


}
