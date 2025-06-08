package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.BlockBuildResultHistoryDao;
import com.honda.galc.dao.product.ConrodBuildResultHistoryDao;
import com.honda.galc.dao.product.CrankshaftBuildResultHistoryDao;
import com.honda.galc.dao.product.HeadBuildResultHistoryDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>ProductBuildResultDaoImpl Class description</h3>
 * <p> ProductBuildResultDaoImpl description </p>
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
 * Apr 16, 2012
 *
 *
 */
public abstract class ProductBuildResultDaoImpl<E extends ProductBuildResult,K> extends BaseDaoImpl<E,K> implements ProductBuildResultDao<E,K> {

    private static final long serialVersionUID = 1L;

	@Transactional
	public ProductBuildResult saveResult(ProductBuildResult result) {
		return saveResult(result, true);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ProductBuildResult saveResult(ProductBuildResult result, Boolean saveHistory) {
		if (saveHistory) saveHistory(result);
		return save((E)result);
	}
	
	@Override
	public E findByRefId(Long refId) {
		Parameters parameters = Parameters.with("defectRefId", refId);
		return findFirst(parameters);
	}
	
	private void saveHistory(ProductBuildResult result) {	
		if(result.getClass().equals(BlockBuildResult.class)) {
			ServiceFactory.getDao(BlockBuildResultHistoryDao.class).saveHistory((BlockBuildResult) result);			
		}else if(result.getClass().equals(ConrodBuildResult.class)) {
			ServiceFactory.getDao(ConrodBuildResultHistoryDao.class).saveHistory((ConrodBuildResult) result); 
		}else if(result.getClass().equals(CrankshaftBuildResult.class)) {
			ServiceFactory.getDao(CrankshaftBuildResultHistoryDao.class).saveHistory((CrankshaftBuildResult) result); 
		}else if(result.getClass().equals(HeadBuildResult.class)) {
			ServiceFactory.getDao(HeadBuildResultHistoryDao.class).saveHistory((HeadBuildResult) result); 
		}
	}
	
	@Transactional
	public List<ProductBuildResult> saveAllResults(List<ProductBuildResult> buildResult) {
		return saveAllResults(buildResult, true);
	}
	
	@Transactional
	public List<ProductBuildResult> saveAllResults(List<ProductBuildResult> buildResults, Boolean saveHistory) {
		List<Measurement> measurements;
		List<ProductBuildResult> results = new ArrayList<ProductBuildResult>();
		for(ProductBuildResult buildResult : buildResults) {
			results.add(saveResult(buildResult, saveHistory));
			measurements = buildResult.getMeasurements();
			if(measurements != null && !measurements.isEmpty()) {
				ServiceFactory.getDao(MeasurementDao.class).saveAll(measurements);
			}
		}
		return results;
	}
	
	public boolean isPartInstalled(String productId, String partName) {
		E part = findById(productId, partName);
		return part != null && part.isStatusOk();
	}
	
	@Transactional
    public void remove(ProductBuildResult entity) {
		super.remove((E) entity);
    }
}
