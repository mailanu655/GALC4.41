package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "orderfulfillments", formBackingObject = OrderFulfillment.class)
@RequestMapping("/orderfulfillments")
@Controller
public class OrderFulfillmentController {
}
