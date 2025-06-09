package com.honda.mfg.stamp.storage.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.ValidDestination;

@RequestMapping("/validdestinations")
@Controller
public class ValidDestinationController {

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid ValidDestination validDestination, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		String existsMsg = "";
		Long existCount = (long) 0;

		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("validDestination", validDestination);
			return "validdestinations/create";
		}

		// Verify the valid destination pair does not already exist
		List<ValidDestination> validDestinationList = ValidDestination.findValidDestinationForGivenStopAndDestination(
				validDestination.getStop(), validDestination.getDestination());
		if (validDestinationList != null && validDestinationList.size() > 0) {
			existsMsg = " Valid destination already exists.";
			uiModel.addAttribute("existsMsg", existsMsg);
			return "validdestinations/create";
		}

		uiModel.asMap().clear();

		validDestination.persist();
		return "redirect:/validdestinations/"
				+ encodeUrlPathSegment(validDestination.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("validDestination", new ValidDestination());
		List dependencies = new ArrayList();
		if (Stop.countStops() == 0) {
			dependencies.add(new String[] { "stop", "stops" });
		}
		if (Stop.countStops() == 0) {
			dependencies.add(new String[] { "stop", "stops" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "validdestinations/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("validdestination", ValidDestination.findValidDestination(id));
		uiModel.addAttribute("itemId", id);
		return "validdestinations/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			uiModel.addAttribute("validdestinations", ValidDestination
					.findValidDestinationEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
			float nrOfPages = (float) ValidDestination.countValidDestinations() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("validdestinations", ValidDestination.findAllValidDestinations());
		}
		return "validdestinations/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid ValidDestination validDestination, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("validDestination", validDestination);
			return "validdestinations/update";
		}
		uiModel.asMap().clear();
		validDestination.merge();
		return "redirect:/validdestinations/"
				+ encodeUrlPathSegment(validDestination.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("validDestination", ValidDestination.findValidDestination(id));
		return "validdestinations/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		ValidDestination.findValidDestination(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/validdestinations";
	}

	@ModelAttribute("stops")
	public Collection<Stop> populateStops() {
		return Stop.findAllStops();
	}

	@ModelAttribute("validdestinations")
	public Collection<ValidDestination> populateValidDestinations() {
		return ValidDestination.findAllValidDestinations();
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
