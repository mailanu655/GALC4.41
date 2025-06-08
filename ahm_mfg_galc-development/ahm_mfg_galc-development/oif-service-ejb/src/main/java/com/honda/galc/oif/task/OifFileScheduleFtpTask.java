package com.honda.galc.oif.task;

import java.io.BufferedReader;

import com.honda.galc.data.TagNames;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.FtpClientHelper;

/**
 * 
 * <h3>MbpnOifScheduleTask</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnOifScheduleTask description </p>
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
 * @author Paul Chou
 * Jun. 9, 2014
 *
 */
public class OifFileScheduleFtpTask extends OifFileScheduleBase implements IEventTaskExecutable{

	FtpClientHelper ftp;
	
	public OifFileScheduleFtpTask(String name) {
		super(name);
	}

	public void execute(Object[] args) {
		
		try {
			
			ftp = getFtpClientHelper();
			
			processSchedule();
			
			logger.info("Oif schedule completed successful " + componentId);
		} catch (Exception e) {
			logger.warn(e, " Exception to process schedule " + componentId);
		}finally {
			if(ftp != null) 
				ftp.disconnect();
		}
		
	}

	private FtpClientHelper getFtpClientHelper() throws Exception {
		
		ftp = new FtpClientHelper(getPropertyBean().getFtpServer(), getPropertyBean().getFtpPort(),
		getPropertyBean().getFtpUser(), getPropertyBean().getFtpPassword(), logger);
		
		
		if(ftp == null)
			throw new Exception("Failed to get Ftp Client");
		
		return ftp;
		
	}

	@Override
	protected BufferedReader getBufferedReader(String filename) {
		return ftp.getBufferedReader(getProperty(TagNames.SCHEDULE_DIR.name()) + filename);
	}

}
