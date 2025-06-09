package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.DEFECT_TYPE;
import com.honda.mfg.stamp.conveyor.domain.enums.REWORK_METHOD;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "DEFECT_ID", table = "DEFECT_TBX")
public class Defect {

    @NotNull
    @Column(name = "CARRIER_NUMBER")
    private Integer carrierNumber;

    @NotNull
    @Column(name = "PRODUCTIONRUNNUMBER")
    private Integer productionRunNo;

    @NotNull
    @Column(name = "DEFECT_TYPE")
    @Enumerated
    private DEFECT_TYPE defectType;

    @NotNull
    @Column(name = "REWORK_METHOD")
    @Enumerated
    private REWORK_METHOD reworkMethod;

    @NotNull
    @Column(name = "X_Area")
    private Integer xArea;

    @NotNull
    @Column(name = "Y_Area")
    private String yArea;

    @Column(name = "DEFECT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date defectTimestamp;

    @Column(name = "DEFECT_REPAIRED")
    private Boolean defectRepaired;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "SOURCE")
    private String source;

     public boolean equals(Object obj) {
        if (obj instanceof Defect ) {
            if (getId() == null) {
                return false;
            }
            return getId().equals(((Defect) obj).getId());
        } else {
            return false;
        }
    }

    public int hashCode() {
        //LOG.info("hashCode() called.  hashcode: " + super.hashCode());
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
