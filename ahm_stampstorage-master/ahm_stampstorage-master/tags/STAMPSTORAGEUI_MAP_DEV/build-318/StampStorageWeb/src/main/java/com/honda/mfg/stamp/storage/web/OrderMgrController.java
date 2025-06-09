package com.honda.mfg.stamp.storage.web;


import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/ordermgrs")
@Controller
public class OrderMgrController {
     private static final Logger LOG = LoggerFactory.getLogger(OrderMgrController.class);

    @ModelAttribute("stops")
    public Collection<Stop> populateStops() {
        return Stop.findAllStops();
    }

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid OrderMgr orderMgr, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("orderMgr", orderMgr);
            return "ordermgrs/create";
        }
        uiModel.asMap().clear();
         SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        if (principal != null) {
            LOG.info("updated by user " + principal.getUsername());
        }
        orderMgr.persist();
        return "redirect:/ordermgrs/" + encodeUrlPathSegment(orderMgr.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("orderMgr", new OrderMgr());
        return "ordermgrs/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("ordermgr", OrderMgr.findOrderMgr(id));
        uiModel.addAttribute("itemId", id);
        return "ordermgrs/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("ordermgrs", OrderMgr.findOrderMgrEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) OrderMgr.countOrderMgrs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ordermgrs", OrderMgr.findAllOrderMgrs());
        }
        return "ordermgrs/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid OrderMgr orderMgr, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("orderMgr", orderMgr);
            return "ordermgrs/update";
        }
        uiModel.asMap().clear();
         SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        if (principal != null) {
            LOG.info("updated by user " + principal.getUsername());
        }
        orderMgr.merge();
        return "redirect:/ordermgrs/" + encodeUrlPathSegment(orderMgr.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        OrderMgr mgr = OrderMgr.findOrderMgr(id);
        uiModel.addAttribute("orderMgr", mgr);

        return "ordermgrs/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
         SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        if (principal != null) {
            LOG.info("updated by user " + principal.getUsername());
        }
        OrderMgr.findOrderMgr(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ordermgrs";
    }

	@ModelAttribute("ordermgrs")
    public Collection<OrderMgr> populateOrderMgrs() {
        return OrderMgr.findAllOrderMgrs();
    }

	@ModelAttribute("weldorders")
    public Collection<WeldOrder> populateWeldOrders() {
        return WeldOrder.findAllWeldOrders();
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
