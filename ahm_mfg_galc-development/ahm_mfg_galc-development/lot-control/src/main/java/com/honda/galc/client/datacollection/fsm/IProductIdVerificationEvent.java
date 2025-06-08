package com.honda.galc.client.datacollection.fsm;

import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.common.message.MessageType;

/**
 * <h3>IPartVerificationEvent</h3>
 * <h4>
 * State Machine interface for Product Id verification events.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public interface IProductIdVerificationEvent {
	void receivedProductId(ProductBean product);
	void productIdOk(ProductBean product);
	void productIdNg(ProductBean product, String msgId, String userMsg);
	void clearMessage();
	void showMessage(MessageType messageType, String title, String msg);
}
