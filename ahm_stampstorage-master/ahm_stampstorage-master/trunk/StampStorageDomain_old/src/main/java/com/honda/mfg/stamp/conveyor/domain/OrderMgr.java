package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "ORDER_MGR_ID", table = "ORDER_MGR_TBX")
public class OrderMgr  {

    @NotNull
    @Column(name = "LINE_NAME")
    private String lineName;

    @OneToOne
    private Stop deliveryStop;

    @OneToOne
    private Stop leftConsumptionStop;

     @OneToOne
    private Stop rightConsumptionStop;

     @OneToOne
    private Stop leftConsumptionExit;

     @OneToOne
    private Stop rightConsumptionExit;

    @NotNull
    @Column(name = "MAX_DELIVERY_CAPACITY")
    private Integer maxDeliveryCapacity;

    @OneToOne
    private Stop leftQueueStop;

    @OneToOne
    private Stop rightQueueStop;
}
