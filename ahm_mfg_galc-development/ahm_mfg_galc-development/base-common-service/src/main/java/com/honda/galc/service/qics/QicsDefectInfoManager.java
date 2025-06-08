package com.honda.galc.service.qics;

import java.util.HashMap;
import java.util.Map;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;


/**
 * 
 * <h3>QicsDefectInfoManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsDefectInfoManager description </p>
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
 * Apr 4, 2011
 *
 */

public class QicsDefectInfoManager {
	private static QicsDefectInfoManager instance;
	private static Map<String, QicsProcessPointDefectInfo> processPointInfos = new HashMap<String, QicsProcessPointDefectInfo>();
	private static Map<String, String> defectCodeMap = new HashMap<String, String>();
	
	private QicsDefectInfoManager() {
		super();
		
		defectCodeMap = loadDefectCodes();
		if(defectCodeMap == null)  {
			defectCodeMap = new HashMap<String, String>();
		}
		
	}

	private Map<String, String> loadDefectCodes() {
		try {
			QicsServicePropertyBean bean = PropertyService.getPropertyBean(QicsServicePropertyBean.class);
			return bean.getDefectCodeMapping();
		} catch (Exception e) {
			Logger.getLogger().info("Failed to load defect code dictionary.", this.getClass().getSimpleName());
		} 
		
		return new HashMap<String, String>();
	}

	public static QicsDefectInfoManager getInstance() {
		if(instance == null)
			instance = new QicsDefectInfoManager();
		
		return instance;
	}
	
	public String getDefectLocation(String processPointId,
			String partName, String name) {
		
		return getProcessPointInfo(processPointId).getDefectLocation(partName, name);	
	}

	public String getDefectName(String processPointId, String partName,
			String errorCode, String value) {
		
		//translate the defect code if it defined in code map
		if(errorCode != null && defectCodeMap != null && !defectCodeMap.isEmpty() && defectCodeMap.keySet().contains(errorCode))
			return defectCodeMap.get(errorCode);
		
		return getProcessPointInfo(processPointId).getDefectName(partName, value);
	}

	public String getResponsibleZone(String ProcessPointId){
		return getProcessPointInfo(ProcessPointId).getResponsibleZone();
	}
	
	public String getResponsibleLine(String ProcessPointId){
		return getProcessPointInfo(ProcessPointId).getResponsibleLine();
	}
	
	public String getResponsibleDepartment(String ProcessPointId){
		return getProcessPointInfo(ProcessPointId).getResponsibleDept();
	}
	
	public String getGroupName(String processPointId, String partName, String location) {
		return getProcessPointInfo(processPointId).getGroupName(partName, location);
		
	}

	private QicsProcessPointDefectInfo getProcessPointInfo(String processPointId) {
		if(!processPointInfos.keySet().contains(processPointId))
			processPointInfos.put(processPointId, new QicsProcessPointDefectInfo(processPointId));
		
		return  processPointInfos.get(processPointId);
		
	}

	public void refresh(String componentId) {
		if(processPointInfos.keySet().contains(componentId)){
			processPointInfos.remove(componentId);
		} else if(componentId.equals("qics")){
			defectCodeMap = loadDefectCodes();
		}
		
	}
	
}
