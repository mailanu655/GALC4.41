package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.validation.constraints.NotNull;
import javax.persistence.Column;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "SCHEDULE_ID", table = "WELD_SCHEDULE_TBX")
public class WeldSchedule {

    @NotNull
    @Column(name = "WELDLINE")
    private Integer weldLine;

    @NotNull
    @Column(name = "LHPRODPLAN")
    private Integer leftHandProdPlan;

    @NotNull
    @Column(name = "LHPRODREMAIN")
    private Integer leftHandProdRemaining;

    @NotNull
    @Column(name = "RHPRODPLAN")
    private Integer rightHandProdPlan;

    @NotNull
    @Column(name = "RHPRODREMAIN")
    private Integer rightHandProdRemaining;

    @NotNull
    @Column(name = "CURRENTMODEL")
    private Integer currentModel;

    @NotNull
    @Column(name = "NEXTMODEL")
    private Integer nextModel;

    @NotNull
    @Column(name = "NEXTQUANTITY")
    private Integer nextQuantity;


}
