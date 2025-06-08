package com.honda.galc.service.vinstamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.client.enumtype.FloorStampInfoCodes;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.on.WeldOnService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class VinStampingServiceImpl implements VinStampingService {

	private static final String ATTRIBUTE_VALUE_NOT_FOUND = "ATTRIBUTE_VALUE_NOT_FOUND";
	private static final String NO_INPUT_VALUES_SPECIFIED = "NO_INPUT_VALUES_SPECIFIED";
	private static final String MANUAL_TRANSMISSION_TYPE = "MT";
	private static final String AUTOMATIC_TRANSMISSION_TYPE = "AT";
	private static final String NOT_FOUND = "Not Found";

	private Logger logger;

	@Autowired
	public BuildAttributeDao buildAttributeDao;

	@Autowired
	public EngineSpecDao engineSpecDao;

	@Autowired
	public ExpectedProductDao expectedProductDao;

	@Autowired
	public FrameDao frameDao;

	@Autowired
	public FrameSpecDao frameSpecDao;

	@Autowired
	public PreProductionLotDao preProductionLotDao;

	@Autowired
	public ProductionLotDao productionLotDao;

	@Autowired
	public ProductStampingSequenceDao productStampingSequenceDao;

	@Override
	public Device execute(Device device) { return null; }

	@Override
	public DataContainer execute(DataContainer data) { return null; }

	@Override
	public DataContainer getNextVin(DataContainer input) {
		try {
			String componentId = (String) input.get(DataContainerTag.COMPONENT_ID);
			String lastVin = (String) input.get(DataContainerTag.LAST_VIN);
			Boolean updateFlag = Boolean.valueOf(input.get(DataContainerTag.UPDATE).toString());

			return getNextVin(componentId, lastVin, updateFlag);
		} catch (Exception e) {
			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.NEXT_VIN, null);
			output.put(DataContainerTag.EXPECTED_VIN, null);
			output.put(DataContainerTag.PRODUCT_SPEC_CODE, null);
			output.put(DataContainerTag.BOUNDARY_MARK, null);
			output.put(DataContainerTag.MODEL, null);
			output.put(DataContainerTag.TYPE, null);
			output.put(DataContainerTag.OPTION, null);
			output.put(DataContainerTag.INT_COLOR, null);
			output.put(DataContainerTag.EXT_COLOR, null);
			output.put(DataContainerTag.MISSION_TYPE, null);
			output.put(DataContainerTag.KD_LOT, null);
			output.put(DataContainerTag.INFO_CODE, DEFAULT_ERRORCODE);
			output.put(DataContainerTag.INFO_MESSAGE, e.toString());
			return output;
		}
	}

	@Override
	public DataContainer getNextVin(String componentId, String lastVin, boolean updateFlag) {
		try {
			NextVinData data = executeNextVinProcessor(lastVin, ProductStampingSendStatus.WAITING.getId(), updateFlag, componentId);

			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.NEXT_VIN, data.getNextVin());
			output.put(DataContainerTag.EXPECTED_VIN, data.getExpectedVin());
			output.put(DataContainerTag.PRODUCT_SPEC_CODE, data.getProductSpecCode());
			output.put(DataContainerTag.BOUNDARY_MARK, data.getBoundaryMark());
			output.put(DataContainerTag.MODEL, data.getModel());
			output.put(DataContainerTag.TYPE, data.getType());
			output.put(DataContainerTag.OPTION, data.getOption());
			output.put(DataContainerTag.INT_COLOR, data.getIntColor());
			output.put(DataContainerTag.EXT_COLOR, data.getExtColor());
			output.put(DataContainerTag.MISSION_TYPE, data.getMissionType());
			output.put(DataContainerTag.KD_LOT, data.getKdLot());
			output.put(DataContainerTag.INFO_CODE, data.getInfoCode());
			output.put(DataContainerTag.INFO_MESSAGE, data.getInfoMessage());
			return output;
		} catch (Exception e) {
			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.NEXT_VIN, null);
			output.put(DataContainerTag.EXPECTED_VIN, null);
			output.put(DataContainerTag.PRODUCT_SPEC_CODE, null);
			output.put(DataContainerTag.BOUNDARY_MARK, null);
			output.put(DataContainerTag.MODEL, null);
			output.put(DataContainerTag.TYPE, null);
			output.put(DataContainerTag.OPTION, null);
			output.put(DataContainerTag.INT_COLOR, null);
			output.put(DataContainerTag.EXT_COLOR, null);
			output.put(DataContainerTag.MISSION_TYPE, null);
			output.put(DataContainerTag.KD_LOT, null);
			output.put(DataContainerTag.INFO_CODE, DEFAULT_ERRORCODE);
			output.put(DataContainerTag.INFO_MESSAGE, e.toString());
			return output;
		}
	}

	@Override
	public DataContainer processStampedVinResults(DataContainer input) {
		try {
			String componentId = (String) input.get(DataContainerTag.COMPONENT_ID);
			String stampedVin = (String) input.get(DataContainerTag.STAMPED_VIN);
			String stampedTimeString = (String) input.get(DataContainerTag.STAMPED_TIME);
			Timestamp stampedTime = Timestamp.valueOf(stampedTimeString.substring(0,stampedTimeString.length()-4));

			return processStampedVinResults(componentId, stampedVin, stampedTime);
		} catch (Exception e) {
			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.STAMPED_VIN, null);
			output.put(DataContainerTag.EXPECTED_VIN, null);
			output.put(DataContainerTag.PRODUCT_SPEC_CODE, null);
			output.put(DataContainerTag.BOUNDARY_MARK, null);
			output.put(DataContainerTag.MODEL, null);
			output.put(DataContainerTag.TYPE, null);
			output.put(DataContainerTag.OPTION, null);
			output.put(DataContainerTag.INT_COLOR, null);
			output.put(DataContainerTag.EXT_COLOR, null);
			output.put(DataContainerTag.MISSION_TYPE, null);
			output.put(DataContainerTag.KD_LOT, null);
			output.put(DataContainerTag.INFO_CODE, DEFAULT_ERRORCODE);
			output.put(DataContainerTag.INFO_MESSAGE, e.toString());
			return output;
		}
	}

	@Override
	public DataContainer processStampedVinResults(String componentId, String stampedVin, Timestamp stampedTime) {
		try {
			ResultVerificationData data = executeResultVerificationProcessor(stampedVin, stampedTime, componentId);

			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.STAMPED_VIN, data.getStampedVin());
			output.put(DataContainerTag.EXPECTED_VIN, data.getExpectedVin());
			output.put(DataContainerTag.PRODUCT_SPEC_CODE, data.getProductSpecCode());
			output.put(DataContainerTag.BOUNDARY_MARK, data.getBoundaryMark());
			output.put(DataContainerTag.MODEL, data.getModel());
			output.put(DataContainerTag.TYPE, data.getType());
			output.put(DataContainerTag.OPTION, data.getOption());
			output.put(DataContainerTag.INT_COLOR, data.getIntColor());
			output.put(DataContainerTag.EXT_COLOR, data.getExtColor());
			output.put(DataContainerTag.MISSION_TYPE, data.getMissionType());
			output.put(DataContainerTag.KD_LOT, data.getKdLot());
			output.put(DataContainerTag.INFO_CODE, data.getInfoCode());
			output.put(DataContainerTag.INFO_MESSAGE, data.getInfoMessage());
			return output;
		} catch (Exception e) {
			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.STAMPED_VIN, null);
			output.put(DataContainerTag.EXPECTED_VIN, null);
			output.put(DataContainerTag.PRODUCT_SPEC_CODE, null);
			output.put(DataContainerTag.BOUNDARY_MARK, null);
			output.put(DataContainerTag.MODEL, null);
			output.put(DataContainerTag.TYPE, null);
			output.put(DataContainerTag.OPTION, null);
			output.put(DataContainerTag.INT_COLOR, null);
			output.put(DataContainerTag.EXT_COLOR, null);
			output.put(DataContainerTag.MISSION_TYPE, null);
			output.put(DataContainerTag.KD_LOT, null);
			output.put(DataContainerTag.INFO_CODE, DEFAULT_ERRORCODE);
			output.put(DataContainerTag.INFO_MESSAGE, e.toString());
			return output;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataContainer getAttributes(DataContainer input) {
		try {
			String componentId = (String) input.get(DataContainerTag.COMPONENT_ID);
			String productSpecCode = (String) input.get(DataContainerTag.PRODUCT_SPEC_CODE);
			List<String> attributes = (List<String>) input.get(DataContainerTag.ATTRIBUTES);

			return getAttributes(componentId, productSpecCode, attributes);
		} catch (Exception e) {
			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.ATTRIBUTE_VALUES, null);
			output.put(DataContainerTag.INFO_CODE, DEFAULT_ERRORCODE);
			output.put(DataContainerTag.INFO_MESSAGE, e.toString());
			return output;
		}
	}

	@Override
	public DataContainer getAttributes(String componentId, String productSpecCode, List<String> attributes) {
		try {
			logger = Logger.getLogger(componentId);

			String infoCode = "";
			StringBuilder infoMessage = new StringBuilder();
			List<String> attributeValues = new ArrayList<String>();
			if (attributes.isEmpty()) {
				infoCode = NO_INPUT_VALUES_SPECIFIED;
				infoMessage.append("No input values were specified. No results returned.");
				logger.warn("No input values were specified. No results returned.");
			} else {
				for (String attribute : attributes) {
					BuildAttribute buildAttribute = buildAttributeDao.findById(attribute, productSpecCode);
					if (buildAttribute != null && !StringUtils.isEmpty(buildAttribute.getAttributeValue()))
						attributeValues.add(buildAttribute.getAttributeValue());
					else {
						attributeValues.add(NOT_FOUND);
						infoCode = ATTRIBUTE_VALUE_NOT_FOUND;
						if (infoMessage.length() == 0) {
							infoMessage.append("Could not find values for the following attribute(s) - ");
							infoMessage.append(attribute);
							logger.warn("Could not find value for attribute \"" + attribute + "\".");
						} else {
							infoMessage.append(", ");
							infoMessage.append(attribute);
						}
					}
				}
			}

			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.ATTRIBUTE_VALUES, attributeValues);
			output.put(DataContainerTag.INFO_CODE, infoCode);
			output.put(DataContainerTag.INFO_MESSAGE, infoMessage.toString());
			return output;
		} catch (Exception e) {
			DataContainer output = new DefaultDataContainer();
			output.put(DataContainerTag.ATTRIBUTE_VALUES, null);
			output.put(DataContainerTag.INFO_CODE, DEFAULT_ERRORCODE);
			output.put(DataContainerTag.INFO_MESSAGE, e.toString());
			return output;
		}
	}

	@Override
	public boolean updateStatusToSent(final String productionLot, final String productId, final String componentId) {
		logger = Logger.getLogger(componentId);
		NextVinData data = new NextVinData();
		data.setProductionLot(productionLot);
		data.setNextVin(productId);
		return updateStatus(data, componentId);
	}





	/*
	 * Nested POJOs for holding data
	 */

	private class BasicData {
		// fields used by the REST service
		protected String expectedVin;
		protected String productSpecCode;
		protected String boundaryMark;
		protected String model;
		protected String type;
		protected String option;
		protected String intColor;
		protected String extColor;
		protected String missionType;
		protected String kdLot;
		protected String infoCode;
		protected String infoMessage;

		public String getExpectedVin() { return expectedVin; }
		public void setExpectedVin(String expectedVin) { this.expectedVin = expectedVin; }
		public String getProductSpecCode() { return productSpecCode; }
		public void setProductSpecCode(String productSpecCode) { this.productSpecCode = productSpecCode; }
		public String getBoundaryMark() { return boundaryMark; }
		public void setBoundaryMark(String boundaryMark) { this.boundaryMark = boundaryMark; }
		public String getModel() { return model; }
		public void setModel(String model) { this.model = model; }
		public String getType() { return type; }
		public void setType(String type) { this.type = type; }
		public String getOption() { return option; }
		public void setOption(String option) { this.option = option; }
		public String getIntColor() { return intColor; }
		public void setIntColor(String intColor) { this.intColor = intColor; }
		public String getExtColor() { return extColor; }
		public void setExtColor(String extColor) { this.extColor = extColor; }
		public String getMissionType() { return missionType; }
		public void setMissionType(String missionType) { this.missionType = missionType; }
		public String getKdLot() { return kdLot; }
		public void setKdLot(String kdLot) { this.kdLot = kdLot; }
		public String getInfoCode() { return infoCode; }
		public void setInfoCode(String infoCode) { this.infoCode = infoCode; }
		public String getInfoMessage() { return infoMessage; }
		public void setInfoMessage(String infoMessage) { this.infoMessage = infoMessage; }

		// fields used for fetching data
		protected int infoPriority = -1;
		protected String productionLot;

		public int getInfoPriority() { return infoPriority; }
		public void setInfoPriority(int infoPriority) { this.infoPriority = infoPriority; }
		public String getProductionLot() { return productionLot; }
		public void setProductionLot(String productionLot) { this.productionLot = productionLot; }
	}

	private class NextVinData extends BasicData {
		// fields used by the REST service
		private String nextVin;

		public String getNextVin() { return nextVin; }
		public void setNextVin(String nextVin) { this.nextVin = nextVin; }

		// fields used for fetching data
		private int errorCode = DEFAULT_ERRORCODE;

		public int getErrorCode() { return errorCode; }
		public void setErrorCode(int errorCode) { this.errorCode = errorCode; }
	}

	private class ResultVerificationData extends BasicData {
		// fields used by the REST service
		private String stampedVin;

		public String getStampedVin() { return stampedVin; }
		public void setStampedVin(String stampedVin) { this.stampedVin = stampedVin; }

		// fields used for fetching data
	}





	/*
	 * Common methods
	 */

	/**
	 * Returns true iff the given vin exists.
	 */
	private boolean doesVinExist(String vin) {
		Frame frame;
		try {
			frame = frameDao.findByKey(vin);
			if (frame != null && frame.getProductId() != null && !frame.getProductId().trim().equals("")) {
				return true;
			}
		} catch(Exception ex) {}
		return false;
	}

	/**
	 * Updates the given data's info to match the given info code iff it is higher priority than the current info.
	 */
	private void updateVinStampInfo(FloorStampInfoCodes vinStampInfo, String vin, BasicData data) {
		if(vinStampInfo.ordinal() > data.getInfoPriority()) {
			data.setInfoPriority(vinStampInfo.ordinal());
			data.setInfoCode("" + vinStampInfo.getInfoCode());
			data.setInfoMessage(vinStampInfo.getInfoMessage(vin));
		}
	}

	/**
	 * Finds the RFID data for the given VIN and stores it in the given BasicData.
	 */
	private boolean findRfidData(BasicData data, String vin) {
		boolean rfidDataOk = true;
		Frame frame = null;
		try {
			frame = frameDao.findByKey(vin);
			if (rfidDataOk &= (frame != null)) {
				// set product spec code
				data.setProductSpecCode(frame.getProductSpecCode());
				rfidDataOk &= !StringUtils.isEmpty(data.getProductSpecCode());
				// set production lot and kd lot
				if (frame.getProductionLot() != null) {
					ProductionLot productionLot = productionLotDao.findByKey(frame.getProductionLot());
					if (productionLot != null) {
						data.setProductionLot(productionLot.getProductionLot());
						data.setKdLot(productionLot.getKdLotNumber());
					} else {
						data.setProductionLot(frame.getProductionLot());
						data.setKdLot(frame.getKdLotNumber());
					}
				}
				rfidDataOk &= !StringUtils.isEmpty(data.getProductionLot());
				rfidDataOk &= !StringUtils.isEmpty(data.getKdLot());

				FrameSpec frameSpec = frameSpecDao.findByKey(frame.getProductSpecCode());
				if (rfidDataOk &= (frameSpec != null)) {
					// set boundary mark
					{
						String boundaryMark = frameSpec.getBoundaryMarkRequired();
						if(boundaryMark == null || boundaryMark.trim().equals(""))
							boundaryMark = " ";
						else
							boundaryMark = boundaryMark.trim().substring(0, 1);
						data.setBoundaryMark(boundaryMark);
					}
					// set model
					{
						String modelYearCode = frameSpec.getModelYearCode();
						String modelCode = frameSpec.getModelCode();
						rfidDataOk &= !StringUtils.isEmpty(modelYearCode);
						rfidDataOk &= !StringUtils.isEmpty(modelCode);
						data.setModel(modelYearCode + modelCode);
					}
					// set type
					data.setType(frameSpec.getModelTypeCode());
					rfidDataOk &= !StringUtils.isEmpty(data.getType());
					// set option
					data.setOption(frameSpec.getModelOptionCode());
					rfidDataOk &= !StringUtils.isEmpty(data.getOption());
					// set int color
					data.setIntColor(frameSpec.getIntColorCode());
					rfidDataOk &= !StringUtils.isEmpty(data.getIntColor());
					// set ext color
					data.setExtColor(frameSpec.getExtColorCode());
					rfidDataOk &= !StringUtils.isEmpty(data.getExtColor());

					// set mission type
					EngineSpec engineSpec = engineSpecDao.findByKey(frameSpec.getEngineMto());
					if (rfidDataOk &= (engineSpec != null)) {
						String transmission = engineSpec.getTransmission();
						if (transmission == null || (!transmission.equals(AUTOMATIC_TRANSMISSION_TYPE) && !transmission.equals(MANUAL_TRANSMISSION_TYPE))) {
							String transmissionDesc = engineSpec.getTransmissionDescription();
							if (StringUtils.containsIgnoreCase(transmissionDesc, "MANUAL")) {
								transmission = MANUAL_TRANSMISSION_TYPE;
							} else {
								transmission = AUTOMATIC_TRANSMISSION_TYPE;
							}
						}
						data.setMissionType(transmission);
					}
				}
			}
		} catch (Exception ex) {
			rfidDataOk = false;
			ex.printStackTrace();
			String message = "Unable to retrieve and set frame data for VIN: " + 
					vin + " with product spec code: " + (frame == null ? null : frame.getProductSpecCode());
			logger.error(ex, message);
			data.setInfoCode(String.valueOf(DEFAULT_ERRORCODE));
			data.setInfoMessage(message + " due to exception: " + ex.toString());
		}
		return rfidDataOk;
	}





	/*
	 * Code from FloorStampVinProcessor.java
	 */

	private static final int DEFAULT_ERRORCODE = -1;
	private static final int VIN_READY = 0;
	private static final int INVALID_VIN = 1;
	private static final int VIN_NOT_IN_SCHEDULE = 2;
	private static final int NO_NEXT_VIN = 3;
	private static final int VIN_ALREADY_STAMPED = 4;
	private static final int VIN_ALREADY_SENT = 5;
	private static final int SKIPPED_VIN = 6;

	private NextVinData executeNextVinProcessor(String lastVin, int sendStatus, boolean update, String componentId) {
		logger = Logger.getLogger(componentId);
		boolean autoSkip;
		NextVinData data;
		do {
			autoSkip = false;
			data = new NextVinData();
			retrieveNextVin(lastVin, sendStatus, componentId, data);
			if (data.getErrorCode() == VIN_ALREADY_STAMPED && PropertyService.getPropertyBoolean(componentId, "SKIP_ALREADY_STAMPED_VIN", false)) {
				autoSkip = true;
				logger.warn("Vin " + data.getNextVin() + " has already been stamped.  Skipping " + data.getNextVin() + ".");
				lastVin = data.getNextVin();
			}
		} while (autoSkip);

		switch (data.getErrorCode()) {
		case VIN_READY:
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_OK, lastVin, data);
			break;
		case INVALID_VIN:
			logger.warn("Vin " + lastVin + " is not valid");
			// invalid VIN also not in schedule
		case VIN_NOT_IN_SCHEDULE:
			logger.warn("Vin " + lastVin + " is not in schedule");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_INVALID, lastVin, data);
			break;
		case NO_NEXT_VIN:
			logger.warn("Vin " + lastVin + " has no next vin");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_NO_NEXT_VIN, lastVin, data);
			break;
		case VIN_ALREADY_STAMPED:
			logger.warn("Vin " + lastVin + " has already been stamped ");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_ALREADY_PROCESSED, lastVin, data);
			break;
		case VIN_ALREADY_SENT:
			logger.warn("Vin " + lastVin + " has already been sent ");
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_ALREADY_PROCESSED, lastVin, data);
			break;
		case SKIPPED_VIN:
			logger.warn("Skipped Vin " + data.getExpectedVin());
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_SKIPPED, data.getExpectedVin(), data);
			break;
		case DEFAULT_ERRORCODE:		
		default:
			logger.warn("Invalid error code returned from getNextVin(): " + data.getErrorCode());
			break;
		}		

		// unexpected null or empty vin
		if (StringUtils.isEmpty(data.getNextVin())) {
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_INVALID, lastVin, data);
			return data;
		}

		// vin not in GALC database
		if (!doesVinExist(data.getNextVin())) {
			updateVinStampInfo(FloorStampInfoCodes.REQUEST_VIN_INVALID, lastVin, data);
			return data;
		}

		logger.info("Retrieved next vin is: " + data.getNextVin() + " for last vin: " + lastVin + " and component id: " + componentId);

		findRfidData(data, data.getNextVin());
		if (update && isUpdatableErrorCode(data.getErrorCode())) {
			if (!updateStatus(data, componentId)) {
				data.setInfoCode(String.valueOf(DEFAULT_ERRORCODE));
				data.setInfoMessage("Failed to update status for next VIN: " + data.getNextVin());
			}
		}

		return data;
	}

	private String retrieveNextVin(String lastVin, int sendStatus, String componentId, NextVinData data) {
		Object[] result = null;
		try {
			String component = (componentId + "_VIN_STAMP_DASH");
			{
				List<?> properties = PropertyService.getComponentProperty(component);
				if (properties == null || properties.isEmpty()) component = null;
			}
			result = preProductionLotDao.findNextWeldOnProductId(lastVin, sendStatus, component);
		} catch (Exception ex) {
			logger.error("Could not retrieve next product id for vin: " + lastVin);
			ex.printStackTrace();
		}

		if (result == null) {
			logger.error("Null returned from preProductionLotDao.findNextWeldOnProductId() for vin: " + lastVin);
			return "";
		}
		if (result.length < 4) {
			logger.error("Missing data from preProductionLotDao.findNextWeldOnProductId() for vin: " 
					+ lastVin + ", number of fields returned: " + result.length);
			return "";
		}

		try {
			if (result[2] != null)
				data.setExpectedVin((String) result[2]);

			if (result[1] != null)
				data.setNextVin((String) result[1]);
			else {
				logger.warn("Retrieved next vin is null for last vin " + lastVin);
				data.setNextVin(data.getExpectedVin());
			}	

			if (result[3] != null)
				data.setErrorCode((Integer) result[3]);

			return data.getNextVin();
		} catch (Exception ex) {
			logger.error("Invalid results retrieved for vin: " + lastVin);
			ex.printStackTrace();
			return "";
		}
	}

	private boolean isUpdatableErrorCode(int errorCode) {
		switch (errorCode) {
		case VIN_READY:
			return true;
		case INVALID_VIN:
			return false;
		case VIN_NOT_IN_SCHEDULE:
			return false;
		case NO_NEXT_VIN:
			return false;
		case VIN_ALREADY_STAMPED:
			return false;
		case VIN_ALREADY_SENT:
			return false;
		case SKIPPED_VIN:
			return true;
		case DEFAULT_ERRORCODE:		
		default:
			return false;
		}
	}

	private boolean updateStatus(NextVinData data, String componentId) {
		return !StringUtils.isEmpty(data.getNextVin()) &&
				updateProductStartDate(data, componentId) & updateProductSendStatus(data) & updateProductionLotSendStatus(data) & updateProductionLotStatus(data);
	}

	private boolean updateProductStartDate(NextVinData data, String componentId) {
		try {
			frameDao.updateProductStartDate(componentId, data.getNextVin());
			logger.info("Successfully updated product start date for vin: " + data.getNextVin());
			return true;
		} catch (Exception ex) {
			logger.error("Could not update product start date for vin: " + data.getNextVin());
			ex.printStackTrace();
		}
		return false;
	}

	private boolean updateProductSendStatus(NextVinData data) {
		try {
			ProductStampingSequence prodStampSeq = productStampingSequenceDao.findById(data.getProductionLot(), data.getNextVin());

			if (prodStampSeq.getSendStatus() == ProductStampingSendStatus.WAITING.getId()) {
				prodStampSeq.setSendStatus(ProductStampingSendStatus.SENT.getId());
				productStampingSequenceDao.update(prodStampSeq);
				logger.info("Successfully updated send status to: " + ProductStampingSendStatus.SENT.getId() + 
						" for vin: " + data.getNextVin());
			} else {
				logger.info("Did not update send status to: " + ProductStampingSendStatus.SENT.getId() + 
						" for vin: " + data.getNextVin() + " because send status is not 0");
			}
			return true;
		}catch(Exception ex) {
			logger.error("Send status could not be updated for vin: " + data.getNextVin());
			ex.printStackTrace();
		}
		return false;
	}

	private boolean updateProductionLotSendStatus(NextVinData data) {
		try {
			preProductionLotDao.updateSendStatus(data.getProductionLot(), ProductStampingSendStatus.SENT.getId());
			preProductionLotDao.updateSentTimestamp(data.getProductionLot());
			logger.info("Successfully updated send status to: " + ProductStampingSendStatus.SENT.getId() + 
					" for production lot: " + data.getProductionLot());
			return true;
		}catch(Exception ex) {
			logger.error("Send status could not be updated for production lot: " + data.getProductionLot());
			ex.printStackTrace();
		}
		return false;
	}

	// 217 update to emulate old ear
	private boolean updateProductionLotStatus(NextVinData data) {
		try {
			if(productionLotDao.findByKey(data.getProductionLot()).getLotStatus() == 0) {
				productionLotDao.updateLotStatus(data.getProductionLot(), ProductStampingSendStatus.SENT.getId());
				logger.info("Successfully updated lot status (in gal217tbx) to: " + ProductStampingSendStatus.SENT.getId() + 
						" for production lot: " + data.getProductionLot());
			}
			else {
				logger.info("Did not update lot status (in gal217tbx) to: " + ProductStampingSendStatus.SENT.getId() + 
						" for production lot: " + data.getProductionLot() + " because lot status is not 0");
			}
			return true;
		}catch(Exception ex) {
			logger.error("Lot status could not be updated (in gal217tbx) for production lot: " + data.getProductionLot());
			ex.printStackTrace();
		}
		return false;
	}





	/*
	 * Code from FloorStampResultVerificationProcessor.java
	 */

	private ResultVerificationData executeResultVerificationProcessor(String stampedVin, Timestamp stampedTime, String componentId) throws Exception {
		logger = Logger.getLogger(componentId);
		ResultVerificationData data = new ResultVerificationData();
		data.setStampedVin(stampedVin);
		try {
			if (!doesVinExist(stampedVin)) {
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_INVALID, stampedVin, data);
				return data;
			}

			data.setExpectedVin(getExpectedVin(componentId));
			if(data.getExpectedVin() == null || data.getExpectedVin().equals("")) {
				logger.warn("Next expected result VIN is [" + data.getExpectedVin() + "]. Using supplied VIN [" + stampedVin + "]");
				data.setExpectedVin(stampedVin);
			} else if (!data.getExpectedVin().equals(stampedVin)) {
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_SKIPPED, data.getExpectedVin(), data);
			}

			findRfidData(data, stampedVin);
			ProductStampingSequenceId id = new ProductStampingSequenceId(data.getProductionLot(), stampedVin);
			ProductStampingSequence prodStampSeq = productStampingSequenceDao.findByKey(id);

			switch (EnumUtil.getType(ProductStampingSendStatus.class, prodStampSeq.getSendStatus())) {
			case STAMPED:	// updates if skipped vin
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_ALREADY_PROCESSED, stampedVin, data);
				break;
			case SENT:		// normal flow
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_OK, stampedVin, data);
				break;
			case WAITING:	// should never happen - invalid vin - through to default
				logger.error("Result vin send status is 0");
			default:
				updateVinStampInfo(FloorStampInfoCodes.RESULT_VIN_INVALID, stampedVin, data);
				return data;
			}

			updateStatus(stampedVin, stampedTime, componentId);
			return data;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	private String getExpectedVin(String componentId) {
		try {
			// Sometimes this value will be null when the next VIN is not foreseeable
			return expectedProductDao.findByKey(componentId).getProductId();
		} catch(Exception ex) {}
		return "";
	}

	public void updateStatus(String stampedVin, Timestamp stampedTime, String componentId) {
		try {
			Frame frame = frameDao.findByKey(stampedVin);
			ProductHistory productHistory = ProductTypeUtil.createProductHistory(frame.getId(), componentId, ProductType.FRAME);

			if (productHistory != null) {
				productHistory.setActualTimestamp(stampedTime);
			}
			ServiceFactory.getService(WeldOnService.class).processProduct(frame, productHistory, componentId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Send status not updated for Result vin: " + stampedVin);
		}
	}
	
	public void updateStatus(String stampedVin, String componentId) {
		try {
			Frame frame = frameDao.findByKey(stampedVin);
			ProductHistory productHistory = ProductTypeUtil.createProductHistory(frame.getId(), componentId, ProductType.FRAME);

			if (productHistory != null) {
				productHistory.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			}
			ServiceFactory.getService(WeldOnService.class).processProduct(frame, productHistory, componentId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Send status not updated for Result vin: " + stampedVin);
		}
	}
}
