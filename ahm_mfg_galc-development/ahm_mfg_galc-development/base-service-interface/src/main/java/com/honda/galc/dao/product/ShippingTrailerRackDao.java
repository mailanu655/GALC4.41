package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ShippingTrailerRack;
import com.honda.galc.entity.product.ShippingTrailerRackId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>ShippingTrailerRackDao Class description</h3>
 * <p> ShippingTrailerRackDao description </p>
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
 * Jan 30, 2015
 *
 *
 */
public interface ShippingTrailerRackDao extends IDaoService<ShippingTrailerRack, ShippingTrailerRackId> {
	
	public List<ShippingTrailerRack> findAllByTrailerNumber(String trailerNumber);
	
	public int deleteRacks(String trailerNumber);
}
