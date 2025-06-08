/**
 * 
 */
package com.honda.galc.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.FactoryNewsCurrent;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.property.FactoryNewsPropertyBean;
import com.honda.galc.service.FactoryNewsUpdateService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


/**
 * @author Subu Kathiresan
 * @date June 04, 2012
 *
 */
public class FactoryNewsService {
	
	private static final long serialVersionUID = 2610765270038742135L;
	
	public FactoryNewsService() {}
	
	public ArrayList<FactoryNewsCurrent> getUpdate(Map<?, ?> requestParams) {
		String plantName = (String) requestParams.get("plantName");
		if(StringUtils.isBlank(plantName))  plantName  = getPropertyBean().getPlantName();
		return (getPropertyBean().isEnableFactoryNews())?ServiceFactory.getService(FactoryNewsUpdateService.class).getFactoryNews(plantName):new ArrayList<FactoryNewsCurrent>(0);
	}
	
	protected FactoryNewsPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(FactoryNewsPropertyBean.class);
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * Gets the inventory for a line
	 *
	 * @param requestParams the request params
	 * 		The request parameters should have "divisionId", "lineId" or empty array list will return.
	 * @return the inventory
	 */
	public List<InProcessProduct> getInventory(Map<?, ?> requestParams){
		String plantName = getPropertyBean().getPlantName();
		String divisionId = (String) requestParams.get("divisionId");
		String lineId = (String) requestParams.get("lineId");
		if(StringUtils.isBlank(plantName) || StringUtils.isBlank(divisionId) 
				|| StringUtils.isBlank(lineId)){
			return new ArrayList<InProcessProduct>(0);
		}
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		return  inProcessProductDao.getInventoryForLine(
				StringUtils.trimToEmpty(plantName), StringUtils.trimToEmpty(divisionId), 
				StringUtils.trimToEmpty(lineId));
	}
	
	/**
	 * Gets the actual products detail.
	 *
	 * @param requestParams the request params. 
	 * 		The request parameters should have "divisionId", "lineId" and "processPointId" or empty array list will return.
	 * 		"shift" can be "01", "02", "03" or ""(for actual total of the shifts)
	 * @return the actual products
	 */
	public List<ProductResult> getActualProducts(Map<?, ?> requestParams){
		String plantName = getPropertyBean().getPlantName();
		String divisionId = (String) requestParams.get("divisionId");
		String lineId = (String) requestParams.get("lineId");
		String processPointId = (String) requestParams.get("processPointId");
		String shift = (String) requestParams.get("shift");
		
		if(StringUtils.isBlank(plantName) || StringUtils.isBlank(divisionId) 
				|| StringUtils.isBlank(lineId) || StringUtils.isBlank(processPointId)){
			return new ArrayList<ProductResult>(0);
		}
		shift = StringUtils.trimToEmpty(shift);
		ProductResultDao productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		String plantCode = getGpcsPlantCode(StringUtils.trimToEmpty(plantName));
		return productResultDao.getActualProductResultsOfToday(plantCode,
						StringUtils.trimToEmpty(divisionId), StringUtils.trimToEmpty(lineId), 
						StringUtils.trimToEmpty(processPointId), StringUtils.trimToEmpty(shift));
	}
	
	public String getGpcsPlantCode(String plantName) {
		GpcsDivisionDao gpcsDivisionDao = ServiceFactory.getDao(GpcsDivisionDao.class);
		return StringUtils.trimToEmpty(gpcsDivisionDao.getGpcsPlantCode(plantName));
	}
	
	/**
	 * Gets the process points of a line
	 *
	 * @param requestParams the request params
	 * 		The request parameters should have "divisionId", "lineId" or empty array list will return.
	 * @return the process points
	 */
	public List<ProcessPoint>getProcessPoints(Map<?, ?> requestParams){
		String plantName = getPropertyBean().getPlantName();
		String divisionId = (String) requestParams.get("divisionId");
		String lineId = (String) requestParams.get("lineId");
		
		List<ProcessPoint> processPoints = new ArrayList<ProcessPoint>(0);
		if(StringUtils.isBlank(plantName) || StringUtils.isBlank(divisionId) 
				|| StringUtils.isBlank(lineId)){
			return processPoints;
		}
		LineDao lineDao = ServiceFactory.getDao(LineDao.class);
		ProcessPointDao processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		
		Line line =lineDao.findByKey(StringUtils.trimToEmpty(lineId));
		if (line == null) {
			return processPoints;
		}
		processPoints = processPointDao.findAllByLine(line);
		return processPoints;
		
	}
	
	/**
	 * return the flag if is detail link enabled.
	 *
	 * @return true, if is detail link enabled
	 */
	public boolean isDetailLinkEnabled(){
		return getPropertyBean().isDetailLinkEnabled();
	}
}
