package com.honda.galc.client.datacollection.observer;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.product.Frame;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LotControlAfOnSeqPersistenceManager</code> is ... .
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
 * @created Jul 30, 2014
 */

public class LotControlAfOnSeqPersistenceManager extends LotControlFramePersistenceManager {

	public LotControlAfOnSeqPersistenceManager(ClientContext context) {
		super(context);
	}

	@Override
	protected void saveCollectedData(ProcessProduct state) {
		if (isSkippedProduct(state) && !context.getProperty().isSaveBuildResultsForSkippedProduct())
			return;

		super.saveCollectedData(state);
		saveAfOnSequence(state);
	}

	protected void saveAfOnSequence(ProcessProduct state) {

		String productId = state.getProductId();
		FrameDao productDao = getDao(FrameDao.class);
		Frame product = productDao.findByKey(productId);
		if (product.getAfOnSequenceNumber() != null) {
			Logger.getLogger().warn("AfOnSequence already exists, sequence update will be skipped, product:", product.toString(), ", sequence: ", product.getAfOnSequenceNumber().toString());
			return;
		}

		Integer max = productDao.maxAfOnSequenceNumber();
		if (max == null) {
			max = 0;
		}
		int sequence = max + 1;
		product.setAfOnSequenceNumber(sequence);
		productDao.update(product);
		String msg = String.format("Created AfOnSequence for product : %s, AfOnSequence: %s", product, product.getAfOnSequenceNumber());
		Logger.getLogger().info(msg);
	}
}
