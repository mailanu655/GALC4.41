package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.enumtype.TrailerStatus;
import com.honda.galc.entity.product.ShippingTrailer;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>ShippingTrailerDao Class description</h3>
 * <p> ShippingTrailerDao description </p>
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
 * Feb 24, 2015
 *
 *
 */
public interface ShippingTrailerDao extends IDaoService<ShippingTrailer, String> {

	public List<ShippingTrailer> findAllAvaiableTrailers();
	
	public int updateStatus(String trailerNumber, TrailerStatus status);
}
