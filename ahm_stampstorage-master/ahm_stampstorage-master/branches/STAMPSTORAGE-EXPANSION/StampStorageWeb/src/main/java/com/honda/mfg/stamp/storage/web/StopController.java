package com.honda.mfg.stamp.storage.web;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
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

import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

@RequestMapping("/stops")
@Controller
public class StopController {
	private static final Logger LOG = LoggerFactory.getLogger(StopController.class);

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid Stop stop, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("stop", stop);
			return "stops/create";
		}
		uiModel.asMap().clear();
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			String principal = httpServletRequest.getUserPrincipal().getName();
			if (principal != null) {
				LOG.info("updated by user " + principal);
			}
			stop.persist();
		} catch (JpaSystemException e) {
			uiModel.addAttribute("id", stop.getId());
			return "stops/exception";
		}
		return "redirect:/stops/" + encodeUrlPathSegment(stop.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("stop", new Stop());
		return "stops/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("stop", Stop.findStop(id));
		uiModel.addAttribute("itemId", id);
		return "stops/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			uiModel.addAttribute("stops",
					Stop.findStopEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
			float nrOfPages = (float) Stop.countStops() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("stops", Stop.findAllStops());
		}
		return "stops/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid Stop stop, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("stop", stop);
			return "stops/update";
		}
		uiModel.asMap().clear();
		SecurityContext context = SecurityContextHolder.getContext();
		String principal = httpServletRequest.getUserPrincipal().getName();
		if (principal != null) {
			LOG.info("updated by user " + principal);
		}
		stop.merge();
		return "redirect:/stops/" + encodeUrlPathSegment(stop.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("stop", Stop.findStop(id));
		return "stops/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		SecurityContext context = SecurityContextHolder.getContext();
		LdapUserDetailsImpl  principal = (LdapUserDetailsImpl)context.getAuthentication().getPrincipal();
		if (principal != null) {
			LOG.info("updated by user " + principal.getUsername());
		}
		Stop.findStop(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/stops";
	}

	@ModelAttribute("stops")
	public Collection<Stop> populateStops() {
		return Stop.findAllStops();
	}

	@ModelAttribute("stoptypes")
	public Collection<StopType> populateStopTypes() {
		return Arrays.asList(StopType.class.getEnumConstants());
	}

	@ModelAttribute("stopareas")
	public Collection<StopArea> populateStopAreas() {
		return Arrays.asList(StopArea.class.getEnumConstants());
	}

	@ModelAttribute("stopavailabilitys")
	public Collection<StopAvailability> populateStopAvailabilitys() {
		return Arrays.asList(StopAvailability.class.getEnumConstants());
	}

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest)
			throws UnsupportedEncodingException {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		return pathSegment;
	}
}
