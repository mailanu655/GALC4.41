package com.honda.galc.dto.qi;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiMostFrequentDefectsDto</code> is the Dto class for most frequently used defects
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>vcc44349</TD>
 * <TD>Dec 6 20186</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2_21</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author vcc44349
 */
public class QiMostFrequentDefectsDto implements IDto{
	
	//adding new file to git
	private static final long serialVersionUID = 1L;

	@DtoTag(outputName ="DEFECT_TYPE_NAME")
	private String defectTypeName;
	@DtoTag(outputName ="DEFECT_TYPE_NAME2")
	private String defectTypeName2;
	@DtoTag(outputName ="COUNT")
	private int count;

	public QiMostFrequentDefectsDto() {
		super();
	}

	public String getPrimaryDefect() {
		return defectTypeName;
	}

	public void setPrimaryDefect(String primaryDefect) {
		this.defectTypeName = primaryDefect;
	}

	public String getSecondaryDefect() {
		return defectTypeName2;
	}

	public void setSecondaryDefect(String secondaryDefect) {
		this.defectTypeName2 = secondaryDefect;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
