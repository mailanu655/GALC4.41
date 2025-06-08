package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.property.OifTaskPropertyBean;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;

/**
 * 
 * <h3>PrunningAbstractTask Class description</h3>
 * <p> PrunningAbstractTask description </p>
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
 * @author Jeffray Huang<br>
 * May 31, 2012
 *
 *
 */
public abstract class PrunningAbstractTask extends OifAbstractTask implements IEventTaskExecutable{

	protected Date startProductionDate;
	protected OifTaskPropertyBean propertyBean;
	
	protected Map<String,Integer> allCounts = new LinkedHashMap<String,Integer>();
	
	
	public PrunningAbstractTask(String name) {
		super(name);
	
	}
	
	public void execute(Object[] args) {
		
		refreshProperties();
		
		logger.info("start OIF Prunning Product Task " + getName());
	
		logger.info("Configuration: BUSINESS_DAYS_TO_KEEP = " + getBusinessDaysToKeep() + ", BATCH_SIZE = " + getBatchSize() + 
				",LOT_BATCH_SIZE = " + getLotBatchSize());
		
		checkConfiguration();
		
		startProductionDate = findStartProductionDate();
		
		logger.info("try to prune production data earlier than " + startProductionDate);
		
		allCounts = new HashMap<String,Integer>();

		Set<String> allTables = getAllPrunningTables();
		
		if(showTableCounts()){
			logger.info("get counts of all prunning tables before prunning");
			getAllCounts(allTables);
		}
		
		try{
			performPrunning();
		}catch(Exception ex) {
			logger.error(ex, "Exception occurs - " + ex.getMessage());
		}
		
		if(showTableCounts()){
			logger.info("get counts of all prunning tables after prunning");
			getAllCounts(allTables);
		}
		
		for(Map.Entry<String, Integer> entry : allCounts.entrySet()) 
			logger.info("Pruned totally " + entry.getValue() + " rows in table " + entry.getKey());

		logger.info("finished OIF Prunning Product Task " + getName());

	}
	
	public int getBusinessDaysToKeep() {
		return getPropertyInt("BUSINESS_DAYS_TO_KEEP", 100);
	}
	
	public int getBatchSize() {
		return getPropertyInt("BATCH_SIZE", 2);
	}
	
	public int getLotBatchSize() {
		return getPropertyInt("LOT_BATCH_SIZE", 2);
	}
	
	public String getProcessLocation() {
		return getProperty("PROCESS_LOCATION","");
	}
	
	public String getShippingPPID() {
		return getProperty("SHIPPING_PROCESS_POINT_ID","");
	}
	
	public boolean showTableCounts() {
		return getPropertyBoolean("SHOW_TABLE_COUNTS",false);
	}
	
	public Date getStartProductionDate() {
		return startProductionDate;
	}

	public void setStartProductionDate(Date startProductionDate) {
		this.startProductionDate = startProductionDate;
	}
	
	protected void getAllCounts(Set<String> tables) {
		for(String tableName : tables) {
			int count = getService(GenericDaoService.class).count(tableName);
			logger.info("Totally " + count + " rows in table " + tableName);
		}
	}
	
	protected Date findStartProductionDate() {
		Date date = new Date(System.currentTimeMillis());
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -getBusinessDaysToKeep());
		date.setTime(c.getTime().getTime());
		return date;
	}
	
	protected void putCount(String tableName,int count) {
		int currentCount = allCounts.containsKey(tableName) ? allCounts.get(tableName) : 0;
		allCounts.put(tableName, count + currentCount);
	}
	
	public List<ProductionLot> getShippedProductionLots(){
		return getService(GenericDaoService.class).
		findFinishedSubProductProdLots(getProcessLocation(), getShippingPPID(), getStartProductionDate());
	}
	
	
	protected OifTaskPropertyBean getPropertyBean() {
		if(propertyBean == null)
			propertyBean = PropertyService.getPropertyBean(OifTaskPropertyBean.class, componentId);

		return propertyBean;
	}

	protected abstract Set<String> getAllPrunningTables();
	
	protected abstract void performPrunning();
	
	protected abstract void checkConfiguration();
	
}
