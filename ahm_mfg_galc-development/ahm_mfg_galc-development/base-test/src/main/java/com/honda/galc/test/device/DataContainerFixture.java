package com.honda.galc.test.device;

import static org.fest.assertions.Assertions.assertThat;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.LineSideContainerTag;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.test.common.GalcTestTag;


/**
 * 
 * <h3>DataContainerFixture</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
 * 
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Feb 10, 2009</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial version</TD>
 * </TR>
 * </TABLE>
 */


public class DataContainerFixture{
	String description = ""; 
	DataContainer target;

	public DataContainerFixture(DataContainer dc) {
		this.target = dc;
	}
	
	public DataContainerFixture(DataContainer dc, String decription) {
		this.target = dc;
		this.description = decription;
	}
	
	public DataContainerFixture requireDataCollectionStatusEqualTo(String s)
	{
	    requireTagValueEqualTo(LineSideContainerTag.DATA_COLLECTION_STATUS, s);
	    return this;
	}
	
	public DataContainerFixture requireDataCollectionComplete()
	{
		return requireDataCollectionStatusEqualTo(LineSideContainerValue.COMPLETE);
	}
	
	public DataContainerFixture requireDataCollectionNotComplete()
	{
		return requireDataCollectionStatusEqualTo(LineSideContainerValue.NOT_COMPLETE);
	}
	
	public DataContainerFixture requireEngineSerialNumberEqualTo(String s)
	{
	    requireTagValueEqualTo(LineSideContainerTag.ENGINE_SERIAL_NUMBER, s);
	    return this;
	}
	
	public DataContainerFixture requireProcessPointIdEqualTo(String s)
	{
		requireTagValueEqualTo(LineSideContainerTag.PROCESS_POINT_ID, s);
	    return this;
	}
	
	public DataContainerFixture requireMessageIdEqualTo(String s)
	{
		requireTagValueEqualTo(DataContainerTag.MESSAGE_ID, s);
	    return this;
	}
	
	public DataContainerFixture requireBlockDCNumberEqualTo(String s)
	{
		requireTagValueEqualTo(LineSideContainerTag.BLOCK_DC_NUMBER, s);
	    return this;
	}
	
	public DataContainerFixture requireBlockMCNumberEqualTo(String s)
	{
		requireTagValueEqualTo(LineSideContainerTag.ALMCB_NUMBER, s);
	    return this;
	}
	
	public DataContainerFixture requireHeadDCNumberEqualTo(String s)
	{
		requireTagValueEqualTo(LineSideContainerTag.HEAD_DC_NUMBER, s);
	    return this;
	}
	
	public DataContainerFixture requireHeadMCNumberEqualTo(String s)
	{
		requireTagValueEqualTo(LineSideContainerTag.ALMCH_NUMBER, s);
	    return this;
	}
	
	public DataContainerFixture requireProductionLotEqualTo(String s)
	{
		requireTagValueEqualTo(LineSideContainerTag.PROD_LOT, s);
	    return this;
	}
	
	public DataContainerFixture requireYMTOEqualTo(String s)
	{
		requireTagValueEqualTo(LineSideContainerTag.YMTO, s);
	    return this;
	}
	
	public DataContainerFixture requireProductIdEqualTo(String s)
	{
		
	    return this;
	}
	
	public DataContainerFixture requireTagExist(String tag)
	{
		assertThat(target.containsKey(tag)).as(tag).isEqualTo(true);
	    return this;
	}
	
	public DataContainerFixture requireTagValueEqualTo(String tag, Object val)
	{
		assertThat(target.get(tag)).as(tag).isEqualTo(val);
	    return this;
	}
	
	public DataContainerFixture requireRepairStatusEqualTo(Object s)
	{
		requireTagValueEqualTo(LineSideContainerTag.REPAIR_STATUS, s);
		return this;
	}

	
	public DataContainerFixture requireValidBlockEqualTo(Object s)
	{
		requireTagValueEqualTo(GalcTestTag.VALID_BLOCK, s);
		return this;
	}
	
	public DataContainerFixture requireValidHeadEqualTo(Object s)
	{
		requireTagValueEqualTo(GalcTestTag.VALID_HEAD, s);
		return this;
	}
	
	public DataContainerFixture requireValidHeadDcEqualTo(Object s)
	{
		requireTagValueEqualTo(GalcTestTag.VALID_HEAD_DC, s);
		return this;
	}
	public DataContainerFixture requireValidBlockDcEqualTo(Object s)
	{
		requireTagValueEqualTo(GalcTestTag.VALID_BLOCK_DC, s);
		return this;
	}
	
	public DataContainerFixture requireProductSpecCode(Object s)
	{
		requireTagValueEqualTo(GalcTestTag.PROD_SPEC_CODE, s);
		return this;
	}
	
	public DataContainerFixture requireCycleCompleteEqualTo(Object s)
	{
		requireTagValueEqualTo(GalcTestTag.CYCLE_COMPLETE, s);
		return this;
	}
	
	public DataContainerFixture requireValidEngineNumber()
	{
		String str = (String)target.get(GalcTestTag.VALID_ENGINE_NUMBER);
		Boolean b = isOK(str.trim());
		assertThat(b.equals(Boolean.TRUE));
		return this;
	}
	
	public DataContainerFixture requireJobEqualTo(Object s)
	{
		requireTagValueEqualTo(GalcTestTag.PARAMETER_SET, s);
		return this;
	}
	
	public DataContainerFixture requireSetJob()
	{
		requireTagExist(GalcTestTag.PARAMETER_SET);
		return this;
	}
	
	public DataContainerFixture requireAbortJob()
	{
		requireTagExist(GalcTestTag.PARAMETER_DATA);
		return this;
	}
	
	/**
	 * simulate functions in EI for backward compatibality to all
	 * data type/values that is OK
	 * @param str
	 * @return
	 */
	private boolean isOK(String str) {
		if(str.equals("OK")) return true;
		else if(str.equals("1")) return true;
		else
		   return Boolean.parseBoolean(str);
	}

	public final String description()
	{
		return description;
	}
	
	public DataContainer target()
	{
		return target;
	}
}
