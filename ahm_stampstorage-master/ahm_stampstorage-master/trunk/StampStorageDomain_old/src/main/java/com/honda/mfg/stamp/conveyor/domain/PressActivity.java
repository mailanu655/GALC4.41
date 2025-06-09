package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "PRESS_ACTIVITY_ID", table = "PRESS_ACTIVITY_TBX")
public class PressActivity {

    @NotNull
    @Column(name = "PRESS_NAME")
    private String pressName;

    @NotNull
    @Column(name = "PRODUCTIONRUNNUMBER")
    private Integer prodRunNumber;

    @NotNull
    @Column(name = "DIENUMBER")
    private Integer dieNumber;

    @NotNull
    @Column(name = "QUANTITY_PRODUCED")
    private Integer quantityProduced;
}
