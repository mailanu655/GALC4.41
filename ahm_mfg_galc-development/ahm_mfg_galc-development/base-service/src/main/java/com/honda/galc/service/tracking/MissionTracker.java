package com.honda.galc.service.tracking;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.MissionDao;
import com.honda.galc.dao.product.MissionSpecDao;
import com.honda.galc.entity.product.Mission;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3>
 * MissionTracker Class is used for Mission tracking.
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
 * <TD>Sep. 25, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140925</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class MissionTracker extends ProductTracker<Mission> {

	@Autowired
	MissionDao missionDao;
	
	@Override
	protected ProductSpec getProductSpec(String productSpecCode) {
		try {
			MissionSpecDao dao = ServiceFactory.getDao(MissionSpecDao.class);
			return dao.findByKey(productSpecCode);
		} catch (Exception e) {
			getLogger().error(e, "Failed to find Mission Product Spec:" + productSpecCode);
			return null;
		}
	}
	
	public MissionDao getProductDao() {
		return missionDao;
	}

	@Override
	Mission findProductById(String productId) {
		return getProductDao().findByKey(productId);
	}

}
