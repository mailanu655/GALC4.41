package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.property.FtpClientPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.FtpClientHelper;
import com.honda.galc.util.OIFConstants;
import com.jcraft.jsch.ChannelSftp;
/**
 * 
 * <h3>VerstSubAssemblyFtpTask.java</h3> <h3>Class description</h3> <h4>Description</h4>
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
 * <TD>KM</TD>
 * <TD>Dec 19, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 *
 * </TABLE>
 */

public class VerstSubAssemblyFtpTask extends OifTask<Object> implements IEventTaskExecutable {
	private PreProductionLotDao preProductionLotDao = null;
	FtpClientPropertyBean pBean = null;
	String exportFileName = "";
	String exportLocalPath = "";
	String exportRemotePath = "";
	String fLocal = "";
	String fRemote = "";
	int noOfRows;
	String ftpType = "";
	Timestamp startTs = null, endTs = null;
	protected ComponentPropertyDao propertyDao;	
    private String textFileHeader = "PPID,PRODUCTION SEQUENCE,KD LOT#,MODEL,LOT SIZE\n";
	public static final String LAST_PROCESS_TIMESTAMP = "LAST_PROCESS_TIMESTAMP";

	public VerstSubAssemblyFtpTask(String name) {
		super(name);
	}
	
	   /**
	 * Initialize runtime parameters.
	 * @return true if the initialization is successful. Otherwise, false
	 */
	protected void initialize() {
		super.initialize();
		this.preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		pBean = PropertyService.getPropertyBean(FtpClientPropertyBean.class, componentId);
		this.propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		
		logger.info("Loading Properties for ...");
		String fileNamePrefix = pBean.getFilenamePrefix();
		String plantCode = pBean.getPlantCode();
		noOfRows = getPropertyInt("NUMBER_OF_ROWS",5);
		ftpType = pBean.getFtpType();
		Calendar now = GregorianCalendar.getInstance();
		if(endTs == null)  {
			endTs = new Timestamp(now.getTimeInMillis());
		}
		String strTs = new SimpleDateFormat("yyyyMMddHHmmss").format(now.getTime());
		exportFileName = (new StringBuilder())
				.append(plantCode).append("_")
				.append(strTs)
				.append(fileNamePrefix)
				.append(".csv").toString();
		exportLocalPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND);
		exportRemotePath = pBean.getFtpPath();
		StringBuilder sb = new StringBuilder();
		fLocal = sb.append(exportLocalPath).append(File.separator).append(exportFileName).toString();
		sb.setLength(0);
		fRemote = sb.append(exportRemotePath).append(exportFileName).toString();
		sb.setLength(0);
	}
 
	public void execute(Object[] args) {
		try {
			initialize();
			processData();
			
			if(StringUtils.endsWithIgnoreCase(ftpType, "SFTP")){
				sftp(fLocal, fRemote);
			}else{
				ftp(fLocal, fRemote);
			}
			updateLastProcessTimestamp(endTs);
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

	private void processData() {
		BufferedWriter ipuWriter = null;
		try {			
			ipuWriter = new BufferedWriter(new FileWriter(fLocal));
			ipuWriter.write(textFileHeader);
			List<String> ppIdList = PropertyService.getPropertyList(componentId,"PROCESS_POINT_LIST");
			for (String ppid : ppIdList){
				ProcessPoint processPoint = getDao(ProcessPointDao.class).findById(ppid);
				String processLocation = PropertyService.getProperty(processPoint.getProcessPointId(),TagNames.PROCESS_LOCATION.name());
				PreProductionLot firstLot = preProductionLotDao.findFirstAvailableLot(processLocation);
				List<String> preProductionList =  preProductionLotDao.findUpcomingLotsForProcessPointCsv(firstLot.getProductionLot(),noOfRows,ppid);
				for(String rec : preProductionList)  {
					ipuWriter.write(rec);
					ipuWriter.newLine();
				}
			}			
		} catch (IOException e) {			
			logger.error(e, "Error creating IPU QA ftp file");
		}
		finally  {
			try {
				ipuWriter.close();
			} catch (IOException e) {
				logger.error(e, "Error closing IPU QA ftp file");
			}
		}
	}

	/**
	 * Copy file via SFTP.<br>
	 */
	private void sftp(String localFile, String remoteFile) {
		logger.info("ftp Inventory File");
		FtpClientHelper ftpHelper = new FtpClientHelper(pBean.getFtpServer(), pBean.getFtpPort(),
				pBean.getFtpUser(), pBean.getFtpPassword(), logger);
		ChannelSftp sftp = ftpHelper.getSftpClient();
		try{ 			    
			    InputStream inputStream = null;
				inputStream = new FileInputStream(localFile);
				
				sftp.put(inputStream, remoteFile);
		}catch(Exception e)
		{
			logger.error("Connection failed");
		}
		finally{
			ftpHelper.closeSftp();
		}		
		logger.info("ftp Successful");
	}
	
	/**
	 * Copy file via FTP.<br>
	 */
	private void ftp(String localFile, String remoteFile) {
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
		}

		try {
			ftpClient.storeFile(remoteFile, inputStream);
		} catch (IOException e) {
			logger.error(e);
			errorsCollector.error(e.getMessage());
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			logger.error(e);
			errorsCollector.error(e.getMessage());
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
