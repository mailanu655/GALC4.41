package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.product.CrankshaftHistoryDao;
import com.honda.galc.dto.ProductHistoryDto;
import com.honda.galc.entity.product.CrankshaftHistory;
import com.honda.galc.entity.product.CrankshaftHistoryId;
import com.honda.galc.service.Parameters;

public class CrankshaftHistoryDaoImpl extends
		ProductHistoryDaoImpl<CrankshaftHistory, CrankshaftHistoryId> implements
		CrankshaftHistoryDao {

    private static final String PRODUCT_PROCESSED = "select count(1) from galadm.CRANKSHAFT_HISTORY_TBX a where a.crankshaft_id = ?1 and a.process_point_id = ?2 and a.ACTUAL_TIMESTAMP <= ?3";
    
    private static final String FIND_PRODUCT_HISTORY_BY_PROCESS_POINT = "SELECT CRANKSHAFT_ID AS PRODUCT_ID, NULL AS PRODUCT_SPEC_CODE, NULL AS ASSOCIATE_NO, ACTUAL_TIMESTAMP FROM CRANKSHAFT_HISTORY_TBX WHERE PROCESS_POINT_ID = ?1 ";
    
	public boolean isProductProcessed(String productId, String processPointId,
			String startTimestamp) {
		return isProductProcessed(productId, processPointId,startTimestamp,PRODUCT_PROCESSED);
	}

	@Override
	protected String getProductIdName() {
		return "crankshaftId";
	}
	
	@Override
	protected String getProductIdColumnName() {
		return "crankshaft_id";
	}

	public List<ProductHistoryDto> findAllByProcessPoint(String processPointId, int rowNumber) {
		String sql = FIND_PRODUCT_HISTORY_BY_PROCESS_POINT +  " order by ACTUAL_TIMESTAMP desc fetch first " + rowNumber + " rows only";
		return findAllByNativeQuery(sql, Parameters.with("1", processPointId), ProductHistoryDto.class);
	}
}
