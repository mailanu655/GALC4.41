package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import java.sql.Timestamp;


@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "ID", table = "PARM_SETTING_TBX")
public class ParmSetting {

    @NotNull
    @Column(name = "FIELD_NAME")
    private String fieldname;

    @NotNull
    @Column(name = "FIELD_VALUE")
    private String fieldvalue;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "UPDATED_BY")
    private String updatedby;

    @NotNull
    @Column(name = "UPDATE_TSTP")
    private Timestamp updatetstp;
}
