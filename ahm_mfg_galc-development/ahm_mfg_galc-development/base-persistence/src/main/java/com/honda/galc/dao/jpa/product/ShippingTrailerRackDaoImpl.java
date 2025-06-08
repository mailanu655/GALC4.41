package com.honda.galc.dao.jpa.product;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ShippingTrailerRackDao;
import com.honda.galc.entity.product.ShippingTrailerRack;
import com.honda.galc.entity.product.ShippingTrailerRackId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>ShippingTrailerRackDaoImpl Class description</h3>
 * <p> ShippingTrailerRackDaoImpl description </p>
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
public class ShippingTrailerRackDaoImpl extends BaseDaoImpl<ShippingTrailerRack,ShippingTrailerRackId> implements ShippingTrailerRackDao {

	public List<ShippingTrailerRack> findAllByTrailerNumber(String trailerNumber) {
		return findAll(Parameters.with("id.trailerNumber", trailerNumber));
	}

	@Transactional
	public int deleteRacks(String trailerNumber) {
		return delete(Parameters.with("id.trailerNumber", trailerNumber));
	}

 
}
