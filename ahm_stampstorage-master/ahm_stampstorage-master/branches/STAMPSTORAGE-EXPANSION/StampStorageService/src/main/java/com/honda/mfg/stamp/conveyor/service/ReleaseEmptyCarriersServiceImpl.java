package com.honda.mfg.stamp.conveyor.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;

/**
 * User: vcc30690 Date: 6/21/11
 */
public class ReleaseEmptyCarriersServiceImpl implements ReleaseEmptyCarriersService, Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ReleaseEmptyCarriersServiceImpl.class);

	private String emptyAreaUnderFilledQuantity;
	private String releaseCarrierCount;
	private ReleaseManager releaseManager;
	private EmptyCarriersServiceHelper orderEmptyCarriersServiceHelper;
	private ServiceRoleWrapper serviceRoleWrapper = null;

	public ServiceRoleWrapper getServiceRoleWrapper() {
		return serviceRoleWrapper;
	}

	@Override
	public void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrapper) {
		this.serviceRoleWrapper = serviceRoleWrapper;
	}

	public ReleaseEmptyCarriersServiceImpl(EmptyCarriersServiceHelper orderEmptyCarriersServiceHelper,
			ReleaseManager releaseManager, String emptyAreaUnderFilledQuantity, String releaseCarrierCount) {
		this.orderEmptyCarriersServiceHelper = orderEmptyCarriersServiceHelper;
		this.releaseManager = releaseManager;
		this.emptyAreaUnderFilledQuantity = emptyAreaUnderFilledQuantity;
		this.releaseCarrierCount = releaseCarrierCount;
		serviceRoleWrapper = ServiceRoleWrapperImpl.getInstance();

		LOG.debug("###############   emptyUnderFilledQuantity: " + this.emptyAreaUnderFilledQuantity);
		LOG.debug("###############   release count requested: " + this.releaseCarrierCount);
	}

	public void run() {
		if (serviceRoleWrapper.isPassive()) {
			LOG.debug("Passive mode...not running");
			return;
		}
		LOG.debug("Running...");

		try {
			if (isEmptyAreaUnderFilled()) {
				int releaseCount = orderEmptyCarriersServiceHelper.getParmValue(releaseCarrierCount);
				releaseEmptyCarriersFromOldWeldLineToEmptyStorageArea(releaseCount);
			}
		} catch (Exception e) {
			LOG.debug(e.getMessage());
		}

		LOG.debug("Done running...");
	}

	public boolean isEmptyAreaUnderFilled() {
		int carrierInEmptyAreaCount = orderEmptyCarriersServiceHelper.getCarrierCountForEmptyArea();
		LOG.info("Total Carriers at or enroute to empty Area " + carrierInEmptyAreaCount);
		int count = orderEmptyCarriersServiceHelper.getParmValue(emptyAreaUnderFilledQuantity);
		return (carrierInEmptyAreaCount < count);
	}

	public void releaseEmptyCarriersFromOldWeldLineToEmptyStorageArea(int carrierCount) {
		try {
			List<CarrierMes> carriers = orderEmptyCarriersServiceHelper
					.getCarriersToMoveToEmptyStorageArea(carrierCount);
			if (carriers.size() == 0) {
				LOG.debug("No Empty Carriers in At ST52 to Retrieve");
			}
			Stop emptyCarrierDeliveryStop = orderEmptyCarriersServiceHelper.getEmptyCarrierDeliveryStop();
			if (emptyCarrierDeliveryStop == null) {
				String message = " EmptyCarriersFromOldWeldLineToEmptyStorageAreaService: No Delivery Stop defined ";
				orderEmptyCarriersServiceHelper.saveToAuditLog(StorageConfig.OHCV_APP, message,
						StorageConfig.OHCV_APP_AUTO_EMPTY);
				LOG.info(" No Delivery Stop Defined");
			}
			for (CarrierMes carrierMes : carriers) {
				Long dieId = new Long(carrierMes.getDieNumber());
				if (!carrierMes.getDestination().equals(emptyCarrierDeliveryStop.getId())
						&& orderEmptyCarriersServiceHelper.getEmptyDie().getId().equals(dieId)) {
					LOG.debug("Moving empty carrier out of Old Weld Line to empty Storage Area");
					releaseManager.releaseCarrier(carrierMes.getCarrierNumber(), emptyCarrierDeliveryStop,
							StorageConfig.OHCV_APP_AUTO_EMPTY, true);
				}
			}
		} catch (Exception e) {
			LOG.info(e.getMessage());
			// e.printStackTrace();
		}
	}
}
