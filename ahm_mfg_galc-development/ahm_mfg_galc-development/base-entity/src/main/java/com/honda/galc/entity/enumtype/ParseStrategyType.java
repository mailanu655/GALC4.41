package com.honda.galc.entity.enumtype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public enum ParseStrategyType {
	/*
	 * Add Parse Strategy Type to appear in the Team Leader Part combo box selection
	 *   1. UNIQUE Name that you want to display to the associate in the combo box
	 *   2. Class path for the Parse strategy type
	 *   3. Strategy will show on team leader Part tab drop down list. null will apply 
	 *      the Parse strategy for all parts
	 */
	DELIMITED("Delimited"),
	FIXED_LENGTH("Fixed Length"),
	TAG_VALUE("Tag Value");
	
	
	private String displayName;
	
	private ParseStrategyType(String comboDisplayName) {
		this.displayName = comboDisplayName;
	}

	public String getDisplayName() {
		return displayName;                                                  
	}
	
	
	public static String getName(String comboDisplayName) {
		if (comboDisplayName == null) return "";
		for(ParseStrategyType type:ParseStrategyType.values()){
			if(StringUtils.trimToEmpty(comboDisplayName).equalsIgnoreCase(type.getDisplayName())) return type.name();
		}
		return comboDisplayName;
	}
	
	public static ArrayList<String> getComboDisplayNames(List<String> typeFilter) {
		ArrayList<String> names = new ArrayList<String>();
		for(ParseStrategyType type:ParseStrategyType.values()){
			if ((typeFilter.size() == 0) || typeFilter.contains(type.name()))
				names.add((type.getDisplayName()));
		}
		
		//add a empty item for remove strategy
		if(names.size() > 0) names.add(0, null);
		return names;
	}
}