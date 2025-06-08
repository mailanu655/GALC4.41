package com.honda.galc.constant;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>EngineManifestPlant</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineManifestPlant description </p>
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
 * <TD>Mar 31, 2017</TD>
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
 * @since Mar 31, 2017
 */
public enum EngineManifestPlant {

	Plant1(1, "001", "MS","010"),
	Plant2(2, "002", "XM","010"),
	Other(3, "003", "ME","600");
	
	int id;
	String plant;
	String department;
	String process;
	String line = "1";
	
	private EngineManifestPlant(int id, String plant, String department, String process){
		this.id = id;
		this.plant = plant;
		this.department = department;
		this.process = process;
	}
	
	
	public static EngineManifestPlant getById(int id){
		for(EngineManifestPlant emp : values())
			if(emp.getId() == id)
				return emp;
		
		return null;
	}
	
	public static EngineManifestPlant getByPlant(String plant){
		for(EngineManifestPlant emp : values())
			if(plant.equals(emp.getPlant()))
				return emp;
		
		return null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlant() {
		return StringUtils.trim(plant);
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getDepartment() {
		return StringUtils.trim(department);
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getProcess() {
		return StringUtils.trim(process);
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getLine() {
		return StringUtils.trim(line);
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	
	
}
