package com.honda.galc.client.dc.fsm;

import java.util.List;

import com.honda.galc.entity.product.InstalledPart;

/**
 * <h3>IPartVerificationEvent</h3>
 * <h4>
 * Interface for Part verification events.
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
public interface IPartVerificationEvent {
	void receivedPartSn(InstalledPart partBean);
	void partSnOk(String partSN);
	void partsSnOk(List<InstalledPart> parts, InstalledPart part);
	void partSnMissing(InstalledPart part);
	void partSnNg(String partSN, String msgId, String userMsg);
	void reject();
	void partNoAction();
}
