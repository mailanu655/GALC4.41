package com.honda.galc.client.product.action;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;

import com.honda.galc.checkers.AbstractBaseChecker;
import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.CheckResultsEvaluator;
import com.honda.galc.checkers.CheckerUtil;
import com.honda.galc.checkers.ICheckPoint;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.BaseCheckerData;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Ambica Gawarla
 * @date September 29, 2015
 */
public class ProductionLotSelectedAction  extends AbstractProductDataAction implements ICheckPoint<BaseCheckerData> {
	
	private static String checkPointName = CheckPoints.AFTER_SET_NEXT_UNIT.toString();
	private Logger logger;
	
	public ProductionLotSelectedAction(ApplicationMainPane appMainPane) {
		super(appMainPane);
		CheckPointsRegistry.getInstance().register(this, checkPointName);
	}
	
	
	
	public boolean executeCheckers(BaseCheckerData ProductData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>(); 
		SortedArrayList<MCAppChecker> appCheckers = CheckerUtil.getAppCheckers(ProductData.getCurrentProcessPoint(), getCheckPointName());
		
		for (MCAppChecker appChecker: appCheckers) {
			getLogger().info("Executing checker: " + appChecker.getCheckName());
			AbstractBaseChecker<ProductId> checker = CheckerUtil.createChecker(appChecker.getChecker(), ProductId.class);
			checker.setReactionType(appChecker.getReactionType());
			List<CheckResult> ckResults = checker.executeCheck(ProductData);
			if(ckResults != null && !ckResults.isEmpty()){
				ckResults.get(0).setResult(dispatchReactions(ckResults, ProductData));
				checkResults.addAll(ckResults);
			}
			
		}

		getLogger().info("Check Results size: " + checkResults.size());
		return CheckResultsEvaluator.evaluate(checkResults);
	}

	public String getCheckPointName() {
		return checkPointName;
	}
	

	
	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}



	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
