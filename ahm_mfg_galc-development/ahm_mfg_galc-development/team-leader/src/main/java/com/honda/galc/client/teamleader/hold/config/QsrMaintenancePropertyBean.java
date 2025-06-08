package com.honda.galc.client.teamleader.hold.config;

import java.io.Serializable;
import java.util.Map;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.ProductHoldPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrMaintenancePropertyBean</code> is ... .
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
 */
public interface QsrMaintenancePropertyBean extends ProductHoldPropertyBean, Serializable {

	@PropertyBeanAttribute(defaultValue = "10080")
	public int getMaxQsrTimeRange();

	/**
	 * Maximum number of hold records to be processed at one time.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "400")
	public int getMaxHoldBatchSize();

	/**
	 * Maximum number of release records to be processed at one time.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "10")
	public int getMaxReleaseBatchSize();

	/**
	 * Maximum number of hold records to be processed in one transaction.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "200")
	public int getHoldPartitionSize();

	/**
	 * Maximum number of hold records to be processed in one transaction.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "200")
	public int getReleasePartitionSize();

	@PropertyBeanAttribute(defaultValue = "CONTAINMENT,MODEL CHANGE,NO INVENTORY,NO MANPOWER,REPAIR")
	public String[] getHoldReasons();

	@PropertyBeanAttribute(defaultValue = "DEFECT")
	public String[] getMassScrapReasons();

	@PropertyBeanAttribute(defaultValue = "REPAIRED")
	public String[] getReleaseReasons();

	@PropertyBeanAttribute(defaultValue = "DIE CHANGE,NEW MODEL TRIAL")
	public String[] getDieHoldReasons();

	/**
	 * Returns an array of kickout reasons
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "QC CHECK")
	public String[] getKickoutReasons();

	@PropertyBeanAttribute(defaultValue = "1,2,3,4,5,6,7,8")
	public String[] getDieIds();

	@PropertyBeanAttribute(defaultValue = "A,B,C,D")
	public String[] getCoreIds();

	@PropertyBeanAttribute(defaultValue = "HP:1|2,LP:A|B")
	public String[] getDivisionMachineIdMapping();

	@PropertyBeanAttribute(defaultValue = "HEAD")
	public String[] getCoreProductTypes();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isHoldNowDisabled();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isHoldAtShippingDisabled();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isBroadcastEnabled();

	/**
	 *  Flag indicate if user can do Kickout
	 * @return boolean
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isKickOutEnabled();

	/**
	 *  Flag indicate if user can do Mass Scrap
	 * @return boolean
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isMassScrapEnabled();
	
	/**
	 *  Enable/disable "Select by List" menu option on QSR release screens 
	 * @return boolean
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isSelectByListEnabled();
	
	/**
	 *  Enable/disable "Unrelease Selected" menu option on QSR release screens 
	 * @return boolean
	 */	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUnreleaseSelectedEnabled();
	
	/**
	 *  Enable/disable "Inline Defect" menu option on QSR release screens 
	 * @return boolean
	 */	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isInlineDefectEnabled();

	/**
	 *  In Hold popup Screen, Hold Associate info input boxes are required entry
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isInputAssociateInfo();

	@PropertyBeanAttribute(defaultValue = "QSR_HOLD_BY_PROCESS,QSR_IMPORT_PRODUCTS,QSR_SCAN_PRODUCT,QSR_DUNNAGE,QSR_DIE_HOLD,QSR_RELEASE")
	public String[] getPanels();

	/**
	 *  In Scrap popup screen, Inspection Parts Info are required entry
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isInputInspectionPart();

	/**
	 * Get Hold Type:
	 *     Operator Hold  -  1
	 *     teamlead Hold  -  2
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "1")
	public int getHoldBy();

	/**
	 *  Won't allow users to put units on hold under these line ID's
	 * @return
	 */

	@PropertyBeanAttribute(defaultValue = "")
	public String getShipLineId();

	/**
	 *  If true will display current product status and product spec on hold panel.
	 * @return
	 */

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowTrackingStatus();

	/**
	 * Maps product types to boolean values. If value is TRUE, Product type's SNs will be checked against product table records.
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, Boolean> isDisableProductIdCheckMap(Class<?> clazz);

	/**
	 * Maps product types to boolean values. Indicates that hold records must be created for owners of the products
	 * with the specified product types.
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, Boolean> isOwnerProductHoldMap(Class<?> clazz);

	/* List of product types for which scrap products functionality is disabled*/

	@PropertyBeanAttribute(defaultValue = "FRAME,ENGINE,MISSION")
	public String[] getScrapDisabledProductTypes();

	/**
	 * Set the default division that will be selected on Hold Panel.
	 * Default value will set the selection index to -1.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "DEFAULT_DIVISION")
	public String getDivision();


	@PropertyBeanAttribute(defaultValue = "ELP 034")
	public String[] getPlanCodes();

	/**
	 * Product Type combo-box values will be restricted to the types set in this property.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getAllowedProductTypes();
	
	/**
	 *  If true will display ProcessPointPanel on hold dialog.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isShowProcessPoint();

	/**
	 *  If true will display SpindleInput on hold dialog.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isShowSpindleInput();

	/**
	 *  If true load HoldAccessTypes using security group.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isLoadHoldAccessBySecurityGroups();

	/**
	 * Maximum number of records to be fetched at one time.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "2000")
	public int getMaxResultsetSize();
	
	/**
	 * Maximum number of records can be added to Qsr.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "2000")
	public int getMaxQsrSize();
	
	/**
	 *  If true will enable remove parts menu item on release panel.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isMassPartRemovalEnabled();
	
	/**
	 * Maps hold type value (HOLD_NOW, HOLD_AT_SHIPPING, KICKOUT) to hold access type ID from HOLD_ACCESS_TYPE_TBX.
	 * If a mapping exists for some access type and it is selected in the Type combo-box on the Hold Products dialog, 
	 * the associated hold type is automatically used. 
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> isHoldToAccessTypeMap(Class<?> clazz);
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAllowHoldEngWithShippedVin();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAllowHoldVinWithShippedEng();
}
