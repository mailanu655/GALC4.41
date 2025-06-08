package com.honda.galc.service.qics;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QicsProcessPointDefectInfo</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsProcessPointDefectInfo description </p>
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
 * <TD>May 11, 2011</TD>
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
 * @since May 11, 2011
 */

public class QicsProcessPointDefectInfo {

	private String processPointId;
	private Integer processingStatus;
	private Map<String, String> dictionary = new HashMap<String, String>();
	private Properties properties;
	private String responsibleZone;
	private String responsibleDept;
	private String responsibleLine;
	
	private enum QicsDefect{QICS_DEFECT_LOCATION, QICS_DEFECT_NAME, QICS_DEFECT_GROUP_NAME};
	private enum QicsLine{RESPONSIBLE_DEPT, RESPONSIBLE_ZONE, RESPONSIBLE_LINE};
	private static final String DEFAULT_VALUE = "DEFAULT_VALUE";
	
	public QicsProcessPointDefectInfo(String processPointId) {
		super();
		this.processPointId = processPointId;
		
		initialize();
	}


	private void initialize() {
		properties = PropertyService.getComponentProperties(processPointId);

		/**
		 * Responsible department and Responsible line, if property is defined, then it 
		 * will override default value from process point
		 * 
		 */
		
		responsibleDept = (String)properties.get(QicsLine.RESPONSIBLE_DEPT.name());
		responsibleLine = (String)properties.get(QicsLine.RESPONSIBLE_LINE.name());
		
	}

	// ----------- methods ------------
	public String getDefectName(String partName, String result){
		
		return getFromDictionary(partName, QicsDefect.QICS_DEFECT_NAME.name(), result);
	}

	public String getDefectLocation(String partName, String index){
		
		return getFromDictionary(partName, QicsDefect.QICS_DEFECT_LOCATION.name(), index);
	}

	public String getGroupName(String partName, String id){
		return getFromDictionary(partName, QicsDefect.QICS_DEFECT_GROUP_NAME.name(), id);
	}
	
	
	public String getResponsibleZone(){
		if(StringUtils.isEmpty(responsibleZone))
			responsibleZone = (String)properties.get(QicsLine.RESPONSIBLE_ZONE.name());
		
		return responsibleZone;
	}
	
	public String getResponsibleDept(){

		return responsibleDept;
	}
	
	public String getResponsibleLine(){

		return responsibleLine;
	}
	
	
	//------------------------------------	
	private String getFromDictionary(String partName, String name, String result) {
		
		dictionary = getQicsDefectDictionary(partName);
		StringBuffer sb = new StringBuffer();
		sb.append(partName).append(".").append(name).append(".");
		if(result == null)  result = "";
		if(dictionary.containsKey(sb.toString() + result))
			return dictionary.get(sb.toString() + result);
		else if(dictionary.containsKey(sb.toString() + DEFAULT_VALUE))
			return dictionary.get(sb.toString() + DEFAULT_VALUE);
		else
			return result;
	}
	
	public Integer getProcessingStatus() {
		if(processingStatus == null){
			String status = PropertyService.getProperty(processPointId, "QICS_PROCESSING_STATUS", "0");
			processingStatus = Integer.valueOf(status);
		}
		return processingStatus;
	}


	protected Map<String, String> getQicsDefectDictionary(String partName) {
		if(dictionary != null && dictionary.containsKey(partName)) 
			return dictionary;

		return loadQicsDefectDictionary(partName);
	}


	private Map<String, String> loadQicsDefectDictionary(String partName) {
		for(QicsDefect defect : QicsDefect.values()){
			
			String attribute = getAttribute(partName, defect);
			if(attribute == null ) continue;
			
			String[] list = attribute.split(",");
			for(int i = 0; i < list.length; i++){
				String[] map = list[i].trim().split(":");
				if(map.length == 1)
					dictionary.put(partName + "." + defect.name() + "." + DEFAULT_VALUE, map[0].trim());
				else
					dictionary.put(partName + "." + defect.name() + "." + map[0].trim(), map[1].trim());
			}
		}
		
		//mark the part name is loaded
		dictionary.put(partName, "");
			
		return dictionary;
	}


	private String getAttribute(String partName, QicsDefect defect) {
		String attribute = null;
		attribute =	properties.getProperty(defect.name());
		if(properties.getProperty(partName + "." + defect.name()) != null)
			attribute = properties.getProperty(partName + "." + defect.name());
		
		return attribute;
	}


}
