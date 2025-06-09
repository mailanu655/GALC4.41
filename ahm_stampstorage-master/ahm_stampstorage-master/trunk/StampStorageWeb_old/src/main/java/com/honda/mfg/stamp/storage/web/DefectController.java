package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.Defect;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "defects", formBackingObject = Defect.class)
@RequestMapping("/defects")
@Controller
public class DefectController {

}
