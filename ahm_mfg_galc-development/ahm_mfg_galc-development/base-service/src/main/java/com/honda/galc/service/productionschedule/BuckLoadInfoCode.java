package com.honda.galc.service.productionschedule;

/**
 * <h3>BearingSelectView</h3>
 * <h3>The class defines the info codes for {@link BuckLoadProductionScheduleServiceImpl}  </h3>
 * <h4>  </h4>
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
 * </TABLE>.
 *
 * @see BuckLoadProductionScheduleServiceImpl
 * @author Hale Xie
 * August 21, 2014
 */
public enum BuckLoadInfoCode {
	
	/** The ok. */
	OK					("01", "Request Processed OK"),
	
	/** The duplicate request. */
	DUPLICATE_REQUEST	("10", "Duplicate Request"),
	
	/** The skip ahead. */
	SKIP_AHEAD			("11", "Skip Ahead"),
	
	/** The invalid sn. */
	INVALID_SN			("12", "Invalid SN"),
	
	/** The invalid request. */
	INVALID_REQUEST		("12", "Invalid Request"),
	
	/** The no schedule. */
	NO_SCHEDULE			("13", "No valid schedule"),
	
	/** The unknown error. */
	UNKNOWN_ERROR		("99", "Unknown Error");
	
	/** The info code. */
	private String infoCode;
	
	/** The info msg. */
	private String infoMsg;
	
	/**
	 * Instantiates a new buck load info code.
	 *
	 * @param code the code
	 * @param msg the msg
	 */
	private BuckLoadInfoCode(String code, String msg) {
		infoCode = code;
		infoMsg = msg;
	}

	/**
	 * Gets the info code.
	 *
	 * @return the info code
	 */
	public String getInfoCode() {
		return infoCode;
	}

	/**
	 * Gets the info msg.
	 *
	 * @param productId the product id
	 * @return the info msg
	 */
	public String getInfoMsg(String productId) {
		if(infoMsg.contains("$1")){
			return infoMsg.replace("$1", productId == null ? "" : productId);
		}else{
			return infoMsg;
		}
	}
}
