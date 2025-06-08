package com.honda.galc.entity.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 *    
 * @version 0.1
 * @author Prasanna
 * @since Dec 16, 2016
 */
public enum QCAction {
	QCHOLD("1","QC Hold"), 
	QCRELEASE("2","QC Release"),
	KICKOUT("3", "Kickout"),
	SCRAP("4", "Scrap"),
	DEFECT("5", "Defect");
	
	private String actionId;
	private String actionName;
	private static Map<String, String> typeNames;
	
	private QCAction(String actionId, String actionName) {
		this.actionId = actionId;
		this.actionName = actionName;
	}

	public String getQcActionId() {
		return actionId;
	}
	
	public String getQcActionName() {
		return actionName;
	}
	
	public static QCAction getQCAction(String qcActionName) {
		
		for(QCAction action :values()) {
			if(action.getQcActionName().equalsIgnoreCase(qcActionName) ||
					action.name().equalsIgnoreCase(qcActionName))	
				return action;
		}
		
		return null;
		
	}
	
	public static Map<String, String> getQCActionNames(){
		if(typeNames == null){
			typeNames = new LinkedHashMap<String, String>();
			for(QCAction type : values())
				typeNames.put(type.name(), type.getQcActionName());
		}
		return typeNames;
		
	}
	
}
