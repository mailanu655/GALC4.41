package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;


@RooJavaBean
@RooToString
@RooEntity(identifierType = OrderFulfillmentPk.class, table = "ORDER_FULFILLMENT_TBX")
public class OrderFulfillment {

    @Column(name = "QUANTITY")
    private Integer quantity;

    @ManyToOne
    private Die die;

    @ManyToOne
    private Stop currentLocation;

    @ManyToOne
    private Stop destination;

    @Column(name = "PRODUCTION_RUN_NO")
    private Integer productionRunNo;

   // @Column(name = "RELEASE_CYCLE")
   // private Integer releaseCycle;

    @Column(name = "CARRIER_FULFILLMENT_STATUS")
    private CarrierFulfillmentStatus carrierFulfillmentStatus;

    @Column(name="UPDATE_DATE")
    private Timestamp updateDate;

}
