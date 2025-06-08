package com.honda.galc.service.datacollection.work;

import java.util.Map;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.HeadlessDCInfoCode;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
/**
 * 
 * <h3>CollectionWork</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CollectionWork description </p>
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
 * <TD>Mar 10, 2014</TD>
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
 * @since Mar 10, 2014
 */
public abstract class CollectorWork {
	private String name;
	HeadlessDataCollectionContext context;
	ProductDataCollectorBase collector;
	
	private long start;
	CollectorWork next;
	
	abstract void doWork() throws Exception;
	
	public void execute() throws Exception{
		start = System.currentTimeMillis();	
		
		doWork();
		
		if(getProperty().isLogPerformance())
			getLogger().info("performance - ", this.getClass().getSimpleName() + (start - System.currentTimeMillis()) );

		if(next !=  null) next.execute();
			
	}
	
	public CollectorWork(HeadlessDataCollectionContext context, ProductDataCollectorBase collector) {
		super();
		this.context = context;
		this.collector = collector;
		this.name = this.getClass().getSimpleName();
	}

	public Logger getLogger() {
		return context.getLogger();
	}
	
	
	public void prepareInfoCode(HeadlessDCInfoCode infoCode) {
		context.prepareInfoCode(infoCode);
	}
	

	protected void checkContext(Map<Object, Object> context) {
		if(context.containsKey(TagNames.EXCEPTION.name()))
			throw new TaskException((String)context.get(TagNames.EXCEPTION.name()));

		if(context.containsKey(TagNames.MESSAGE.name()))
			getLogger().info((String)context.get(TagNames.MESSAGE.name()));
	}


	protected HeadLessPropertyBean getProperty() {
		
		return context.getProperty();
	}

	public CollectorWork getNext() {
		return next;
	}
	
	public void setNext(CollectorWork work) {
		this.next = work;
		
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String  name){
		this.name = name;
	}
	
	
}
