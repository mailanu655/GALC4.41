package com.honda.galc.dto.qi;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DataMigrationDto</code> is the Dto class for Data Migration.
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
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class DataMigrationDto implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "PART_DEFECT_DESC")
	private String partDefectDesc;
	
	@DtoTag(outputName = "REGIONAL_DEFECT_COMBINATION_ID")
	private int regionalDefectCombinationId;

	public String getPartDefectDesc() {
		return partDefectDesc;
	}

	public void setPartDefectDesc(String partDefectDesc) {
		this.partDefectDesc = partDefectDesc;
	}

	public int getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(int regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}
	
}
