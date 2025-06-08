package com.honda.galc.service.qics;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.product.Frame;

/**
 * 
 * <h3>QicsUpdaterFrame</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsUpdaterFrame description </p>
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
 * <TD>May 11, 2011</TD>
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
 * @since May 11, 2011
 */

public class QicsUpdaterFrame extends QicsUpdaterProduct<Frame> {

	@Autowired
	FrameDao dao;
	
	@Override
	protected Frame findProductById(String productId) {
		return dao.findByKey(productId);
	}


}
