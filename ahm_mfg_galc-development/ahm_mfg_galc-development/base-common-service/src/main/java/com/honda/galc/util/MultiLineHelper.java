/**
 * 
 */
package com.honda.galc.util;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.QiMultiLinePropertyBean;
import com.honda.galc.service.property.PropertyService;

public class MultiLineHelper  {

	protected boolean isMultiLineStation = false;
	protected Map<String, String> secondaryPPMap;
	protected String lineOffPP = "";
	protected String processPointId = "";
	
	private MultiLineHelper(String newProcessPoint) {
		processPointId = newProcessPoint;
		QiMultiLinePropertyBean qBean = PropertyService.getPropertyBean(QiMultiLinePropertyBean.class,getProcessPointId());
		isMultiLineStation = qBean.isMultiLineStation();
		secondaryPPMap = qBean.getAltStationLineOffMap();
		lineOffPP = qBean.getLineOff();
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public ProcessPoint findProcessPoint(String processPointId) {
		return getDao(ProcessPointDao.class).findByKey(processPointId);
	}
	
	public ProcessPoint getCurrentProcessPoint() {
		return findProcessPoint(getProcessPointId());
	}
	
	public ProcessPoint getProcessPointToUse(BaseProduct product)  {
				
		ProcessPoint currentStation = getCurrentProcessPoint();
		ProductCheckUtil helper = new ProductCheckUtil(product, currentStation);
		//if not a multi line station or line-off to process_point map not set, default to current station
		if(!isMultiLineStation || secondaryPPMap == null || secondaryPPMap.isEmpty())  {
			return currentStation;
		}

		//first check if product has a tracking record for lineOff process_point specified for this station
		else if(!StringUtils.isEmpty(lineOffPP) && helper.passedProcessPointCheck(lineOffPP))  {
			return currentStation;
		}

		//if list of line_off process points for alternate stations is not provided,  default to current station
		else if(getAlternateStations() == null || getAlternateStations().isEmpty())  {
			return currentStation;
		}
		//which of the specified line_off process points has a tracking record for this product?
		String newStationId = null;
		for(Map.Entry<String, String> stationEntry : secondaryPPMap.entrySet())  {
			if(stationEntry != null && helper.passedProcessPointCheck(stationEntry.getValue()))  {
				newStationId = stationEntry.getKey();
				break;
			}
		}
		//if none of the provided line_off process points has a tracking record,  default to current station
		if(StringUtils.isBlank(newStationId))  return currentStation;
		
		else return findProcessPoint(newStationId);
		
	}
	
	public boolean isSameStation(String otherStationId)  {
		boolean result = false;
		if(!StringUtils.isEmpty(otherStationId))  {
			if(getProcessPointId().equalsIgnoreCase(otherStationId))  {
				result = true;
			}
		}
		return result;
	}
	
	public boolean isMultiLine()  {
		return isMultiLineStation;
	}
	
	public List<String> getAlternateStations()  {
		return new ArrayList<String>(secondaryPPMap.keySet());
	}
	
	public String getFirstAlternateStation()  {
		if(secondaryPPMap != null && !secondaryPPMap.isEmpty())  {
			List<String> stations = new ArrayList<String>(secondaryPPMap.keySet());
			return stations.get(0);
		}
		return null;
	}
	
	public static MultiLineHelper getInstance(String newProcessPointId)  {
		return new MultiLineHelper(newProcessPointId);
	}
	
	public List<String> getValidProcessPoints()  {
		
		QiMultiLinePropertyBean qBean = PropertyService.getPropertyBean(QiMultiLinePropertyBean.class,getProcessPointId());
		Map<String,String> secondaryPPMap = qBean.getAltStationLineOffMap();
		
		ArrayList<String> ppList = new ArrayList<String>();
		if(!qBean.isMultiLineStation() || secondaryPPMap == null || secondaryPPMap.isEmpty())  {
			ppList.add(getProcessPointId());
			return ppList;
		} else  { 
			ppList.addAll(secondaryPPMap.keySet());
			return ppList;
		}
	}
	
	public List<ProcessPoint> getAllProcessPoints()  {
		List<ProcessPoint> allPP = new ArrayList<ProcessPoint>();
		allPP.add(getCurrentProcessPoint());
		for(String ppId : getValidProcessPoints())  {
			ProcessPoint pp = findProcessPoint(ppId);
			if(pp != null) allPP.add(pp);
		}
		return allPP;
	}
	
}
