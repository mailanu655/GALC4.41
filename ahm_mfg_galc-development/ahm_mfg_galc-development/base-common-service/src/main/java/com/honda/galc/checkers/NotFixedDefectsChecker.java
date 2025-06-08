package com.honda.galc.checkers;

import java.util.List;

import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.util.ProductCheckUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>NotFixedDefectsChecker</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Nov 8, 2017
 */
public class NotFixedDefectsChecker extends OutstandingDefectsChecker {

	@Override
	protected String getHeaderMessage() {
		return "Not Fixed Defects Check";
	}

	@Override
	protected List<String> findDefects(BaseProduct product, ProcessPoint processPoint) {
		ProductCheckUtil checkUtil = new ProductCheckUtil(product, processPoint);
		List<String> defectResults = checkUtil.notFixedDefects();
		return defectResults;
	}
}
