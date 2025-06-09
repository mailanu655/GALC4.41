package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "ORDER_ID", table = "WELD_ORDER_TBX", finders = { "findWeldOrdersByDeliveryStatus" })
/**
 * 
 * The order for parts which the system should send to Weld.
 *
 */
public class WeldOrder {
    private static final Logger LOG = LoggerFactory.getLogger(WeldOrder.class);

    public WeldOrder() {
        orderSequence = 9999;
    }

    @ManyToOne
    private OrderMgr orderMgr;

    @NotNull
    @Column(name = "ORDER_SEQUENCE")
    private Integer orderSequence;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Model model;

    @Column(name = "ORDER_STATUS")
    @Enumerated
    private OrderStatus orderStatus;

    @NotNull
    @Column(name = "LEFT_QTY")
    private Integer leftQuantity;

    @NotNull
    @Column(name = "RIGHT_QTY")
    private Integer rightQuantity;

    @Column(name = "LEFT_DELIVERED_QTY")
    private Integer leftDeliveredQuantity;

    @Column(name = "RIGHT_DELIVERED_QTY")
    private Integer rightDeliveredQuantity;

    @Column(name = "LEFT_CONSUMED_QTY")
    private Integer leftConsumedQuantity;

    @Column(name = "RIGHT_CONSUMED_QTY")
    private Integer rightConsumedQuantity;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "DELIVERY_STATUS")
    @Enumerated
    private OrderStatus deliveryStatus;
    
    @Column(name = "RIGHT_QUEUED_QTY")
    private Integer rightQueuedQty;

    @Column(name = "LEFT_QUEUED_QTY")
    private Integer leftQueuedQty;

}
