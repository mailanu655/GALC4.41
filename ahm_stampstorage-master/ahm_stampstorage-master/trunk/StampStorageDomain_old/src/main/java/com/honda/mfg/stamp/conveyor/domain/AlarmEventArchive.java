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
@RooEntity(identifierColumn = "ALARM_EVENT_ARCHIVE_ID", table = "ALARM_EVENT_ARCHIVE_TBX")
public class AlarmEventArchive {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmEventArchive.class);

     @NotNull
    @Column(name = "ALARM_EVENT_ID")
    private Long alarmEventId;

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

    @Column(name = "ARCHIVAL_TIMESTAMP")
    private Timestamp archivalTimestamp;

     @Column(name = "ARCHIVED_BY")
    private String archivedBy;
}
