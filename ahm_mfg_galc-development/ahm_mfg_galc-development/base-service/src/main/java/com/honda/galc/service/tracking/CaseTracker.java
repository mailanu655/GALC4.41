package com.honda.galc.service.tracking;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.CaseDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Case;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;

/**
 * <h3>Class description</h3>
 * CaseTracker Class is used for M-Case and TC-Case tracking.
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
public class CaseTracker extends DieCastMachiningTracker<Case> {

	@Autowired
	CaseDao caseDao;
	
	@Autowired
	ProductResultDao productResultDao;

	@Override
	Case findProductById(String productId) {
		return getCaseDao().findByKey(productId);
	}

	@Override
	boolean isProductHistoryExist(Case product, ProcessPoint processPoint) {
		return getProductResultDao().hasProductHistory(product.getProductId(), processPoint.getProcessPointId());
	}

	@Override
	void saveProductHistory(ProductHistory productHistory) {
		getProductResultDao().save((ProductResult) productHistory);
		
	}

	@Override
	void updateTrackingAttributes(Case product, DailyDepartmentSchedule schedule) {
		getCaseDao().updateTrackingAttributes(product);
	}

	public CaseDao getCaseDao() {
		return caseDao;
	}
	
	public ProductResultDao getProductResultDao() {
		return productResultDao;
	}
}
