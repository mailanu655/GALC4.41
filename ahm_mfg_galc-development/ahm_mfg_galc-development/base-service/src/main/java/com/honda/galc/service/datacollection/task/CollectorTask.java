package com.honda.galc.service.datacollection.task;

import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductResultUtil;
/**
 * 
 * <h3>BaseTask</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BaseTask description </p>
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
 * <TD>Apr 25, 2012</TD>
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
 * @since Apr 25, 2012
 */
public abstract class CollectorTask implements IHlServiceTask{
	
	protected String processPointId;
	protected HeadlessDataCollectionContext context;
	private Logger logger;
	
	public abstract void execute();
	
	public CollectorTask(HeadlessDataCollectionContext context, String processPointId) {
		this.context = context;
		this.processPointId = processPointId;
	}
	
	
	public Logger getLogger(){
		if(logger == null)
			logger = Logger.getLogger(PropertyService.getLoggerName(processPointId));
		
		return logger;
	}
	
    public void logContext(){
    	if(context.containsKey(TagNames.MESSAGE.name()))
    		getLogger().info((String)context.get(TagNames.MESSAGE.name()));
    	
    	if(context.containsKey(TagNames.EXCEPTION.name()))
    		getLogger().info((String)context.get(TagNames.EXCEPTION.name()));
    }
    
    public Boolean isContinue(){
    	return context.get(TagNames.CONTINUE.name()) == null || (Boolean)context.get(TagNames.CONTINUE.name());
    }
    
    protected List<InstalledPart> saveAllInstalledParts(List<InstalledPart> installedParts) {
		return ProductResultUtil.saveAll(processPointId, installedParts);
	}
	
}
