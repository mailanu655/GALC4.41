package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.ContactType;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "ALARM_CONTACT_ID", table = "ALARM_CONTACT_TBX")

public class AlarmContact {

    @NotNull
    @OneToOne
    private AlarmDefinition alarm;

    @NotNull
    @OneToOne
    private Contact contact;

    @Column(name = "CONTACT_TYPE")
    @Enumerated
    private ContactType contactType;
}
