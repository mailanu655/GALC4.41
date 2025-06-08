package com.honda.galc.client.teamleader;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;

/**
 * 
 * <h3>ProductCheckUtil Class description</h3>
 * <p> ProductCheckUtil description </p>
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
 * @author Paul Chou<br>
 * May 5, 2015
 *
 *
 */
public class ManualLotControlRepairMbpnController extends 
	ManualLotControlRepairController<MbpnProduct, InstalledPart> {

	public ManualLotControlRepairMbpnController(MainWindow mainWin, ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}
	
	public ManualLotControlRepairMbpnController(ApplicationContext applicationContext, Application application,
			ManualLotControlRepairPanel repairPanel) {
		super(applicationContext, application, repairPanel);
	}

	@Override
	protected void loadProductBuildResults() {
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		productBuildResulits = installedPartDao.findAllByProductId(product.getProductId());
		
		loadInstalledParts(productBuildResulits);
		if(areAllPartsShippable())
			this.isRepaired = true;
	}
	
	private boolean areAllPartsShippable() {
		for (PartResult eachPartResult : lotControlPartResultData) {
			if (eachPartResult.getShipStatus() != InstalledPartStatus.OK.name())
				return false;
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void loadLotControlRules() {
		LotControlRuleId ruleId = new LotControlRuleId((Mbpn)productSpec);
		
		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		rulesAndProcessPoints = lotControlRuleDao.findAllRulesAndProcessPointsForMbpn(ruleId, productTypeData.getProductTypeName());
		
		//Filtering rules further for * 
		List<Object[]> filteredRulesAndProcessPoints = new ArrayList<Object[]>();
		for(Object[] objects : rulesAndProcessPoints){
			LotControlRule rule = (LotControlRule)objects[0];
				if(ProductSpecUtil.matchMbpn(productSpec.getProductSpecCode(), rule.getId().getProductSpecCode())) {
					filteredRulesAndProcessPoints.add(objects);
				}
		}
		rulesAndProcessPoints = filteredRulesAndProcessPoints;
		assembleLotControl();
		
		Logger.getLogger().debug("number of part:" + lotControlPartResultData.size());
		
	}
	
	protected boolean checkShippedAndException(BaseProduct product){ 
		// check parent product shipping status
		return isParentProductShipped(product) || super.checkShippedAndException(product);
	}

	private boolean isParentProductShipped(BaseProduct product) {
		BaseProduct productToCheckShipping = findParentProduct(product);
		boolean parentProductShipped = false;
		if(productToCheckShipping != null && !productToCheckShipping.getProductId().equalsIgnoreCase(product.getProductId())) {
			parentProductShipped = super.checkShippedAndException(productToCheckShipping);
		} else {
			return false; // no parent, so parrent is not shipped
		}
		
	    /**
	     * this will handle the case - Parent part is an MBPN part			
	     */
		if(parentProductShipped == false && ProductTypeUtil.isMbpnProduct(product.getProductType())) {
			
			return isParentProductShipped(productToCheckShipping);
			
		} 
		
		return parentProductShipped;
	}
	
	@Override
	protected List<String> getShippingProcessPointIds(BaseProduct product) {
		return  getShippingProcessPointIds(product.getLastPassingProcessPointId(), product.getProductType().name());
	}
	
	
	protected void removeInstalledPart(PartResult result) {
		InstalledPart installedPart = result.getInstalledPart();
		super.removeInstalledPart(result);
		
		InstalledPart parentInstalledPart =findParentInstalledPart(installedPart.getProductId(), installedPart.getProductType());
		if(parentInstalledPart == null ) return;
		
			
		parentInstalledPart.setInstalledPartStatus(InstalledPartStatus.NG);
		ServiceFactory.getDao(InstalledPartDao.class).save(parentInstalledPart);
	}
		
}
