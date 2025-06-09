package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.domain.AlarmEventArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;

/**
 * User: vcc30690
 * Date: 7/27/11
 */

@RooWebScaffold(path = "alarmevents", formBackingObject = AlarmEvent.class)
@RequestMapping("/alarmevents")
@Controller
public class AlarmEventController {
    public static final Logger LOG = LoggerFactory.getLogger(AlarmEventController.class);

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
}
