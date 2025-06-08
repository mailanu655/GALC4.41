package com.honda.galc.client.product.receiving;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.HeadBuildResultDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3>
 * The processor is responsible for saving head related information.
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
public class HeadReceivingProcessor extends ProductReceivingProcessor {

	private HeadDao headDao;
	private HeadBuildResultDao buildResultDao;
	
	public HeadReceivingProcessor() {
		super();
	}
	
	public void saveProduct(String productId) throws ProductReceivingException {
		if(getHeadDao().findByKey(productId) != null) {
			Logger.getLogger().warn("Head exists in system. Duplicate head ID entered: " + productId);
			throw new ProductReceivingException("Product ID already in system " + productId + ".");
		}
		Head head = (Head) ProductTypeUtil.createProduct(getData().getProductType().getProductName(), productId);
		head.setModelCode(getData().getNewModelCode());
		head.setLastPassingProcessPointId(getApplicationContext().getProcessPointId());
		getHeadDao().save(head);
		Logger.getLogger().info("Head saved: " + productId);
	}

	public void saveBuildResult(String newId, String partName, String originalId) throws ProductReceivingException {
		HeadBuildResult result = (HeadBuildResult) ProductTypeUtil.createBuildResult(getData().getProductType().getProductName(), newId, partName);
		result.setResultValue(originalId);
		result.setAssociateNo(getApplicationContext().getUserId());
		result.setInstalledPartStatus(InstalledPartStatus.OK);
		getBuildResultDao().save(result);
		Logger.getLogger().info("Head build results saved for: " + newId);
	}

	public HeadDao getHeadDao() {
		if(headDao == null) {
			headDao = ServiceFactory.getDao(HeadDao.class);
		}
		return headDao;
	}

	public HeadBuildResultDao getBuildResultDao() {
		if(buildResultDao == null) {
			buildResultDao = ServiceFactory.getDao(HeadBuildResultDao.class);
		}
		return buildResultDao;
	}
}
