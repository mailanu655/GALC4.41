package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.product.ProductShipping;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>ProductShippingDao Class description</h3>
 * <p> ProductShippingDao description </p>
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
 * @author Jeffray Huang<br>
 * Sep 11, 2014
 *
 *
 */
public interface ProductShippingDao extends IDaoService<ProductShipping, String> {
	
	public List<ProductShipping> findAllActiveDunnages(); 
	
	public List<String> findAllConsumedDunnages(String trailerNumber);
	
	public List<ProductShipping> findAllShippments(String trailerNumber,String dunnage);
	
	public List<ProductShipping> findAllShippments(String trailerNumber);
	
	public int removeDunnage(String trailerNumber,String dunnage);
	
	public int shipTrailer(String trailerNumber,String trackingStaus,String processPointId);
	
	public void completeTrailer(String trailerNumber,String trackingStaus, String processPointId);
	public List<String> findAllShippingDestinations();

	public List<String> getTrailers(String destination, String dateStr);
	
	public List<ProductShipping> findAllDunnages(String trailerNumber, String shipDate);
	
	public List<ProductShipping> getAllByDunnage(String trailernumber, String dunnage, Date shipDate);
}
