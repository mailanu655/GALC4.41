package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * User: Adam S. Kendell
 * Date: Feb 02, 2012
 */

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "CARRIER_ID", table = "CARRIER_MES_ARCHIVE_TBX")
public class CarrierHistory {

    @Column(name = "BUFFER")
    private Integer buffer;

    @Column(name = "CARRIERNUMBER")
    private Integer carrierNumber;

    @Column(name = "CURRENTLOCATION")
    private Long currentLocation;

    @Column(name = "DESTINATION")
    private Long destination;

    @Column(name = "DIENUMBER")
    private Long dieNumber;

    @Column(name = "ORIGINATIONLOCATION")
    private Integer originationLocation;

    @Column(name = "PRODUCTIONRUNDATE")
    private Timestamp productionRunDate;

    @Column(name = "PRODUCTIONRUNNUMBER")
    private Integer productionRunNumber;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "STATUS")
    @Enumerated
    private CarrierStatus status;

    @Column(name = "TAGID")
    private Long tagId;

    @Column(name = "UPDATEDATE")
    private Timestamp updateDate;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "CARRIER_MES_ARCHIVE_TSTP")
    private Timestamp carrierMesArchiveTstp;

    public CarrierHistory() {
        super();
    }

    public CarrierHistory(long id) {
        setId(id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CarrierHistory) {
            if (getId() == null) {
                return false;
            }
            return getId().equals(((CarrierHistory) obj).getId());
        } else {
            return false;
        }
    }

    public int hashCode() {
        //LOG.info("hashCode() called.  hashcode: " + super.hashCode());
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
