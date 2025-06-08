package com.honda.galc.client.teamleader;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductSpec;
import com.honda.galc.service.ServiceFactory;



public class ManualLotControlRepairIpuController extends
		ManualLotControlRepairController<SubProduct, InstalledPart> {

	public ManualLotControlRepairIpuController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}
	
	protected SubProduct checkProductOnServer(String productId) {
		try {
			SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
			return subProductDao.findByKey(productId);
		} catch (Exception e) {
			String msg = "failed to load " + property.getProductType() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
	}
	

	@Override
	protected void loadProductBuildResults() {

		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		productBuildResulits = installedPartDao.findAllByProductId(product.getProductId());
		
		loadInstalledParts(productBuildResulits);
		
	}
	
	//No ProductNumberDef for IPU so I overwrite this method 
	@Override
	protected void checkProductId(String productId) {
		if(productId.length() != productTypeData.getProductType().getProductIdLength()){
			throw new TaskException("Invalid product id length:" + productId.length());
		}
	}

	@Override
	protected ProductSpec loadProductSpec(BaseProduct product) {
		ProductSpec productSpec= null;
		if(!StringUtils.isEmpty(product.getProductSpecCode())){
			SubProductSpec subProductSpec = new SubProductSpec();
			subProductSpec.setProductSpecCode(product.getProductSpecCode());
			subProductSpec.setModelYearCode(StringUtils.substring(product.getProductSpecCode(),0,1));
			subProductSpec.setModelCode(StringUtils.trim(StringUtils.substring(product.getProductSpecCode(),1,4)));
			subProductSpec.setModelTypeCode(StringUtils.trim(StringUtils.substring(product.getProductSpecCode(),4,7)));
			subProductSpec.setModelOptionCode(StringUtils.trim(StringUtils.substring(product.getProductSpecCode(),7,10)));
			return subProductSpec;
		}
		else
			return productSpec;
	}
	
	
}