package com.honda.galc.service.vinstamp;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.data.DataContainer;
import com.honda.galc.service.IoService;

public interface VinStampingService extends IoService {

	/**
	 * Returns the next vin.
	 * @param input - <br>
	 * TERMINAL_ID : the component id,<br>
	 * LAST_VIN : the last vin,<br>
	 * UPDATE : flag for whether to update the status
	 * @return NEXT_VIN : the next vin,<br>
	 * EXPECTED_VIN : the expected vin,<br>
	 * PRODUCT_SPEC_CODE : the product spec code,<br>
	 * BOUNDARY_MARK : the boundary mark,<br>
	 * MODEL : the model,<br>
	 * TYPE : the type,<br>
	 * OPTION : the option,<br>
	 * INT_COLOR : the interior colour,<br>
	 * EXT_COLOR : the exterior colour,<br>
	 * MISSION_TYPE : the transmission type (AT or MT),<br>
	 * KD_LOT : the kd lot number,<br>
	 * INFO_CODE : the info code,<br>
	 * INFO_MESSAGE : the info message
	 */
	public DataContainer getNextVin(DataContainer input);

	/**
	 * Returns the next vin.
	 * @param componentId : the component id,<br>
	 * @param lastVin : the last vin,<br>
	 * @param updateFlag : flag for whether to update the status
	 * @return NEXT_VIN : the next vin,<br>
	 * EXPECTED_VIN : the expected vin,<br>
	 * PRODUCT_SPEC_CODE : the product spec code,<br>
	 * BOUNDARY_MARK : the boundary mark,<br>
	 * MODEL : the model,<br>
	 * TYPE : the type,<br>
	 * OPTION : the option,<br>
	 * INT_COLOR : the interior colour,<br>
	 * EXT_COLOR : the exterior colour,<br>
	 * MISSION_TYPE : the transmission type (AT or MT),<br>
	 * KD_LOT : the kd lot number,<br>
	 * INFO_CODE : the info code,<br>
	 * INFO_MESSAGE : the info message
	 */
	public DataContainer getNextVin(String componentId, String lastVin, boolean updateFlag);

	/**
	 * Processes the stamped vin.
	 * @param input - <br>
	 * TERMINAL_ID : the component id,<br>
	 * STAMPED_VIN : the stamped vin,<br>
	 * STAMPED_TIME : the timestamp for the stamp
	 * @return STAMPED_VIN : the stamped vin,<br>
	 * EXPECTED_VIN : the expected vin,<br>
	 * PRODUCT_SPEC_CODE : the product spec code,<br>
	 * MODEL : the model,<br>
	 * TYPE : the type,<br>
	 * OPTION : the option,<br>
	 * INT_COLOR : the interior color,<br>
	 * EXT_COLOR : the exterior color,<br>
	 * MISSION_TYPE : the transmission type (AT or MT),<br>
	 * KD_LOT : the kd lot number,<br>
	 * INFO_CODE : the info code,<br>
	 * INFO_MESSAGE : the info message
	 */
	public DataContainer processStampedVinResults(DataContainer input);

	/**
	 * Processes the stamped vin.
	 * @param componentId : the component id,<br>
	 * @param stampedVin : the stamped vin,<br>
	 * @param stampedTime : the timestamp for the stamp
	 * @return STAMPED_VIN : the stamped vin,<br>
	 * EXPECTED_VIN : the expected vin,<br>
	 * PRODUCT_SPEC_CODE : the product spec code,<br>
	 * MODEL : the model,<br>
	 * TYPE : the type,<br>
	 * OPTION : the option,<br>
	 * INT_COLOR : the interior color,<br>
	 * EXT_COLOR : the exterior color,<br>
	 * MISSION_TYPE : the transmission type (AT or MT),<br>
	 * KD_LOT : the kd lot number,<br>
	 * INFO_CODE : the info code,<br>
	 * INFO_MESSAGE : the info message
	 */
	public DataContainer processStampedVinResults(String componentId, String stampedVin, Timestamp stampedTime);

	/**
	 * Returns the attributes.
	 * @param input - <br>
	 * TERMINAL_ID : the component id,<br>
	 * PRODUCT_SPEC_CODE : the product spec code,<br>
	 * ATTRIBUTES : the list of attributes to get
	 * @return ATTRIBUTE_VALUES : the values of the attributes,<br>
	 * INFO_CODE : the info code,<br>
	 * INFO_MESSAGE : the info message
	 */
	public DataContainer getAttributes(DataContainer input);

	/**
	 * Returns the attributes.
	 * @param componentId : the component id,<br>
	 * @param productSpecCode : the product spec code,<br>
	 * @param attributes : the list of attributes to get
	 * @return ATTRIBUTE_VALUES : the values of the attributes,<br>
	 * INFO_CODE : the info code,<br>
	 * INFO_MESSAGE : the info message
	 */
	public DataContainer getAttributes(String componentId, String productSpecCode, List<String> attributes);

	/**
	 * Updates the product's send status to SENT.
	 * @param productionLot : the product's lot,<br>
	 * @param productId : the product's id,<br>
	 * @param componentId : the tracking process point
	 * @return true iff the update was successful
	 */
	public boolean updateStatusToSent(final String productionLot, final String productId, final String componentId);
	
	/**
	 * Creates product History and calls weld On Service.
	 * @param stampedVin : the stamped product's id,<br>
	 * @param stampedTime : the stamped Time,<br>
	 * @param componentId : the tracking process point
	 */
	public void updateStatus(String stampedVin, Timestamp stampedTime, String componentId);
	/**
	 * Creates product History and calls weld On Service.
	 * @param stampedVin : the stamped product's id,<br>
	 * @param componentId : the tracking process point
	 */
	public void updateStatus(String stampedVin,String componentId);
}
