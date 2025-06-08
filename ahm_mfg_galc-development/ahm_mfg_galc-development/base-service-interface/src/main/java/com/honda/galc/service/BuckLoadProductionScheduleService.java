package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

/**
 * 
 * <h3>BuckOnProductionScheduleService</h3> 
 * <h3>The class is a service interface
 * for Buck On Production Service</h3> 
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
 * @author Hale Xie August 20, 2014
 * 
 */
public interface BuckLoadProductionScheduleService extends IService {

	/**
	 * Gets the schedule information of the next product.
	 * 
	 * @param data
	 *            the request data container. The tags in the data container:<br>
	 *            <li>PLAN_CODE 
	 *            <li>LAST_VIN
	 *            <li>Process Point ID
	 * 
	 * @return the next product schedule: the data container contains:<br>
	 *  		  <li>PLAN_CODE
	 *            <li>KD_LOT
	 *            <li>NEXT_VIN
	 *            <li>PRODUCT_SPEC_CODE
	 *            <li>FIF_CODES
	 *            <li>AFB
	 *            <li>ALC_INFO_CODE
	 */
	DataContainer getNextProductSchedule(DefaultDataContainer data);
	/**
	 * Gets the schedule information of the next product.
	 * 
	 * @param data
	 *            the request data container. The tags in the data container:<br>
	 *            <li>PLAN_CODE 
	 *            <li>VIN
	 *            <li>Process Point ID
	 * 
	 * @return the product schedule: the data container contains:<br>
	 *  		  <li>PLAN_CODE
	 *            <li>KD_LOT
	 *            <li>VIN
	 *            <li>PRODUCT_SPEC_CODE
	 *            <li>FIF_CODES
	 *            <li>AFB
	 *            <li>ALC_INFO_CODE
	 */
	
	DataContainer getProductSchedule(DefaultDataContainer data);
}
