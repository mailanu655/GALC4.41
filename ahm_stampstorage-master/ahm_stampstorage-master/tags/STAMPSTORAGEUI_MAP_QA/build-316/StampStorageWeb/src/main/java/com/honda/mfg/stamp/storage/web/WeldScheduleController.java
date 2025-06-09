package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.WeldSchedule;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

@RequestMapping("/weldschedules")
@Controller
public class WeldScheduleController {

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid WeldSchedule weldSchedule, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("weldSchedule", weldSchedule);
            return "weldschedules/create";
        }
        uiModel.asMap().clear();
        weldSchedule.persist();
        return "redirect:/weldschedules/" + encodeUrlPathSegment(weldSchedule.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("weldSchedule", new WeldSchedule());
        return "weldschedules/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("weldschedule", WeldSchedule.findWeldSchedule(id));
        uiModel.addAttribute("itemId", id);
        return "weldschedules/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("weldschedules", WeldSchedule.findWeldScheduleEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) WeldSchedule.countWeldSchedules() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("weldschedules", WeldSchedule.findAllWeldSchedules());
        }
        return "weldschedules/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid WeldSchedule weldSchedule, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("weldSchedule", weldSchedule);
            return "weldschedules/update";
        }
        uiModel.asMap().clear();
        weldSchedule.merge();
        return "redirect:/weldschedules/" + encodeUrlPathSegment(weldSchedule.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("weldSchedule", WeldSchedule.findWeldSchedule(id));
        return "weldschedules/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        WeldSchedule.findWeldSchedule(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/weldschedules";
    }

	@ModelAttribute("weldschedules")
    public Collection<WeldSchedule> populateWeldSchedules() {
        return WeldSchedule.findAllWeldSchedules();
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
