package com.honda.galc.client.dc.fsm;

import com.honda.galc.fsm.IActionType;

public interface ActionTypes {
	
	/* State initialization */
	public static interface INIT extends IActionType{}; 
	
	
	public static interface SKIP extends IActionType{};
	
	/* Failed verification */
	public static interface NG extends IActionType{};
	
	/* Verification OK */
	public static interface OK extends IActionType{};
	
	/* User action to cancel operation */
	public static interface CANCEL extends IActionType{};
	
	/* Completed work on current state e.g. before transit to another state. 
	Could be used to sign for Abort Job and set total torque status etc. */
	public static interface COMPLETE extends IActionType{};
	
	/* User action to skip part */
	public static interface SKIP_PART extends IActionType{};  
	
	/* User action to skip Engine/Car */
	public static interface SKIP_PRODUCT extends IActionType{};
	
	/* reject a part or torque */
	public static interface REJECT extends IActionType{};
	
	/* Abort the operation on current state, for example, max measurement attempts exceeded*/
	public static interface ABORT extends IActionType{};
	
	/* Received data */
	public static interface RECEIVED extends IActionType{}; 
	
	/* To notify client errors */
	public static interface ERROR extends IActionType{};
	
	/* To broadcasting message */ 
	public static interface MESSAGE extends IActionType{};
	
	/* Play sound indicating no user action required for a product */ 
	public static interface NO_ACTION extends IActionType{};
}
