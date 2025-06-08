package com.honda.galc.oif.task;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * <h3>AnnualCalendarTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AnnualCalendarTask.java description </p>
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
 * <TD>Feb 11, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Larry Karpov
 * @created Sep 09, 2014
 */
public class VerifyProductionScheduleTime 
	implements IEventTaskExecutable {
	
	protected String siteLineId;
	protected String siteName;
	protected OifErrorsCollector errorsCollector;
	protected Logger logger;
	protected String componentId;
	
	public VerifyProductionScheduleTime(String name) {
		componentId = name;
		errorsCollector = new OifErrorsCollector(name);
		logger = Logger.getLogger(name);
	}
	
	public void execute(Object[] args) {
		String strProductionDate = PropertyService.getProperty(componentId, "PRODUCTION_DATE");
		Date productionDate = new java.sql.Date(System.currentTimeMillis());
		if(strProductionDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(
				PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, "PROD_DATE_FORMAT"));
			try {
				java.util.Date utilDate = sdf.parse(strProductionDate);
				productionDate = new java.sql.Date(utilDate.getTime());
			} catch (ParseException e) {
				logger.error(e, "Cannot parse production date value, defaulting to current date.");
				errorsCollector.error(e, "Cannot parse custom production date value, defaulting to current date.");
				productionDate = new java.sql.Date(System.currentTimeMillis());
			}
		}
		List<String> timeGaps = ServiceFactory.getDao(DailyDepartmentScheduleDao.class).findTimeGaps(productionDate);
		if(timeGaps != null && timeGaps.size() > 0) {
		    StringBuffer errorMsg = new StringBuffer();
            errorMsg.append("The following production schedule(s) may be out of sequence. Please check GAL226TBX to verify. \n");
            for(String timeGap : timeGaps) {
                errorMsg.append(timeGap).append("\n");
            }
			errorsCollector.error(errorMsg.toString());
		}
		errorsCollector.sendEmail();
	}
	
}
