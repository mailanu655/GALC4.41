package com.honda.galc.oif.task;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.oif.property.OifTaskPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>OifFileScheduleBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> OifFileScheduleBase description </p>
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
public abstract class OifFileScheduleBase extends OifAbstractTask{
	public static final String FILE_LIST = "FILE_LIST";
	protected OifTaskPropertyBean oifPropertyBean;
	protected abstract BufferedReader getBufferedReader(String fileName);
	
	public OifFileScheduleBase(String name) {
		super(name);
	}

	protected void processSchedule() {
		String[] fileNames = getFileList();
		for(String filename : fileNames){
			logger.info("Start to process file:", filename);
			processFile(StringUtils.trimToEmpty(filename));
		}
		
	}
	
	private void processFile(String filename) {
		
		if(StringUtils.isEmpty(filename)){
			logger.error("Invalid file name:" + filename);
			return;
		}
		
		BufferedReader reader = null;
		try {
			
			reader = getBufferedReader(filename);
			new OifFileScheduleHandler(getPropertyBean(), filename, reader, logger).process();
		
		} catch (SystemException se){
			logger.warn(se.getMessage() + filename);
		} catch (IOException e) {
			logger.error(e, " Exception to read file " + filename);
		} catch (Exception e) {
			logger.error(e, e.getMessage() + filename);
		} finally{
			if(reader != null) {
				try {
					reader.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
	
	private String[] getFileList() {
		String fileListStr = PropertyService.getProperty(componentId, FILE_LIST);
		
		if(!StringUtils.isEmpty(fileListStr))
			return fileListStr.split(Delimiter.COMMA);
		else
			return new String[]{};
	}
	

	protected OifTaskPropertyBean getPropertyBean() {
		if(oifPropertyBean == null)
			oifPropertyBean = PropertyService.getPropertyBean(OifTaskPropertyBean.class, componentId);

		return oifPropertyBean;
	}

	public Logger getLogger() {
		return logger;
	}
	
}
