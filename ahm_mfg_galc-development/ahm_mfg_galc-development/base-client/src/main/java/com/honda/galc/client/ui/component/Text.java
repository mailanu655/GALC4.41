package com.honda.galc.client.ui.component;
/**
 * 
 * <h3>Text</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Text description </p>
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
 * Aug 5, 2010
 *
 */
public class Text {
	private String value;
	private boolean status;
	
	
	public Text() {
		super();
	}

	public Text(String value, boolean status) {
		super();
		this.value = value;
		this.status = status;
	}
	
	public Text(String value) {
		super();
		this.value = value;
	}

	public Text(boolean b) {
		this(null, false);
	}

	//Getters & Setters
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

}
