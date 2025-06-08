package com.honda.galc.dao.jpa.product;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ShippingTrailerDao;
import com.honda.galc.entity.enumtype.TrailerStatus;
import com.honda.galc.entity.product.ShippingTrailer;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ShippingTrailerInfoDaoImpl Class description</h3>
 * <p> ShippingTrailerInfoDaoImpl description </p>
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
 * Jun 1, 2012
 *
 *
 */
public class ShippingTrailerDaoImpl extends BaseDaoImpl<ShippingTrailer,String> implements ShippingTrailerDao {

	public List<ShippingTrailer> findAllAvaiableTrailers(){
		return findAll(Parameters.with("statusId",0),new String[]{"trailerNumber"});
	}
	
	@Transactional
	public int updateStatus(String trailerNumber, TrailerStatus status) {
		return update(Parameters.with("statusId", status.getId()),Parameters.with("trailerNumber", trailerNumber));
	}
 
}
