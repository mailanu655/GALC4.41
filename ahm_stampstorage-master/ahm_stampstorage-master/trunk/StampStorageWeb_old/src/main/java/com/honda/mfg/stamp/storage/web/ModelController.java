package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@RooWebScaffold(path = "models", formBackingObject = Model.class)
@RequestMapping("/models")
@Controller
public class ModelController {
     private static final Logger LOG = LoggerFactory.getLogger(ModelController.class);
    @ModelAttribute("dies")
    public Collection<Die> populateDies() {
        return Die.findAllDies();
    }
}
