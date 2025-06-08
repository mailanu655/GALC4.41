package com.honda.galc.dao.oif;

import java.util.List;

import com.honda.galc.entity.fif.SalesOrderFif;
import com.honda.galc.entity.fif.SalesOrderFifId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>SalesOrderFifDao.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SalesOrderFifDao.java description </p>
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
 * <TD>Justin Jiang</TD>
 * <TD>Feb 16, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */


public interface SalesOrderFifDao extends
		IDaoService<SalesOrderFif, SalesOrderFifId> {
	
	/**
	 * Method to get the FIF Code for a specific Frame product spec code 
	 * select the correct FIF_CODES value based on the greatest ORDER_SEQ_NO value
	 * @param productSpecCode
	 * @return
	 */
	public String getFIFCodeByProductSpec ( final FrameSpec productSpecCode );
	public List<SalesOrderFif> getSalesOrderFifBySpecCode ( final SalesOrderFifId salesOrderId );
}
