package com.honda.galc.service.tracking;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>EngineTracker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineTracker description </p>
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
 * Sep 8, 2010
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class EngineTracker extends ProductTracker<Engine>{
	//@Autowired
	EngineDao engineDao;
	
	@Override
	protected ProductSpec getProductSpec(String productSpecCode) {
		try {
			EngineSpecDao dao = ServiceFactory.getDao(EngineSpecDao.class);
			return dao.findByKey(productSpecCode);
		} catch (Exception e) {
			getLogger().error(e, "Failed to find Engine Product Spec:" + productSpecCode);
			return null;
		}
	}
	
	public EngineDao getProductDao() {
		if(engineDao == null)
			engineDao = ServiceFactory.getDao(EngineDao.class);
		
		return engineDao;
	}

	@Override
	Engine findProductById(String productId) {
		return getProductDao().findByKey(productId);
	}

}
