package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.oif.dto.IPlanCodeDTO;
import com.honda.galc.oif.dto.PreProductionLotDTO;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

public abstract class BasePlanCodeTask<T extends IPlanCodeDTO> extends BasePriorityPlanTask<T> {
	public final static String PLANCODES = "PLAN_CODES";
	public final static String DEPTCODES = "DEPARTMENTS";

	private final String InterfaceId;
	private final String resultPath;
	private List<String> planCodes;
	protected int totalRecords = 0, failedRecords = 0;

	public BasePlanCodeTask(String Name) {
		super(Name);

		refreshProperties();

		InterfaceId = getProperty(OIFConstants.INTERFACE_ID);
		resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.honda.galc.oif.task.OifTask#initialize() This initialize is only
	 * called for GPP306, 307, GPP305 and GPP631 not called for shop calendar
	 */
	@Override
	protected void initialize() {
		refreshProperties();

		String planCodes = getProperty(PLANCODES);

		if (planCodes == null)
			throw new TaskException(
					String.format("Could not find the %s property of component %s", PLANCODES, componentId));

		setPlanCodes(planCodes);

		if (this.planCodes.isEmpty())
			throw new TaskException(
					String.format("Property %s of component %s does not have a value", PLANCODES, componentId));

		logger.info(String.format("Current Plan Codes: %s", getPlanCodes()));
	}

	protected String getPlanCodes() {
		return StringUtils.join(this.planCodes, ",");
	}

	protected List<String> getPlanCodesAsList() {
		return this.planCodes;
	}

	/**
	 * 
	 * @param planCode gets the Plan Codes that are in the same Plant as this Plan Code
	 * @return {@code List<String>} of Plan Codes
	 */
	protected List<String> getPlanCodesForPlantLine(String planCode) {

		if (StringUtils.isBlank(planCode)) {
			logger.warn("planCode not found in record, skipping record.");
			return null;
		}

		ArrayList<String> subset = new ArrayList<String>();
		// if the plant_code, line and product match, return true
		for (String thisPlanCode : planCodes) {
			if (thisPlanCode.substring(0, 7).equalsIgnoreCase(planCode.trim().substring(0, 7))) {
				subset.add(thisPlanCode);
			}
		}
		return subset;

	}

	protected void setPlanCodes(String planCodes) {
		this.planCodes = java.util.Arrays.asList(planCodes.split(","));
	}

	/**
	 * Gets all tails from {@link PreProductionLot} table using {@link BasePlanCodeTask#planCodes}
	 * @return {@code Map<String, PreProductionLotDTO>} where key is plan code and value is {@link PreProductionLotDTO}
	 */
	protected Map<String, PreProductionLotDTO> getTailsByPlanCode() {
		return getTailsByPlanCode(this.planCodes.toArray(new String[this.planCodes.size()]));
	}

	/**
	 * Gets all tails from {@link PreProductionLot} table
	 * @param planCodes to get tails for
	 * @return {@code Map<String, PreProductionLotDTO>} where key is plan code and value is {@link PreProductionLotDTO}
	 */
	protected Map<String, PreProductionLotDTO> getTailsByPlanCode(String[] planCodes) {
		Map<String, PreProductionLotDTO> tailsByPlanCode = new HashMap<String, PreProductionLotDTO>();

		for (String planCode : planCodes) {
			List<PreProductionLot> tails = getDao(PreProductionLotDao.class).getTailsByPlanCode(planCode);

			// There should be only one "tail" i.e. a record in PreProduction Plan table
			// with next_production_plan set to null for plantCode, lineNo and
			// processLocation
			switch (tails.size()) {
			case 0:
				StringBuffer sb = new StringBuffer("Unable to find the last preproduction lot record with ")
						.append(" PlanCode = ").append(planCode);

				logger.emergency(sb.toString());
				errorsCollector.emergency(sb.toString());

				return null;
			case 1: // OK
				logger.info("Tail is OK for plan code: " + planCode);
				break;
			default:
				sb = new StringBuffer("Found more than one production lot with null in the next_production_lot with ")
						.append(" PlanCode = ").append(planCode);

				logger.emergency(sb.toString());
				errorsCollector.emergency(sb.toString());

				return null;
			};

			logger.info(String.format("Tail for %s is %s", planCode, tails.get(0).getProductionLot()));
			tailsByPlanCode.put(planCode, new PreProductionLotDTO(tails.get(0)));
		}

		return tailsByPlanCode;
	}

	/**
	 * Gets files from MQ Queue for {@link BasePlanCodeTask#InterfaceId}
	 * @return {@code int} count of files received
	 */
	protected int getFilesFromMQ() {
		int filesCount = 0;

		refreshProperties();

		
		// Get list of objects created from received file
		receivedFileList = getFilesFromMQ(InterfaceId, getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));

		if (receivedFileList != null){
			filesCount = receivedFileList.length;
		}

		return filesCount;
	}
	/**
	 * Checks if plan code is in this object's plan code list
	 * @param currentPlan {@link IPlanCodeDTO} 
	 * @return {@code boolean}
	 */
	protected boolean validatePlanCode(IPlanCodeDTO currentPlan) {
		if (currentPlan == null)
			return false;

		String planCode = currentPlan.getPlanCode();

		return validatePlanCode(planCode);
	}

	/**
	 * Checks if {@link BasePlanCodeTask#planCodes} contains {@code String} planCode
	 * @param planCode {@code String}
	 * @return {@code boolean}
	 */
	protected boolean validatePlanCode(String planCode) {
		if (StringUtils.isBlank(planCode)) {
			logger.warn("planCode not found in record, skipping record.");
			return false;
		}

		if (!this.planCodes.contains(planCode.trim())) {
			logger.debug(String.format("Not in current plan codes: %s; skipping record.", getPlanCodes()));

			return false;
		}

		return true;
	}

	/**
	 * checks if any of the configured plan codes match the plan/line/product
	 * @param planCode {@code String}
	 * @return {@code boolean} 
	 */
	protected boolean isSameLine(String planCode) {
		List<String> subset = getPlanCodesForPlantLine(planCode);
		if (subset != null && !subset.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Compares input line to {@link OifTask#siteLineId}
	 * @param lineNo {@code String}
	 * @return {@code boolean}
	 */
	protected boolean validateLine(String lineNo) {
		if (lineNo == null)
			return false;
		else if (!lineNo.trim().equalsIgnoreCase(this.siteLineId)) {
			logger.debug("Not current line: " + lineNo + ", skipping record");
			return false;
		}
		return true;
	}

	/**
	 * Compares input site to {@link OifTask#siteName}
	 * @param site {@code String}
	 * @return {@code boolean}
	 */
	protected boolean validateSite(String site) {
		if (site == null)
			return false;
		else if (!site.trim().equalsIgnoreCase(this.siteName)) {
			logger.debug("Not current site: " + site + ", skipping record");
			return false;
		}
		return true;
	}

	/**
	 * Gets each line from the input FileName
	 * @param FileName {@code String}
	 * @return {@code List<String>} where each item in the list is a line from the file read
	 */
	protected List<String> LoadRecordsFromFile(String FileName) {
		List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + FileName, logger);

		if (receivedRecords.isEmpty()) {
			logger.warn("No records in received file: " + FileName);
			setIncomingJobStatus(OifRunStatus.NO_RECORDS_IN_RECEIVED_FILE);
			return null;
		}
		totalRecords = receivedRecords.size();

		return receivedRecords;
	}
	
	/**
	 * Takes a file name and returns a {@code Map<String, ArrayList<T>>} where the Key is the plan code
	 * @param FileName the name of the file to read from.
	 * @return {@code Map<String, ArrayList<T>>} where T extends {@link IPlanCodeDTO}
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, ArrayList<T>> getRecordsByPlanCode(String FileName)
			throws IllegalAccessException, InstantiationException {
		Class<T> planCodeDto = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		Map<String, ArrayList<T>> planCodeDTOMap = new HashMap<String, ArrayList<T>>();
	
		// Iterate over each line of the input file
		List<String> receivedRecords = LoadRecordsFromFile(FileName);
		if(receivedRecords != null && !receivedRecords.isEmpty()) {
			for(String receivedRecord : receivedRecords) {
				IPlanCodeDTO plan = planCodeDto.newInstance();
				simpleParseHelper.parseData(plan, receivedRecord);
				
				if(!validatePlanCode(plan)) // If plan code is not in this object's list of plan codes
					continue; // Then skip this iteration
				
				if(!planCodeDTOMap.containsKey(plan.getPlanCode().trim()))
					planCodeDTOMap.put(plan.getPlanCode().trim(), new ArrayList<T>());
				
				planCodeDTOMap.get(plan.getPlanCode().trim()).add((T) plan);
			}
			if(planCodeDTOMap.isEmpty()) {
				String message = String.format("Records matching current plan code(s): %s not found in file: %s",  getPlanCodes(), FileName);
				logger.debug(message);
				setIncomingJobStatus(OifRunStatus.RECORDS_MISSING_FOR_CONFIGURED_PLAN_CODE);
			}
		}      
	
		return planCodeDTOMap;
	}

}