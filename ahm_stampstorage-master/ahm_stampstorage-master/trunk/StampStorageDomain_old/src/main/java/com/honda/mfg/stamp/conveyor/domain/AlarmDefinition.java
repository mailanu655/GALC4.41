package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.AlarmNotificationCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.SEVERITY;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "ALARM_DEFINITION_ID", table = "ALARMS_DEFINITION_TBX")
public class AlarmDefinition {

    @NotNull
    @Column(name = "ALARM_NUMBER")
    private Integer alarmNumber;

    @NotNull
    @Column(name = "LOCATION")
    private Integer location;

    @NotNull
    @Column(name = "ALARM_NAME")
    private String name;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "NOTIFICATION_CATEGORY")
    @Enumerated
    private AlarmNotificationCategory notificationCategory;

    @NotNull
    @Column(name = "SEVERITY")
    @Enumerated
    private SEVERITY severity;

    @NotNull
    @Column(name = "AUTO_ARCHIVE_TIME")
    private Double autoArchiveTimeInMinutes;

    @NotNull
    @Column(name = "NOTIFICATION_REQUIRED")
    private Boolean notificationRequired;

    @NotNull
    @Column(name = "QPC_NOTIFICATION_REQUIRED")
    private Boolean qpcNotificationRequired;


    @NotNull
    @Column(name = "ACTIVE")
    private Boolean active;


   // private String notification;

}



