package com.honda.galc.oif.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.IpuDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.oif.property.IpuFtpBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.FtpClientHelper;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * <h3>IpuFtpTask.java</h3> <h3>Class description</h3> <h4>Description</h4>
 * <P>
 * <B>Purpose:
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
 * <TD>LK</TD>
 * <TD>Jan 06, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 *
 * </TABLE>
 * 
 * @version 0.1
 * @author Larry Karpov
 * @created Jan 07, 2015
 */
public class IpuFtpTask extends OifTask<Object> implements IEventTaskExecutable {

	private IpuDao ipuDao = null;
	IpuFtpBean pBean = null;
	String exportFileNameQA = "";
	String exportFileNameLET = "";
	String exportLocalPath = "";
	String exportRemotePath = "";
	String fQALocal = "";
	String fQARemote = "";
	String fLETLocal = "";
	String fLETRemote = "";
	Timestamp startTs = null, endTs = null;
	int noOfIPURecords= 0, noOfLETRecords=0;
	protected ComponentPropertyDao propertyDao;	
    private String textFileHeader = "PRODUCT_ID,IPU_SN,BATTERY,START_TIMESTAMP,START_UTC,TOTAL_STATUS,CELL_VOLTAGE_STATUS,VBC1,VBC2,VBC3,VBC4,VBC5,VBC6,VBC7,VBC8,VBC9,VBC10,VBC11,VBC12,VBC13,VBC14,VBC15,VBC16,VBC17,VBC18,VBC19,VBC20,VBC21,VBC22,VBC23,VBC24,VBC25,VBC26,VBC27,VBC28,VBC29,VBC30,VBC31,VBC32,VBC33,VBC34,VBC35,VBC36,VBC37,VBC38,VBC39,VBC40,VBC41,VBC42,VBC43,VBC44,VBC45,VBC46,VBC47,VBC48,VBC49,VBC50,VBC51,VBC52,VBC53,VBC54,VBC55,VBC56,VBC57,VBC58,VBC59,VBC60,VBC61,VBC62,VBC63,VBC64,VBC65,VBC66,VBC67,VBC68,VBC69,VBC70,VBC71,VBC72\n";
	public static final String LAST_PROCESS_TIMESTAMP = "LAST_PROCESS_TIMESTAMP";

	public IpuFtpTask(String name) {
		super(name);
	}
	
	   /**
	 * Initialize runtime parameters.
	 * @return true if the initialization is successful. Otherwise, false
	 */
	protected void initialize() {
		super.initialize();
		this.ipuDao = ServiceFactory.getDao(IpuDao.class);
		pBean = PropertyService.getPropertyBean(IpuFtpBean.class, componentId);
		this.propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		
		logger.info("Loading Properties for IPU QA...");
		String startTs_property = pBean.getLastProcessTimestamp();//getProperty(LAST_PROCESS_TIMESTAMP);
		String endTs_property = pBean.getEndTimestamp();//getProperty(END_TIMESTAMP);
		String ipuQAPrefix = pBean.getIpuqaFilenamePrefix();//getProperty(IPUQA_FILENAME_PREFIX, "_IPUQA");
		String ipuLETPrefix = pBean.getIpuletFilenamePrefix();//getProperty(IPULET_FILENAME_PREFIX, "_IPULET");
		String plantCode = pBean.getPlantCode();//getProperty(PLANT_CODE, "");
		startTs = getTimestamp(startTs_property);  //LAST_PROCESS_TIMESTAMP
		endTs = getTimestamp(endTs_property);
		Calendar now = GregorianCalendar.getInstance();
		if(endTs == null)  {
			endTs = new Timestamp(now.getTimeInMillis());
		}
		if(startTs == null)  {
			Calendar aWeekAgo = GregorianCalendar.getInstance();
			aWeekAgo.set(Calendar.DATE, now.get(Calendar.DATE) - 7);
			startTs = new Timestamp(aWeekAgo.getTimeInMillis());
		}
		String strTs = new SimpleDateFormat("yyyyMMddHHmmss").format(now.getTime());
		exportFileNameQA = (new StringBuilder())
				.append(plantCode).append("_")
				.append(strTs)
				.append(ipuQAPrefix)
				.append(".csv").toString();
		exportFileNameLET = (new StringBuilder())
				.append(plantCode).append("_")
				.append(strTs)
				.append(ipuLETPrefix)
				.append(".csv").toString();
		exportLocalPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		exportRemotePath = pBean.getFtpPath();
		StringBuilder sb = new StringBuilder();
		fQALocal = sb.append(exportLocalPath).append(File.separator).append(exportFileNameQA).toString();
		sb.setLength(0);
		fQARemote = sb.append(exportRemotePath).append(exportFileNameQA).toString();
		sb.setLength(0);
		fLETLocal = sb.append(exportLocalPath).append(File.separator).append(exportFileNameLET).toString();
		sb.setLength(0);
		fLETRemote = sb.append(exportRemotePath).append(exportFileNameLET).toString();
	}
 
	public void execute(Object[] args) {
		try {
			initialize();
			String[] batteryPartNames = pBean.getBatteryPartName();
			List<String> batteryPartNameslist = new ArrayList<String>();
			for(String batteryPartName:batteryPartNames){
				if(batteryPartName != null && !StringUtils.isEmpty(batteryPartName)){
					batteryPartNameslist.add(batteryPartName);
				}
			}
			 
			String[] ipuPartNames = pBean.getIpuPartName();
			List<String> ipuPartNameslist = new ArrayList<String>();
			for(String ipuPartName:ipuPartNames){
				if(ipuPartName != null && !StringUtils.isEmpty(ipuPartName)){
					ipuPartNameslist.add(ipuPartName);
				}
			}
			List<String> operationNameBatteryPartNames = ServiceFactory.getDao(MCOperationRevisionDao.class).findOperationsNameByPartNamelist(batteryPartNameslist);
			List<String> operationNameIpuPartNames  = ServiceFactory.getDao(MCOperationRevisionDao.class).findOperationsNameByPartNamelist(ipuPartNameslist);
			
			processIpuQA(startTs, endTs, pBean.getProcessPoint(), operationNameIpuPartNames, operationNameBatteryPartNames);
			ftp(fQALocal, fQARemote);
			if(pBean.isSendLet())  {  //if LET results are also to be sent
				processIpuLET(startTs, endTs, pBean.getProcessPoint(), operationNameIpuPartNames, operationNameBatteryPartNames);
				ftp(fLETLocal, fLETRemote);
			}
			updateLastProcessTimestamp(endTs);
			setOutgoingJobStatus(noOfIPURecords+noOfLETRecords, fQARemote);
			//processIpuWeeklyData();
		} catch (TaskException e) {
			logger.error(e.getMessage());
			errorsCollector.error(e.getMessage());
		} catch (Exception e) {
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}

	private void processIpuQA(Timestamp startTs, Timestamp endTs, String pp, List<String> ipuPartNames, List<String>  batteryPartNames) {
		BufferedWriter ipuWriter = null;
		List<String> ipuData = null;
		try {
			ipuData = ipuDao.getIpuQaDataCsv(startTs, endTs, ipuPartNames, batteryPartNames, pBean.isVinRequired());
			ipuWriter = new BufferedWriter(new FileWriter(fQALocal));
			if(ipuData != null && ipuData.size() > 0)
			{
				noOfIPURecords = ipuData.size();
				ipuWriter.write(textFileHeader);
				for(String rec : ipuData)  {
					ipuWriter.write(rec);
					ipuWriter.newLine();
				}		
			}
		} catch (IOException e) {			
			logger.error(e, "Error creating IPU QA ftp file");
			errorsCollector.error("Error creating IPU QA ftp file");
			this.setOutgoingJobStatusAndFailedCount(ipuData.size(), OifRunStatus.IPU_FTP_ERROR_CREATING_FILE,null);
		}
		finally  {
			try {
				ipuWriter.close();
			} catch (IOException e) {
				logger.error(e, "Error closing IPU QA ftp file");
				errorsCollector.error("Error closing IPU QA ftp file");
				setJobStatus(OifRunStatus.IPU_FTP_ERROR_CLOSING_FILE);
			}
		}
	}
	
	

	private void processIpuLET(Timestamp startTs, Timestamp endTs, String pp, List<String> ipuPartNames, List<String> batteryPartNamse) {
		BufferedWriter ipuWriter = null;
		try {
			List<String> ipuData = ipuDao.getIpuLetDataCsv(startTs, endTs, pp, ipuPartNames, batteryPartNamse);
			ipuWriter = new BufferedWriter(new FileWriter(fLETLocal));
			ipuWriter.write(textFileHeader);
			if(ipuData !=null && ipuData.size() >0)
			{
				noOfLETRecords=ipuData.size();
				for(String rec : ipuData)  {
					ipuWriter.write(rec);
					ipuWriter.newLine();
				}
			}
		} catch (IOException e) {			
			logger.error(e, "Error creating IPU QA LET file");
			errorsCollector.error("Error creating IPU QA LET file");
			setOutgoingJobStatusAndFailedCount(noOfLETRecords, OifRunStatus.IPU_FTP_ERROR_CREATING_FILE, null);
		}
		finally  {
			try {
				ipuWriter.close();
			} catch (IOException e) {
				logger.error(e, "Error closing IPU QA LET file");
				errorsCollector.error("Error closing IPU QA LET file");
				setJobStatus(OifRunStatus.IPU_FTP_ERROR_CLOSING_FILE);
			}
		}
	}


	/**
	 * Copy file via FTP.<br>
	 */
	private void ftp(String localFile, String remoteFile) throws Exception {
		logger.info("ftp Inventory File");
		FtpClientHelper ftpHelper = new FtpClientHelper(pBean.getFtpServer(), pBean.getFtpPort(),
				pBean.getFtpUser(), pBean.getFtpPassword(), logger);
		FTPClient ftpClient = ftpHelper.getClient();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(localFile);
		} catch (FileNotFoundException e) {
			logger.error(e);
			errorsCollector.error(e.getMessage());
			setOutgoingJobStatusAndFailedCount(noOfIPURecords+noOfLETRecords,OifRunStatus.FTP_FILE_NOT_FOUND,null);
			throw e;
		}

		try {
			ftpClient.storeFile(remoteFile, inputStream);
		} catch (IOException e) {
			logger.error(e);
			errorsCollector.error(e.getMessage());
			setOutgoingJobStatusAndFailedCount(noOfIPURecords+noOfLETRecords,OifRunStatus.FILE_TRANSFER_ERROR,null);
			throw e;
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			logger.error(e);
			errorsCollector.error(e.getMessage());
			setJobStatus(OifRunStatus.FAILED_TO_CLOSE_INPUT_STREAM);
			throw e;
		}
	}

	protected Timestamp getTimestamp(String tsVal) {
		Timestamp ts = null;
		if (!StringUtils.isBlank(tsVal)) {
			ts = Timestamp.valueOf(StringUtils.trim(tsVal));
		}
		return ts;
	}

	protected void updateLastProcessTimestamp(Timestamp currentTimestamp) {
		ComponentPropertyId id = new ComponentPropertyId(getComponentId(), LAST_PROCESS_TIMESTAMP);
		ComponentProperty cProp = propertyDao.findByKey(id);
		if(cProp == null)  {
			cProp = new ComponentProperty(getComponentId(), LAST_PROCESS_TIMESTAMP, currentTimestamp.toString());			
		} else {
			cProp.setPropertyValue(currentTimestamp.toString());
		}
		propertyDao.save(cProp);
	}

	/**
	 * @return the propertyDao
	 */
	public ComponentPropertyDao getPropertyDao() {
		return propertyDao;
	}

	/**
	 * @param propertyDao
	 *            the propertyDao to set
	 */
	public void setPropertyDao(ComponentPropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}
}
