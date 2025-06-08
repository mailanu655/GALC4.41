package com.honda.galc.client.product.receiving;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3>
 * The processor is responsible for saving block related information.
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
 * <TD>Apr 22, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140422</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class BlockReceivingProcessor extends ProductReceivingProcessor {
	private BlockDao blockDao;
	private BlockBuildResultDao buildResultDao;
	
	public BlockReceivingProcessor() {
		super();
	}
	
	public void saveProduct(String productId) throws ProductReceivingException {
		if(getBlockDao().findByKey(productId) != null) {
			Logger.getLogger().warn("Block exists in system. Duplicate block ID entered: " + productId);
			throw new ProductReceivingException("Product ID already in system " + productId + ".");
		}
		Block block = (Block) ProductTypeUtil.createProduct(getData().getProductType().getProductName(), productId);
		block.setModelCode(getData().getNewModelCode());
		block.setLastPassingProcessPointId(getApplicationContext().getProcessPointId());
		getBlockDao().save(block);
		Logger.getLogger().info("Block saved: " + productId);
	}

	public void saveBuildResult(String newId, String partName, String originalId) throws ProductReceivingException {
		BlockBuildResult result = (BlockBuildResult) ProductTypeUtil.createBuildResult(getData().getProductType().getProductName(), newId, partName);
		result.setResultValue(originalId);
		result.setAssociateNo(getApplicationContext().getUserId());
		result.setInstalledPartStatus(InstalledPartStatus.OK);
		getBuildResultDao().save(result);
		Logger.getLogger().info("Block build results saved for: " + newId);
	}

	public BlockDao getBlockDao() {
		if(blockDao == null) {
			blockDao = ServiceFactory.getDao(BlockDao.class);
		}
		return blockDao;
	}

	public BlockBuildResultDao getBuildResultDao() {
		if(buildResultDao == null) {
			buildResultDao = ServiceFactory.getDao(BlockBuildResultDao.class);
		}
		return buildResultDao;
	}
}
