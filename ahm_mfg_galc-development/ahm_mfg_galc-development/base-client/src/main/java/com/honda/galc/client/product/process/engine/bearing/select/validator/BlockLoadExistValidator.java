package com.honda.galc.client.product.process.engine.bearing.select.validator;

import com.honda.galc.client.product.process.engine.bearing.select.controller.BearingSelectController;
import com.honda.galc.client.product.validator.AbstractValidator;
import com.honda.galc.dao.product.BlockLoadDao;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BlockLoadExistValidator</code> is ... .
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
 */
public class BlockLoadExistValidator extends AbstractValidator {

	private BearingSelectController controller;

	public BlockLoadExistValidator(BearingSelectController controller) {
		this.controller = controller;
		setDetailedMessageTemplate("Block MCB does not exist in block load table");
	}

	public boolean execute(String value) {

		BlockLoad blockLoad = ServiceFactory.getDao(BlockLoadDao.class).findByKey(value);
		if (blockLoad != null) {
			return true;
		}
		setDetailedMessageTemplate(String.format("Block MCB : %s does not exist in block load table", value));
		return false;
	}

	protected BearingSelectController getController() {
		return controller;
	}
}
