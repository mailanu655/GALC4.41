package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: 6/18/11
 */
@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "CARRIER_ID", table = "CARRIER_MES_TBX")
public class CarrierMes {

    @Column(name = "CARRIERNUMBER")
    private Integer carrierNumber;
    @Column(name = "TAGID")
    private Integer tagId;
    @Column(name = "DIENUMBER")
    private Integer dieNumber;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Column(name = "UPDATEDATE")
    private Date updateDate;
    @Column(name = "CURRENTLOCATION")
    private Long currentLocation;
    @Column(name = "DESTINATION")
    private Long destination;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "ORIGINATIONLOCATION")
    private Integer originationLocation;
    @Column(name = "BUFFER")
    private Integer buffer;
    @Column(name = "PRODUCTIONRUNNUMBER")
    private Integer productionRunNumber;
    @Column(name = "PRODUCTIONRUNDATE")
    private Date productionRunDate;
    @Column(name="SOURCE")
    private String source;



    public CarrierMes() {
        super();
    }

    public CarrierMes(long id) {
        setId(id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CarrierMes ) {
            if (getId() == null) {
                return false;
            }
            return getId().equals(((CarrierMes) obj).getId());
        } else {
            return false;
        }
    }

    public int hashCode() {
        //LOG.info("hashCode() called.  hashcode: " + super.hashCode());
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
