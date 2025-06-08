package com.honda.galc.service.datacollection.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.task.CollectorTask;
/**
 * 
 * <h4> Description </h4>
 * <p> print attribute for buck parts unload </p>
 * 
 * @author Zack Chai
 * @since Sept 29, 2014
 */
public class ReadyForAfTask extends CollectorTask {

	DateFormat simpleDateFormat = null;
	
	private final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH.mm.ss";
	
	private final String CURE_TIME_OFFSET = "CureTimeOffset";
	
	public ReadyForAfTask(HeadlessDataCollectionContext context, String processPointId) {
		super(context, processPointId);
	}

	@Override
	public void execute() {
		setAfForReadyToDataContainer();
	}
	
	
	void setAfForReadyToDataContainer(){
		context.put(DataContainerTag.READY_FOR_AF_TIME, getReadyForAFTime(CURE_TIME_OFFSET));
	}
	/**
	 * get the time of Ready For AF
	 * @param attrName
	 * @return date of string include both current time and cure time offset time which configure in build attribute
	 */
	private String getReadyForAFTime(String attrName) {
		String cureTimeOffset = findAttributeValue(context.getProduct().getProductSpecCode(), attrName);
		if(StringUtils.isEmpty(cureTimeOffset)){
			DateFormat df = getSimpleDateFormat(DEFAULT_DATE_FORMAT);
			return df.format(System.currentTimeMillis());
		}
		return calculateReadyForAF(System.currentTimeMillis(), cureTimeOffset);
	}
	
	private String calculateReadyForAF(long currentTime, String cureTimeOffset){
		DateFormat df = getSimpleDateFormat(DEFAULT_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(currentTime);
		cal.add(Calendar.MINUTE, Integer.valueOf(cureTimeOffset));
		return df.format(cal.getTime());
	}
	
	private DateFormat getSimpleDateFormat(String datePattern){
		if(simpleDateFormat != null){
			return simpleDateFormat;
		}
		if(StringUtils.isEmpty(datePattern)){
			datePattern = DEFAULT_DATE_FORMAT;
		}
		simpleDateFormat = new SimpleDateFormat(datePattern);
		return simpleDateFormat;
	}
	
	public BuildAttribute findByKey(BuildAttributeId id) {
		
		List<BuildAttribute> buildAttributes  = getDao().findAllMatchId(ProductSpec.excludeToModelCode(id.getProductSpecCode()) + "%", id.getAttribute());
		if(buildAttributes.isEmpty()) return null;
		else return buildAttributes.get(buildAttributes.size() -1);
	
	}
	
	public BuildAttribute findById(String productSpecCode, String attribute) {
		 
		return findByKey(new BuildAttributeId(attribute,productSpecCode));
	}
	
	public String findAttributeValue(String productSpecCode,String attribute) {
		BuildAttribute item = findById(productSpecCode,attribute);
		return item == null ? "" : item.getAttributeValue();
	}
	
	public BuildAttributeDao getDao() {
		return ServiceFactory.getDao(BuildAttributeDao.class);
	}
	
}
