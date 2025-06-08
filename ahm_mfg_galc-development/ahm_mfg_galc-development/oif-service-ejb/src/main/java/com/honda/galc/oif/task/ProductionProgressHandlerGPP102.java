package com.honda.galc.oif.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFConstants.DEPARTMENT_CODE;

/**
 * 
 * <h3>ProductionProgressHandlerGPP102</h3>
 * <p> ProductionProgressHandlerGPP102 is for GPP102 </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Ratul Chakravarty<br>
 * April 03, 2014
 *
 */


public class ProductionProgressHandlerGPP102 extends OifTask<Object> implements IEventTaskExecutable {

	private File gpp102AEFile;
	private File updatedRecsGPP102AELogFile;
	private File gpp102AFFile;
	private File updatedRecsGPP102AFLogFile;
	private File gpp102WEFile;
	private File updatedRecsGPP102WELogFile;
	private File gpp102PAFile;
	private File updatedRecsGPP102PALogFile;
	private File gpp102IAFile;
	private File updatedRecsGPP102IALogFile;
	private static final String RUN_GPP102 = "GPP102";
	private static String serviceId = null;
	private ProductionProgressCommon ppc;
	private boolean isDebug = false;
	private String createTimestamp;
	// Track the number of records being written to the interface file
	private int totalRecordCount = 0, totalAERecordCount = 0, totalAFRecordCount = 0,
				totalWERecordCount = 0, totalPARecordCount = 0, totalIARecordCount = 0;
	public ProductionProgressHandlerGPP102(String componentId) throws IOException {
		super(componentId);
		serviceId = componentId;
	}

	protected void initialize() {
		logger.info("initializing ProductionProgressHandlerGPP102.");
		isDebug = getPropertyBoolean("DEBUG_PRODUCTION_PROGRESS", true);
		
		if(isDebug) {
			logger.info("GPP102AE_ID is set to " + getProperty("GPP102AE_ID"));
			logger.info("GPP102AF_ID is set to " + getProperty("GPP102AF_ID"));
			logger.info("GPP102WE_ID is set to " + getProperty("GPP102WE_ID"));
			logger.info("GPP102PA_ID is set to " + getProperty("GPP102PA_ID"));
			logger.info("GPP102IA_ID is set to " + getProperty("GPP102IA_ID"));
			logger.info("Updated records log file name for GPP102AE is :- " + getProperty("UPDATED_RECS_GPP102AE_FILE_NAME"));
			logger.info("Updated records log file name for GPP102AF is :- " + getProperty("UPDATED_RECS_GPP102AF_FILE_NAME"));
			logger.info("Updated records log file name for GPP102WE is :- " + getProperty("UPDATED_RECS_GPP102WE_FILE_NAME"));
			logger.info("Updated records log file name for GPP102PA is :- " + getProperty("UPDATED_RECS_GPP102PA_FILE_NAME"));
			logger.info("Updated records log file name for GPP102IA is :- " + getProperty("UPDATED_RECS_GPP102IA_FILE_NAME"));
			logger.info("ALLOW_DB_UPDATE is :- " + getProperty("ALLOW_DB_UPDATE"));
		}
		errorsCollector = new OifErrorsCollector(serviceId);
		ppc = new ProductionProgressCommon();
		createTimestamp = ppc.getCurrentTimestamp();
	}
	
	public void execute(Object[] args) {
		initialize();
		exportRecord();
	}

	private void exportRecord() {
		/***
		 * Set up booleans to determine if the output file needs to be sent to
		 * GPCS. It is mainly used for Saturday production. When this method
		 * gets executed on Saturday the sendFileToMQ will be called if it is a
		 * Saturday production. Otherwise, it will skip sending file to MQ
		 ***/
		
		/**
		 * Set up interface IDs
		 * */
		String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		String logFilePath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, "LOGGING");
		String departmentString = getProperty("DEPARTMENTS");
		List<String> deptList = null;
		if(departmentString != null && !"".equals(departmentString.trim()))  {
			String[] departments = departmentString.trim().split("\\s*,\\s*");
			deptList = Arrays.asList(departments);
		}
		boolean isAE = true;
		boolean isAF = true;
		boolean isPA = true;
		boolean isWE = true;
		boolean isIA = true;
		
		if(!deptList.contains(DEPARTMENT_CODE.AE.name()))  isAE = false;
		if(!deptList.contains(DEPARTMENT_CODE.AF.name()))  isAF = false;
		if(!deptList.contains(DEPARTMENT_CODE.PA.name()))  isPA = false;
		if(!deptList.contains(DEPARTMENT_CODE.WE.name()))  isWE = false;
		if(!deptList.contains(DEPARTMENT_CODE.IA.name()))  isIA = false;
		
		
		String gpp102AEFileName = getProperty("GPP102AE_ID") + this.createTimestamp + ".oif";
		String updatedRecsGPP102AELogFileName = getProperty("UPDATED_RECS_GPP102AE_FILE_NAME") + "_" + this.createTimestamp + ".log";
		String gpp102AFFileName = getProperty("GPP102AF_ID") + this.createTimestamp + ".oif";
		String updatedRecsGPP102AFLogFileName = getProperty("UPDATED_RECS_GPP102AF_FILE_NAME") + "_" + this.createTimestamp + ".log";
		String gpp102WEFileName = getProperty("GPP102WE_ID") + this.createTimestamp + ".oif";
		String updatedRecsGPP102WELogFileName = getProperty("UPDATED_RECS_GPP102WE_FILE_NAME") + "_" + this.createTimestamp + ".log";
		String gpp102PAFileName = getProperty("GPP102PA_ID") + this.createTimestamp + ".oif";
		String updatedRecsGPP102PALogFileName = getProperty("UPDATED_RECS_GPP102PA_FILE_NAME") + "_" + this.createTimestamp + ".log";
		String gpp102IAFileName = getProperty("GPP102IA_ID") + this.createTimestamp + ".oif";
		String updatedRecsGPP102IALogFileName = getProperty("UPDATED_RECS_GPP102IA_FILE_NAME") + "_" + this.createTimestamp + ".log";
		
		String fileInExecutionName = null;
		boolean allowDBUpdate = getPropertyBoolean("ALLOW_DB_UPDATE", true);

		try {
			
			// Create the output file objects
			if(isAE)  {
				gpp102AEFile = new File(resultPath + gpp102AEFileName);
				updatedRecsGPP102AELogFile = new File(logFilePath + updatedRecsGPP102AELogFileName);
				if (isDebug) {
					logger.info("After creating file " + gpp102AEFile.getAbsolutePath() + ". File exists " + gpp102AEFile.exists());
				}
			}
			if(isAF)  {
				gpp102AFFile = new File(resultPath + gpp102AFFileName);
				updatedRecsGPP102AFLogFile = new File(logFilePath + updatedRecsGPP102AFLogFileName);
				if (isDebug) {
					logger.info("After creating file " + gpp102AFFile.getAbsolutePath() + ". File exists " + gpp102AFFile.exists());
				}
			}
			if(isWE)  {
				gpp102WEFile = new File(resultPath + gpp102WEFileName);
				updatedRecsGPP102WELogFile = new File(logFilePath + updatedRecsGPP102WELogFileName);
				if (isDebug) {
					logger.info("After creating file " + gpp102WEFile.getAbsolutePath() + ". File exists " + gpp102WEFile.exists());
				}
			}
			if(isPA)  {
				gpp102PAFile = new File(resultPath + gpp102PAFileName);
				updatedRecsGPP102PALogFile = new File(logFilePath + updatedRecsGPP102PALogFileName);
				if (isDebug) {
					logger.info("After creating file " + gpp102PAFile.getAbsolutePath() + ". File exists " + gpp102PAFile.exists());
				}
			}
			if(isIA)  {
				gpp102IAFile = new File(resultPath + gpp102IAFileName);
				updatedRecsGPP102IALogFile = new File(logFilePath + updatedRecsGPP102IALogFileName);
				if (isDebug) {
					logger.info("After creating file " + gpp102IAFile.getAbsolutePath() + ". File exists " + gpp102IAFile.exists());
				}
			}
			
			
			// call the web services on the active lines and get bearing usage data from each line
			String[] activeLinesURLsArr = getProperty("ACTIVE_LINES_URLS").split(",");
			String[] activeLinesArr = getProperty("ACTIVE_LINES").split(",");
			for (int i = 0; i < activeLinesArr.length; i++) {
				if (isAE) {
					// AE records 
					int noOfAERecs = ppc.exportProductionProgress(gpp102AEFile, activeLinesURLsArr[i],
							this.componentId, RUN_GPP102, DEPARTMENT_CODE.AE, errorsCollector,
							updatedRecsGPP102AELogFile, allowDBUpdate);
					totalAERecordCount += noOfAERecs;
					totalRecordCount = totalRecordCount + totalAERecordCount;
					if (isDebug)
						logger.info("Total record count = " + totalRecordCount + " vTotalAERecordCount "
								+ totalAERecordCount);
				}
				if (isPA) {
					// PA records
					int noOfPARecs = ppc.exportProductionProgress(gpp102PAFile, activeLinesURLsArr[i],
							this.componentId, RUN_GPP102, DEPARTMENT_CODE.PA, errorsCollector,
							updatedRecsGPP102PALogFile, allowDBUpdate);
					totalPARecordCount += noOfPARecs;
					totalRecordCount = totalRecordCount + totalPARecordCount;
					if (isDebug)
						logger.info("Total record count = " + totalRecordCount + " vTotalPARecordCount "
								+ totalPARecordCount);
				}
				if (isAF) {
					// AF records 
					int noOfAFRecs = ppc.exportProductionProgress(gpp102AFFile, activeLinesURLsArr[i],
							this.componentId, RUN_GPP102, DEPARTMENT_CODE.AF, errorsCollector,
							updatedRecsGPP102AFLogFile, allowDBUpdate);
					totalAFRecordCount += noOfAFRecs;
					totalRecordCount = totalRecordCount + totalAFRecordCount;
					if (isDebug)
						logger.info("Total record count = " + totalRecordCount + " vTotalAFRecordCount "
								+ totalAFRecordCount);
				}
				if (isWE) {
					// WE records
					int noOfWERecs = ppc.exportProductionProgress(gpp102WEFile, activeLinesURLsArr[i],
							this.componentId, RUN_GPP102, DEPARTMENT_CODE.WE, errorsCollector,
							updatedRecsGPP102WELogFile, allowDBUpdate);
					totalWERecordCount += noOfWERecs;
					totalRecordCount = totalRecordCount + totalWERecordCount;
					if (isDebug)
						logger.info("Total record count = " + totalRecordCount + " vTotalWERecordCount "
								+ totalAFRecordCount);
				}
				if (isIA) {
					// IA records 
					int noOfIARecs = ppc.exportProductionProgress(gpp102IAFile, activeLinesURLsArr[i],
							this.componentId, RUN_GPP102, DEPARTMENT_CODE.IA, errorsCollector,
							updatedRecsGPP102IALogFile, allowDBUpdate);
					totalIARecordCount += noOfIARecs;
					totalRecordCount = totalRecordCount + totalIARecordCount;
					if (isDebug)
						logger.info("Total record count = " + totalRecordCount + " vTotalIARecordCount "
								+ totalIARecordCount);
				}
			}
			
			
			//For each department, create a record with a message of "NO RECORD" if it does not find any production lot that has
			//remaining bodies
			if (isDebug) {
				logger.info("total count = " + totalRecordCount);
			}
			if (isAE && totalAERecordCount == 0) {
				logger.info("Creating an AE export file that contains NoRecords message for interface ID = " + serviceId + "; file name = " + gpp102AEFile.getAbsolutePath());
				BufferedWriter vGPP102AEWriter = new BufferedWriter(new FileWriter(this.gpp102AEFile));
				vGPP102AEWriter.write(MQUtility.createNoDataString(70));
				vGPP102AEWriter.newLine();
				vGPP102AEWriter.close();

			}
			if (isPA && totalPARecordCount == 0) {
				logger.info("Creating a PA export file that contains NoRecords message for interface ID = " + serviceId + "; file name = " + gpp102PAFile.getAbsolutePath());
				BufferedWriter vGPP102PAWriter = new BufferedWriter(new FileWriter(this.gpp102PAFile));
				vGPP102PAWriter.write(MQUtility.createNoDataString(70));
				vGPP102PAWriter.newLine();
				vGPP102PAWriter.close();

			}
			if (isAF && totalAFRecordCount == 0) {
				logger.info("Creating an AF export file that contains NoRecords message for interface ID = " + serviceId + "; file name = " + gpp102AFFile.getAbsolutePath());
				BufferedWriter vGPP102AFWriter = new BufferedWriter(new FileWriter(gpp102AFFile));
				vGPP102AFWriter.write(MQUtility.createNoDataString(70));
				vGPP102AFWriter.newLine();
				vGPP102AFWriter.close();
			}

			if (isWE && totalWERecordCount == 0) {

				logger.info("Creating a WE export file that contains NoRecords message for interface ID = " + serviceId + "; file name = " + gpp102WEFile.getAbsolutePath());
				BufferedWriter vGPP102WEWriter = new BufferedWriter(new FileWriter(gpp102WEFile));
				vGPP102WEWriter.write(MQUtility.createNoDataString(70));
				vGPP102WEWriter.newLine();
				vGPP102WEWriter.close();
			}
			
			if (isIA && totalIARecordCount == 0) {

				logger.info("Creating a IA export file that contains NoRecords message for interface ID = " + serviceId + "; file name = " + gpp102IAFile.getAbsolutePath());
				BufferedWriter vGPP102IAWriter = new BufferedWriter(new FileWriter(gpp102IAFile));
				vGPP102IAWriter.write(MQUtility.createNoDataString(70));
				vGPP102IAWriter.newLine();
				vGPP102IAWriter.close();
			}

			Boolean sendFileToGPCS = PropertyService.getPropertyBoolean(componentId, "SEND_FILE_TO_GPCS", true);
			String mqConfig = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG);
			if(sendFileToGPCS) {
				MQUtility mqu = new MQUtility(this);
				if(isAE)  {
					logger.info("Sending " + gpp102AEFileName + " to MQ with total AE record count = " + totalAERecordCount);
					logger.info("Calling executeMQSendAPI with send file name  = " + resultPath + gpp102AEFileName + " with Interface ID = " + getProperty("GPP102AE_ID"));
					fileInExecutionName = gpp102AEFileName;
					mqu.executeMQSendAPI(getProperty("GPP102AE_ID"), mqConfig, resultPath + gpp102AEFileName);
					setOutgoingJobStatus(totalAERecordCount, gpp102AEFileName);
				}
				
				if(isPA)  {
					logger.info("Sending " + gpp102PAFileName + " to MQ with total PA record count = " + totalPARecordCount);
					logger.info("Calling executeMQSendAPI with send file name  = " + resultPath + gpp102PAFileName + " with Interface ID = " + getProperty("GPP102PA_ID"));
					fileInExecutionName = gpp102PAFileName;
					mqu.executeMQSendAPI(getProperty("GPP102PA_ID"), mqConfig, resultPath + gpp102PAFileName);
					setOutgoingJobStatus(totalPARecordCount, gpp102PAFileName);
				}
				
				if(isAF)  {
					logger.info("Sending " + gpp102AFFileName + " to MQ with total AF record count = " + totalAFRecordCount);
					logger.info("Calling executeMQSendAPI with send file name  = " + resultPath + gpp102AFFileName + " with Interface ID = " + getProperty("GPP102AF_ID"));
					fileInExecutionName = gpp102AFFileName;
					mqu.executeMQSendAPI(getProperty("GPP102AF_ID"), mqConfig, resultPath + gpp102AFFileName);
					setOutgoingJobStatus(totalAFRecordCount, gpp102AFFileName);
				}
				
				if(isWE)  {
					logger.info("Sending " + gpp102WEFileName + " to MQ with total WE record count = " + totalWERecordCount);
					logger.info("Calling executeMQSendAPI with send file name  = " + resultPath + gpp102WEFileName + " with Interface ID = " + getProperty("GPP102WE_ID"));
					fileInExecutionName = gpp102WEFileName;
					mqu.executeMQSendAPI(getProperty("GPP102WE_ID"), mqConfig, resultPath + gpp102WEFileName);
					setOutgoingJobStatus(totalWERecordCount, gpp102WEFileName);
				}
				
				if(isIA)  {
					logger.info("Sending " + gpp102IAFileName + " to MQ with total IA record count = " + totalIARecordCount);
					logger.info("Calling executeMQSendAPI with send file name  = " + resultPath + gpp102IAFileName + " with Interface ID = " + getProperty("GPP102IA_ID"));
					fileInExecutionName = gpp102IAFileName;
					mqu.executeMQSendAPI(getProperty("GPP102IA_ID"), mqConfig, resultPath + gpp102IAFileName);
					setOutgoingJobStatus(totalIARecordCount, gpp102IAFileName);
				}
			} else {
				logger.info("Flag SEND_FILE_TO_GPCS is set to false, files were not sent for GPP102 interfaces.");
				setOutgoingJobStatus(totalRecordCount, null);
			}
				

		} catch (MQUtilityException e) {
			String errorStr = "MQUtilityException raised when sending interface file for " + resultPath + fileInExecutionName + " for the interface " + serviceId + ". Exception : " + e.getMessage();
			logger.error(errorStr);
			errorsCollector.emergency(errorStr);
			setOutgoingJobStatusAndFailedCount(totalRecordCount-errorsCollector.getRunHistory().getSuccessCount(), OifRunStatus.MQ_ERROR, null);
		
		} catch (Exception e) {
			logger.error(e, "Exception occured while executing task with interface ID : " +serviceId);
			errorsCollector.emergency(e, "Exception occured while executing task with interface ID : " + serviceId);
			setOutgoingJobStatusAndFailedCount(totalRecordCount-errorsCollector.getRunHistory().getSuccessCount(), OifRunStatus.FAILURE, null);
			
		} finally {
			errorsCollector.sendEmail();
		}
	}
}