package com.honda.galc.service.tracking;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.ProcessPoint;
/**
 * 
 * <h3>IProductSequenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IProductSequenceManager description </p>
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
 * @author Paul Chou
 * Sep 8, 2010
 *
 * @param <T>
 */

public interface IProductSequenceManager<T>{
	/**
	 * Add product to the end of the production line and maintain sequence of the line.
	 * @param product
	 * @param processPoint
	 */
	void addToLine(T product, ProcessPoint processPoint);
	
	/**
	 * Move product on the current production line
	 * @param product
	 * @param processPoint
	 */
	void moveOnLine(T product, ProcessPoint processPoint);
	
	/**
	 * The product is leaving the factory from 
	 * one of the following process points
	 * 
	 *   ProcessPointType.Scrap
	 *   ProcessPointType.ExceptionalOut
	 *   ProcessPointType.ProductExit
	 * 
	 * Remove the product from Product Sequence table.
	 * 
	 * @param product
	 * @param processPoint
	 */
	void productFactoryExit(T product, ProcessPoint processPoint);
	
	/**
	 * Logger for the InProcessProductManager
	 * @param logger
	 */
	void setLogger(Logger logger);
}
