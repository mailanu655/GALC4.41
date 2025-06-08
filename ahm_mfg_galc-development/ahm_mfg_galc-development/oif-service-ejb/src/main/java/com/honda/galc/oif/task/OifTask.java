package com.honda.galc.oif.task;

import static com.honda.galc.common.logging.Logger.getLogger;
import static com.honda.galc.service.ServiceFactory.getService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.oif.RunHistory;
import com.honda.galc.oif.dto.IOutputFormat;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IOifRunHistory;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;
import com.honda.galc.util.OIFFileUtilityException;
import com.honda.galc.util.RegularMQClient;
import com.ibm.mq.MQException;

/**
 * 
 * <h3>OifAbstractTask Class description</h3>
 * <p> OifAbstractTask description </p>
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
 * <TR>
 * <TD>YX</TD>
 * <TD>2014.8.25</TD>
 * <TD>0.1</TD>
 * <TD>TASK0013687</TD>
 * <TD>add mehtod to format and save data in a file and send the file to mq if needed</TD>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 31, 2012
 *
 *
 */
/**
 * * *
 * 
 * @version 0.2
 * @author Larry Karpov
 * @since Nov 14, 2013
 */
public class OifTask<T> extends OifAbstractTask implements IOifRunHistory {

	protected static String ASSEMBLY_LINE_ID = "ASSEMBLY_LINE_ID";

	public static final Integer CHARACTER_SET = 819;
	public static final Integer ENCODING_ASCII = 279;
	public static final String JAPAN_VIN_LEFT_JUSTIFIED = "JAPAN_VIN_LEFT_JUSTIFIED";
	public static final String LAST_PROCESS_TIMESTAMP = ApplicationConstants.LAST_PROCESS_TIMESTAMP;

	public static final String TEMP_FILE_LIST = "TEMP_FILE_LIST";

	public OifTask(String name) {
		super(name);
		this.propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		this.cStatusDao = ServiceFactory.getDao(ComponentStatusDao.class);
		errorsCollector = new OifErrorsCollector(name);
	}

	protected String siteLineId;
	protected String siteName;
	protected OifErrorsCollector errorsCollector;
	protected ComponentPropertyDao propertyDao;
	protected ComponentStatusDao cStatusDao;
	protected OIFSimpleParsingHelper<T> simpleParseHelper;
	protected RunHistory runHistory;

	// The list of file names that are received from GPCS(MQ).
	protected String[] receivedFileList;

	/**
	 * Reads interface files from MQ and checks record length
	 * <p>
	 * 
	 * @return a list of file names downloaded from MQ
	 * @param interface id
	 * @param integer   for the record length
	 */
	protected String[] getFilesFromMQ(String pInterfaceID, int pRecordLength) {
		logger.info("Checking the receiving queue to see if the import file has arrived");
		logger.info(String.format("Getting files from queue for interface: %s", pInterfaceID));
		String[] rcvdFileList = null;
		// set source of the file
		if (errorsCollector.getRunHistory() != null) {
			errorsCollector.getRunHistory().setInterfaceId(pInterfaceID);
		}
		try {
			int[] vRecordLength = new int[1];
			vRecordLength[0] = pRecordLength;
			// Read the interface files from MQ.
			MQUtility mqutil = new MQUtility(this);
			String mqConfig = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG);
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
			if (mqConfig == null || resultPath == null) {
				logger.emergency("Could not find properties for MQ config or/and output path.");
				setIncomingJobStatus(OifRunStatus.MISSING_CONFIGURATION);
				throw new MQUtilityException("");
			}
			rcvdFileList = mqutil.getReceiveFile(pInterfaceID, mqConfig, resultPath);
			
			if (rcvdFileList == null) {
				logger.info("Received File List is null. Setting to empty array.");
				rcvdFileList = new String[0];
			}

			int startingOffset = PropertyService.getPropertyInt(OIFConstants.OIF_SYSTEM_PROPERTIES,
					OIFConstants.STARTING_OFFSET);

			// Checks record length
			for (int counter = 0; counter < rcvdFileList.length; counter++) {
				try {
					OIFFileUtility.isValidFileFormat(resultPath + rcvdFileList[counter], vRecordLength, startingOffset,
							logger);
				} catch (OIFFileUtilityException e) {
					logger.error(e,
							"IFFileValidationException raised when validating record length in getFilesFromMQ. "
									+ "File Name = " + rcvdFileList[counter] + "; Column Length = "
									+ vRecordLength[counter] + "; aStartingOffset " + startingOffset);
					setIncomingJobStatus(OifRunStatus.INVALID_FILE_FORMAT);
				}
			}

		} catch (MQUtilityException e) {
			logger.error(e, "MQUtilityException raised when executing getFilesFromMQ.");
			setIncomingJobStatus(OifRunStatus.MQ_ERROR);

		} catch (Exception e) {
			logger.error(e, "Exception raised when executing getFilesFromMQ.");
			setIncomingJobStatus(OifRunStatus.MQ_ERROR);

		}

		if (rcvdFileList == null || rcvdFileList.length <= 0) {
			logger.warn("No file in the incoming MQ queue for interface = " + pInterfaceID);
			setIncomingJobStatus(OifRunStatus.NO_FILE_RECEIVED);
			return null;
		}
		else {
			logger.info(String.format("%d file(s) received for interface %s", rcvdFileList.length, pInterfaceID));
		}

		return rcvdFileList;
	}

	/**
	 * <ul>
	 * <li>Calls {@link OifTask#initialize}</li>
	 * <li>Initializes {@code OIFSimpleParsingHelper<T>}{@link OifTask#simpleParseHelper} with an {@link com.honda.galc.oif.dto.IPlanCodeDTO} implementation</li>
	 * </ul>
	 * @param classType {@link com.honda.galc.oif.dto.IPlanCodeDTO} implementation Class Type
	 */
	protected void initData(Class<T> classType) {
		initialize();
		String strPriorityPlan = getProperty("PARSE_LINE_DEFS");
		simpleParseHelper = new OIFSimpleParsingHelper<T>(classType, strPriorityPlan, logger);
		simpleParseHelper.getParsingInfo();
	}

	/**
	 * Initialize the following attributes
	 * <ul>
	 * <li>{@code String} {@link OifTask#siteLineId}</li>
	 * <li>{@code String} {@link OifTask#siteName}</li>
	 * <li>{@code ComponentPropertyDao} {@link OifTask#propertyDao}</li>
	 * <li>{@code ComponentStatusDao}{@link OifTask#cStatusDao}</li>
	 * </ul>
	 */
	protected void initialize() {
		// set the current assembly line Id
		siteLineId = getProperty(ASSEMBLY_LINE_ID, PropertyService.getAssemblyLineId());
		this.propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		this.cStatusDao = ServiceFactory.getDao(ComponentStatusDao.class);
		if (StringUtils.isEmpty(siteLineId)) {
			setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			throw new TaskException("Could not find the ASSEMBLY_LINE_ID property");
		}
		logger.info("Current assembly line Id: " + siteLineId);
		siteName = PropertyService.getSiteName();
		if (StringUtils.isEmpty(siteName)) {
			setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			throw new TaskException("Could not find the SITE_NAME property");
		}
		logger.info("Current site name: " + siteName);
	}

	/**
	 * TASK0013687 format data by the {@link IOutputFormat} definition and send to mq if
	 * needed
	 * @param <K> where K extends {@link IOutputFormat}
	 * @param outputClass the {@link IOutputFormat} definition
	 * @param outputData
	 */
	protected <K extends IOutputFormat> void exportDataByOutputFormatHelper(Class<K> outputClass, List<K> outputData) {

		String exportPath = getProperty(OIFConstants.EXPORT_FILE_PATH);
		String currentTimeStamp = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES,
				OIFConstants.TIMESTAMP_FORMAT) != null ? OIFConstants.stsf1.format(new Date())
						: new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String exportFileName = getProperty(OIFConstants.EXPORT_FILE_NAME) + currentTimeStamp + ".oif";
		String mqConfig = getProperty(OIFConstants.MQ_CONFIG);
		String opFormatDefKey = this.componentId;
		exportDataByOutputFormatHelper(outputClass, outputData, exportPath, exportFileName, mqConfig, opFormatDefKey);

	}

	/**
	 * For files with header or footer
	 * @param <K> where K extends {@link IOutputFormat}
	 * @param header {@code String}
	 * @param outputClass {@code Class<K>} where K extends {@link IOutputFormat}
	 * @param outputData {@code List<K>} where K extends {@link IOutputFormat}
	 * @param footer {@code String}
	 */
	protected <K extends IOutputFormat> void exportDataByOutputFormatHelper(String header, Class<K> outputClass,
			List<K> outputData, String footer) {

		String exportPath = getProperty(OIFConstants.EXPORT_FILE_PATH);
		String currentTimeStamp = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES,
				OIFConstants.TIMESTAMP_FORMAT) != null ? OIFConstants.stsf1.format(new Date())
						: new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String exportFileName = getProperty(OIFConstants.EXPORT_FILE_NAME) + currentTimeStamp + ".oif";
		String mqConfig = getProperty(OIFConstants.MQ_CONFIG);
		String opFormatDefKey = this.componentId;
		exportDataByOutputFormatHelper(header, outputClass, outputData, exportPath, exportFileName, mqConfig,
				opFormatDefKey, footer);
	}

	/**
	 * TASK0013687 format data by the {@link IOutputFormat} definition and send to mq if
	 * needed
	 * @param <K> where K extends {@link IOutputFormat}
	 * @param outputClass the {@link IOutputFormat} definition
	 * @param outputData {@code List<K>} where K extends {@link IOutputFormat}
	 */
	protected <K extends IOutputFormat> void exportDataByOutputFormatHelper(Class<K> outputClass, List<K> outputData,
			String exportPath, String exportFileName, String mqConfig, String opFormatDefKey) {
		File target = new File(exportPath + File.separator + exportFileName);
		int length = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);

		if (StringUtils.isBlank(exportPath) || StringUtils.isBlank(exportFileName)) {
			logger.error("Export file path or name is missing!");
			setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			return;
		}

		// format data
		List<String> recordStrList = formatData(outputData, outputClass, length, opFormatDefKey);

		// write the records to the file
		writeRecordsToFile(target, recordStrList, null, null);

		// send file to MQ
		sendFileToMQ(target, mqConfig, recordStrList.size());
	}

	/**
	 * 
	 * @param <K> where K extends {@link IOutputFormat}
	 * @param header
	 * @param outputClass
	 * @param outputData
	 * @param exportPath
	 * @param exportFileName
	 * @param mqConfig
	 * @param opFormatDefKey
	 * @param footer
	 */
	protected <K extends IOutputFormat> void exportDataByOutputFormatHelper(String header, Class<K> outputClass,
			List<K> outputData, String exportPath, String exportFileName, String mqConfig, String opFormatDefKey,
			String footer) {
		File target = new File(exportPath + File.separator + exportFileName);
		int length = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);
		if (StringUtils.isBlank(exportPath) || StringUtils.isBlank(exportFileName)) {
			logger.error("Export file path or name is missing!");
			return;
		}

		// format data
		List<String> recordStrList = formatData(outputData, outputClass, length, opFormatDefKey);

		// write the records to the file
		writeRecordsToFile(target, recordStrList, header, footer);

		// send file to MQ
		sendFileToMQ(target, mqConfig, recordStrList.size());
	}

	/**
	 * 
	 * @param target
	 * @param recordStrList
	 * @param header
	 * @param footer
	 */
	private void writeRecordsToFile(File target, List<String> recordStrList, String header, String footer) {
		if (target.getParentFile() == null || !target.getParentFile().exists()
				|| !target.getParentFile().isDirectory()) {
			target.getParentFile().mkdir();
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(target));
			if (header != null) {
				writer.write(header);
				writer.newLine();
			}
			for (String record : recordStrList) {
				writer.write(record);
				writer.newLine();
			}
			if (footer != null) {
				writer.write(footer);
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			logger.error(e, "Error to create exported file");
			setJobStatus(OifRunStatus.FILE_CREATE_ERROR);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e, "Error to create exported file");
					setJobStatus(OifRunStatus.FILE_CREATE_ERROR);
				}
			}
		}
	}

	/**
	 * 
	 * @param target
	 * @param mqConfig
	 * @param noOfRecords
	 */
	private void sendFileToMQ(File target, String mqConfig, int noOfRecords) {
		Boolean isMQSend = getPropertyBoolean(OIFConstants.MQ_SENDER_FLAG, true);
		String mqInterfaceID = getProperty(OIFConstants.INTERFACE_ID);
		if (isMQSend) {
			try {
				MQUtility mqu = new MQUtility(this);
				logger.info("Calling executeMQSendAPI with send file name  = " + target.getAbsolutePath()
						+ " with Interface ID = " + mqInterfaceID);
				mqu.executeMQSendAPI(mqInterfaceID, mqConfig, target.getAbsolutePath());
			} catch (MQUtilityException e) {
				logger.error("Fail to send file name  = " + target.getAbsolutePath() + " with Interface ID = "
						+ mqInterfaceID);
				setOutgoingJobStatusAndFailedCount(noOfRecords, OifRunStatus.MQ_ERROR, target.getName());
			}
		} else {
			logger.info("Flag MQ_SENDER_FLAG is set to false, file was not sent = " + target.getAbsolutePath()
					+ " with Interface ID = " + mqInterfaceID);
		}
		setOutgoingJobStatus(noOfRecords, target.getName());
	}

	/**
	 * 
	 * @param <K> where K extends {@link IOutputFormat}
	 * @param outputData
	 * @param outputClass
	 * @param length
	 * @param opFormatDefKey
	 * @return
	 */
	private <K extends IOutputFormat> List<String> formatData(List<K> outputData, Class<K> outputClass, int length,
			String opFormatDefKey) {
		List<String> recordStrList = new ArrayList<String>();
		try {
			if (!outputData.isEmpty()) {
				OutputFormatHelper<K> ofHelper = new OutputFormatHelper<K>(opFormatDefKey, logger,
						this.errorsCollector);
				ofHelper.initialize(outputClass);
				char[] charArray = new char[length];
				Arrays.fill(charArray, ' ');

				for (K dto : outputData) {
					String strResult = ofHelper.formatOutput(dto, charArray);
					recordStrList.add(strResult);
				}
			}
		} catch (Exception e) {
			setSuccessCount(recordStrList.size());
			setOutgoingJobStatusAndFailedCount(outputData.size() - recordStrList.size(),
					OifRunStatus.ERROR_WHILE_FORMATING_DATA, null);
		}

		return recordStrList;
	}

	/**
	 * @param <K> where K extends {@link IOutputFormat}
	 * @param DTO class to initialize helper
	 * @return OutputFormatHelper: initialized instance of OutputFormatHelper
	 */
	protected <K extends IOutputFormat> OutputFormatHelper<K> getOutputFormatHelper(Class<K> outputClass) {
		String opFormatDefKey = getProperty(OIFConstants.PARSE_LINE_DEFS, getComponentId());
		OutputFormatHelper<K> ofHelper = new OutputFormatHelper<K>(opFormatDefKey, logger, this.errorsCollector);
		ofHelper.initialize(outputClass);
		return ofHelper;
	}

	/**
	 * Method to send a mqftp message with NO RECORD. send the message with GPCS
	 * NORECORD YYYYMMDDHHMMSS + 37 bytes(spaces)
	 */
	protected void sendNoDataMessage(final List<String> message, final String path, final String fileName,
			final String mqConfig) {
		File target = new File(path + File.separator + fileName);
		writeRecordsToFile(target, message, null, null);
		sendFileToMQ(target, mqConfig, message.size());
	}

	/**
	 * 
	 * @param exportFileName
	 * @param recordStrList
	 */
	protected void createExportFile(String exportFileName, List<String> recordStrList) {
		try {
			File target = new File(exportFileName);
			writeRecordsToFile(target, recordStrList, null, null);
		} catch (Exception e) {
			logger.error(e, "Error to create exported file");
			setJobStatus(OifRunStatus.FILE_CREATE_ERROR);
		}
	}

	/**
	 * 
	 * @param fileName
	 * @param mqConfig
	 * @param noOfRecords
	 */
	protected void sendFile(String fileName, String mqConfig, int noOfRecords) {
		// send file to MQ
		Boolean isMQSend = getPropertyBoolean(OIFConstants.MQ_SENDER_FLAG, true);
		String mqInterfaceID = getProperty(OIFConstants.INTERFACE_ID);
		try {
			File target = new File(fileName);
			if (isMQSend) {
				sendFileToMQ(target, mqConfig, noOfRecords);
			} else {
				logger.info("Flag MQ_SENDER_FLAG is set to false, file was not sent = " + target.getAbsolutePath()
						+ " with Interface ID = " + mqInterfaceID);
			}
		} catch (Exception e) {
			logger.error("Fail to send file name  = " + fileName + " with Interface ID = " + mqInterfaceID);
			setJobStatus(OifRunStatus.FILE_TRANSFER_ERROR);
		}
	}

	/**
	 * 
	 * @param <K>
	 * @param outputClass
	 * @param outputData
	 * @param opFormatDefKey
	 * @return
	 */
	public <K extends IOutputFormat> List<String> createOutputRecords(Class<K> outputClass, List<K> outputData,
			String opFormatDefKey) {

		int length = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);

		// format data
		List<String> recordStrList = formatData(outputData, outputClass, length, opFormatDefKey);
		return recordStrList;
	}

	protected <K extends IOutputFormat> void sendData(Class<K> outputClass, List<K> outputData, String header,
			String footer) throws MQUtilityException, MQException, IOException {
		int length = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);
		String formatDefComponentId = getProperty(OIFConstants.PARSE_LINE_DEFS, getComponentId());
		List<String> messageData = formatData(outputData, outputClass, length, formatDefComponentId);
		if (StringUtils.isNotBlank(header)) {
			messageData.add(0, header);
		}
		if (StringUtils.isNotBlank(footer)) {
			messageData.add(footer);
		}
		send(messageData);
	}

	/**
	 * 
	 * @param data
	 * @throws MQException
	 * @throws IOException
	 * @throws MQUtilityException
	 */
	protected void send(List<String> data) throws MQException, IOException, MQUtilityException {
		String msgType = getProperty("MESSAGE_TYPE", "MQ_FT");
		// only checking null as sometimes an empty file is also valid requirement.
		if (data != null) {
			if ("MQ".equals(msgType)) {
				try {
					sendMq(data);
				} catch (Exception e) {
					if (e instanceof MQException) {
						setOutgoingJobStatusAndFailedCount(data.size(), OifRunStatus.MQ_ERROR, null);
						throw (MQException) e;
					} else if (e instanceof IOException) {
						setOutgoingJobStatusAndFailedCount(data.size(),
								OifRunStatus.MQ_ERROR_WHILE_TRYING_TO_POST_MESSAGE, null);
						throw (IOException) e;
					}
				}
			} else {
				sendMqFt(data);
			}
			setSuccessCount(data.size());
		}
	}

	/**
	 * 
	 * @param data
	 * @throws MQUtilityException
	 */
	protected void sendMqFt(List<String> data) throws MQUtilityException {
		String exportPath = getProperty(OIFConstants.EXPORT_FILE_PATH);
		String mqConfig = getProperty(OIFConstants.MQ_CONFIG);
		String mqInterfaceId = getProperty(OIFConstants.INTERFACE_ID);
		// create the filename as interface-id + timestamp to be consistent with other
		// all
		// other mqft export file names
		Calendar cal = new GregorianCalendar();
		String strTimestampPart = new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
		String exportFileName = mqInterfaceId + strTimestampPart + ".oif";
		File target = new File(exportPath + File.separator + exportFileName);
		if (StringUtils.isBlank(exportPath) || StringUtils.isBlank(exportFileName) || StringUtils.isBlank(mqConfig)
				|| StringUtils.isBlank(mqInterfaceId)) {
			setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			throw new TaskException(
					String.format("Missing properties: %s or %s or %s or %s", OIFConstants.EXPORT_FILE_PATH,
							OIFConstants.EXPORT_FILE_NAME, OIFConstants.MQ_CONFIG, OIFConstants.INTERFACE_ID));
		}
		writeRecordsToFile(target, data, null, null);
		// set export file Name
		setOutGoingFileName(exportFileName);
		MQUtility mqu = new MQUtility(this);
		logger.info("Calling executeMQSendAPI with send file name  = " + target.getAbsolutePath()
				+ " with Interface ID = " + mqInterfaceId);
		try {
			mqu.executeMQSendAPI(mqInterfaceId, mqConfig, target.getAbsolutePath());
		} catch (MQUtilityException e) {
			setOutgoingJobStatusAndFailedCount(data.size(), OifRunStatus.MQ_ERROR, exportFileName);
			throw e;
		}
	}

	/**
	 * 
	 * @param data
	 * @throws MQException
	 * @throws IOException
	 */
	protected void sendMq(List<String> data) throws MQException, IOException {
		RegularMQClient mqClient = null;
		try {
			mqClient = createRegMqClient();
			logger.info(mqClient.toString());
			String separator = System.getProperty("line.separator");
			String msgBody = StringUtils.join(data, separator);
			int characterSet = getPropertyInt("CHARACTER_SET", CHARACTER_SET);
			int encoding = getPropertyInt("ENCODING", ENCODING_ASCII);
			mqClient.putMessage(msgBody, characterSet, encoding);
			try {
				createExportLog(data);
			} catch (Exception e) {
				logger.error(e, "Data put in queue, but unable to create log file");
				setJobStatus(OifRunStatus.COMPLETE_WITH_ERRORS);
			}
		} finally {
			try {
				if (mqClient != null) {
					mqClient.finalize();
				}
			} catch (Exception e) {
				logger.error("Exception in closing MQ connection: " + e.getMessage());
				setJobStatus(OifRunStatus.MQ_ERROR_CLOSING_CONNECTION);
			}
		}
	}

	/**
	 * 
	 * @param sendFileToGPCS
	 * @param resultPath
	 * @param exportFileName
	 * @param mqInterfaceID
	 * @param recordCount
	 * @throws MQUtilityException
	 */
	protected void sendFileToGPCS(boolean sendFileToGPCS, String resultPath, String exportFileName,
			String mqInterfaceID, int recordCount) throws MQUtilityException {
		if (sendFileToGPCS) {
			try {
				// Sending file to MQ
				MQUtility mqu = new MQUtility(this);
				logger.info("Calling executeMQSendAPI with send file name  = " + resultPath + exportFileName
						+ " with Interface ID = " + mqInterfaceID);
				String mqConfig = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES,
						OIFConstants.MQ_CONFIG);
				mqu.executeMQSendAPI(mqInterfaceID, mqConfig, resultPath + exportFileName);
			} catch (MQUtilityException e) {
				String errorStr = "MQUtilityException raised when sending interface file for " + resultPath
						+ exportFileName + " for the MQ interface " + mqInterfaceID + "  Exception : " + e.getMessage();
				logger.error(errorStr);
				setOutgoingJobStatusAndFailedCount(recordCount, OifRunStatus.MQ_ERROR, exportFileName);
				throw e;
			}
		} else {
			logger.info("Flag SEND_FILE_TO_GPCS is set to false, file was not sent = " + resultPath + exportFileName
					+ " with Interface ID = " + mqInterfaceID);
		}
		setOutgoingJobStatus(recordCount, exportFileName);

	}

	/**
	 * 
	 * @return
	 * @throws MQException
	 */
	protected RegularMQClient createRegMqClient() throws MQException {
		String hostName = getProperty(OIFConstants.RMQ_HOST_NAME);
		int port = getPropertyInt(OIFConstants.RMQ_PORT);
		String queueManagerName = getProperty(OIFConstants.RMQ_QUEUE_MANAGER);
		String channel = getProperty(OIFConstants.RMQ_CHANNEL);
		String queueName = getProperty(OIFConstants.RMQ_QUEUE_NAME);
		String userName = getProperty(OIFConstants.RMQ_USER);
		String pass = getProperty(OIFConstants.RMQ_PASSWORD);
		RegularMQClient rmq = new RegularMQClient(hostName, port, queueManagerName, channel, queueName, userName, pass);
		return rmq;
	}

	/**
	 * 
	 * @param data
	 */
	protected void createExportLog(List<String> data) {
		String exportPath = getProperty(OIFConstants.EXPORT_FILE_PATH);
		String mqInterfaceId = getProperty(OIFConstants.INTERFACE_ID);
		if (StringUtils.isBlank(exportPath) || StringUtils.isBlank(mqInterfaceId)) {
			logger.error(String.format("Missing properties: %s or %s", OIFConstants.EXPORT_FILE_PATH, OIFConstants.INTERFACE_ID));
			errorsCollector.getRunHistory().setStatus(OifRunStatus.MISSING_CONFIGURATION);
			setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			throw new TaskException(String.format("Missing properties: %s or %s", OIFConstants.EXPORT_FILE_PATH,
					OIFConstants.INTERFACE_ID));
		}
		// create the filename as interface-id + timestamp to be consistent with other
		// all
		// other mqft export file names
		Calendar cal = new GregorianCalendar();
		String strTimestampPart = new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
		// String exportFileName = mqInterfaceId + strTimestampPart + data.hashCode() +
		// ".oif";
		StringBuilder sb = new StringBuilder();

		sb.append(exportPath).append(File.separator).append(mqInterfaceId).append(strTimestampPart)
				.append(data.hashCode()).append(".oif");
		createExportFile(sb.toString(), data);

	}

	/**
	 * This method get the layout from properties into Map<String, String>. Example:
	 * <PLANT_CODE=1,3>
	 * 
	 * @return
	 */
	protected Map<String, String> getMapLayout() {

		List<ComponentProperty> formats = PropertyService
				.getComponentProperty(getProperty(OIFConstants.PARSE_LINE_DEFS));
		Map<String, String> odAnnotated = new HashMap<String, String>();

		for (ComponentProperty componentProperty : formats) {
			odAnnotated.put(componentProperty.getId().getPropertyKey(), componentProperty.getPropertyValue());
		}
		return odAnnotated;
	}

	/**
	 * Method to write and send message to MQ with header and footer
	 * 
	 * @param message
	 * @param header
	 * @param footer
	 * @param path
	 * @param fileName
	 * @param mqConfig
	 * @throws TaskException
	 */
	protected void sendData(final List<String> message, final String header, final String footer, final String path,
			final String fileName, final String mqConfig) throws TaskException {
		File target = new File(path + File.separator + fileName);
		writeRecordsToFile(target, message, null, null);

		Boolean isMQSend = getPropertyBoolean(OIFConstants.MQ_SENDER_FLAG, true);
		String mqInterfaceID = getProperty(OIFConstants.INTERFACE_ID);
		if (isMQSend) {
			try {
				MQUtility mqu = new MQUtility(this);
				logger.info("Calling executeMQSendAPI with send file name  = " + target.getAbsolutePath()
						+ " with Interface ID = " + mqInterfaceID);
				mqu.executeMQSendAPI(mqInterfaceID, mqConfig, target.getAbsolutePath());
			} catch (MQUtilityException e) {
				logger.error("Fail to send file name  = " + target.getAbsolutePath() + " with Interface ID = "
						+ mqInterfaceID);
				setOutgoingJobStatusAndFailedCount(message.size(), OifRunStatus.MQ_ERROR, fileName);
				throw new TaskException("Error sending the message", e.getCause());
			}
		} else {
			logger.info("Flag MQ_SENDER_FLAG is set to false, file was not sent = " + target.getAbsolutePath()
					+ " with Interface ID = " + mqInterfaceID);
		}
		setOutgoingJobStatus(message.size(), fileName);
	}

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public Timestamp getPropertyTimestamp(String propertyName) {
		Timestamp ts = null;
		String propValue = "";
		try {
			propValue = getProperty(propertyName, "");
			if (!StringUtils.isEmpty(propValue)) {
				ts = Timestamp.valueOf(propValue.trim());
			}
		} catch (Exception e) {
			getLogger().error(e, "Error parsing timestamp" + propValue);
		}
		return ts;
	}

	/**
	 * @param compTs: ComponentStatus
	 * @return Timestamp given component status, convert the status value to a
	 *         Timestamp
	 */
	protected Timestamp getComponentTimestamp(ComponentStatus compTs) {
		Timestamp ts = null;
		if (compTs != null && !StringUtils.isBlank(compTs.getStatusValue())) {
			try {
				String strTS = StringUtils.trim(compTs.getStatusValue());
				ts = Timestamp.valueOf(strTS);
			} catch (Exception e) {
				logger.error(e, "Error parsing timestamp" + compTs.getStatusValue());
			}
		}
		return ts;
	}

	/**
	 * @param key:String
	 * @return Timestamp find timestamp value given the status key, for the current
	 *         component id
	 */
	protected Timestamp getComponentTimestamp(String key) {
		Timestamp ts = null;
		if (!StringUtils.isEmpty(key)) {
			ComponentStatus compTs = getComponentStatus(LAST_PROCESS_TIMESTAMP);
			if (compTs != null) {
				ts = getComponentTimestamp(compTs);
			}
		}
		return ts;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	protected ComponentStatus getComponentStatus(String key) {
		ComponentStatusId id = new ComponentStatusId(getComponentId(), key);
		ComponentStatus cStat = cStatusDao.findByKey(id);
		return cStat;
	}

	/**
	 * 
	 */
	protected String[] getTempFiles() {
		ComponentStatus componentStatus = getComponentStatus(TEMP_FILE_LIST);
		if (componentStatus == null || StringUtils.isEmpty(componentStatus.getStatusValue()))
			return null;
		return componentStatus.getStatusValue().split(Delimiter.COMMA);
	}

	/**
	 * 
	 */
	protected void clearTempFiles() {
		this.updateComponentStatus(TEMP_FILE_LIST, "");
	}

	/**
	 * 
	 * @param tempFiles
	 */
	protected void updateTempFiles(String[] tempFiles) {
		String tempFileStr = "";
		boolean isFirst = true;
		for (String str : tempFiles) {
			if (!isFirst)
				tempFileStr += ",";
			else
				isFirst = false;
			tempFileStr += str == null ? "null" : str.toString();
		}
		this.updateComponentStatus(TEMP_FILE_LIST, tempFileStr);
	}

	/**
	 * @return Timestamp get timestamp for key=LAST_PROCESS_TIMESTAMP
	 */
	protected Timestamp getLastProcessTimestamp() {
		return getComponentTimestamp(LAST_PROCESS_TIMESTAMP);
	}

	/**
	 * @param currentTimestamp update the timestamp for key=LAST_PROCESS_TIMESTAMP
	 *                         to the given Timestamp
	 */
	protected void updateLastProcessTimestamp(Timestamp currentTimestamp) {

		if (currentTimestamp == null)
			return;

		ComponentStatus cStat = getComponentStatus(LAST_PROCESS_TIMESTAMP);
		if (cStat == null) {
			cStat = new ComponentStatus(getComponentId(), LAST_PROCESS_TIMESTAMP, currentTimestamp.toString());
		} else {
			cStat.setStatusValue(currentTimestamp.toString());
		}
		cStatusDao.save(cStat);
	}

	/**
	 * @param String update the component value for key
	 */
	protected void updateComponentStatus(String key, String value) {

		String val = "";
		if (StringUtils.isBlank(key))
			return;
		if (!StringUtils.isBlank(value)) {
			val = value;
		}
		ComponentStatus cStat = getComponentStatus(key);
		if (cStat == null) {
			cStat = new ComponentStatus(getComponentId(), key, val);
		} else {
			cStat.setStatusValue(val);
		}
		cStatusDao.save(cStat);
	}

	/**
	 * @param isDBTimestamp: true=use database timestamp, else use system TS update
	 *                       the timestamp for key=LAST_PROCESS_TIMESTAMP to the
	 *                       CURRENT Timestamp
	 */
	protected void updateLastProcessTimestamp(boolean isDBTimestamp) {

		Timestamp ts = getCurrentTime(isDBTimestamp);

		updateLastProcessTimestamp(ts);
	}

	/**
	 * @deprecated
	 * @param: none update with current system timestamp
	 */
	protected void updateLastProcessTimestamp() {

		updateLastProcessTimestamp(false);
	}

	/**
	 * 
	 * @return {@code Timestamp}
	 */
	protected Timestamp getStartOfToday() {
		Timestamp startTimestamp = null;
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		startTimestamp = new Timestamp(cal.getTimeInMillis());

		return startTimestamp;
	}

	/**
	 * @return {@code Timestamp}
	 */
	public Timestamp getStartTime() {
		Timestamp startTs = null;
		// get the configured start timestamp, if not set/null then get the component
		// last run timestamp
		startTs = getPropertyTimestamp("START_TIMESTAMP");
		if (startTs == null) {
			startTs = getLastProcessTimestamp(); // component status
		}
		// if startTs still cannot be found, set it to start of current day/midnight
		if (startTs == null) {
			startTs = getStartOfToday();
		}
		return startTs;
	}

	/**
	 * @return Timestamp
	 */
	public Timestamp getEndTimestamp() {
		Timestamp endTs = getPropertyTimestamp("END_TIMESTAMP");
		return endTs;
	}

	/**
	 * @param isDBTimestamp: yes=default to database current TS, else default to
	 *                       system time
	 * @return
	 */
	public Timestamp getEndTime(boolean isDBTimestamp) {
		return getEndTime(null, isDBTimestamp);
	}

	/**
	 * @deprecated
	 * @param isDBTimestamp: true=default to database current TS, else default to
	 *                       system time
	 * @return: configured end_time or current time or start_time provided if start
	 *          > end
	 */
	public Timestamp getEndTime(Timestamp startTs) {
		return getEndTime(startTs, false);
	}

	/**
	 * @param startTs: start timestamp to validate, if startTs provided is >
	 *                 configured endTs. then, set endTs = startTs
	 * @return
	 */
	public Timestamp getEndTime(Timestamp startTs, boolean isDBTimestamp) {
		Timestamp nowTs = getCurrentTime(isDBTimestamp);
		Timestamp endTs = null;
		// get the configured start timestamp, if not-set/null then default to now
		endTs = getPropertyTimestamp("END_TIMESTAMP");
		if (endTs == null) {
			endTs = nowTs;
		}
		// if start time was provided and if the start > end, then set end = start
		if (startTs != null && startTs.after(endTs)) {
			endTs = startTs;
		}
		return endTs;
	}

	/**
	 * 
	 */
	public Timestamp getCurrentTime(boolean isDBTimestamp) {
		GenericDaoService genericDao = getService(GenericDaoService.class);
		Calendar now = GregorianCalendar.getInstance();
		Timestamp nowTs = null;
		if (isDBTimestamp) {
			nowTs = genericDao.getCurrentDBTime();
		} else {
			nowTs = new Timestamp(now.getTimeInMillis());
		}

		return nowTs;
	}

	/**
	 * 
	 */
	protected void setIncomingJobStatus(OifRunStatus status) {
		try {
			if (errorsCollector != null && errorsCollector.getRunHistory() != null
					&& (errorsCollector.getRunHistory().getStatus() == null
							|| errorsCollector.getRunHistory().getStatus() == OifRunStatus.SUCCESS
							|| errorsCollector.getRunHistory().getStatus() == OifRunStatus.FAILURE))
				errorsCollector.getRunHistory().setStatus(status);
		} catch (Exception e) {
			logger.error(e, "Exception while setting RunStatus.");
		}
	}

	/**
	 * 
	 * @param status
	 */
	protected void setJobStatus(OifRunStatus status) {
		try {
			// check the status as users would like to know the first failure
			if (errorsCollector != null && errorsCollector.getRunHistory() != null
					&& errorsCollector.getRunHistory().getStatus() != null)
				errorsCollector.getRunHistory().setStatus(status);
		} catch (Exception e) {
			logger.error(e, "Exception while setting RunStatus.");
		}
	}

	/**
	 * 
	 * @param processedRecords
	 * @param fileName
	 */
	protected void setOutgoingJobStatus(int processedRecords, String fileName) {
		try {
			// Only update the success count when job is actually success or no status
			if (errorsCollector != null && errorsCollector.getRunHistory() != null) {
				if (errorsCollector.getRunHistory().getStatus() == null
						|| errorsCollector.getRunHistory().getStatus() == OifRunStatus.SUCCESS) {
					errorsCollector.getRunHistory()
							.setSuccessCount(errorsCollector.getRunHistory().getSuccessCount() + processedRecords);
					if (StringUtils.isEmpty(errorsCollector.getRunHistory().getOutgoingFileName()))
						errorsCollector.getRunHistory().setOutgoingFileName(fileName);
					else
						errorsCollector.getRunHistory().setOutgoingFileName(
								errorsCollector.getRunHistory().getOutgoingFileName() + "," + fileName);
				}
			}
		} catch (Exception e) {
			logger.error(e, "Exception while setting RunStatus.");
		}
	}

	/**
	 * 
	 * @param failedRecords
	 * @param status
	 * @param fileName
	 */
	protected void setOutgoingJobStatusAndFailedCount(int failedRecords, OifRunStatus status, String fileName) {
		try {
			if (errorsCollector != null && errorsCollector.getRunHistory() != null) {
				if (errorsCollector.getRunHistory().getStatus() == null
						|| errorsCollector.getRunHistory().getStatus() == OifRunStatus.SUCCESS
						|| errorsCollector.getRunHistory().getStatus() == OifRunStatus.FAILURE) {
					errorsCollector.getRunHistory().setStatus(status);
				}
				errorsCollector.getRunHistory()
						.setFailedCount(errorsCollector.getRunHistory().getFailedCount() + failedRecords);

				if (StringUtils.isNotEmpty(fileName))
					errorsCollector.getRunHistory().setOutgoingFileName(fileName);
			}
		} catch (Exception e) {
			logger.error(e, "Exception while setting RunStatus.");
		}
	}

	/**
	 * 
	 * @param successCount
	 * @param failureCount
	 * @param fileNames
	 */
	protected void setIncomingJobCount(int successCount, int failureCount, String[] fileNames) {
		try {
			if (errorsCollector != null && errorsCollector.getRunHistory() != null) {
				errorsCollector.getRunHistory()
						.setSuccessCount(errorsCollector.getRunHistory().getSuccessCount() + successCount);
				errorsCollector.getRunHistory()
						.setFailedCount(errorsCollector.getRunHistory().getFailedCount() + failureCount);
				if (fileNames != null && fileNames.length > 0)
					errorsCollector.getRunHistory().setIncomingFileName(Arrays.toString(fileNames));
			}
		} catch (Exception e) {
			logger.error(e, "Exception while setting RunStatus.");
		}
	}

	/**
	 * 
	 * @param successCount
	 */
	protected void setSuccessCount(int successCount) {
		try {
			if (errorsCollector != null && errorsCollector.getRunHistory() != null) {
				errorsCollector.getRunHistory()
						.setSuccessCount(successCount + errorsCollector.getRunHistory().getSuccessCount());

			}
		} catch (Exception e) {
			logger.error(e, "Exception while setting RunStatus.");
		}
	}

	/**
	 * 
	 * @param failedCount
	 */
	protected void setFailedCount(int failedCount) {
		try {
			if (errorsCollector != null && errorsCollector.getRunHistory() != null) {
				errorsCollector.getRunHistory()
						.setFailedCount(failedCount + errorsCollector.getRunHistory().getFailedCount());

			}
		} catch (Exception e) {
			logger.error(e, "Exception while setting RunStatus.");
		}
	}

	/**
	 * 
	 * @param exportFileName
	 */
	protected void setOutGoingFileName(String exportFileName) {
		try {
			if (errorsCollector != null && errorsCollector.getRunHistory() != null) {
				errorsCollector.getRunHistory().setOutgoingFileName(exportFileName);

			}
		} catch (Exception e) {
			logger.error(e, "Exception while setting RunStatus.");
		}
	}

	/**
	 * 
	 */
	@Override
	public OifErrorsCollector getOifErrorsCollector() {
		return this.errorsCollector;
	}

	/**
	 * 
	 */
	@Override
	public void setOifErrorsCollector(OifErrorsCollector oifErrorsCollector) {
		this.errorsCollector = oifErrorsCollector;
	}

	/**
	 * 
	 */
	@Override
	public void execute(Object[] args) {
		// TODO Auto-generated method stub

	}

}