package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: vcc30690
 * Date: 6/21/11
 */
public class OrderEmptyCarriersToOldWeldLineServiceImpl extends OrderEmptyCarrierAbstractServiceImpl implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(OrderEmptyCarriersToOldWeldLineServiceImpl.class);

    private EmptyCarriersServiceHelper orderEmptyCarriersServiceHelper;
    private ServiceRoleWrapper serviceRoleWrapper = null;
    private Storage storage;

    public ServiceRoleWrapper getServiceRoleWrapper() {
        return serviceRoleWrapper;
    }

    @Override
    public void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrapper) {
        this.serviceRoleWrapper = serviceRoleWrapper;
    }

    public OrderEmptyCarriersToOldWeldLineServiceImpl(EmptyCarriersServiceHelper orderEmptyCarriersServiceHelper, Storage storage, ReleaseManager releaseManager, String oldWeldLineEmptyUnderFilledQuantity, String carrierCount) {

        super(orderEmptyCarriersServiceHelper, storage, releaseManager, orderEmptyCarriersServiceHelper.getOldWeldLineEmptyCarrierDeliveryStop(), oldWeldLineEmptyUnderFilledQuantity, carrierCount);
        this.orderEmptyCarriersServiceHelper = orderEmptyCarriersServiceHelper;
        this.storage = storage;
        serviceRoleWrapper = ServiceRoleWrapperImpl.getInstance();

    }

    public void run() {

        if (serviceRoleWrapper.isPassive()) {
            LOG.debug("Passive mode...not running");
            return;
        }
        LOG.debug("Running...");

        try {
            int carrierAtOldWeldLineDestCount = orderEmptyCarriersServiceHelper.getCarrierCountForOldWeldLineEmptyArea();
            LOG.info("Carriers at or in route to old weld line empty carrier destination: " + carrierAtOldWeldLineDestCount);
            Stop deliveryStop = orderEmptyCarriersServiceHelper.getOldWeldLineEmptyCarrierDeliveryStop();

            if (deliveryStop == null) {
                String message = " EmptyCarriersToOldWeldLineService: No Delivery Stop defined ";
                orderEmptyCarriersServiceHelper.saveToAuditLog(StorageConfig.OHCV_APP, message, StorageConfig.OHCV_APP_AUTO_EMPTY);
                LOG.info(" No Delivery Stop defined ");
            } else {
                if (isAreaUnderFilled(carrierAtOldWeldLineDestCount) && !orderEmptyCarriersServiceHelper.anyOrderInProcess() && !orderEmptyCarriersServiceHelper.anyCarrierSetToReleaseFromRows()) {
                    releaseEmptyCarriers(deliveryStop);
                } else {
                    LOG.debug("Old Weld Line under filled: " + isAreaUnderFilled(carrierAtOldWeldLineDestCount) +
                            " - Auto Order inProcess: " + orderEmptyCarriersServiceHelper.anyOrderInProcess() +
                            " - Carriers releasing: " + orderEmptyCarriersServiceHelper.anyCarrierSetToReleaseFromRows() +
                            " - Cannot retrieve empty carriers at this time.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug(e.getMessage());
        }

        LOG.debug("Done running...");

    }

    @Override
    public StorageRow getRowToReleaseEmptyCarrier() {
        return storage.retrieveEmptyCarrierForOldWeldLine();
    }

}
