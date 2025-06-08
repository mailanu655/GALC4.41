package com.honda.galc.client.product.process.engine.bearing.select.validator;

import com.honda.galc.client.product.process.engine.bearing.select.controller.BearingSelectController;
import com.honda.galc.client.product.validator.AbstractValidator;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.entity.product.Block;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BlockExistValidatorCommand</code> is ... .
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
public class BlockExistValidatorCommand extends AbstractValidator {

	private BearingSelectController controller;

	public BlockExistValidatorCommand(BearingSelectController controller) {
		this.controller = controller;
		setDetailedMessageTemplate(String.format("Block does not exist for SN"));
	}

	public boolean execute(String value) {

		String sn = value;
		BlockDao blockDao = ServiceFactory.getDao(BlockDao.class);
		Block block = blockDao.findBySn(sn);
		if (block != null) {
			getController().getModel().setBlock(block);
			getController().getView().getLogger().info(String.format("Found block by SN: %s: %s ", sn, block));
			return true;
		}
		setDetailedMessageTemplate(String.format("Block does not exist for SN: %s ", sn));
		return false;
	}

	protected BearingSelectController getController() {
		return controller;
	}
}
