package com.honda.galc.service.datacollection.work;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
import com.honda.galc.service.datacollection.task.CollectorTask;
import com.honda.galc.service.datacollection.task.IHlServiceTask;
import com.honda.galc.service.utils.ServiceUtil;
/**
 * 
 * <h3>CustomTaskWork</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CustomTaskWork description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 12, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 12, 2014
 */
public class CustomTask extends CollectorWork{
	
	List<CollectorTask> taskList = new ArrayList<CollectorTask>();
	
	public CustomTask(HeadlessDataCollectionContext context, ProductDataCollectorBase collector) {
		super(context, collector);
	}

	@Override
	void doWork() throws Exception {
		if(StringUtils.isEmpty(getProperty().getTask())){
			getLogger().info("No data collector task.");
			return;
		} else {
			for(String taskName: getProperty().getTask().split(Delimiter.COMMA))
			taskList.add((CollectorTask)createTask(taskName));
		}
		
		if(getProperty().isAutoRunTask())
			for(CollectorTask task : taskList){
				task.execute();
				if(!task.isContinue()) break;
			}
	}

	private IHlServiceTask createTask(String taskName) {
		return ServiceUtil.createTask(taskName, context, context.getProcessPointId());
	}

	public List<CollectorTask> getTasks(){
		return taskList;
	}
}
