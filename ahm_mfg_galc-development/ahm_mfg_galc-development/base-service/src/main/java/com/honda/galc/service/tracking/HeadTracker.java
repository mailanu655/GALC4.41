package com.honda.galc.service.tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.HeadHistoryDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.HeadHistory;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>HeadTracker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HeadTracker description </p>
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
 * <TD>Jul 13, 2011</TD>
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
 * @since Jul 13, 2011
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class HeadTracker extends DieCastMachiningTracker<Head> {

	@Autowired
	HeadHistoryDao headHistoryDao;
	
	@Autowired
	HeadDao headDao;
	
	@Override
	boolean isProductHistoryExist(Head product, ProcessPoint processPoint) {
		return getHeadHistoryDao().hasProductHistory(product.getHeadId(), processPoint.getProcessPointId());
	}

	private HeadHistoryDao getHeadHistoryDao() {
		if(headHistoryDao == null)
			headHistoryDao = ServiceFactory.getDao(HeadHistoryDao.class);
		
		return headHistoryDao;
	}

	@Override
	@Transactional
	void updateTrackingAttributes(Head product, DailyDepartmentSchedule schedule) {
		try {
			headDao.updateTrackingAttributes(product);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, "Failed to update tracking attributes for Head, headId=", product.getProductId());
		}
		
	}

	@Override
	protected boolean isProductShipped(Head product) {
		
		return super.isProductShipped(product) || isParentEngineShipped(product);
	}
	
	@Override
	void saveProductHistory(ProductHistory productHistory) {
		getHeadHistoryDao().save((HeadHistory)productHistory);
	}
	
	public HeadDao getHeadDao() {
		if(headDao == null)
			headDao = ServiceFactory.getDao(HeadDao.class);
		return headDao;
	}

	@Override
	Head findProductById(String productId) {
		return getHeadDao().findByKey(productId);
	}
}
