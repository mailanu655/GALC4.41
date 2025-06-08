package com.honda.galc.oif.task;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;

public class PddaInterfaceToPartMaskTask extends OifAbstractTask implements IEventTaskExecutable {

	private static final String LAST_RUN_TIMESTAMP = "LAST_RUN_TIMESTAMP";
	private static final String PLANT_LOC_CODE = "PLANT_LOC_CODE";
	protected OifErrorsCollector errorsCollector;
	
	public PddaInterfaceToPartMaskTask(String name) {
		super(name);
		errorsCollector = new OifErrorsCollector(name);
	}

	public void execute(Object[] args) {
		try {
			getLogger().info("Started processing Part Mask Interface");
			
			//Always refresh properties to ensure the latest LAST_RUN_TIMESTAMP is returned
			refreshProperties();			

			boolean createTimestamp = false;
			String lastRunTimestamp = getProperty(LAST_RUN_TIMESTAMP, "");
			
			//Check for property if it exists, otherwise set to create
			if (StringUtils.isEmpty(lastRunTimestamp)) {
				createTimestamp = true;
				lastRunTimestamp = "2000-01-01-00.00.00.000000";
			}
			
			String plantLocCode = getProperty(PLANT_LOC_CODE, "");
			
			if (StringUtils.isEmpty(plantLocCode)) {
				getLogger().info("No Plant Code Defined");
				errorsCollector.error("No Plant Code Defined");
				return;
			}

			processPddaPartIdentificationRecords(plantLocCode, lastRunTimestamp);

			ComponentPropertyDao componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
			
			if (createTimestamp) {
				ComponentProperty compProp = new ComponentProperty();
				compProp.setId(new ComponentPropertyId(componentId, LAST_RUN_TIMESTAMP));
				compProp.setPropertyValue(lastRunTimestamp);
				compProp.setChangeUserId("OIFTask");
				componentPropertyDao.insert(compProp);				
			}
			
			componentPropertyDao.updateTimestamp(componentId, LAST_RUN_TIMESTAMP);						

			getLogger().info("Finished processing PDDA Part Mask Interface");
		} catch (Exception e) {
			getLogger().info(
					"Unexpected Exception Occurred  while running the PddaPartMaskPartMarkTask :" + e.getMessage());
			e.printStackTrace();
			errorsCollector.emergency(e,"Unexpected exception occured");
		} finally {
			//errorsCollector.error("test error notification ");
			errorsCollector.sendEmail();
		}
	
	}

	private void processPddaPartIdentificationRecords(String plantCode, String lastRunTimestamp) {

		PartSpecDao partSpecDao = ServiceFactory.getDao(PartSpecDao.class);
		PartNameDao partNameDao = ServiceFactory.getDao(PartNameDao.class);
		BomDao		bomDao = ServiceFactory.getDao(BomDao.class);

		// retrieve updated records in the VIOS.PAPID1 table from the last time
		// that the OIF ran
		List<Object[]> papiDataList = bomDao.getUpdatedPapidData(plantCode, lastRunTimestamp);

		if (papiDataList.size() == 0) {
			getLogger().info(" No new updated part records found after - " + lastRunTimestamp);
		}

		for (Object[] papiData : papiDataList) {

			try {
				String partNumber = (String) papiData[0];
				String plantLocCode = (String) papiData[1];
				BigDecimal modelYearDate = (BigDecimal) papiData[2];
				String vehicleModelCode = (String) papiData[3];
				String partMark = (String) papiData[4];
				String partDesc = (String) papiData[5];
				String stationAddress = (String) papiData[6];
				String partCodeLocator = (String) papiData[7];
				Integer maskPosition = (Integer) papiData[8];
				Integer maskLength = (Integer) papiData[9];
				String partMask = (String) papiData[10];
				String displayType = (String) papiData[11];
				Integer structureLength = (Integer) papiData[12];
				Integer partIdentificationId = (Integer) papiData[13];
				String partName = "";
				// retrieve partName from StationAddress
				if (stationAddress.contains("&")) {
					partName = (stationAddress.split("&")[0]).toUpperCase();
				} else {
					partName = stationAddress.toUpperCase();
				}
	
				boolean partExistsInPartNameTable = false;
	
				// part name exists in PartNameTable(GAL261TBX)
				if (partName.trim().length() > 0 && partNameDao.findByKey(partName) != null) {
					partExistsInPartNameTable = true;
				} else {
					getLogger().info(partName + "- part name does not exists");
					continue;
				}
				
				//Check for a part number sent from PDDA
				if (partNumber.trim().length() == 0) {
					getLogger().info("No Part Number");
					continue;
				}
	
				boolean prototypePart = false;
				String modelYearCode = getModelYearCode(modelYearDate);
				getLogger().info(partNumber +" partNumber");
			
				boolean partNamePartNumberExistsForModelYear = false;
	
				if (partExistsInPartNameTable && !prototypePart) {
	
					List<PartSpec> partSpecs = partSpecDao.findAllByPartName(partName);
					List<String> diffModelYearPartIdList = new ArrayList<String>();
					if (partSpecs != null && !partSpecs.isEmpty()) {
	
						for (PartSpec partSpec : partSpecs) {
	
							/*
							 * PartName and PartNumber combination exists in
							 * PartSpec Table update partMark, partMask, displayType
							 * and Image
							 */
							if (partSpec.getPartNumber() != null && (partSpec.getPartNumber().equalsIgnoreCase(partNumber) || partSpec.getPartNumber().substring(0, 11)
									.equalsIgnoreCase(partNumber.substring(0, 11)))) {
								getLogger().info("PartName- " + partName + " and PartNumber- " + partNumber
										+ " Combination exists ");
	
								// modelYearExist in partId
								String partId = partSpec.getId().getPartId();
								if (partId.substring(0, 1).equalsIgnoreCase(modelYearCode)
										&& partId.substring(1, 2).equalsIgnoreCase("~")) {
									getLogger().info("PartId (ModelYear)- " + partId + " and ModelYearCode - " + modelYearCode
											+ " Match, Updating part spec");
									partNamePartNumberExistsForModelYear = true;
									// update the PART_MARK,
									// PART_SERIAL_NUMBER_MASK, DISPLAY_TYPE, and
									// IMAGE columns
									partSpec.setPartDescription(partDesc);
									partSpec.setPartMark(partMark);
									partSpec.setDisplayType(displayType);
									partSpec.setPartSerialNumberMask(
											getPartSerialNumberMask(maskPosition, partMask, maskLength, structureLength));
									partSpec.setPartNumber(partNumber);
	
									partSpecDao.update(partSpec);
									continue;
								} else {
									getLogger().info("PartId (ModelYear)- " + partId + " and ModelYearCode - " + modelYearCode
											+ " do not Match, Adding to diffModelYearPartIdList");
									diffModelYearPartIdList.add(partId);
								}
							}
						}
					} else {
						getLogger().info(" No Part Spec records found for partName - " + partName);
					}
					if (!partNamePartNumberExistsForModelYear) {
						getLogger().info("PartName-" + partName + " and PartNumber-" + partNumber
								+ " Combination does not exist, Creating new part spec");
						/*
						 * PartName and PartNumber combination does not exist,
						 * create new part and part spec
						 */
						String partId = getNextPartId(partSpecs, modelYearCode);
						getLogger().info("Next Part Id - "+partId +" for modelYear - "+modelYearCode );
						PartSpecId partSpecId = new PartSpecId();
						partSpecId.setPartId(partId);
						partSpecId.setPartName(partName);
	
						PartSpec newPartSpec = new PartSpec();
						newPartSpec.setId(partSpecId);
						newPartSpec.setPartNumber(partNumber);
						newPartSpec.setPartMaxAttempts(0);
						newPartSpec.setMeasurementCount(0);
						newPartSpec.setEntryTimestamp(new Timestamp(System.currentTimeMillis()));
						newPartSpec.setPartDescription(partDesc);
						newPartSpec.setPartMark(partMark);
						newPartSpec.setDisplayType(displayType);
						newPartSpec.setPartSerialNumberMask(
								getPartSerialNumberMask(maskPosition, partMask, maskLength, structureLength));
						partSpecDao.save(newPartSpec);
						
						//Copy measurements from previous model year, if it exists
						if (diffModelYearPartIdList.size() > 0) {
							String priorYearPartId = getPreviousModelYearPartId(getPreviousModelYearCode(modelYearCode),
									diffModelYearPartIdList);
							getLogger().info("Prior Year Part Id - "+ priorYearPartId +" for modelYear - "+modelYearCode );
							if(priorYearPartId != null){
								saveMeasurementSpecs(partName,partId, priorYearPartId);
							}
						}
						
					}
	
				}
			} catch (Exception e) {
				getLogger().info(
						"Unexpected Exception Occurred  while running the PddaPartMaskPartMarkTask :" + e.getMessage());
				e.printStackTrace();
				errorsCollector.emergency(e,"Unexpected exception occured");
			}
		}
	}

	private void saveMeasurementSpecs(String partName, String partId, String priorYearPartId) {
		MeasurementSpecDao measSpecDao = ServiceFactory.getDao(MeasurementSpecDao.class);
		getLogger().info("Retrieving Measurement Specs for PartName: "+ partName +" and PartId: "+partId);
		List<MeasurementSpec> priorYearMeasSpecs = measSpecDao.findAllByPartNamePartId(partName, priorYearPartId);
		for(MeasurementSpec spec: priorYearMeasSpecs){
			MeasurementSpec newMeaSpec = new MeasurementSpec();
			MeasurementSpecId newMeaSpecId = new MeasurementSpecId();
			newMeaSpecId.setPartId(partId);
			newMeaSpecId.setPartName(partName);
			newMeaSpecId.setMeasurementSeqNum(spec.getId().getMeasurementSeqNum());
			newMeaSpec.setId(newMeaSpecId);
			newMeaSpec.setMinimumLimit(spec.getMinimumLimit());
			newMeaSpec.setMaximumLimit(spec.getMaximumLimit());
			newMeaSpec.setMaxAttempts(spec.getMaxAttempts());			
			
			measSpecDao.save(newMeaSpec);
		}
	}

	private Logger getLogger() {
		return Logger.getLogger(componentId);
	}

	/*
	 * If MASK_POSITION is > 1 then need to add % for each character prior to
	 * starting position. Once at starting position insert MASK_CODE for the
	 * part mask that is defined. If MASK_POSITION + MASK_LENGTH <
	 * STRUCTURE_LENGTH then need to add % for each character at the end.
	 */
	private String getPartSerialNumberMask(Integer maskPosition, String maskCode, Integer maskLength,
			Integer structureLength) {
		String partMask = "";
		//if maskCode is null set partMask as  * 
		if(maskCode == null){
			partMask = "*";
			return partMask;
		}
		if (maskPosition > 0) {
			for (int i = 1; i < maskPosition; i++) {
				partMask = partMask + "%";
			}
		}
		partMask = partMask + maskCode;
		getLogger().info("PartMask -" + partMask);

		int diffInLength = 0;
		int mLength = maskPosition + maskLength;

		if (mLength < structureLength) {
			diffInLength = structureLength - mLength;
			for (int j = 0; j < diffInLength; j++) {
				partMask = partMask + "%";
			}
		}
		getLogger().info(" MaskPosition - " + maskPosition + ", MaskLength - " + maskLength + ", StructureLength - "
				+ structureLength + ", MaskCode - " + maskCode + ", PartSerialNumberMask - " + partMask);
		return partMask;
	}

	private String getNextPartId(List<PartSpec> partSpecs, String modelYearCode) {
		String partId = null;

		for (PartSpec partSpec : partSpecs) {
			if (partId == null)
				partId = partSpec.getId().getPartId().substring(0, 1).equalsIgnoreCase(modelYearCode)?partSpec.getId().getPartId():null;
			else if (partSpec.getId().getPartId().substring(0, 1).equals(modelYearCode) && partSpec.getId().getPartId().compareTo(partId) > 0)
				partId = partSpec.getId().getPartId();
		}

		int num = 0;
		if (partId != null)
			num = Integer.parseInt(partId.substring(2, 5)) + 1;

		return modelYearCode + "~" + new DecimalFormat("000").format(num);
	}

	private String getModelYearCode(BigDecimal modelYearDate) {
		String modelYear = modelYearDate.toPlainString();
		String modelYearCode = "";
		Map<String, String> modelCodeYearMap = ServiceFactory.getDao(FrameSpecDao.class).findAllModelCodeYear();

		for (String modelCodeYearDesc : modelCodeYearMap.keySet()) {
			String[] tempYearDesc = modelCodeYearDesc.split("-");
			if (tempYearDesc[1].equalsIgnoreCase(modelYear.substring(0, 4))) {
				modelYearCode = modelCodeYearMap.get(modelCodeYearDesc);
				break;
			}
		}
		return modelYearCode;
	}

	private String getPreviousModelYearCode(String modelYear) {
		String previousModelYearCode = "";
		String modelYearCode = "";
		int previousYear = 0;
		Map<String, String> modelCodeYearMap = ServiceFactory.getDao(FrameSpecDao.class).findAllModelCodeYear();

		for (String modelCodeYearDesc : modelCodeYearMap.keySet()) {

			modelYearCode = modelCodeYearMap.get(modelCodeYearDesc);
			if (modelYearCode.equalsIgnoreCase(modelYear)) {
				String[] tempYearDesc = modelCodeYearDesc.split("-");
				String modelYearDesc = tempYearDesc[1];
				int year = Integer.parseInt(modelYearDesc);
				previousYear = year - 1;
				break;
			}
		}
		for (String modelCodeYearDesc : modelCodeYearMap.keySet()) {
			String[] tempYearDesc = modelCodeYearDesc.split("-");
			if (tempYearDesc[1].equalsIgnoreCase(String.valueOf(previousYear))) {
				previousModelYearCode = modelCodeYearMap.get(modelCodeYearDesc);
				break;
			}
		}

		return previousModelYearCode;
	}

	private String getPreviousModelYearPartId(String previousModelYearCode, List<String> partIdList) {
		for (String partId : partIdList) {
			if (partId.substring(0, 1).equalsIgnoreCase(previousModelYearCode)) {
				return partId;
			}
		}
		return null;
	}

	
}