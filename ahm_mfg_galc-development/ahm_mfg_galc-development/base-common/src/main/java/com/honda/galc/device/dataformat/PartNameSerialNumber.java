package com.honda.galc.device.dataformat;
/**
 * 
 * <h3>PartNameSerialNumber</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartNameSerialNumber description </p>
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
 * Mar 8, 2011
 *
 */
public class PartNameSerialNumber extends PartSerialNumber {
	private static final long serialVersionUID = 1L;
	private String partName;
	
	public PartNameSerialNumber() {
		super();
	}

	public PartNameSerialNumber(String partName, String partSerialNumber) {
		super(partSerialNumber);
		this.partName = partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartName() {
		return partName;
	}
}
