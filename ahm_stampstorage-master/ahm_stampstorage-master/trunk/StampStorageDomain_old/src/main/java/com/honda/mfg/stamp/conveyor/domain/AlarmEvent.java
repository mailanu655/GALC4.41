package com.honda.mfg.stamp.conveyor.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@RooJavaBean
@RooToString
@RooEntity(identifierColumn = "ALARM_EVENT_ID", table = "ALARM_EVENT_TBX")
public class AlarmEvent {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmEvent.class);
    @NotNull
    @Column(name = "LOCATION")
    private Integer location;

    @NotNull
    @Column(name = "ALARM_NUMBER")
    private Integer alarmNumber;

    //@NotNull
    @Column(name = "EVENT_TIMESTAMP")
    private Timestamp eventTimestamp;

    @Column(name = "CLEARED")
    private Boolean cleared;

    @Column(name = "CLEARED_BY")
    private String clearedBy;

    @Column(name = "CLEARED_TIMESTAMP")
    private Timestamp clearedTimestamp;


    @Column(name = "NOTIFIED")
    private Boolean notified;


}
