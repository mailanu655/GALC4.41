package com.honda.galc.property;

import java.util.Map;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>FrameWarrantyPropertyBean</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Apr 2, 2015
 */
public interface FrameWarrantyPropertyBean extends IProperty {

	/**
	 * PARSE_LINE_DEFS - mapped property that defines information about oif file
	 * fields. <br />
	 * It is comma delimited string of 3 values : <br />
	 * First value - start ix <br />
	 * Second - length <br />
	 * Third - default values <br />
	 * Example: <br />
	 * PARSE_LINE_DEFS{AF_ON_DATE}:338,8,00000000 <br />
	 * PARSE_LINE_DEFS{AF_ON_TIME}:346,4,0000 <br /.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getParseLineDefs();

	/**
	 * OIF File (Fixed Length File) record length
	 * 
	 * @return
	 */
	@PropertyBeanAttribute()
	public int getMessageLineLength();

	/**
	 * Required - selection deciding process point (usually AE_OFF or VQ_OFF)
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getSelectingProcessPointIds();

	/**
	 * Optional, you may include product with certain tracking status (scrap or
	 * exceptional out)
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getNotSellableTrackingStatus();

	/**
	 * Expected following processPointIds:
	 * WE_ON,WE_OFF,PA_ON,PA_OFF,AF_ON,AF_OFF,VQ_ON,VQ_OFF,VQ_SHIP for
	 * example:PROCESS_POINT_IDS{WE_ON}:PP00102
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProcessPointIds();

	// === engine === //
	/**
	 * URL to GALC Engine Instance to collect engine relate data (AE_OFF, BLOCK,
	 * HEAD, TRANSMISSION, SHIFT, ...).
	 * http://server:port/BaseWeb/HttpServiceHandler <br />
	 * 'LOCAL' - will use local GALC to collect engine data (local
	 * ServiceFactory) <br />
	 * ' ' - blank will not collect engine related data
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getEngineServiceUrl();

	/**
	 * AE OFF - process point id
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getAeOffProcessPointId();

	/**
	 * Part name used to associate Engine and Block in gal185tbx table.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "BLOCK")
	public String getBlockPartName();

	/**
	 * Part name used to associate Engine and Head in gal185tbx table.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "HEAD")
	public String getHeadPartName();

	/**
	 * Part name used to associate Engine and Transmission in gal185tbx table.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "TRANSMISSION")
	public String getTransmissionPartName();

	/**
	 * If Block Number has embeded machine id, then it can be used to identify
	 * from which machine block is coming from. <br />
	 * Optional
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "-1")
	public int getBlockNumberMachineIx();

	/**
	 * If Head Number has embeded machine id, then it can be used to identify
	 * from which machine head is coming from.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "-1")
	public int getHeadNumberMachineIx();
	
	/**
	 * Optional, flag to indicate that is necessary to exclude certain Plan Codes.
	 * Property name: EXCLUDE_LISTED_PLANT_CODES
	 * Default value: false
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isExcludeListedPlantCodes();
	
	/**
	 * Optional, required if EXCLUDE_LISTED_PLANT_CODES is set to true.
	 * You may put Plan Codes to be excluded from the query,
	 * for more than one Plan Code use commas to separate them.
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getPlantCodesToExclude();
	
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProcessLocations();
	
	/**
	 * Optional - used in case of multi line
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLinesUrls();
}
