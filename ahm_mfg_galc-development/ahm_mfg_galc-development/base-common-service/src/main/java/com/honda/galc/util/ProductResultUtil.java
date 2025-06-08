package com.honda.galc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.ConrodBuildResultDao;
import com.honda.galc.dao.product.CrankshaftBuildResultDao;
import com.honda.galc.dao.product.HeadBuildResultDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.ProductResultPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>ProductResultUtil Class description</h3>
 * <p> ProductResultUtil description </p>
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
 * Mar 19, 2015
 *
 *
 */
public class ProductResultUtil {
	public static List<InstalledPart> saveAll(String processPointId, List<InstalledPart> installedParts) {
		return ServiceFactory.getDao(InstalledPartDao.class).saveAll(installedParts, isSaveInstalledPartHistory(processPointId));
	}
	
	public static ProductBuildResult saveBuildResult(String processPointId, ProductBuildResult productBuildResult) {
		ArrayList<ProductBuildResult> productBuildResults = new ArrayList<ProductBuildResult>();
		productBuildResults.add(productBuildResult);
		List<ProductBuildResult> results = saveBuildResults(processPointId, productBuildResults);
		return (results == null || results.isEmpty()) ? null : results.get(0);
	}
	
	public static List<ProductBuildResult> saveBuildResults(String processPointId, List<ProductBuildResult> productBuildResults) {
		if (productBuildResults.isEmpty()) return new ArrayList<ProductBuildResult>();
		Class<? extends ProductBuildResult> resultType = productBuildResults.get(0).getClass();
		
		if (resultType.equals(BlockBuildResult.class))
			return ServiceFactory.getDao(BlockBuildResultDao.class).saveAllResults((List<ProductBuildResult>)productBuildResults, isSaveBuildResultHistory(processPointId, ProductType.BLOCK.name()));
		if (resultType.equals(ConrodBuildResult.class))
			return ServiceFactory.getDao(ConrodBuildResultDao.class).saveAllResults((List<ProductBuildResult>)productBuildResults, isSaveBuildResultHistory(processPointId, ProductType.CONROD.name()));
		if (resultType.equals(CrankshaftBuildResult.class))
			return ServiceFactory.getDao(CrankshaftBuildResultDao.class).saveAllResults((List<ProductBuildResult>)productBuildResults, isSaveBuildResultHistory(processPointId, ProductType.CRANKSHAFT.name()));
		if (resultType.equals(HeadBuildResult.class))
			return ServiceFactory.getDao(HeadBuildResultDao.class).saveAllResults((List<ProductBuildResult>)productBuildResults, isSaveBuildResultHistory(processPointId, ProductType.HEAD.name()));
		return null;
	}
	
	private static boolean isSaveInstalledPartHistory(String processPointId) {
		return PropertyService.getPropertyBean(ProductResultPropertyBean.class, processPointId).isSaveInstalledPartHistory();
	}
	
	private static boolean isSaveBuildResultHistory(String processPointId, String productType) {
		if (productType == null) return false;
		Map<String, Boolean> saveHistoryMap = getPropertyBean(processPointId).isSaveBuildResultHistoryMap(Boolean.class);
		if (saveHistoryMap != null && saveHistoryMap.containsKey(productType)) return saveHistoryMap.get(productType);
		return true;
	}
	
	private static ProductResultPropertyBean getPropertyBean(String processPointId) {
		return PropertyService.getPropertyBean(ProductResultPropertyBean.class, processPointId);
	}
}
