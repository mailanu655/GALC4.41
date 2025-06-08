package com.honda.galc.service.qics;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.entity.product.Conrod;

/**
 * <h3>QicsUpdaterCrankshaft.java Class description</h3>
 * <p> QicsUpdaterCrankshaft.java description </p>
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
 * @author K Maharjan (vf036360)
 *Aug 27, 2014
 */

public class QicsUpdaterConrod 
	extends QicsUpdaterDiecast<Conrod>{

		@Autowired
		ConrodDao dao;
		
		@Override
		protected Conrod findProductById(String productId) {
			return dao.findByKey(productId);
		}

}
