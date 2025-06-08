package com.honda.galc.client.dc.control;

/**
 * @author Sachin Kudikala
 * @date September 25, 2015
 */
public enum FilterCriteria {
	
	PROCESS_POINT			("Process Point","getComboBox",""),
	OP_TYPE					("Operation Type", "getComboBox",""),
	ALL						("All","getTextBox",""),
	PART_NAME				("Part Number","getTextBox",""),
	PART_MASK				("Part Mask","getTextBox",""),
	INCOMPLETE_UNITS        ("Incomplete", "getComboBox","COMPLETE,INCOMPLETE"),
	SPECIALITY_SCREENS      ("Speciality Screens", "getComboBox",""),
	TOOL_TYPE	        	("Tool Type", "getComboBox",""),
	PSET_VALUE	        	("Pset Value", "getTextBox","");
	
	private String description;
	private String generateMethod;
	private String listValues;
	

	private FilterCriteria(String description, String generateMethod,String listValues) {
		this.description = description;
		this.generateMethod = generateMethod;
		this.listValues = listValues;
	}

	public String getGenerateMethod() {
		return generateMethod;
	}

	public void setGenerateMethod(String generateMethod) {
		this.generateMethod = generateMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getListValues() {
		return listValues;
	}

	public void setListValues(String listValues) {
		this.listValues = listValues;
	}

	public static String getComponentMethod(String criteria){
		FilterCriteria filterCriteria = FilterCriteria.valueOf(criteria);
		if(filterCriteria == null) return null;
		try {
			return filterCriteria.getGenerateMethod();
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String getComponentDescription(String criteria){
		FilterCriteria filterCriteria = FilterCriteria.valueOf(criteria);
		if(filterCriteria == null) return null;
		try {
			return filterCriteria.getDescription();
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String getListValues(String criteria){
		FilterCriteria filterCriteria = FilterCriteria.valueOf(criteria);
		if(filterCriteria == null) return null;
		try {
			return filterCriteria.getListValues();
		} catch (Exception e) {
		}
		return null;
	}
	
}


