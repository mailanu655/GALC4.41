package com.honda.galc.client.datacollection.processor;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.Block;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ProductCheckUtil;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 * Validate engine and block marriage.
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>July 17, 2013</TD>
 * <TD>1.0</TD>
 * <TD>20130717</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */

public class BlockTransferProcessor extends PartSerialNumberProcessor {

	public BlockTransferProcessor(ClientContext context) {
		super(context);
	}

	public boolean confirmPartSerialNumber(PartSerialNumber psn) {
		boolean result = super.confirmPartSerialNumber(psn);
		return result && verifyBlockMarriage(psn);
	}
	
	private boolean verifyBlockMarriage(PartSerialNumber psn) {
		boolean result = false;
		ProductBean productBean = getController().getState().getProduct();
		Block block = ServiceFactory.getDao(BlockDao.class).findBySn(psn.getPartSn());
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		if(block == null) {
			handleException("Block does not exist: " + psn);
		} else {
			String esn = block.getEngineSerialNumber();
			if(esn == null) {
				handleException("The block does not have an EIN: " + psn);
			} else {
				result = esn.equalsIgnoreCase(productBean.getProductId());
				if(!result) {
					handleException("The block married to a different EIN.");
				}
				
				StringBuffer msg = new StringBuffer();
				List<String> externalChecks = productCheckUtil.externalProductOnHoldCheck();
				for (int i = 0; i < externalChecks.size(); i++) {
					msg.append(externalChecks.get(i));
					if (i != externalChecks.size() - 1) {
						msg.append(", ");
					}
				}
				handleException("External Product On Hold Check : "+ msg.toString());
			}
		}
		return result;
	}

}
