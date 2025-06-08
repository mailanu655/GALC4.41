package com.honda.galc.dao.jpa.product;


import java.util.List;

import com.honda.galc.dao.product.HeadHistoryDao;
import com.honda.galc.dto.ProductHistoryDto;
import com.honda.galc.entity.product.HeadHistory;
import com.honda.galc.entity.product.HeadHistoryId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>HeadHistoryDaoImpl Class description</h3>
 * <p> HeadHistoryDaoImpl description </p>
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
 * Jul 19, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class HeadHistoryDaoImpl extends ProductHistoryDaoImpl<HeadHistory,HeadHistoryId> implements HeadHistoryDao{

    private static final String PRODUCT_PROCESSED = "select count(1) from galadm.HEAD_HISTORY_TBX a where a.head_id = ?1 and a.process_point_id = ?2 and a.ACTUAL_TIMESTAMP <= ?3";

    private static final String FIND_PRODUCT_HISTORY_BY_PROCESS_POINT = "SELECT HEAD_ID AS PRODUCT_ID, NULL AS PRODUCT_SPEC_CODE, NULL AS ASSOCIATE_NO, ACTUAL_TIMESTAMP FROM HEAD_HISTORY_TBX FROM CRANKSHAFT_HISTORY_TBX WHERE PROCESS_POINT_ID = ?1 ";
    
	public boolean isProductProcessed(String productId, String processPointId,
			String startTimestamp) {
		return isProductProcessed(productId, processPointId,startTimestamp,PRODUCT_PROCESSED);
	}

	@Override
	protected String getProductIdName() {
		return "headId";
	}

	@Override
	protected String getProductIdColumnName() {
		return "head_id";
	}

	public List<ProductHistoryDto> findAllByProcessPoint(String processPointId, int rowNumber) {
		String sql = FIND_PRODUCT_HISTORY_BY_PROCESS_POINT +  " order by ACTUAL_TIMESTAMP desc fetch first " + rowNumber + " rows only";
		return findAllByNativeQuery(sql, Parameters.with("1", processPointId), ProductHistoryDto.class);
	}

}
