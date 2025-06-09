package com.honda.mfg.stamp.conveyor.release;

import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.helper.Helper;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 2/29/12 Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ReleaseManagerHelper extends Helper {

	/**
	 * @param currentLocation
	 * @return are carriers set to release from this stop?
	 */
	boolean anyCarriersReleasingInStorageArea(Stop currentLocation);

	/**
	 * @param currentLocation
	 * @return are carriers set to release from the storage arrea of this stop?
	 */
	boolean anyCarriersSetToReleaseFromThisRow(Stop currentLocation);

	/**
	 * @param currentLocation
	 * @return the first carrier set to release from the storage area of Current
	 *         location, null if none. .
	 */
	CarrierRelease getCarriersSetToReleaseFromThisStorageArea(Stop currentLocation);

	/**
	 * @param currentLocation
	 * @return the first carrier set to release from the storage area of Current
	 *         location, null if none or stop is not a release check stop.
	 */
	CarrierRelease getCarrierToReleaseFromStorage(Stop currentLocation);
}
