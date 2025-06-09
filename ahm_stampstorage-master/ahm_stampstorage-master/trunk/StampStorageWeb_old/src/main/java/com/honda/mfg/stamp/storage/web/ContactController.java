package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.Contact;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "contacts", formBackingObject = Contact.class)
@RequestMapping("/contacts")
@Controller
public class ContactController {
}
