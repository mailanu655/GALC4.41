package com.honda.galc.dao.jpa;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>CommonDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CommonDaoImpl description </p>
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
 * <TD>Jul 22, 2011</TD>
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
 * @since Jul 22, 2011
 * @param <E>
 * @param <K>
 */
public class CommonDaoImpl<E,K> extends BaseDaoImpl<E, K> {
	@Transactional
	protected void updateTrackingAttributes(BaseProduct product){
		update(Parameters.with("lastPassingProcessPointId", product.getLastPassingProcessPointId())
				.put("trackingStatus", product.getTrackingStatus()),
				Parameters.with("productId", product.getProductId()));
	}
	
	@Transactional
	public void updateDefectStatus(String productId, DefectStatus status) {
		update(Parameters.with("defectStatus", status.getId()), Parameters.with("productId", productId));
		
	}
	
}
