package com.honda.galc.service.qics;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.entity.product.MbpnProduct;

/**
 * 
 * <h3>QicsUpdaterMbpnProduct</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsUpdaterMbpnProduct description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Apr 20, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 20, 2015
 */
public class QicsUpdaterMbpnProduct extends QicsUpdaterProduct<MbpnProduct>{
	@Autowired
	MbpnProductDao dao;
	
	@Override
	protected MbpnProduct findProductById(String productId) {
		return dao.findByKey(productId);
	}
}
