package com.honda.galc.dao.jpa.qics;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.ReuseProductResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.qics.ReuseProductResult;
import com.honda.galc.entity.qics.ReuseProductResultId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>IqsDao Class description</h3>
 * <p> IqsDao description </p>
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
public class ReuseProductResultDaoImpl extends BaseDaoImpl<ReuseProductResult, ReuseProductResultId> implements ReuseProductResultDao{

	   private static final String DELETE_BY_PRODUCTION_LOT_ENGINE = "delete from ReuseProductResult m where m.id.productId in(" + 
		"select e.productId from Engine e where e.productionLot = :productionLot)";

	    private static final String DELETE_BY_PRODUCTION_LOT_FRAME = "delete from ReuseProductResult m where m.id.productId in(" + 
		"select f.productId from frame f where f.productionLot = :productionLot)";


		@Transactional 
		public int deleteAllByProductionLot(ProductType productType,String productionLot) {
			if(ProductType.ENGINE == productType) 
				return executeUpdate(DELETE_BY_PRODUCTION_LOT_ENGINE, Parameters.with("productionLot", productionLot));
			else if(ProductType.FRAME == productType)
				return executeUpdate(DELETE_BY_PRODUCTION_LOT_FRAME, Parameters.with("productionLot", productionLot));
			else return 0;
		}


}
