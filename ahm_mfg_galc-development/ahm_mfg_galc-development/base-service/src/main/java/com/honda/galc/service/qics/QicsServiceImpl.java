package com.honda.galc.service.qics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.QicsService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>QicsServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsServiceImpl description </p>
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
 * @author Paul Chou
 * Apr 4, 2011
 *
 */


public class QicsServiceImpl implements QicsService{
	@SuppressWarnings("unchecked")
	static Map<ProductType, Class<? extends QicsUpdaterBase>> classMap = 
		new HashMap<ProductType, Class<? extends QicsUpdaterBase>>();
	
	static {
		classMap.put(ProductType.ENGINE, QicsUpdaterEngine.class);
		classMap.put(ProductType.FRAME, QicsUpdaterFrame.class);
		classMap.put(ProductType.KNUCKLE, QicsUpdaterSubProduct.class);
		classMap.put(ProductType.HEAD, QicsUpdaterHead.class);
		classMap.put(ProductType.BLOCK, QicsUpdaterBlock.class);
		classMap.put(ProductType.CONROD, QicsUpdaterConrod.class);
		classMap.put(ProductType.CRANKSHAFT, QicsUpdaterCrankshaft.class);
		classMap.put(ProductType.MBPN, QicsUpdaterMbpnProduct.class);
		classMap.put(ProductType.MBPN_PART, QicsUpdaterMbpnProduct.class);
		classMap.put(ProductType.FIPUCASE, QicsUpdaterFrontIpu.class);
		classMap.put(ProductType.RIPUCASE, QicsUpdaterRearIpu.class);
		classMap.put(ProductType.MCASE, QicsUpdaterCase.class);
	}
	
	public boolean update(String processPointId, BaseProduct product, List<? extends ProductBuildResult> buildResults) {
		return update(processPointId, product.getProductType(), buildResults);
	}

	@SuppressWarnings("unchecked")
	public boolean update(String processPointId, ProductType productType,
			List<? extends ProductBuildResult> buildResults) {

		QicsUpdater qicsUpdater = getQicsUpdater(productType);
		if(qicsUpdater != null)
			return qicsUpdater.update(processPointId, buildResults);
		else {
			Logger.getLogger().error("Error: product type:" + productType, " is not supported.");
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	private QicsUpdater getQicsUpdater(ProductType productType) {
		
		try {
			return ServiceFactory.getService(getServiceClass(productType));
		} catch (Exception e) {
			Logger.getLogger().error("Failed to get qics updater. unspported product type:" + productType.toString());
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends QicsUpdaterBase> getServiceClass(ProductType productType) {
		if(classMap.keySet().contains(productType)) return classMap.get(productType);
		else {
			if(ProductTypeUtil.isInstanceOf(productType, MbpnProduct.class))
				return QicsUpdaterMbpnProduct.class;
			else if(ProductTypeUtil.isInstanceOf(productType, SubProduct.class))
				return QicsUpdaterSubProduct.class;
			else
				return null;
		}
			
	}
}
