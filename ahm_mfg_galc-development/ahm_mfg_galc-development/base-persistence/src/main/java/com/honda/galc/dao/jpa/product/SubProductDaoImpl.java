package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>SubProductDaoImpl Class description</h3>
 * <p> SubProductDaoImpl description </p>
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
 * Nov 2, 2010
 *
 *
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class SubProductDaoImpl extends BaseSubProductDaoImpl<SubProduct> implements SubProductDao{
    
	private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM SUB_PRODUCT_TBX A WHERE A.PRODUCT_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";

	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		SubProduct result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}
}
