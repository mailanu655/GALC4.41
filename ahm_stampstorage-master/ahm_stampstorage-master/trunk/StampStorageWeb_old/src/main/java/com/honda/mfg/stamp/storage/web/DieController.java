package com.honda.mfg.stamp.storage.web;


import com.honda.mfg.stamp.conveyor.domain.Die;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "dies", formBackingObject = Die.class)
@RequestMapping("/dies")
@Controller
public class DieController {

}
