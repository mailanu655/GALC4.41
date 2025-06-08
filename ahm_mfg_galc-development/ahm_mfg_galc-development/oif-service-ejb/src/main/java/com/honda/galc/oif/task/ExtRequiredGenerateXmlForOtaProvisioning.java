package com.honda.galc.oif.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.ExtRequiredPartSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.ota.xstream.OTAProvisioning;
import com.honda.galc.ota.xstream.ProvisionVehicle;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/** * * 
 * @version 0.1 
 * @author Bradley Brown
 * @since June 04, 2018
 * 
 * This class generates Vehicle Provisioning XML for giving dates.
 */

public class ExtRequiredGenerateXmlForOtaProvisioning extends OifAbstractTask implements
IEventTaskExecutable {

	private static final String TIMESTAMP_LAST_RUN = "TIMESTAMP_LAST_RUN";
	private static final String XML_OUTPUT_FILE_LOCATION = "XML_OUTPUT_FILE_LOCATION";
	private static final String VINS_PER_XML_FILE = "VINS_PER_XML_FILE";
	private static final String XML_FILE_NAME_FORMAT = "XML_FILE_NAME_FORMAT";
	private static final String XML_FILENAME_TIMESTAMP_FORMAT = "XML_FILENAME_TIMESTAMP_FORMAT";
	private static final String FACTORY_SITE = "Factory";
	private static final String SENDER_ID = "Sender_ID";
	private static final String SEQUENCES_RAN_TODAY = "SEQ_CREATED_TILL_LAST_RUN";
	private static final String TIMESTAMP_FORMAT_JAVA = "yyyy/MM/dd HH:mm:ss";
	private static final String GENERIC_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
	private static final String SEQUENCE = "sequence";
	private static final String GENERAL_PATTERN = "EEE, d MMM yyyy HH:mm:ss z";
	private static final String TIMESTAMP_PATTERN_FOR_JAVA="yyyyMMddHHmmss";
	private static final String EXCEPTION_EMAIL_SUBJECT1 = "Exception caught while processing for Vehicle Provisioning xml on : ";
	private static final String EXCEPTION_MESSAGE1 = "Exception caught while generating Vehicle Provisioning XML.\nPlease report the issue to the support team";
	private static final String EXCEPTION_MESSAGE2 = "Exception caught while createing file. \nPlease report the issue to the support team";
	private static final String NO_NAMESPACE_SCHEMA_LOCATION = "NO_NAMESPACE_SCHEMA_LOCATION";
	private static final String OIF_VEHICLE_PROVISIONING = "OIF_EXT_REQUIRED_VEHICLE_PROVISIONING";
	private static final String SEQ_CREATED_TILL_LAST_RUN = "SEQ_CREATED_TILL_LAST_RUN";
	private static final String TIMESTAMP_TO_CURRENT_RUN = "TIMESTAMP_TO_CURRENT_RUN";
	private static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
	private static final String DB2_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSS";
	private static final String THANKS = "\n\nMessage From";
	private static String THANKS_MESSAGE = "\nVehicle Provisioning OIF Job";
	private static final String ENVIRONMENT_INFO = "ENVIRONMENT_INFO";
	private static final String INSUFFICIENT_INFO_SUBJECT = "VEHICLE PROVISIONING XML GENERATION :: Need Proper Configuration ," ;
	private static final String DELIMITER = ",";
	private static final String DASH = "-";
	private static final String EXT_REQUIRED_PART_GROUP_NAME = "EXT_REQUIRED_PART_GROUP_NAME";
	private static final String CREATE_FILE = "CREATE_FILE";
	private OifServiceEMailHandler emailHandler = null;
	String messageContent = null;
	private Timestamp currentTimestamp = null;
	private Integer fileSeqNo = null;
	private String processingDate = null;
	private Map<String, ComponentStatus> componentStatusMap = null;
	private String lastRunTimeStamp = null;
	private String runUptoTimeStamp = null;
	private boolean runningOnSameDay = false;
	private String partNames = null;
	private String partGroup = null;
	private static final String NEW_LINE = System.getProperty("line.separator");
	private String site = "NOT_CONFIGURED";
	private static SimpleDateFormat weekOfTheYearFormat = new SimpleDateFormat("MM/dd/yyyy");
	private List<Object[]> partNameList = new ArrayList<Object[]>();


	public ExtRequiredGenerateXmlForOtaProvisioning(String name) {
		super(name);
		setProcessingdate(getDateTimeInFormat(GENERAL_PATTERN, new Timestamp(System.currentTimeMillis())));
		setComponentStatusMap(ServiceFactory.getDao(ComponentStatusDao.class).findAllByComponentIdMap(getName()));
		setCurrentTimestamp(new Timestamp(System.currentTimeMillis()));
		setTimeStampsFromDB();
	}

	@Override
	public void execute(Object[] args) {
		logger.info("Started processing for the day : "  + getProcessingdate());

		try {
			emailHandler = new OifServiceEMailHandler(getName());
			setSite(getProperty(ENVIRONMENT_INFO));
			partGroup = getProperty(EXT_REQUIRED_PART_GROUP_NAME);
			
			if(StringUtils.isBlank(partGroup)) {
				String message = "Needed configuration is missing " + EXT_REQUIRED_PART_GROUP_NAME + " ,  Factory / Site : "+ getSite();
				logger.error(message);
				emailHandler.delivery(INSUFFICIENT_INFO_SUBJECT + getProcessingdate() + " , Factory / Site INFO : " + getSite(), "\n"+message +THANKS+THANKS_MESSAGE);
				return;
			}
			
			partNameList = getPartNames(partGroup);

			if(partNameList.isEmpty()) {
				String message = "No parts are configured for Part Group(s). Factory / Site : "+ getSite();
				logger.error("Part Group " + partGroup + " is not set up for any parts.");
				emailHandler.delivery(INSUFFICIENT_INFO_SUBJECT + getProcessingdate() + " , Factory / Site INFO : " + getSite(), "\n"+message +THANKS+THANKS_MESSAGE);
				return;
			}
			
			parsePartNames();

			setFileSeqNo(new Integer(getFileSequenceNoFromLastRun()));

			List<String> timeStampIntervalsLst = parseDatesToIntervals(getLastRunTimeStamp(), getRunUptoTimeStamp());

			for(String timeInterval : timeStampIntervalsLst){
				StringTokenizer intervals = new StringTokenizer(timeInterval, DASH);
				String fromDateTimeStamp = convertDateStrToPattern(TIMESTAMP_FORMAT_JAVA, GENERIC_DATE_FORMAT, intervals.nextToken().trim());
				String toDateTimeStamp = convertDateStrToPattern(TIMESTAMP_FORMAT_JAVA, GENERIC_DATE_FORMAT, intervals.nextToken().trim());

				List<Object[]> shippedVinDetails = new ArrayList<Object[]>();
				List<String> framePrefix = ServiceFactory.getDao(FrameSpecDao.class).findDistinctFramePrefix();

				shippedVinDetails.addAll(ServiceFactory.getDao(ShippingStatusDao.class).getInvoicedVindDetailsWithExtRequired(fromDateTimeStamp, toDateTimeStamp, partNames, framePrefix));

				if(shippedVinDetails != null && shippedVinDetails.size() > 0){
					logger.info("There are :" + shippedVinDetails.size() + " to process for the interval " + timeInterval);
					generateXmlSequence(shippedVinDetails);
				}

				if(!isRunningOnSameDay())
					setFileSeqNo(Integer.valueOf(1));

				intervals = null;
				fromDateTimeStamp = null;
				toDateTimeStamp = null;
			}
			updateOtaSchedulerDetails();
			logger.info("Vehicle Provisioning XMLs Creating process finished successfully on " + getProcessingdate() );

		} catch (Exception ex){
			logger.error(ex, "Could not create XML due to : " + ex.getMessage());
			emailHandler.delivery(EXCEPTION_EMAIL_SUBJECT1 + getProcessingdate() + " , Factory /Site INFO : " + getSite(), EXCEPTION_MESSAGE1 + "\nFactory / Site : " + getSite() +"\n\nIssue detail : " + ex.getMessage() + "\n\nException StackTrace : \n" +getStackTraceAsString(ex) +THANKS+THANKS_MESSAGE);
		}
	}

	private void setTimeStampsFromDB(){
		setLastRunTimeStamp(getComponentStatusValue(TIMESTAMP_LAST_RUN));
		String toTimeStamp = getComponentStatusValue(TIMESTAMP_TO_CURRENT_RUN);

		if(toTimeStamp.equalsIgnoreCase(CURRENT_TIMESTAMP))
			toTimeStamp = getDateTimeInFormat(TIMESTAMP_FORMAT_JAVA, getCurrentTimestamp());

		setRunUptoTimeStamp(toTimeStamp);
	}
	
	private void parsePartNames() {
		for(Object[] partName : partNameList) {
				if(partNames == null) {
					partNames = partName[0].toString().trim();
				}else
					partNames = partNames + DELIMITER +  partName[0].toString().trim();	
		}
	}

	private void updateOtaSchedulerDetails() throws Exception{
		ComponentStatus lastSuccRun = new ComponentStatus(OIF_VEHICLE_PROVISIONING, TIMESTAMP_LAST_RUN, convertDateStrToPattern(TIMESTAMP_FORMAT_JAVA,TIMESTAMP_FORMAT_JAVA, getRunUptoTimeStamp()));
		ComponentStatus nextRun = new ComponentStatus(OIF_VEHICLE_PROVISIONING, TIMESTAMP_TO_CURRENT_RUN, CURRENT_TIMESTAMP);
		ComponentStatus seqCreateTillLastRun = new ComponentStatus(OIF_VEHICLE_PROVISIONING, SEQ_CREATED_TILL_LAST_RUN, getFileSeqNo().toString());
		List<ComponentStatus> componentStatusLst = new ArrayList<ComponentStatus>();
		componentStatusLst.add(lastSuccRun);
		componentStatusLst.add(nextRun);
		componentStatusLst.add(seqCreateTillLastRun);
		ServiceFactory.getDao(ComponentStatusDao.class).saveAll(componentStatusLst);
	}

	private void generateXmlSequence(List<Object[]> vinsTobeProcessedLst) throws IOException, Exception{
		boolean createFile = getPropertyBoolean(CREATE_FILE, true);

		if(createFile){
		ArrayList<ProvisionVehicle> provisionVehicleLst = null;
		int noofVinsPerXml = getPropertyInt(VINS_PER_XML_FILE);
		String xmlFileName = getXmlFileName(vinsTobeProcessedLst.get(0));

		for (Object[] vinDetails : vinsTobeProcessedLst) {

			if(provisionVehicleLst == null)
				provisionVehicleLst = new ArrayList<ProvisionVehicle>();

			ProvisionVehicle provisionVehicle = createProvisionVehicle(vinDetails);
			if(provisionVehicle != null)
				provisionVehicleLst.add(provisionVehicle);

			if(provisionVehicleLst.size() >= noofVinsPerXml){
				createProvisioningXml(provisionVehicleLst, xmlFileName);
				provisionVehicleLst = null;
			}
			provisionVehicle = null;
		}
		if(provisionVehicleLst != null)
			createProvisioningXml(provisionVehicleLst, xmlFileName);
		}else {
			logger.info("CREATE_FILE set to false, no XML file will be created.");
		}
	}

	private void createProvisioningXml(ArrayList<ProvisionVehicle> provisionVehicleLst, String xmlFileName) throws IOException, Exception{

		OTAProvisioning otaProvisioning = new OTAProvisioning(getProperty(NO_NAMESPACE_SCHEMA_LOCATION));
		otaProvisioning.setProvisionVehicle(provisionVehicleLst);
		copyToNetworkFolder(otaProvisioning, xmlFileName.replace(getProperty(SEQUENCE), getFileSeqNo().toString())+".xml");
		setFileSeqNo(getFileSeqNo() + 1);

	}

	private ProvisionVehicle createProvisionVehicle(Object[] vinDetails) throws Exception{
		ProvisionVehicle provisionVehicle = new ProvisionVehicle(vinDetails[0].toString(), vinDetails[1].toString(), vinDetails[2].toString(), vinDetails[3].toString(), weekOfTheYear(vinDetails[4].toString()), vinDetails[5].toString(), vinDetails[6].toString(), vinDetails[7].toString());
		return provisionVehicle;
	}

	/*
	 * the network file location listened for a particular file name format to get it transfered to another location 
	 * So create the provisioning xml as a temporary file and write the content and then rename it to the actual file name format.
	 * */
	private void copyToNetworkFolder(OTAProvisioning otaProvisioning, String fileName) throws IOException, Exception{
		
		FileOutputStream fos = null;
		try{
			
		logger.info("Writing file for the file name : " + fileName);

		String path = getProperty(XML_OUTPUT_FILE_LOCATION)+ File.separator;

		String tempOtaFile = path+"TempOTAFILE.xml";
		fos = new FileOutputStream(tempOtaFile);

		XStream xstream = new XStream(new StaxDriver());
		xstream.autodetectAnnotations(true);

		xstream.toXML(otaProvisioning, fos);

		File tempFile = new File(tempOtaFile);
		File actualFileName = new File(path+fileName);

		if(actualFileName.exists())
			actualFileName.delete();

		tempFile.renameTo(actualFileName);

		logger.info("XML File Created successfully , file name : " + actualFileName);
		
		}catch(Exception e) {
			logger.error(e, "Could not create file due to : " + e.getMessage());
			emailHandler.delivery(EXCEPTION_EMAIL_SUBJECT1 + getProcessingdate() + " , Factory /Site INFO : " + getSite(), EXCEPTION_MESSAGE2 + 
					"\nFactory / Site : " + getSite() +"\n\nIssue detail : " + e.getMessage() + "\n\nException StackTrace : \n" +
					getStackTraceAsString(e) +THANKS+THANKS_MESSAGE);
		}
		finally {
			fos.close();
		}
	}

	private int getFileSequenceNoFromLastRun() throws ParseException, Exception{

		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_FORMAT_JAVA);
		Date timeLastRun = sdf.parse(getLastRunTimeStamp());
		Date currentTimestamp = sdf.parse(getRunUptoTimeStamp());

		if(DateUtils.isSameDay(timeLastRun, currentTimestamp)){
			setRunningOnSameDay(true);
			return Integer.valueOf(getComponentStatusValue(SEQUENCES_RAN_TODAY)).intValue();
		}else{
			return 1;
		}
	}

	private String getXmlFileName(Object[] recordForTimeStamp) throws ParseException{
		String fileNameFromProp = getProperty(XML_FILE_NAME_FORMAT);
		fileNameFromProp = fileNameFromProp.replace(FACTORY_SITE, getProperty(FACTORY_SITE));
		fileNameFromProp = fileNameFromProp.replace(SENDER_ID, getProperty(SENDER_ID));
		fileNameFromProp = fileNameFromProp.replace(getProperty(XML_FILENAME_TIMESTAMP_FORMAT),  convertDateStrToPattern(TIMESTAMP_PATTERN_FOR_JAVA, DB2_TIMESTAMP_FORMAT, recordForTimeStamp[8].toString()));
		return fileNameFromProp;
	}

	private String getDateTimeInFormat(String pattern, Date date){
		return new SimpleDateFormat(pattern).format(date);
	}

	private String convertDateStrToPattern(String formatTo, String parseTo, String genericDateFormat) throws ParseException{
		return  getDateTimeInFormat(formatTo, new SimpleDateFormat(parseTo).parse(genericDateFormat));
	}

	/* This method generates from and to timestamp, incremental of 24 hours
	 * for example last run date is : 2015/11/18 12:16:08
	 * toTimestamp is : 2015/11/20 13:16:10
	 * the result is
	 * 
	 *  Wed Nov 18 12:16:08 EST 2015-Thu Nov 19 12:16:08 EST 2015
	 *	Thu Nov 19 12:16:08 EST 2015-Fri Nov 20 12:16:08 EST 2015
	 *	Fri Nov 20 12:16:08 EST 2015-Fri Nov 20 13:16:10 EST 2015
	 * 
	 * */	
	private List<String> parseDatesToIntervals(String lastRunTimeStamp, String toTimeStamp) throws Exception{

		final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;

		java.util.Date startTimeStampFmt = new SimpleDateFormat(TIMESTAMP_FORMAT_JAVA).parse(lastRunTimeStamp);
		java.util.Date toTimeStampFmt = new SimpleDateFormat(TIMESTAMP_FORMAT_JAVA).parse(toTimeStamp);
		java.util.Date tempStartTimeStampFmt = null;
		java.util.Date tempEndTimeStampFmt = null;

		List<String> executionDateLst = new ArrayList<String>();

		if(toTimeStampFmt.before(startTimeStampFmt)){
			logger.error("Scheduler start time is greater than end time or current time stamp, Please report to support team");
			throw new Exception("Scheduler start time is greater than end time or current time stamp, Please report to support team");
		}else if (DateUtils.isSameDay(startTimeStampFmt, toTimeStampFmt) && startTimeStampFmt.before(toTimeStampFmt)) {
			// run once
			executionDateLst.add(startTimeStampFmt.toString() + DASH + toTimeStampFmt.toString());
		}else{
			// should be start time greater then end time on different date
			tempStartTimeStampFmt = startTimeStampFmt;
			tempEndTimeStampFmt = DateUtils.addHours(startTimeStampFmt, 24);


			while(!DateUtils.isSameDay(tempStartTimeStampFmt, toTimeStampFmt) && !(Math.abs(tempStartTimeStampFmt.getTime() - toTimeStampFmt.getTime()) <= MILLIS_PER_DAY) && tempStartTimeStampFmt.before(toTimeStampFmt) ){
				executionDateLst.add(tempStartTimeStampFmt.toString() + DASH + tempEndTimeStampFmt.toString());

				tempStartTimeStampFmt = tempEndTimeStampFmt;
				tempEndTimeStampFmt = DateUtils.addHours(tempStartTimeStampFmt, 24);

			}
			tempEndTimeStampFmt = toTimeStampFmt;

			executionDateLst.add(tempStartTimeStampFmt.toString() + DASH + tempEndTimeStampFmt.toString());
		}

		return executionDateLst;
	}

	private Timestamp getCurrentTimestamp() {
		return currentTimestamp;
	}

	private void setCurrentTimestamp(Timestamp lastSuccessfulRunTimestamp) {
		this.currentTimestamp = lastSuccessfulRunTimestamp;
	}

	private Integer getFileSeqNo() {
		return fileSeqNo;
	}

	private void setFileSeqNo(Integer fileSeqNo) {
		this.fileSeqNo = fileSeqNo;
	}

	private  String getProcessingdate() {
		return processingDate;
	}

	private void setProcessingdate(String processingDate) {
		this.processingDate = processingDate;
	}

	private Map<String, ComponentStatus> getComponentStatusMap() {
		return componentStatusMap;
	}

	private void setComponentStatusMap(Map<String, ComponentStatus> componentStatusMap) {
		this.componentStatusMap = componentStatusMap;
	}

	private String getComponentStatusValue(String statusKey){
		return getComponentStatusMap().get(statusKey).getStatusValue();
	}

	private String getLastRunTimeStamp() {
		return lastRunTimeStamp;
	}

	private void setLastRunTimeStamp(String lastRunTimeStamp) {
		this.lastRunTimeStamp = lastRunTimeStamp;
	}

	private String getRunUptoTimeStamp() {
		return runUptoTimeStamp;
	}

	private void setRunUptoTimeStamp(String runToTimeStamp) {
		this.runUptoTimeStamp = runToTimeStamp;
	}

	public boolean isRunningOnSameDay() {
		return runningOnSameDay;
	}

	public void setRunningOnSameDay(boolean runningOnSameDay) {
		this.runningOnSameDay = runningOnSameDay;
	}

	private static String getStackTraceAsString(Exception exception){

		StringBuilder exceptionStr = new StringBuilder();

		for(StackTraceElement element : exception.getStackTrace()){
			exceptionStr.append(element);
			exceptionStr.append(NEW_LINE);
		}
		return exceptionStr.toString();
	}

	private String getSite() {
		return site;
	}

	private void setSite(String site) {
		this.site = site;
	}

	private List<Object[]> getPartNames(String partGroup) {
		List<Object[]> parts =  ServiceFactory.getDao(ExtRequiredPartSpecDao.class).findAllActiveByPartGroup(partGroup);
		return parts;
	}
	
	private static String weekOfTheYear(String productionDate) throws ParseException{

		Date installedDate = weekOfTheYearFormat.parse(productionDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(installedDate);
		return calendar.get(Calendar.WEEK_OF_YEAR)+ "/" + calendar.get(Calendar.YEAR);
	}
}