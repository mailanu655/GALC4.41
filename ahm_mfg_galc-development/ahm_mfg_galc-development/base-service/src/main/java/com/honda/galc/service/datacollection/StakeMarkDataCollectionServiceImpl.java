package com.honda.galc.service.datacollection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.property.StakeMarkDataCollectionPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.StakeMarkDataCollectionService;
import com.honda.galc.service.property.PropertyService;

public class StakeMarkDataCollectionServiceImpl implements
		StakeMarkDataCollectionService {
	/** validate pass value from data collection service */
	public final static String VALIDATE_PASS = "1";
	
	/** validation result in response */
	public final static String VALID = "1";
	public final static String INVALID = "2";
	
	/** whether the rear glass and stake mark value from request is valid */
	public final static String VALIDATE_SUCCESS = "VALIDATE_SUCCESS";
	public final static String VALIDATE_FAILURE = "VALIDATE_FAILURE";
	
	/** Replay tag for front glass request data received */
	public final static String FRONT_GLASS_STATUS = "ROBOT_SCAN_FRONT_GLASS_STATUS";
	/** Replay tag for rear glass request data received */
	public final static String REAR_GLASS_STATUS = "ROBOT_SCAN_REAR_GLASS_STATUS";
	/** Replay tag for staked mark request data received */
	public final static String STAKE_MARK_STATUS = "STAKE_MARK_STATUS";
	
	/** Error code */
	public final static int ERROR_CODE_INVALID_VIN = 1;
	public final static int ERROR_CODE_INVALID_FRONT_GLASS = 2;
	public final static int ERROR_CODE_INVALID_REAR_GLASS = 3;
	public final static int ERROR_CODE_INVALID_STAKE_MARK = 4;
	public final static int ERROR_CODE_GENERIC_ERROR = 5;

	@Override
	public DataContainer execute(DefaultDataContainer data) {
		String ppid = data.getString(TagNames.PROCESS_POINT_ID.name());
		StakeMarkDataCollectionPropertyBean property = PropertyService
				.getPropertyBean(StakeMarkDataCollectionPropertyBean.class,
						ppid);
		
		ProductDataCollector dc = ServiceFactory.getService(ProductDataCollector.class);
		
		String rearGlassValidateResult = VALIDATE_FAILURE;
		String stakedMarkValidateResult = VALIDATE_FAILURE;
		DataContainer result = new DefaultDataContainer();
		try {
			// validate rear glass
			rearGlassValidateResult = validateRobotScanRearGlass(data, property);
			// validate staked mark
			stakedMarkValidateResult = validateStakedMark(data, property);
			result = dc.execute(data);
		} catch (Exception e) {
			addErrorMessage(TagNames.ERROR_MESSAGES.name(), "Generic Error", result);
			addErrorCode(TagNames.ERROR_CODE.name(), ERROR_CODE_GENERIC_ERROR, result);
		}
		
		prepareResponse(rearGlassValidateResult, stakedMarkValidateResult, result, property, dc);
		return result;
	}

	/**
	 * Merge response data. Including VIN, front glass, rear glass and stake mark validation result. 
	 * And remove unnecessary items from result.
	 * @param rearGlassValidateResult
	 * @param stakedMarkValidateResult
	 * @param result
	 */
	private void prepareResponse(String rearGlassValidateResult,
			String stakedMarkValidateResult, DataContainer result, 
			StakeMarkDataCollectionPropertyBean property,
			ProductDataCollector dataCollector) {
		
		//product VIN validation result
		String isProdcutIdValidTag = TagNames.VALID_PRODUCT_ID.name();
		Object isProductIdValid = result.get(isProdcutIdValidTag);
		if(!Boolean.parseBoolean((String)isProductIdValid)) {
			setErrorToResult(ERROR_CODE_INVALID_VIN, result, property);
		}
		
		//front glass validation result
		String frontGlassStatusTag = TagNames.ROBOT_SCAN_FRONT_GLASS_STATUS.name();
		if(VALIDATE_PASS.equals(result.get(frontGlassStatusTag))) {
			result.put(FRONT_GLASS_STATUS, VALID);
		} else {
			setErrorToResult(ERROR_CODE_INVALID_FRONT_GLASS, result, property);
		}
		
		//rear glass validation result
		String clearGlassStatusTag = TagNames.ROBOT_SCAN_REAR_CLEAR_GLASS_STATUS.name();
		String tintedGlassStatusTag = TagNames.ROBOT_SCAN_REAR_TINTED_GLASS_STATUS.name();
		if(VALIDATE_FAILURE.equals(rearGlassValidateResult)) {
			//Input rear glass is not valid, set the response status as "2"(bad)
			setErrorToResult(ERROR_CODE_INVALID_REAR_GLASS, result, property);
		} else {
			if(VALIDATE_PASS.equals(result.get(clearGlassStatusTag)) || VALIDATE_PASS.equals(result.get(tintedGlassStatusTag))) {
				result.put(REAR_GLASS_STATUS, VALID);
			} else {
				setErrorToResult(ERROR_CODE_INVALID_REAR_GLASS, result, property);
			}
		}
		
		//stake mark validation result
		String stakedMarkStatusTag = TagNames.STAKED_MARK_STATUS.name();
		String notStakedMarkStatusTag = TagNames.NOT_STAKED_MARK_STATUS.name();
		if(VALIDATE_FAILURE.equals(stakedMarkValidateResult)) {
			//Input stake mark is not valid, set the response status as "2"(bad)
			setErrorToResult(ERROR_CODE_INVALID_STAKE_MARK, result, property);
		} else {
			if(VALIDATE_PASS.equals(result.get(stakedMarkStatusTag)) || VALIDATE_PASS.equals(result.get(notStakedMarkStatusTag))) {
				result.put(STAKE_MARK_STATUS, VALID);
			} else {
				setErrorToResult(ERROR_CODE_INVALID_STAKE_MARK, result, property);
			}
		}
		//remove raw status info from data container
		result.remove(isProdcutIdValidTag);
		result.remove(frontGlassStatusTag);
		result.remove(clearGlassStatusTag);
		result.remove(tintedGlassStatusTag);
		result.remove(stakedMarkStatusTag);
		result.remove(notStakedMarkStatusTag);
	}
	
	
	/**
	 * Sets the error code and error messages to response body.
	 *
	 * @param errorCode the error code
	 * @param result the result
	 * @param property the property
	 */
	private void setErrorToResult(int errorCode, DataContainer result, StakeMarkDataCollectionPropertyBean property) {
		String errorMessageTag = TagNames.ERROR_MESSAGES.name();
		String errorCodeTag = TagNames.ERROR_CODE.name();
		
		switch(errorCode) {
		case ERROR_CODE_INVALID_VIN:
			addErrorMessage(errorMessageTag, "Invalid VIN", result);
			break;
		case ERROR_CODE_INVALID_FRONT_GLASS:
			addErrorMessage(errorMessageTag, "Invalid Robot Scan Front Glass", result);
			break;
		case ERROR_CODE_INVALID_REAR_GLASS:
			addErrorMessage(errorMessageTag, "Invalid Robot Scan Rear Glass", result);
			result.put(REAR_GLASS_STATUS, INVALID);
			break;
		case ERROR_CODE_INVALID_STAKE_MARK:
			addErrorMessage(errorMessageTag, "Invalid Stake Mark", result);
			result.put(STAKE_MARK_STATUS, INVALID);
			break;
		}
		addErrorCode(errorCodeTag, errorCode, result);
	}

	/**
	 * Adds the error message to response body.
	 *
	 * @param errorMessageTag the error message tag
	 * @param errorMessage the error message
	 * @param result the result
	 */
	@SuppressWarnings("unchecked")
	private void addErrorMessage(String errorMessageTag, String errorMessage, DataContainer result) {
		List<String> errorMessages;
		if(result.get(errorMessageTag) != null) {
			errorMessages = (List<String>)result.get(errorMessageTag);
		} else {
			errorMessages = new ArrayList<String>();
			result.put(errorMessageTag, errorMessages);
		}
		errorMessages.add(errorMessage);
	}
	
	/**
	 * Adds the error code to response body.
	 *
	 * @param errorCodeTag the error code tag
	 * @param errorCode the error code
	 * @param result the result
	 */
	@SuppressWarnings("unchecked")
	private void addErrorCode(String errorCodeTag, int errorCode, DataContainer result) {
		List<Integer> errorCodes;
		if(result.get(errorCodeTag) != null) {
			errorCodes = (List<Integer>)result.get(errorCodeTag);
		} else {
			errorCodes = new ArrayList<Integer>();
			result.put(errorCodeTag, errorCodes);
		}
		errorCodes.add(errorCode);
	}
	
	/**
	 * Validate rear glass information from request data and set ignore glass type.
	 * @param data
	 * @param property
	 * @return
	 */
	private String validateRobotScanRearGlass(DataContainer data,
			StakeMarkDataCollectionPropertyBean property) {
		String rearGlass = data.getString(TagNames.ROBOT_SCAN_REAR_GLASS.name());
		if (StringUtils.equals(rearGlass, property.getTintedGlass())) {
			// Rear glass is tinted. So ignore the part of clear glass.
			ignorePart(data, property.getClearRearGlassPartName());
		} else if (StringUtils.equals(rearGlass, property.getClearGlass())) {
			// Rear glass is clear. So ignore the part of tinted glass.
			ignorePart(data, property.getTintedRearGlassPartName());
		} else {
			// error
			ignorePart(data, property.getClearRearGlassPartName());
			ignorePart(data, property.getTintedRearGlassPartName());
			return VALIDATE_FAILURE;
		}
		
		return VALIDATE_SUCCESS;
	}

	/**
	 * Validate staked mark information from request data and set ignore part.
	 * @param data
	 * @param property
	 * @return
	 */
	private String validateStakedMark(DataContainer data,
			StakeMarkDataCollectionPropertyBean property) {
		String stakedMark = data.getString(TagNames.STAKED_MARK.name());
		if (StringUtils.equals(stakedMark, property.getStakedMark())) {
			// It is staked mark. so ignore the part of not staked mark
			ignorePart(data, property.getNotStakedPartName());
		} else if (StringUtils.equals(stakedMark, property.getNotStakedMark())) {
			// It is not staked mark. so ignore the part of staked mark
			ignorePart(data, property.getStakedPartName());
		} else {
			// error
			ignorePart(data, property.getNotStakedPartName());
			ignorePart(data, property.getStakedPartName());
			return VALIDATE_FAILURE;
		}

		return VALIDATE_SUCCESS;
	}

	/**
	 * Set ignore part into data container.
	 * @param data
	 * @param partName
	 */
	private void ignorePart(DataContainer data, String partName) {
		String ignoredParts = data.getString(TagNames.IGNORED_PARTS.name());
		if (StringUtils.isBlank(ignoredParts)) {
			ignoredParts = partName;
		} else {
			ignoredParts = ignoredParts + "," + partName;
		}
		data.put(TagNames.IGNORED_PARTS.name(), ignoredParts);
	}

}
