package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 2/29/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class OrderEmptyCarrierAbstractServiceImpl implements OrderEmptyCarriersService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderEmptyCarrierAbstractServiceImpl.class);

    private String underFilledQuantity;
    private String releaseCarrierCount;
    private EmptyCarriersServiceHelper orderEmptyCarriersServiceHelper;
    private ReleaseManager releaseManager;
    private Storage storage;
    private Stop deliveryStop;

    public OrderEmptyCarrierAbstractServiceImpl(EmptyCarriersServiceHelper orderEmptyCarriersServiceHelper, Storage storage, ReleaseManager releaseManager, Stop deliveryStop, String underFilledQuantity, String releaseCarrierCount) {
        this.orderEmptyCarriersServiceHelper = orderEmptyCarriersServiceHelper;
        this.storage = storage;
        this.releaseManager = releaseManager;
        this.deliveryStop = deliveryStop;
        this.underFilledQuantity = underFilledQuantity;
        this.releaseCarrierCount = releaseCarrierCount;

        LOG.debug("###############   emptyUnderFilledQuantity: " + this.underFilledQuantity);
        LOG.debug("###############   release count requested: " + this.releaseCarrierCount);
    }


    public boolean isAreaUnderFilled(int carrierInEmptyAreaCount) {
        int count  = orderEmptyCarriersServiceHelper.getParmValue(underFilledQuantity);
        return (carrierInEmptyAreaCount < count);
    }


    public abstract void run();

    public void releaseEmptyCarriers(Stop destination) {
        try {
            int carrierCount = orderEmptyCarriersServiceHelper.getParmValue(releaseCarrierCount);
            int i = 0;
            StorageRow lane = getRowToReleaseEmptyCarrier();

            while (i < carrierCount) {
                if (lane != null && !lane.isEmpty()) {
                    Carrier carrier = lane.release();
                    if (orderEmptyCarriersServiceHelper.canReleaseCarrier(carrier.getCarrierNumber(), lane.getStop())) {
                        releaseManager.releaseCarrier(carrier.getCarrierNumber(), deliveryStop, StorageConfig.OHCV_APP_AUTO_EMPTY, true);
                        storage.getStorageState().releaseCarrierIfExistsAtHeadOfLane(carrier);
                    }
                } else {
                    break;
                }
                i++;
            }

        } catch (Exception e) {
            //e.printStackTrace();
            LOG.info(e.getMessage());
            LOG.debug("No empty carriers in storage rows to retrieve");
        }
    }

    public abstract StorageRow getRowToReleaseEmptyCarrier();


}
