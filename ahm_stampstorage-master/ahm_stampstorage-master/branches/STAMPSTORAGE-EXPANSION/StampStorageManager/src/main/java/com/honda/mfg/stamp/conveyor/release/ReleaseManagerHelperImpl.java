package com.honda.mfg.stamp.conveyor.release;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.helper.AbstractHelperImpl;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 2/29/12 Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */

public class ReleaseManagerHelperImpl extends AbstractHelperImpl implements ReleaseManagerHelper {
	private static final Logger LOG = LoggerFactory.getLogger(ReleaseManagerHelperImpl.class);

	public static final Long C_HIGH_STORAGE_RELEASE_CHECK_STOP = 903L;
	public static final Long A_AREA_STORAGE_RELEASE_CHECK_STOP = 1300L;
	public static final Long C_LOW_STORAGE_RELEASE_CHECK_STOP = 904L;
	public static final Long B_AREA_STORAGE_RELEASE_CHECK_STOP = 3027L;
	public static final Long B_AREA_STORAGE_RECIRC_CHECK_STOP = 3018L;

	public boolean anyCarriersReleasingInStorageArea(Stop currentLocation) {
		List<CarrierMes> carrierMesList = new ArrayList<CarrierMes>();
		if (currentLocation.isRowStop()) {
			StorageRow row = StorageRow.findStorageRowsByStop(currentLocation);
			carrierMesList = CarrierMes.getReleasingCarriersInArea(getStopsByStorageArea(row.getStorageArea()));
		}

		if (carrierMesList.size() > 0) {
			LOG.debug(" Carriers Releasing from storage Area are- " + getCarriers(carrierMesList));
			return true;
		}
		return false;
	}

	public boolean anyCarriersSetToReleaseFromThisRow(Stop currentLocation) {
		List<CarrierRelease> carrierReleases = CarrierRelease.findCarrierReleaseEntriesAtStop(currentLocation.getId());

		if (carrierReleases != null && carrierReleases.size() > 0) {
			return true;
		}
		return false;
	}

	public CarrierRelease getCarriersSetToReleaseFromThisStorageArea(Stop currentLocation) {
		List<CarrierRelease> carrierReleaseList = new ArrayList<CarrierRelease>();
		if (currentLocation.isRowStop()) {
			StorageRow row = StorageRow.findStorageRowsByStop(currentLocation);
			carrierReleaseList = CarrierRelease
					.findCarrierReleaseEntriesInStorageArea(getStopsByStorageArea(row.getStorageArea()));
		}

		if (carrierReleaseList.size() > 0) {
			return carrierReleaseList.get(0);
		}
		return null;
	}

	public CarrierRelease getCarrierToReleaseFromStorage(Stop currentLocation) {
		List<CarrierRelease> carrierReleaseList = new ArrayList<CarrierRelease>();

		if (currentLocation.getId().equals(C_HIGH_STORAGE_RELEASE_CHECK_STOP)) {
			carrierReleaseList = CarrierRelease.findCarrierReleaseEntriesInStorageAreaThatNeedDestinationUpdated(
					getStopsByStorageArea(StorageArea.C_HIGH));
		}
		if (currentLocation.getId().equals(A_AREA_STORAGE_RELEASE_CHECK_STOP)) {
			carrierReleaseList = CarrierRelease.findCarrierReleaseEntriesInStorageAreaThatNeedDestinationUpdated(
					getStopsByStorageArea(StorageArea.A_AREA));
		}
		if (currentLocation.getId().equals(C_LOW_STORAGE_RELEASE_CHECK_STOP)) {
			carrierReleaseList = CarrierRelease.findCarrierReleaseEntriesInStorageAreaThatNeedDestinationUpdated(
					getStopsByStorageArea(StorageArea.C_LOW));
		}
		if (currentLocation.getId().equals(B_AREA_STORAGE_RELEASE_CHECK_STOP)
				|| currentLocation.getId().equals(B_AREA_STORAGE_RECIRC_CHECK_STOP)) {
			List<CarrierRelease> carrierReleaseList1 = CarrierRelease
					.findCarrierReleaseEntriesInStorageAreaThatNeedDestinationUpdated(
							getStopsByStorageArea(StorageArea.B_AREA));
			carrierReleaseList = CarrierRelease.findCarrierReleaseEntriesInStorageAreaThatNeedDestinationUpdated(
					getStopsByStorageArea(StorageArea.S_AREA));
			carrierReleaseList.addAll(carrierReleaseList1);
		}

		if (carrierReleaseList.size() > 0) {
			return carrierReleaseList.get(0);
		}
		return null;
	}

	private String getCarriers(List<CarrierMes> carrierMesList) {
		String carrierList = "";

		for (CarrierMes carrierMes : carrierMesList) {
			carrierList = carrierList + carrierMes.getCarrierNumber() + ",";
		}
		return carrierList;
	}

}
