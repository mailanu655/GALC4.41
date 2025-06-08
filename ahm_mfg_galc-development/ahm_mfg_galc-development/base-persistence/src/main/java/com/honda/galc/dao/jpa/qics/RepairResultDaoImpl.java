package com.honda.galc.dao.jpa.qics;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.RepairResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.RepairResult;
import com.honda.galc.entity.qics.RepairResultId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>RepairResultDao Class description</h3>
 * <p> RepairResultDao description </p>
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
 * Apr 1, 2011
 *
 *
 */
public class RepairResultDaoImpl extends BaseDaoImpl<RepairResult, RepairResultId> implements RepairResultDao{

    
    private static final String DELETE_BY_PRODUCTION_LOT_ENGINE = "delete from RepairResult m where m.id.productId in(" + 
	"select e.productId from Engine e where e.productionLot = :productionLot)";

    private static final String DELETE_BY_PRODUCTION_LOT_FRAME = "delete from RepairResult m where m.id.productId in(" + 
	"select f.productId from frame f where f.productionLot = :productionLot)";
    
    private static final String FIND_ALL_BY_PRODUCT_ID_AND_DEFECT_ID = "select m from RepairResult m where m.id.productId = :productId "+
    	"and m.id.defectresultid = :defectResultId";

	@Transactional 
	public int deleteAllByProductionLot(ProductType productType,String productionLot) {
		if(ProductType.ENGINE == productType) 
			return executeUpdate(DELETE_BY_PRODUCTION_LOT_ENGINE, Parameters.with("productionLot", productionLot));
		else if(ProductType.FRAME == productType)
			return executeUpdate(DELETE_BY_PRODUCTION_LOT_FRAME, Parameters.with("productionLot", productionLot));
		else return 0;
	}
	
	public List<RepairResult> findAllByProductIdandDefectId(String productId, int defectResultId) {
		Parameters params = Parameters.with("productId", productId).put("defectResultId", defectResultId);
		return findAllByQuery(FIND_ALL_BY_PRODUCT_ID_AND_DEFECT_ID, params);
		
	}


}
