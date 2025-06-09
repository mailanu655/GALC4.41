package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import java.sql.Timestamp;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "CARRIER_NUMBER", table = "CARRIER_RELEASE_TBX")
public class CarrierRelease {

    @NotNull
    @ManyToOne
    private Stop currentLocation;

    @NotNull
    @ManyToOne
    private Stop destination;

    @NotNull
    @Column(name = "SOURCE")
    private String source;

    @NotNull
    @Column(name = "EVENT_TIMESTAMP")
    private Timestamp requestTimestamp;
}
