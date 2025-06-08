package com.honda.galc.client.ui.component;

/**
 * 
 * <h3>MbpnColumns</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnColumns description </p>
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
 * <TD>Jan 30, 2015</TD>
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
 * @since Jan 30, 2015
 */
public enum MbpnColumns {
	Main_No("Main No"),Class_No("Class No"), ProtoType_Code("ProtoType"),Type_No("Type No"),
	Supplementary_No("Suppl No"), Target_No("Target No"),Hes_Color("Hes Clr");

	private String columnName;
	private static MbpnColumns[] vals = values();
	
	
	private MbpnColumns(String columnName){
		this.columnName = columnName;
	}
	
	public MbpnColumns next() {
		return vals[(this.ordinal() + 1)%vals.length];
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
}
