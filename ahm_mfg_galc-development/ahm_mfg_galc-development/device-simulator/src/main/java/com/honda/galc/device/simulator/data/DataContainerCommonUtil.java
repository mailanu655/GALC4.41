package com.honda.galc.device.simulator.data;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * DataContainerCommonUtil - DataContainer tag manipulation utility for common tags:<ul>
 * <li>Answers common questions about DataContainer tags
 * <li>Sets common DataContainer tags
 * </ul>
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
 * <TD>R.Lasenko</TD>
 * <TD>Dec 12, 2007</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL010</TD>
 * <TD>Created to ease DataContainer tags manipulation</TD>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Mar 27, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL014</TD>
 * <TD>Setter for Application ID</TD>
 * </TR>
 * </TABLE>
 */

public class DataContainerCommonUtil {

	/**
	 * Private constructor makes this class a utility
	 */
	private DataContainerCommonUtil() {
		// It is a utility
	}
	
	public static boolean isEquipmentTypeOpc(DataContainer dataContainer) {
		Object equipType = dataContainer.get(DataContainerTag.EQUIPMENT_TYPE);
	
		return (equipType != null 
				&& equipType.toString().equals(DataContainerTag.EQUIPMENT_TYPE_OPC));
	}
	
	public static void setEquipmentOpc(DataContainer dataContainer) {
		dataContainer.put(DataContainerTag.SENDER, DataContainerTag.EQUIPMENT);
		dataContainer.put(DataContainerTag.EQUIPMENT_TYPE, DataContainerTag.EQUIPMENT_TYPE_OPC);
	}
	
	// @KM-JSON
	public static void setEquipmentJson(DataContainer dataContainer) {
		dataContainer.put(DataContainerTag.SENDER, DataContainerTag.EQUIPMENT);
		dataContainer.put(DataContainerTag.EQUIPMENT_TYPE, DataContainerTag.EQUIPMENT_TYPE_JSON);
	}
	
	// @KM-JSON
	public static boolean isEquipmentTypeJson(DataContainer dataContainer) {
		Object equipType = dataContainer.get(DataContainerTag.EQUIPMENT_TYPE);
	
		return (equipType != null 
				&& equipType.toString().equals(DataContainerTag.EQUIPMENT_TYPE_JSON));
	}
	
	public static boolean isEquipmentRead(DataContainer dataContainer)
	{
		Object eiOPMode = dataContainer.get(DataContainerTag.EI_OP_MODE);
		return(eiOPMode != null 
				&& eiOPMode.toString().equals(DataContainerTag.EI_OP_MODE_READ));
	}
	
	public static void setEquipmentRead(DataContainer dataContainer)
	{
		dataContainer.put(DataContainerTag.EI_OP_MODE, DataContainerTag.EI_OP_MODE_READ);		
	}
	
	/**
	 * Sets application ID in data container
	 * 
	 * @param dataContainer - geiven data container
	 * @param applicationId - application ID
	 */
	public static void setApplicationId(DataContainer dataContainer, String applicationId)
	{
		dataContainer.put(DataContainerTag.APPLICATION_ID, applicationId);		
	}
}
