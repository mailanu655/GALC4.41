package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.domain.AlarmEventArchive;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * User: vcc30690
 * Date: 7/27/11
 */

@RequestMapping("/alarmevents")
@Controller
public class AlarmEventController {
    public static final Logger LOG = LoggerFactory.getLogger(AlarmEventController.class);
    @Autowired
    CarrierManagementServiceProxy carrierManagementServiceProxy;
     public void archiveAlarm(Long id, String user) {
        try{
            AlarmEvent event = AlarmEvent.findCurrent_Alarm(id);
            AlarmEventArchive archive = new AlarmEventArchive();
            archive.setAlarmEventId(event.getId());
            archive.setAlarmNumber(event.getAlarmNumber());
            archive.setLocation(event.getLocation());
            archive.setEventTimestamp(event.getEventTimestamp());
            archive.setCleared(true);
            archive.setClearedBy(user);
            archive.setClearedTimestamp(new Timestamp(System.currentTimeMillis()));
            archive.setArchivedTimestamp(new Timestamp(System.currentTimeMillis()));
            archive.setArchivedBy(user);
            archive.persist();

            event.remove();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid AlarmEvent alarmEvent, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("alarmEvent", alarmEvent);
            return "alarmevents/create";
        }
        uiModel.asMap().clear();
        alarmEvent.persist();
        return "redirect:/alarmevents/" + encodeUrlPathSegment(alarmEvent.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("alarmevent", new AlarmEvent());
        return "alarmevents/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("alarmevent", AlarmEvent.findCurrent_Alarm(id));
        uiModel.addAttribute("itemId", id);
        return "alarmevents/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null && size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            List<AlarmEvent> current_Alarms = AlarmEvent.findCurrent_AlarmEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo);
            LOG.debug("page-" + page + " , size-" + size);
            uiModel.addAttribute("alarmevents", current_Alarms);
            float nrOfPages = (float) AlarmEvent.countCurrent_Alarms() / sizeNo;
            if (nrOfPages > 20) {
                uiModel.addAttribute("maxPages", 20);
            } else {
                uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
            }
        } else {
            List<AlarmEvent> current_Alarms = AlarmEvent.findAllCurrent_Alarms();
            LOG.debug("alarmevents size" + current_Alarms.size());
            uiModel.addAttribute("alarmevents", current_Alarms);
        }
        return "alarmevents/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(AlarmEvent alarmEvent, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (alarmEvent.getCleared()) {
            String user = httpServletRequest.getUserPrincipal().getName();
            AlarmEvent event = AlarmEvent.findCurrent_Alarm(alarmEvent.getId());
            carrierManagementServiceProxy.clearAlarm(event.getAlarmNumber());
            archiveAlarm(alarmEvent.getId(), user);

        }
        uiModel.asMap().clear();

        return "redirect:/alarmevents";
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("alarmevent", AlarmEvent.findCurrent_Alarm(id));
        return "alarmevents/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel, HttpServletRequest httpServletRequest) {
        String user = httpServletRequest.getUserPrincipal().getName();
        AlarmEvent event = AlarmEvent.findCurrent_Alarm(id);
        carrierManagementServiceProxy.clearAlarm(event.getAlarmNumber());
        archiveAlarm(id, user);

        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/alarmevents";
    }

	@RequestMapping(params = "ByClearAll", method = RequestMethod.GET)
    public String clearAllAlarmsForm(org.springframework.ui.Model uiModel) {
        return "redirect:/alarmevents";
    }

	@RequestMapping(params = "find=ByClearAll", method = RequestMethod.GET)
    public String clearAllAlarms(org.springframework.ui.Model uiModel, HttpServletRequest httpServletRequest) {
        String user = httpServletRequest.getUserPrincipal().getName();

        List<AlarmEvent> alarmEvents = AlarmEvent.findCurrentUnClearedAlarms(50);

        for (AlarmEvent event : alarmEvents) {
             archiveAlarm(event.getId(), user);
        }
        uiModel.asMap().clear();
        uiModel.addAttribute("page", "1");
        uiModel.addAttribute("size", "10");
        return "redirect:/alarmevents";
    }

	@ModelAttribute("alarmevents")
    public Collection<AlarmEvent> populateCurrent_Alarms() {
        return AlarmEvent.findAllCurrent_Alarms();
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
