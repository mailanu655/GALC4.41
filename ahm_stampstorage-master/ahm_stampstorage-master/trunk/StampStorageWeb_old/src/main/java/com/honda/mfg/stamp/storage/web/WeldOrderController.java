package com.honda.mfg.stamp.storage.web;


import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.storage.service.CarrierManagementService;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RooWebScaffold(path = "weldorders", formBackingObject = WeldOrder.class)
@RequestMapping("/weldorders")

@Controller
public class WeldOrderController {
    private static final Logger LOG = LoggerFactory.getLogger(WeldOrderController.class);

    @Autowired
    CarrierManagementService carrierManagementService;

    @Autowired
    CarrierManagementServiceProxy carrierManagementServiceProxy;

    @ModelAttribute("models")
    public Collection<Model> populateModels() {
        //return Model.findAllModels();

        Collection<Model> models = Model.findAllModels();
        List<Model> activeModels = new ArrayList<Model>();

        for (Model model : models) {
            if (model.getActive()) {
                activeModels.add(model);
            }
        }

        return activeModels;
    }

    public List<OrderFulfillment> getOrderFulfillmentsByOrder(WeldOrder order) {

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(order);

        List<OrderFulfillment> fulfillmentList = new ArrayList<OrderFulfillment>();
        for (OrderFulfillment fulfillment : fulfillments) {
            CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(fulfillment.getId().getCarrierNumber());
            Stop currentLocation = carrierMes == null ? null : Stop.findStop(carrierMes.getCurrentLocation());
            fulfillment.setCurrentLocation(currentLocation);

            fulfillmentList.add(fulfillment);
        }
        return fulfillmentList;
    }
}
