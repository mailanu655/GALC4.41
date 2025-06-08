package com.honda.galc.service.tracking;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>SubProductTracker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SubProductTracker description </p>
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
 * Nov 27, 2010
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class SubProductTracker extends ProductTracker<SubProduct>{
	//@Autowired
	SubProductDao subProductDao;
	
	@Override
	protected ProductSpec getProductSpec(String productSpecCode) {
		try {
			FrameSpecDao dao = ServiceFactory.getDao(FrameSpecDao.class);
			return dao.findByKey(productSpecCode);
		} catch (Exception e) {
			getLogger().error(e, "Failed to find SubProduct Spec:" + productSpecCode);
			return null;
		}
	}

	public SubProductDao getProductDao() {
		if(subProductDao == null)
			subProductDao = ServiceFactory.getDao(SubProductDao.class);
		return subProductDao;
	}

	@Override
	SubProduct findProductById(String productId) {
		return getProductDao().findByKey(productId);
	}

}
