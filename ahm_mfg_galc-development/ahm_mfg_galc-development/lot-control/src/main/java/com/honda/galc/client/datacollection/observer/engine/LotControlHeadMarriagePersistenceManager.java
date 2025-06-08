package com.honda.galc.client.datacollection.observer.engine;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.LotControlPersistenceManagerExt;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.ProductBuildResult;

/**
 * 
 * <h3>LotControlHeadMarriagePersistenceManager Class description</h3>
 * <p> LotControlHeadMarriagePersistenceManager description </p>
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
 * Aug 10, 2012
 *
 *
 */
public class LotControlHeadMarriagePersistenceManager extends
		LotControlPersistenceManagerExt {

	private static final String HEAD_DC ="HEAD DC";
	
	public LotControlHeadMarriagePersistenceManager(ClientContext context) {
		super(context);
	}

	@Override
	protected void saveCollectedData(ProcessProduct state) {
		//skip product will not save build results
		if(isSkippedProduct(state) && !context.getProperty().isSaveBuildResultsForSkippedProduct()) return;
				
		super.saveCollectedData(state);
		saveEngineSerialNumberToHead(state);
	}
	
	private void saveEngineSerialNumberToHead(ProcessProduct state) {
		ProductBuildResult result = findProductBuildResult(state.getProduct().getPartList(), HEAD_DC);
		if(result == null) return;
		HeadDao headDao = getDao(HeadDao.class);
		Head head = headDao.findByKey(result.getPartSerialNumber());
		if(head == null) {
			Logger.getLogger().error("Head " + result.getPartSerialNumber() + "does not exist");
			return;
		}
		
		if(head.getEngineFiringFlag()) {
			getDao(EngineDao.class).updateFiringFlag(state.getProductId(), head.getEngineFiringFlag());
			Logger.getLogger().info("update engine firing flag from head " + head.getEngineFiringFlagValue());
		}
		
        getDao(HeadDao.class).updateEngineSerialNumber(result.getPartSerialNumber(),state.getProductId());
        getDao(HeadDao.class).updateDunnage(result.getPartSerialNumber(), null);
        Logger.getLogger().info("engine number " + state.getProductId()  + " is associated with " + result.getPartSerialNumber() + " in Head table");
	}
	
	
}
