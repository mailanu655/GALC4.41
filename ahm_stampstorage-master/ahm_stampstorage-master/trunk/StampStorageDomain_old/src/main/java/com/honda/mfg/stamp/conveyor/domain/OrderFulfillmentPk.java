package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooIdentifier;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RooToString
@RooIdentifier
public final class OrderFulfillmentPk implements Serializable {

    @NotNull
    @ManyToOne
    private WeldOrder weldOrder;

    @Column(name = "CARRIER_NUMBER")
    private Integer carrierNumber;

    @Column(name = "RELEASE_CYCLE")
    private Integer releaseCycle;
}
