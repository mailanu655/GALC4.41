package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * User: Jeffrey M Lutz
 * Date: 5/3/11
 */
@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "STOP_ID", table = "STOP_TBX")
public class Stop {
    private static final Logger LOG = LoggerFactory.getLogger(Stop.class);

    public Stop() {
        super();
    }

    public Stop(long id) {
        this.setId(id);
    }

    public Stop(String name) {
        this.name = name;
    }

    @NotNull
    @Column(name = "NAME")
    String name;

    @NotNull
    @Column(name = "STOP_TYPE")
    @Enumerated
    StopType stopType;


    @Column(name = "DESCRIPTION")
    String description;

    @NotNull
    @Column(name = "STOP_AREA")
    @Enumerated
    StopArea stopArea;

//    @NotNull
//    @Column(name = "CAPACITY")
//    Integer capacity;
//
//    @NotNull
//    @Column(name = "AVAILABILITY")
//    @Enumerated
//    StopAvailability stopAvailability;

    public boolean equals(Object obj) {
        if (obj instanceof Stop) {

            Long lhsId = getId();
            Long rhsId = ((Stop) obj).getId();
            if (lhsId == null) {
                return false;
            }
            return lhsId.equals(rhsId);
        }

        return false;
    }


    public boolean isRowStop() {
        return this.stopArea.equals(StopArea.ROW);
    }

    public int hashCode() {
        //LOG.info("hashCode() called.  hashcode: " + super.hashCode());
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
