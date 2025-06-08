package com.honda.galc.service.datacollection.task;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.RequiredPartsChecker;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.task.CollectorTask;
/**
 * 
 * <h4> Description </h4>
 * <p> Set test fire flag if required part check fails </p>
 * 
 * @author Dmitri Kouznetsov
 * @since December 12, 2018
 */
public class EngineFiringOnPartFailTask extends CollectorTask {
	
	public EngineFiringOnPartFailTask(HeadlessDataCollectionContext context, String processPointId) {
		super(context, processPointId);
	}

	@Override
	public void execute() {
		if (context.getProductType() != ProductType.ENGINE) return;
		this.setFiringFlagOnPartFail();
	}
	
	void setFiringFlagOnPartFail(){
		Engine product = (Engine)context.getProduct();
		if (product == null) return;
		getLogger().info("FiringFlagOnPartFail task started.");
		BaseProductCheckerData checkerData = new BaseProductCheckerData();
		checkerData.setProductId(context.getProductId());
		checkerData.setProductType(ProductType.ENGINE.toString());
		checkerData.setCurrentProcessPoint(context.getProcessPointId());
		
		RequiredPartsChecker requiredPartChecker = new RequiredPartsChecker();
		List<CheckResult> checkResults = requiredPartChecker.executeCheck(checkerData);
		if (checkResults != null && !checkResults.isEmpty()) {
			for (CheckResult checkResult : checkResults)
				getLogger().info(checkResult.getCheckMessage());
			this.getDao().updateEngineFiringFlag(product.getId(), (short)1);
			getLogger().info("Firing flag for product " + product.getId() + " set.");
		} else {
			getLogger().info("Required part check passed. Firing flag for product " + product.getId() + " not set.");
		}
		getLogger().info("FiringFlagOnPartFail task finished.");
	}
	
	public EngineDao getDao() {
		return ServiceFactory.getDao(EngineDao.class);
	}
}
