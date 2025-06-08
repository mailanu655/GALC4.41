package com.honda.galc.client.product.action;

import com.honda.galc.client.product.process.AbstractProcessController;

/**
 * 
 * 
 * <h3>AbstractProcessAction Class description</h3>
 * <p> AbstractProcessAction description </p>
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
 * @author Jeffray Huang<br>
 * Mar 10, 2014
 *
 *
 */

@SuppressWarnings("unchecked")
public abstract class AbstractProcessAction extends AbstractAction {
	private AbstractProcessController processController;
	
	public AbstractProcessAction(AbstractProcessController processController) {
		this.processController = processController;
	}

	protected AbstractProcessController getProcessController() {
		return processController;
	}

	protected void setProcessController(AbstractProcessController processController) {
		this.processController = processController;
	}
}
