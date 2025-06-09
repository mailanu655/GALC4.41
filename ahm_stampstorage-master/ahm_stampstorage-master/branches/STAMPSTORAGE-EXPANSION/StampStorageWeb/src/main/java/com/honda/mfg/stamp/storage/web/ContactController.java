package com.honda.mfg.stamp.storage.web;

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

import com.honda.mfg.stamp.conveyor.domain.Contact;

@RequestMapping("/contacts")
@Controller
public class ContactController {

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid Contact contact, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("contact", contact);
			return "contacts/create";
		}
		uiModel.asMap().clear();
		contact.persist();
		return "redirect:/contacts/" + encodeUrlPathSegment(contact.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("contact", new Contact());
		return "contacts/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("contact", Contact.findContact(id));
		uiModel.addAttribute("itemId", id);
		return "contacts/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			uiModel.addAttribute("contacts",
					Contact.findContactEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
			float nrOfPages = (float) Contact.countContacts() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("contacts", Contact.findAllContacts());
		}
		return "contacts/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid Contact contact, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("contact", contact);
			return "contacts/update";
		}
		uiModel.asMap().clear();
		contact.merge();
		return "redirect:/contacts/" + encodeUrlPathSegment(contact.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("contact", Contact.findContact(id));
		return "contacts/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Contact.findContact(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/contacts";
	}

	@ModelAttribute("contacts")
	public Collection<Contact> populateContacts() {
		return Contact.findAllContacts();
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
