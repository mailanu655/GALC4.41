package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.storage.service.CarrierManagementService;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequestMapping("/carrierreleases")
@Controller
public class CarrierReleaseController {

     @Autowired
     CarrierManagementService carrierManagementService;
     @Autowired
     CarrierManagementServiceProxy carrierManagementServiceProxy;

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid CarrierRelease carrierRelease, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("carrierRelease", carrierRelease);
            return "carrierreleases/create";
        }
        uiModel.asMap().clear();
        carrierRelease.persist();
        return "redirect:/carrierreleases/" + encodeUrlPathSegment(carrierRelease.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("carrierRelease", new CarrierRelease());
        List dependencies = new ArrayList();
        if (Stop.countStops() == 0) {
            dependencies.add(new String[]{"stop", "stops"});
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "carrierreleases/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("carrierrelease", CarrierRelease.findCarrierRelease(id));
        uiModel.addAttribute("itemId", id);
        return "carrierreleases/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("carrierreleases", CarrierRelease.findCarrierReleaseEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) CarrierRelease.countCarrierReleases() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("carrierreleases", CarrierRelease.findAllCarrierReleases());
        }
        return "carrierreleases/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid CarrierRelease carrierRelease, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("carrierRelease", carrierRelease);
            return "carrierreleases/update";
        }
        uiModel.asMap().clear();
        carrierRelease.merge();
        return "redirect:/carrierreleases/" + encodeUrlPathSegment(carrierRelease.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("carrierRelease", CarrierRelease.findCarrierRelease(id));
        return "carrierreleases/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CarrierRelease release = CarrierRelease.findCarrierRelease(id);
        release.remove();
        Integer carrierNumber = Integer.parseInt(id.toString());
        CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
        if (carrierMes != null) {
            Stop currentLocation = Stop.findStop(carrierMes.getCurrentLocation());
            if (currentLocation.isRowStop()) {
            	//2013-02-01:VB:remove storage state dependency
                carrierManagementServiceProxy.reorderCarriersInRow(carrierMes.getCurrentLocation());
            }
        }
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/carrierreleases";
    }

	@ModelAttribute("carrierreleases")
    public Collection<CarrierRelease> populateCarrierReleases() {
        return CarrierRelease.findAllCarrierReleases();
    }

	@ModelAttribute("stops")
    public Collection<Stop> populateStops() {
        return Stop.findAllStops();
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
