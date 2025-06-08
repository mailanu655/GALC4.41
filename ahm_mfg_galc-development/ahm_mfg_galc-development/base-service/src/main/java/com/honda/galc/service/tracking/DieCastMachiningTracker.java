package com.honda.galc.service.tracking;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ProductHistory;
/**
 * 
 * <h3>DieCastMachiningTracker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DieCastMachiningTracker description </p>
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
 * @author Paul Chou
 * Sep 29, 2010
 *
 * @param <T>
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public abstract class DieCastMachiningTracker <T extends BaseProduct> extends ProductTrackerBase<T>{
	@Autowired
	EngineDao engineDao;
	
	protected boolean isParentEngineShipped(DieCast product) {
		String ein = product.getEngineSerialNumber();
		if (StringUtils.isEmpty(ein)) {
			return false;
		}

		try{
			Engine engine = engineDao.findByKey(ein);

			if (engine == null) {
				getLogger().warn("Tracking, Engine:", ein, " associated with ", product.getClass().getSimpleName(),
						" :", product.getProductId(), " does not exit.");
				return false;
			}

			if (isProcessPointShipping(engine.getLastPassingProcessPointId())) {
				getLogger().warn("Tracking, Product will not be tracked as Engine associated with it is already shipped, :",
						" product:", product.getProductId());
				return true;
			}
		} catch (Exception e){
			 getLogger().error(e, "Exception to find Engine shipping status.");
		}
		
		return false;
	}
	
	@Override
	protected List<? extends ProductHistory> findAllProductHistory(T product, ProcessPoint processPoint) {
		ProductHistoryDao<? extends ProductHistory, ?> dao = getProductHistoryDao(product.getProductType());
		List<? extends ProductHistory> productHistoryList = dao.findAllByProductAndProcessPoint(product.getProductId(), processPoint.getProcessPointId());
		return productHistoryList;
	}
	

}
