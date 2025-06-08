package com.honda.galc.client.datacollection.processor;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.device.dataformat.PartSerialNumber;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>IqaProcessor</code> is ... .
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
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jul 28, 2014
 */
public class IqaProcessor extends PartSerialNumberProcessor {

	private final static String IQA_PATTERN = ".+%s.+";

	public IqaProcessor(ClientContext context) {
		super(context);
	}

	@Override
	protected void checkPartSerialNumber(PartSerialNumber partnumber) {
		if (partnumber == null || StringUtils.isBlank(partnumber.getPartSn())) {
			handleException("Received part serial number is null!");
		}
		installedPart.setPartSerialNumber(partnumber.getPartSn());
		ProductBean product = ((DataCollectionState) getController().getState()).getProduct();
		String ein = product.getProductId();
		String psn = partnumber.getPartSn();
		String pattern = String.format(IQA_PATTERN, ein);
		if (!psn.matches(pattern)) {
			String msg = "Part serial number:" + installedPart.getPartSerialNumber() + " verification failed. Masks:*EIN*";
			handleException(msg);
		}
		installedPart.setPartId(getController().getState().getCurrentLotControlRulePartList().get(0).getId().getPartId());
	}
}
