package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: Jan 24, 2011
 */
@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "CARRIER_ID", table = "CARRIER_TBX", finders = {"findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo"})
public class Carrier {
    private static final Logger LOG = LoggerFactory.getLogger(Carrier.class);

    @NotNull
    @Column(name = "Carrier_Number")
    private Integer carrierNumber;

    @NotNull
    @Column(name = "QUANTITY")
    private Integer quantity;

    @ManyToOne
    private Die die;

    @OneToOne
    private Stop currentLocation;

    @OneToOne
    private Stop destination;

    @NotNull
    @Column(name = "Carrier_STATUS")
    @Enumerated
    private CarrierStatus carrierStatus;

    @NotNull
    @Column(name = "PRESS_INFO")
    @Enumerated
    private Press press;

    @Column(name = "LOAD_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date loadTimestamp;

    @Column(name = "UNLOAD_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date unloadTimestamp;

    @Column(name = "PRODUCTION_RUN_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date stampingProductionRunTimestamp;

    @Column(name = "PRODUCTION_RUN_NO")
    private Integer productionRunNo;

    private Integer buffer;

    private Date updateDate;

    private boolean reprocess;

    private Integer positionInLane;

    private boolean alreadyInLane;

    private boolean movingOutOfLane = false;

    private String source;

    private boolean submitToReleaseManager;

    private boolean inQueueToMoveOutOfLane = false;

    private boolean movingInToLane = false;

    public Carrier() {
    }

    public Carrier(long id) {
        this(id, null, 0, null, null);
    }

    public Carrier(long id, Timestamp stampingProductionRunTimestamp, Integer productionRunNo, Stop currentLocation, Stop destination, CarrierStatus carrierStatus, Integer carrierNumber, Die die) {
        this(id, die, Integer.valueOf(10), stampingProductionRunTimestamp, productionRunNo);

        this.currentLocation = currentLocation;
        this.destination = destination;
        this.carrierStatus = carrierStatus;
        this.carrierNumber = carrierNumber;
    }

    public Carrier(long id, Die die, int quantity, Timestamp stampingProductionRunTimestamp, Integer productionRunNo) {
        this.setId(id);
        this.productionRunNo = productionRunNo;
        this.stampingProductionRunTimestamp = stampingProductionRunTimestamp;
        this.die = die;
        this.quantity = quantity;
        this.carrierNumber = (int) id;

    }

    public PartProductionVolume getPartProductionVolume() {
        if (die != null) {
            return die.getPartProductionVolume();
        }
        return null;
    }

//    public boolean equals(Object obj, boolean storageStateCompare) {
//
//        if (storageStateCompare) {
//            if (obj instanceof Carrier) {
//                Carrier tempCarrier = (Carrier) obj;
//                LOG.trace(tempCarrier.getQuantity() + "-" + getQuantity() + "," + tempCarrier.getDie() + "-" + getDie());
//                if (tempCarrier.getDie() == null && getDie() == null) {
//                    return true;
//                }
//
//                boolean flag1 = tempCarrier.getQuantity().equals(quantity) && tempCarrier.getDie().equals(getDie());
//                boolean flag2 = tempCarrier.getCarrierStatus().equals(getCarrierStatus()) && tempCarrier.getProductionRunNo().equals(getProductionRunNo()) && tempCarrier.getStampingProductionRunTimestamp().equals(getStampingProductionRunTimestamp());
//                boolean flag3 = tempCarrier.getId() == getId() && tempCarrier.getCarrierNumber().equals(carrierNumber);
//                boolean flag4 = tempCarrier.getCurrentLocation().equals(getCurrentLocation(), storageStateCompare) && tempCarrier.getDestination().equals(getDestination(), storageStateCompare);
//                if (flag1 && flag2 && flag3 && flag4) {
//                    return true;
//                } else {
//                    return false;
//                }
//
//            } else {
//                return false;
//            }
//        }
//        return ((Carrier) obj).getId().longValue() == getId().longValue();
//    }

    public boolean isMoving() {
        if (currentLocation != null) {
            if (currentLocation.equals(destination)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        LOG.info("hashCode() called.  hashcode: " + super.hashCode());
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isLoadedLike(Carrier rhs) {
        Carrier lhs = this;
        if (lhs.getCarrierNumber() != null) {
            if (!lhs.getCarrierNumber().equals(rhs.getCarrierNumber())) {
                return false;
            }
        } else if (rhs.getCarrierNumber() != null) {
            return false;
        }
        if (lhs.getQuantity() != null) {
            if (!lhs.getQuantity().equals(rhs.getQuantity())) {
                return false;
            }
        } else if (rhs.getQuantity() != null) {
            return false;
        }
        if (lhs.getDie() != null) {
            if (!lhs.getDie().equals(rhs.getDie())) {
                return false;
            }
        } else if (rhs.getDie() != null) {
            return false;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Carrier ) {
            if (getId() == null) {
                return false;
            }
            return getId().equals(((Carrier) obj).getId());
        }
        return false;
    }


}