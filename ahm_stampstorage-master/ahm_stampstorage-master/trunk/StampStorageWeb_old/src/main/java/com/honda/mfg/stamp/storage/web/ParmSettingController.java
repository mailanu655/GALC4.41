package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.ParmSetting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "parmsettings", formBackingObject = ParmSetting.class)
@RequestMapping("/parmsettings")
@Controller
public class ParmSettingController {
    private static final Logger LOG = LoggerFactory.getLogger(StopController.class);
}
