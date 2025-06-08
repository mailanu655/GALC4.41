package com.honda.galc.service.tracking;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>FrameTracker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrameTracker description </p>
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
public class FrameTracker extends ProductTracker<Frame> {
	//@Autowired
	FrameDao frameDao;
	
	
	@Override
	protected ProductSpec getProductSpec(String productSpecCode) {
		try {
			FrameSpecDao dao = ServiceFactory.getDao(FrameSpecDao.class);
			return dao.findByKey(productSpecCode);
		} catch (Exception e) {
			getLogger().error(e, "Failed to find Frame Product Spec:" + productSpecCode);
			return null;
		}
	}
	
	public FrameDao getProductDao() {
		if(frameDao == null)
			frameDao = ServiceFactory.getDao(FrameDao.class);
		return frameDao;
	}

	@Override
	Frame findProductById(String productId) {
		return productId!= null?getProductDao().findByKey(productId):null;
	}

}
