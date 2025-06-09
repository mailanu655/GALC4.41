package com.honda.mfg.stamp.storage.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.honda.mfg.stamp.conveyor.domain.AlarmContact;
import com.honda.mfg.stamp.conveyor.domain.AlarmDefinition;
import com.honda.mfg.stamp.conveyor.domain.AlarmDefinitionWrapper;
import com.honda.mfg.stamp.conveyor.domain.Contact;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmNotificationCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.AutoArchiveTime;
import com.honda.mfg.stamp.conveyor.domain.enums.ContactType;
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;

@RequestMapping("/alarms")
@Controller
public class AlarmDefinitionController {

	private static final Logger LOG = LoggerFactory.getLogger(AlarmDefinitionController.class);

	private static final String ACTIVE_ALARM_FILTER = "activeAlarmFilter";
	private static final int COOKIE_MAX_AGE = 900;

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid AlarmDefinition alarmDefinition, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("alarm", alarmDefinition);
			return "alarms/create";
		}
		uiModel.asMap().clear();
		alarmDefinition.persist();
		return "redirect:/alarms/" + encodeUrlPathSegment(alarmDefinition.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		AlarmDefinition alarmDefinition = new AlarmDefinition();

		uiModel.addAttribute("alarm", alarmDefinition);

		return "alarms/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("alarm", AlarmDefinition.findAlarmDefinition(id));
		uiModel.addAttribute("itemId", id);
		return "alarms/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel,
			HttpServletRequest httpServletRequest) {

		Boolean activeAlarms = getAlarmFilter(httpServletRequest);
		LOG.info(" active alarms-" + activeAlarms);
		if (page != null || size != null) {
			int sizeNo = size == null ? 50 : size.intValue();

			uiModel.addAttribute("alarms", AlarmDefinition.findAlarmEntriesByActiveStatus(
					page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo, activeAlarms));
			float nrOfPages = (float) AlarmDefinition.countActiveAlarms(activeAlarms) / sizeNo;

			if (nrOfPages > 20) {
				uiModel.addAttribute("maxPages", 20);
			} else {
				uiModel.addAttribute("maxPages",
						(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
			}
		} else {
			uiModel.addAttribute("alarms", AlarmDefinition.findAllAlarmDefinitions());
		}
		uiModel.addAttribute("active", activeAlarms);
		return "alarms/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(AlarmDefinitionWrapper alarmDefinitionWrapper, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
//        if (bindingResult.hasErrors()) {
//            uiModel.addAttribute("alarmwrapper", alarmDefinitionWrapper);
//              AlarmDefinition alarmDefinition = AlarmDefinition.findAlarmDefinition(alarmDefinitionWrapper.getId());
//            uiModel.addAttribute("alarmcontacts", populateAlarmContacts(alarmDefinition));
//            return "alarms/update";
//        }
		uiModel.asMap().clear();
		AlarmDefinition alarmDefinition = AlarmDefinition.findAlarmDefinition(alarmDefinitionWrapper.getId());
		if (alarmDefinition != null) {
			alarmDefinition.setAlarmNumber(alarmDefinitionWrapper.getAlarmNumber());
			alarmDefinition.setDescription(alarmDefinitionWrapper.getDescription());
			alarmDefinition.setLocation(alarmDefinitionWrapper.getLocation());
			alarmDefinition.setName(alarmDefinitionWrapper.getName());
			alarmDefinition.setNotificationCategory(alarmDefinitionWrapper.getNotificationCategory());
			alarmDefinition.setSeverity(alarmDefinitionWrapper.getSeverity());
			alarmDefinition.setActive(alarmDefinitionWrapper.getActive());
			alarmDefinition.setAutoArchiveTimeInMinutes(alarmDefinitionWrapper.getAutoArchiveTimeInMinutes());
			alarmDefinition.setNotificationRequired(alarmDefinitionWrapper.getNotificationRequired());
			alarmDefinition.setQpcNotificationRequired(alarmDefinitionWrapper.getQpcNotificationRequired());
			alarmDefinition.merge();

			List<AlarmContact> alarmContacts = AlarmContact.findAlarmContactsByAlarm(alarmDefinition);

			for (AlarmContact alarmContact : alarmContacts) {
				alarmContact.remove();
			}

			saveAlarmContact(alarmDefinition, alarmDefinitionWrapper.getContact1(),
					alarmDefinitionWrapper.getContactType1());
			saveAlarmContact(alarmDefinition, alarmDefinitionWrapper.getContact2(),
					alarmDefinitionWrapper.getContactType2());
			saveAlarmContact(alarmDefinition, alarmDefinitionWrapper.getContact3(),
					alarmDefinitionWrapper.getContactType3());
			saveAlarmContact(alarmDefinition, alarmDefinitionWrapper.getContact4(),
					alarmDefinitionWrapper.getContactType4());
			saveAlarmContact(alarmDefinition, alarmDefinitionWrapper.getContact5(),
					alarmDefinitionWrapper.getContactType5());
		}

		// alarmDefinition.merge();
		return "redirect:/alarms/" + encodeUrlPathSegment(alarmDefinition.getId().toString(), httpServletRequest);
	}

	private void saveAlarmContact(AlarmDefinition alarmDefinition, Contact contact, ContactType contactType) {
		if (contact != null) {
			AlarmContact contact1 = new AlarmContact();
			contact1.setAlarm(alarmDefinition);
			contact1.setContact(contact);
			contact1.setContactType(contactType);
			contact1.persist();
		}
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		AlarmDefinition alarmDefinition = AlarmDefinition.findAlarmDefinition(id);

		AlarmDefinitionWrapper alarmDefinitionWrapper = new AlarmDefinitionWrapper();
		if (alarmDefinition != null) {
			alarmDefinitionWrapper.setId(alarmDefinition.getId());
			alarmDefinitionWrapper.setAlarmNumber(alarmDefinition.getAlarmNumber());
			alarmDefinitionWrapper.setDescription(alarmDefinition.getDescription());
			alarmDefinitionWrapper.setLocation(alarmDefinition.getLocation());
			alarmDefinitionWrapper.setName(alarmDefinition.getName());
			alarmDefinitionWrapper.setNotificationCategory(alarmDefinition.getNotificationCategory());
			alarmDefinitionWrapper.setSeverity(alarmDefinition.getSeverity());
			alarmDefinitionWrapper.setActive(alarmDefinition.getActive());
			alarmDefinitionWrapper.setNotificationRequired(alarmDefinition.getNotificationRequired());
			alarmDefinitionWrapper.setQpcNotificationRequired(alarmDefinition.getQpcNotificationRequired());
			alarmDefinitionWrapper.setAutoArchiveTimeInMinutes(alarmDefinition.getAutoArchiveTimeInMinutes());

			List<AlarmContact> alarmContacts = AlarmContact.findAlarmContactsByAlarm(alarmDefinition);
			if (alarmContacts != null && alarmContacts.size() > 0) {
				LOG.info("found alarm contacts..." + alarmContacts.size());
				if (alarmContacts.size() > 0) {
					alarmDefinitionWrapper.setContact1(alarmContacts.get(0).getContact());
					alarmDefinitionWrapper.setContactType1(alarmContacts.get(0).getContactType());
				}
				if (alarmContacts.size() > 1) {
					alarmDefinitionWrapper.setContact2(alarmContacts.get(1).getContact());
					alarmDefinitionWrapper.setContactType2(alarmContacts.get(1).getContactType());
				}
				if (alarmContacts.size() > 2) {
					alarmDefinitionWrapper.setContact3(alarmContacts.get(2).getContact());
					alarmDefinitionWrapper.setContactType3(alarmContacts.get(2).getContactType());
				}
				if (alarmContacts.size() > 3) {
					alarmDefinitionWrapper.setContact4(alarmContacts.get(3).getContact());
					alarmDefinitionWrapper.setContactType4(alarmContacts.get(3).getContactType());
				}
				if (alarmContacts.size() > 4) {
					alarmDefinitionWrapper.setContact5(alarmContacts.get(4).getContact());
					alarmDefinitionWrapper.setContactType5(alarmContacts.get(4).getContactType());
				}
			}
		}

		uiModel.addAttribute("alarmwrapper", alarmDefinitionWrapper);

//uiModel.addAttribute("alarmcontacts", populateAlarmContacts(alarmDefinition));
		return "alarms/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		AlarmDefinition alarmDefinition = AlarmDefinition.findAlarmDefinition(id);
		List<AlarmContact> alarmContacts = AlarmContact.findAlarmContactsByAlarm(alarmDefinition);

		for (AlarmContact alarmContact : alarmContacts) {
			alarmContact.remove();
		}

		alarmDefinition.remove();

		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/alarms";
	}

	@RequestMapping(params = "ByShowInActive", method = RequestMethod.GET)
	public String showInactiveAlarmsForm(org.springframework.ui.Model uiModel) {
		return "redirect:/alarms";
	}

	@RequestMapping(params = "find=ByShowInActive", method = RequestMethod.GET)
	public String showInactiveAlarms(org.springframework.ui.Model uiModel, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		uiModel.asMap().clear();
		Cookie alarmCookie = new Cookie(ACTIVE_ALARM_FILTER, "false");
		alarmCookie.setMaxAge(COOKIE_MAX_AGE);
		httpServletResponse.addCookie(alarmCookie);

		uiModel.addAttribute("page", 1);
		uiModel.addAttribute("size", 50);
		return "redirect:/alarms";
	}

	@RequestMapping(params = "ByShowActive", method = RequestMethod.GET)
	public String showActiveAlarmsForm(org.springframework.ui.Model uiModel) {
		return "redirect:/alarms";
	}

	@RequestMapping(params = "find=ByShowActive", method = RequestMethod.GET)
	public String showActiveAlarms(org.springframework.ui.Model uiModel, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		uiModel.asMap().clear();
		Cookie alarmCookie = new Cookie(ACTIVE_ALARM_FILTER, "true");
		alarmCookie.setMaxAge(COOKIE_MAX_AGE);
		httpServletResponse.addCookie(alarmCookie);

		uiModel.addAttribute("page", 1);
		uiModel.addAttribute("size", 50);
		return "redirect:/alarms";
	}

	private Boolean getAlarmFilter(HttpServletRequest httpServletRequest) {
		Cookie[] cookies = httpServletRequest.getCookies();
		Boolean flag = true;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(ACTIVE_ALARM_FILTER)) {
				String value = cookie.getValue();
				flag = Boolean.valueOf(value);
				break;
			}
		}
		return flag;
	}

	@ModelAttribute("alarm_category")
	public Collection<AlarmNotificationCategory> populateALARM_CATEGORY() {
		return Arrays.asList(AlarmNotificationCategory.class.getEnumConstants());
	}

	@ModelAttribute("alarms")
	public Collection<AlarmDefinition> populateAlarms() {
		return AlarmDefinition.findAllAlarmDefinitions();
	}

	@ModelAttribute("severity")
	public Collection<SEVERITY> populateSEVERITY() {
		return Arrays.asList(SEVERITY.class.getEnumConstants());
	}

	@ModelAttribute("contacttypes")
	public Collection<ContactType> populateContactTypes() {
		return Arrays.asList(ContactType.class.getEnumConstants());
	}

	@ModelAttribute("contacts")
	public Collection<Contact> populateContacts() {
		return Contact.findAllContacts();
	}

	@ModelAttribute("autoarchivetimes")
	public Collection<AutoArchiveTime> populateAutoArchieTimes() {
		return Arrays.asList(AutoArchiveTime.class.getEnumConstants());
	}

	public List<AlarmContact> populateAlarmContacts(AlarmDefinition alarm) {
		List<AlarmContact> alarmContacts = new ArrayList<AlarmContact>();
		List<Contact> contacts = Contact.findAllContacts();

		for (Contact contact : contacts) {
			AlarmContact alarmContact = new AlarmContact();
			alarmContact.setContact(contact);
			alarmContact.setAlarm(alarm);
			if (contact.getEmail() != null && contact.getEmail().length() > 0) {
				alarmContact.setContactType(ContactType.EMAIL);
				alarmContacts.add(alarmContact);
			}

			if (contact.getPagerNo() != null && contact.getPagerNo().length() > 0) {
				alarmContact.setContactType(ContactType.PAGER);
				alarmContacts.add(alarmContact);
			}
		}

		return alarmContacts;
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
