package com.honda.galc.data;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Insert the description here.
 * <h4>Usage and Example</h4>
 * Insert the usage and example here.
 * <h4>Special Notes</h4>
 * Insert the special notes here if any.
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH></TR>
 * <TR>
 * <TD>T.Naoi</TD>
 * <TD>(2001/06/08 18:30:40)</TD>
 * <TD>0.1.0</TD>
 * <TD>(none)</TD>
 * <TD>Add DUPLICATE, NOT_DUPLICATE Value</TD>
 * </TR>
  * <TR>
 * <TD>M.Kinoshita</TD>
 * <TD>(2001/05/26 10:45:40)</TD>
 * <TD>0.1.0</TD>
 * <TD>(none)</TD>
 * <TD>Add comment</TD>
 * </TR>
* <TR><TD>T.Naoi</TD>
 * <TD>(2001/03/13 16:47:21)</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>J. Martinek</TD>
 * <TD>February 11th, 2004</TD>
 * <TD></TD>
 * <TD>@OIM282</TD>
 * <TD>PropertyServicesFacade Task bean constants.</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 19, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM004.1</TD>
 * <TD>Moved BLOCK_STORAGE and HEAD_STORAGE</TD>
 * </TR>
 * 
 * <TR>
 * <TD>D. Jensen</TD>
 * <TD>07/01/05</TD>
 * <TD>1.0</TD>
 * <TD>@DEJ002</TD>
 * <TD>Add constants in support of SR 6481 - LPDC Inquiry <BR>
 *     Add constants in support of SR 5433 - Emissions Maintenance Counter
 * </TD>
 * </TR>
 * <TR>
 * <TD>D. Jensen</TD>
 * <TD>09/07/05</TD>
 * <TD>1.0</TD>
 * <TD>@DEJ004</TD>
 * <TD>Add constants in support of HPDC Inquiry <BR>
 * </TD>
 * </TR>
 * <TR>
 * <TD>D. Jensen</TD>
 * <TD>12/05/05</TD>
 * <TD>1.0</TD>
 * <TD>@DEJ005</TD>
 * <TD>Add constants in support of LPDC Inquiry <BR>
 * </TD>
 * </TR>
 * 
 * </TABLE>
 * @see
 * @ver 0.1
 * @author T.Naoi
 */
public class LineSideContainerValue {
	// Common DataContainer Value
	public final static java.lang.String HOLD_STATUS = "Hold";
	public final static java.lang.String NOT_HOLD_STATUS = "NotHold";
	public final static java.lang.String NOT_COMPLETE = "0";
	public final static java.lang.String COMPLETE = "1";
	public final static java.lang.String CHECK_OK = "OK";
	public final static java.lang.String CHECK_NG = "NG";
	public final static java.lang.String CHECK_ACCEPT = "Accept";
	public final static java.lang.String CHECK_REJECT = "Reject";
	public final static java.lang.String COLLECTION_OK = "0";
	public final static java.lang.String COLLECTION_NG = "1";
	public final static java.lang.String OK_EVENT = "OK";
	public final static java.lang.String INITIALIZE_EVENT = "INITIALIZE";
	public final static java.lang.String FIND_EVENT = "FIND";
	public final static java.lang.String FINDBYDEST_EVENT = "FINDBYDEST_EVENT";
	public final static java.lang.String FINDBYITEM_EVENT = "FINDBYITEM_EVENT";
	public final static java.lang.String PRINT_EVENT = "PRINT_EVENT";
	public final static java.lang.String REMOVE_EVENT = "REMOVE";
	public final static java.lang.String ADD_EVENT = "ADD";
	public final static java.lang.String ADDCART_EVENT = "ADD_CART";
	public final static java.lang.String ADDSKID_EVENT = "ADD_SKID";
	public final static java.lang.String ADDRACK_EVENT = "ADD_RACK";
	public final static java.lang.String ADDPALLET_EVENT = "ADD_PALLET";
	public final static java.lang.String ADDCONTAINER_EVENT = "ADD_CONTAINER";
	public final static java.lang.String CREATE_EVENT = "CREATE";
	public final static java.lang.String UPDATE_EVENT = "UPDATE";
	public final static java.lang.String RECEIVE_EVENT = "RECEIVE";
	public final static java.lang.String CLOSE_EVENT = "CLOSE_EVENT";
	public final static java.lang.String CHANGE_EVENT = "CHANGE";
	public final static java.lang.String OVERRIDERECEIVE_EVENT = "OVERRIDERECEIVE";
	public final static java.lang.String SAVE_EVENT = "SAVE";
	public final static java.lang.String CANCEL_EVENT = "CANCEL";
	public final static java.lang.String NEXT_EVENT = "NEXT";
	public final static java.lang.String CLEAR_EVENT = "CLEAR";
	public final static java.lang.String NEXT_VIN = "NEXT_VIN";
	//@SR8855 - JJ
	public final static java.lang.String FINDBLOCKBYCONTAINER_EVENT = "FINDBLOCKBYCONTAINER_EVENT";
	public final static java.lang.String FINDHEADBYCONTAINER_EVENT = "FINDHEADBYCONTAINER_EVENT";
	public final static java.lang.String FINDPARTSBYCONTAINER_EVENT = "FINDPARTSBYCONTAINER_EVENT";
	// Only use at AF Off point
	public final static java.lang.String MISMATCH_SERIAL = "MismatchNo";
	public final static java.lang.String MISMATCH_TYPE = "MismatchType";
	public final static java.lang.String REINPUT_SERIAL = "ReInputEngNo";
	public final static java.lang.String CONTINUE_PROCESS = "ContinueProc";
	public final static java.lang.String DUPLICATE = "DUPLICATE";
	public final static java.lang.String NOT_DUPLICATE = "NOT_DUPLICATE";

	public final static java.lang.String ADD_DCNO = "ADD_DCNO";
	//@JM004.1 public final static String BLOCK_STORAGE = "LINE39";
	//@JM004.1 public final static String HEAD_STORAGE = "LINE40";
	public final static String LINE2 = "2";

	// @JM004.1 Shipping/Receiving values
	public final static String ADD_SHIPMENT_ENTRY = "ADD_SHIPMENT_ENTRY";
	public final static String REMOVE_SHIPMENT_ENTRY = "REMOVE_SHIPMENT_ENTRY";
	public final static String ADD_RACK_TO_SHIPMENT_ENTRY = "ADD_RACK_TO_SHIPMENT_ENTRY";
	public final static String ADD_PALLET_TO_SHIPMENT_ENTRY = "ADD_PALLET_TO_SHIPMENT_ENTRY";
	public final static String REMOVE_RACK_FROM_SHIPMENT_ENTRY = "REMOVE_RACK_FROM_SHIPMENT_ENTRY";
	public final static String REMOVE_PALLET_FROM_SHIPMENT_ENTRY = "REMOVE_PALLET_FROM_SHIPMENT_ENTRY";

	// @FJF Container values
	public final static String CONFIRM_CONTAINER_ID = "CONFIRM_CONTAINER_ID";
	public final static String CONFIRM_FIFO_NUMBER = "CONFIRM_FIFO_NUMBER";

	// @DEJ002 - SR 6481
	public final static String LPDC_INQ_PROC_PT_LPDC_ON = "LPDC-On";
	public final static String LPDC_INQ_PROC_PT_LPDC_INSP = "LPDC-Inspection";
	public final static String LPDC_INQ_PROC_PT_LPDC_OFF = "LPDC-Off";
	public final static String LPDC_INQ_PROC_PT_MC_ON = "MC-On";
	public final static String LPDC_INQ_STATUS_ALL = "All";
	public final static String LPDC_INQ_STATUS_DIRECT_PASS = "Direct Pass";
	public final static String LPDC_INQ_STATUS_PRE_HEAT = "Pre-Heat";
	public final static String LPDC_INQ_STATUS_SCRAP = "Scrap";
	public final static String LPDC_INQ_STATUS_REPAIR = "Repair";
	public final static String LPDC_INQ_STATUS_SHIP = "Ship";
	public final static String LPDC_INQ_STATUS_HOLD = "Hold";
	
	// @DEJ005 - HEAT 581069 12/05/05 
	public final static String LPDC_MACHINE_1 = "1";
	public final static String LPDC_MACHINE_2 = "2";
	public final static String LPDC_MACHINE_3 = "3";
	public final static String LPDC_MACHINE_4 = "4";
	public final static String LPDC_DIE_UNIT_ALL = "All";
	
	// @DEJ004 - HPDC Inquiry
	public final static String HPDC_INQ_PROCPT_HPDC_ON_3501 = "HPDC-On (3501)";
	public final static String HPDC_INQ_PROCPT_HPDC_ON_3402 = "HPDC-On (3402)";
	public final static String HPDC_INQ_PROCPT_HPDC_OFF_3501 = "HPDC-Off (3501)";
	public final static String HPDC_INQ_PROCPT_HPDC_OFF_3402 = "HPDC-Off (3402)";
	
	public final static String HPDC_INQ_PROCPT_BLOCK_QC_REPAIR = "Block QC Repair";
	public final static String HPDC_INQ_PROCPT_BLOCK_LEAK_TEST = "Block Leak Test";
	public final static String HPDC_INQ_PROCPT_BLOCK_DC_REPAIR_DC = "Block QC Repair (DC)";
	public final static String HPDC_INQ_PROCPT_BLOCK_DC_REPAIR_MC = "Block QC Repair (MC)";
	public final static String HPDC_INQ_PROCPT_BLOCK_MC_ON = "Block MC On";

	public final static String HPDC_INQ_STATUS_ALL = "All";
	public final static String HPDC_INQ_STATUS_DIRECT_PASS = "Direct Pass";
	public final static String HPDC_INQ_STATUS_PRE_HEAT = "Pre-Heat";
	public final static String HPDC_INQ_STATUS_SCRAP = "Scrap";
	
	//@DEJ002 - SR 5433
	public static final int SAMPLING_NOTHING = 0; // not sampling
	public static final int SAMPLING_EMISSION = 1; // emission sampling
	public static final int SAMPLING_DIMENSION = 2; // dimension sampling
	public static final int SAMPLING_BOTH = 3; // emission and dimension

	//@PLP001 - SR 9830
	public final static int NOT_HOLD = 0;
	
	// PROPERTY SERVICES
	public final static String PROPERTY_SERVICES_GET_PROPERTIES = "0";
	public final static String PROPERTY_SERVICES_REFRESH_COMPONENT = "1";
	public final static String PROPERTY_SERVICES_GET_PROPERTY = "2";
	
	//LET
	public final static String ACK_OK = "ACK";
	public final static String ACK_NG = "NAK";
	
	//ENGINE PLANT
	public final static String PART_CRANKSHAFT = "CRANKSHAFT";
	public final static String PART_CONROD = "CONROD";
	
	/*20070619 jma 2cx on/off number marriage*/
	public static final String TASK_TYPE_validatePartOnNumber = "validatePartOnNumber";
	public static final String TASK_TYPE_validatePartOffNumber = "validatePartOffNumber";
	public static final String TASK_TYPE_marryOnOffNumbers = "marryOnOffNumbers";
	
	public final static String FIND_USER = "FIND_USER";

	/**
	 * LineSideContainerValue constructor comment.
	 */
	public LineSideContainerValue() {
		super();
	}
}
