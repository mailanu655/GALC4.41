package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/orderfulfillments")
@Controller
public class OrderFulfillmentController {

	private ConversionService conversionService;

	@Autowired
    public OrderFulfillmentController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid OrderFulfillment orderFulfillment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("orderFulfillment", orderFulfillment);
            return "orderfulfillments/create";
        }
        uiModel.asMap().clear();
        orderFulfillment.persist();
        return "redirect:/orderfulfillments/" + encodeUrlPathSegment(conversionService.convert(orderFulfillment.getId(), String.class), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("orderFulfillment", new OrderFulfillment());
        return "orderfulfillments/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") OrderFulfillmentPk id, Model uiModel) {
        uiModel.addAttribute("orderfulfillment", OrderFulfillment.findOrderFulfillment(id));
        uiModel.addAttribute("itemId", conversionService.convert(id, String.class));
        return "orderfulfillments/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("orderfulfillments", OrderFulfillment.findOrderFulfillmentEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) OrderFulfillment.countOrderFulfillments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("orderfulfillments", OrderFulfillment.findAllOrderFulfillments());
        }
        return "orderfulfillments/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid OrderFulfillment orderFulfillment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("orderFulfillment", orderFulfillment);
            return "orderfulfillments/update";
        }
        uiModel.asMap().clear();
        orderFulfillment.merge();
        return "redirect:/orderfulfillments/" + encodeUrlPathSegment(conversionService.convert(orderFulfillment.getId(), String.class), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") OrderFulfillmentPk id, Model uiModel) {
        uiModel.addAttribute("orderFulfillment", OrderFulfillment.findOrderFulfillment(id));
        return "orderfulfillments/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") OrderFulfillmentPk id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        OrderFulfillment.findOrderFulfillment(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/orderfulfillments";
    }

	@ModelAttribute("orderfulfillments")
    public Collection<OrderFulfillment> populateOrderFulfillments() {
        return OrderFulfillment.findAllOrderFulfillments();
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
