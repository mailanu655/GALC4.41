package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "CONTACT_ID", table = "CONTACT_TBX")
public class Contact {

    @NotNull
    @Column(name = "CONTACT_NAME")
    private String contactName;

    @Column(name = "EMAIL_ADDRESS")
    private String email;

    @Column(name = "PAGER_NUMBER")
    private String pagerNo;
}
