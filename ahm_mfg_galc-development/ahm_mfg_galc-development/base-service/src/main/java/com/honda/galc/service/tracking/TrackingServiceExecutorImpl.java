package com.honda.galc.service.tracking;

import java.sql.Timestamp;

import org.springframework.core.task.TaskExecutor;

import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
/**
 * 
 * <h3>TrackingServiceExecutorImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TrackingServiceExecutorImpl description </p>
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
 * Sep 8, 2010
 *
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class TrackingServiceExecutorImpl implements TrackingServiceExecutor{
	private TaskExecutor taskExecutor;
	
	public TrackingServiceExecutorImpl(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	public <T extends BaseProduct> void invoke(ProductTrackerBase<T> tracker,
			T product, ProcessPoint processPoint, DailyDepartmentSchedule schedule, 
			Timestamp actualTimestamp, String associateNo, String approverNo, String deviceId) {
		TrackingServiceWorker<T> worker = new TrackingServiceWorker<T> (tracker, product, processPoint, schedule,
				actualTimestamp, associateNo, approverNo, deviceId);
		taskExecutor.execute(worker);		
	}
	
}
