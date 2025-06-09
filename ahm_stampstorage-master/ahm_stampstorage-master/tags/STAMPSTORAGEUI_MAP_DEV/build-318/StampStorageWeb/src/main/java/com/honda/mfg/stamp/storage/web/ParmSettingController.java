package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.ParmSetting;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
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

@RequestMapping("/parmsettings")
@Controller
public class ParmSettingController {
    private static final Logger LOG = LoggerFactory.getLogger(StopController.class);

	@RequestMapping(method = RequestMethod.POST)
    public String create(ParmSetting parmSetting, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("parmSetting", parmSetting);
            return "parmsettings/create";
        }
          if (parmSetting.getFieldvalue().length() == 0) {
            uiModel.addAttribute("parmSetting", parmSetting);
            return "parmsettings/create";
        }

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        if (principal != null) {
            LOG.info("updated by user " + principal.getUsername());
        }

        parmSetting.setUpdatedby(principal.getUsername());
        parmSetting.setUpdatetstp(new Timestamp(System.currentTimeMillis()));
        uiModel.asMap().clear();

        parmSetting.persist();
        return "redirect:/parmsettings/" + encodeUrlPathSegment(parmSetting.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("parmSetting", new ParmSetting());
        return "parmsettings/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("parmsetting", ParmSetting.findParmSetting(id));
        uiModel.addAttribute("itemId", id);
        return "parmsettings/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("parmsettings", ParmSetting.findParmSettingEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) ParmSetting.countParmSettings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("parmsettings", ParmSetting.findAllParmSettings());
        }
        return "parmsettings/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(ParmSetting parmSetting, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("parmSetting", parmSetting);
            return "parmsettings/update";
        }

//        if (parmSetting.getFieldvalue().length() == 0) {
//            uiModel.addAttribute("parmSetting", parmSetting);
//            return "parmsettings/update";
//        }

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        if (principal != null) {
            LOG.info("updated by user " + principal.getUsername());
        }
        ParmSetting ps = ParmSetting.findParmSetting(parmSetting.getId()) ;
        ps.setFieldvalue(parmSetting.getFieldvalue());
        ps.setDescription(parmSetting.getDescription());
        ps.setUpdatedby(principal.getUsername());
        ps.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

        ps.merge();

        uiModel.asMap().clear();
        return "redirect:/parmsettings/" + encodeUrlPathSegment(parmSetting.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("parmSetting", ParmSetting.findParmSetting(id));
        return "parmsettings/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ParmSetting.findParmSetting(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/parmsettings";
    }

	@ModelAttribute("parmsettings")
    public Collection<ParmSetting> populateParmSettings() {
        return ParmSetting.findAllParmSettings();
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
