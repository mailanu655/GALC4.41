package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.AlarmContact;
import com.honda.mfg.stamp.conveyor.domain.AlarmDefinition;
import com.honda.mfg.stamp.conveyor.domain.Contact;
import com.honda.mfg.stamp.conveyor.domain.enums.ContactType;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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

@RequestMapping("/alarmcontacts")
@Controller
public class AlarmContactController {

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid AlarmContact alarmContact, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("alarmContact", alarmContact);
            return "alarmcontacts/create";
        }
        uiModel.asMap().clear();
        alarmContact.persist();
        return "redirect:/alarmcontacts/" + encodeUrlPathSegment(alarmContact.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("alarmContact", new AlarmContact());
//        List dependencies = new ArrayList();
//        if (AlarmDefinition.countAlarmDefinitions() == 0) {
//            dependencies.add(new String[]{"alarmdefinition", "alarmdefinitions"});
//        }
//        if (Contact.countContacts() == 0) {
//            dependencies.add(new String[]{"contact", "contacts"});
//        }
 //       uiModel.addAttribute("dependencies", dependencies);
        return "alarmcontacts/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("alarmcontact", AlarmContact.findAlarmContact(id));
        uiModel.addAttribute("itemId", id);
        return "alarmcontacts/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("alarmcontacts", AlarmContact.findAlarmContactEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) AlarmContact.countAlarmContacts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("alarmcontacts", AlarmContact.findAllAlarmContacts());
        }
        return "alarmcontacts/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid AlarmContact alarmContact, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("alarmContact", alarmContact);
            return "alarmcontacts/update";
        }
        uiModel.asMap().clear();
        alarmContact.merge();
        return "redirect:/alarmcontacts/" + encodeUrlPathSegment(alarmContact.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("alarmContact", AlarmContact.findAlarmContact(id));
        return "alarmcontacts/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AlarmContact.findAlarmContact(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/alarmcontacts";
    }

	@ModelAttribute("alarmcontacts")
    public Collection<AlarmContact> populateAlarmContacts() {
        return AlarmContact.findAllAlarmContacts();
    }

	@ModelAttribute("alarmdefinitions")
    public Collection<AlarmDefinition> populateAlarmDefinitions() {
        return AlarmDefinition.findAllAlarmDefinitions();
    }

	@ModelAttribute("contacts")
    public Collection<Contact> populateContacts() {
        return Contact.findAllContacts();
    }

	@ModelAttribute("contacttypes")
    public Collection<ContactType> populateContactTypes() {
        return Arrays.asList(ContactType.class.getEnumConstants());
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
