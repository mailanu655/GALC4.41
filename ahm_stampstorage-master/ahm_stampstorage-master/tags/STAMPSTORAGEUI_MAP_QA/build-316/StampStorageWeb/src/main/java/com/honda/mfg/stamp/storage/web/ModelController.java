package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
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

@RequestMapping("/models")
@Controller
public class ModelController {
     private static final Logger LOG = LoggerFactory.getLogger(ModelController.class);
    @ModelAttribute("dies")
    public Collection<Die> populateDies() {
        return Die.findAllDies();
    }

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Model model, BindingResult bindingResult, org.springframework.ui.Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("model", model);
            return "models/create";
        }
        uiModel.asMap().clear();
         SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        if (principal != null) {
            LOG.info("updated by user " + principal.getUsername());
        }
        model.persist();
        return "redirect:/models/" + encodeUrlPathSegment(model.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(org.springframework.ui.Model uiModel) {
        uiModel.addAttribute("model", new Model());
        return "models/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, org.springframework.ui.Model uiModel) {
        uiModel.addAttribute("model", Model.findModel(id));
        uiModel.addAttribute("itemId", id);
        return "models/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, org.springframework.ui.Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("models", Model.findModelEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Model.countModels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("models", Model.findAllModels());
        }
        return "models/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid Model model, BindingResult bindingResult, org.springframework.ui.Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("model", model);
            return "models/update";
        }
        uiModel.asMap().clear();
         SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        if (principal != null) {
            LOG.info("updated by user " + principal.getUsername());
        }
        model.merge();
        return "redirect:/models/" + encodeUrlPathSegment(model.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, org.springframework.ui.Model uiModel) {
        uiModel.addAttribute("model", Model.findModel(id));
        return "models/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, org.springframework.ui.Model uiModel) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        if (principal != null) {
            LOG.info("updated by user " + principal.getUsername());
        }
        Model.findModel(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/models";
    }

	@ModelAttribute("models")
    public Collection<Model> populateModels() {
        return Model.findAllModels();
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
