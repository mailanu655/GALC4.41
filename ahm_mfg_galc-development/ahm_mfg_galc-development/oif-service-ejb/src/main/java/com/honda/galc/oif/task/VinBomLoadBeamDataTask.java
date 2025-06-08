package com.honda.galc.oif.task;

import java.text.ParseException;
import java.util.ArrayList;

import java.util.List;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.enumtype.UploadStatusType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.lcvinbom.VinBomService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
/**
 * 
 * <h3>VinBomLoadBeamDataTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> VinBomLoadBeamDataTask.java description </p>
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
 * <TD>Ambica Gawarla</TD>
 * <TD>Mar 12, 2021</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */
public class VinBomLoadBeamDataTask extends OifTask<Object> implements IEventTaskExecutable {

	
	public static final String LOAD_STATUS = "LOAD_STATUS";
	private static final String PLANT_CODE = "PLANT_CODE";
	private static final String DEPT_CODE = "DEPT_CODE";
	//private int failedRecords=0;
	
	
	
	// File names received from GPCS(MQ).
	protected String[] aReceivedFileList;

	public VinBomLoadBeamDataTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		try {
			processBeamData();
		} catch (TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}

	/**
	 * This method is used for receive files/files from MQ and process it/them for current line.
	 * @throws ParseException
	 */
	private void processBeamData() throws Exception{
		
		long startTime = System.currentTimeMillis();
		logger.info("start to process VinBomLoadBeamDataTask ");

		// Refresh properties

		refreshProperties();
	
		String plantCode = getProperty(PLANT_CODE);
		String deptCode = getProperty(DEPT_CODE);
		//int totalCount = 0;
		try {
							
			VinBomService vinBomService = ServiceFactory.getService(VinBomService.class);
			vinBomService.updateBeamPartData(plantCode,deptCode);	
				
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			updateComponentStatus(LOAD_STATUS, UploadStatusType.PASS.name());
			//setIncomingJobCount(totalCount, failedRecords,aReceivedFileList );
			
			logger.info("VinBomLoadBeamDataTask  complete. inserted and updated records in "+totalTime+" milliseconds.");
			
			
		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			updateComponentStatus(LOAD_STATUS, UploadStatusType.FAIL.name());
			//setIncomingJobCount(0,totalCount , aReceivedFileList);
			logger.error("VinBomLoadBeamDataTask  failed. inserted and updated records in "+totalTime+" milliseconds.");
			throw e;
		}finally{
			errorsCollector.sendEmail();
		}
		
	}

		
	
}