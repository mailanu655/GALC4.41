package com.honda.galc.service.broadcast;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.FtpClientPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.FtpClientHelper;
import com.jcraft.jsch.ChannelSftp;


/**
 * 
 * 
 * <h3>FtpBroadcast Class description</h3>
 * <p> FtpBroadcast description </p>
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
 * @author is08925<br>
 * Nov 13, 2017
 *
 *
 */

public class FtpBroadcast extends AbstractBroadcast {

	public FtpBroadcast(BroadcastDestination destination, String processPointId, DataContainer dc) {
		super(destination, processPointId, dc);
	}

	@Override
	public DataContainer send(DataContainer dc) {
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(destination.getDestinationId());
		if (device == null) {
			logger.error("Could not find the device id " + destination.getDestinationId());
			return dc;
		}

		FtpClientPropertyBean propertyBean = PropertyService.getPropertyBean(
				FtpClientPropertyBean.class, device.getClientId());

		ListPrintDataAssembler dataAssembler = new ListPrintDataAssembler();
		dc.put(DataContainerTag.DELIMITER, propertyBean.getDelimiter());
		byte[] data = dataAssembler.assembleData(dc);

		logger.info("sending data - " + new String(data));

		FtpClientHelper ftpClient = new FtpClientHelper(
				device.getEifIpAddress(), device.getEifPort(),
				propertyBean.getFtpUser(), propertyBean.getFtpPassword(),
				logger);
			
		//only run this if property is set otherwise run the default FTP
		if(StringUtils.endsWithIgnoreCase(propertyBean.getFtpType(), "SFTP")) {
			ChannelSftp sftp = ftpClient.getSftpClient();
			try {
				if (sftp != null){
					sftp.put(new ByteArrayInputStream(data), propertyBean.getOutputFile());
				}
			} catch (Exception e) {
				logger.error("Connection failed");
			} finally {
				ftpClient.closeSftp();
			}
		} else {
			
			FTPClient ftp = null;
			
			try {
				ftp = ftpClient.getClient();
				
				if(ftp == null) return null;
				
				if(!StringUtils.isEmpty(StringUtils.trimToEmpty(propertyBean.getFtpPath()))){
						ftp.changeWorkingDirectory(propertyBean.getFtpPath());
						logger.info("set the ftp file path : " + propertyBean.getFtpPath());
				}
			
				ftp.appendFile(propertyBean.getOutputFile(), new ByteArrayInputStream(data));
				String replyString = ftp.getReplyString();
	            int replyCode = ftp.getReplyCode();
	            if (FTPReply.isNegativeTransient(replyCode) || FTPReply.isNegativePermanent(replyCode)) {
	                logger.error("Ftp appendFile failed - replyString:" + replyString);
	            } else {
	                logger.info("Ftp appendFile succeed");
	            }
			} catch (IOException e) {
				logger.error(e, "Could not send data due to " + e.getMessage());
			}finally {
				if(ftp != null && ftp.isConnected())
					try {
						ftp.disconnect();
					} catch (IOException e) {
						logger.error(e, "Could not close the ftp connection " + e.getMessage());
					}
			}
		}
		return null;

	}

}
