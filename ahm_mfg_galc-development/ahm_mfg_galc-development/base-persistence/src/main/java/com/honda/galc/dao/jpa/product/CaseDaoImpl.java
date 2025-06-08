package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.product.CaseDao;
import com.honda.galc.entity.product.Case;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3>
 * Case DAO Impl Class.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 2, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140902</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class CaseDaoImpl extends BaseCaseDaoImpl<Case> implements CaseDao {

	private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM CASE_TBX A WHERE A.PRODUCT_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";
	
	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		Case result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}
}
