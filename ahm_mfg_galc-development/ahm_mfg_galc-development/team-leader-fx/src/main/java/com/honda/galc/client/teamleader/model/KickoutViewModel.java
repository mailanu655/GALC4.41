/**
 * Model class for the Kickout View teamleader screen
 *
 * @author Bradley Brown
 * @version 1.0
 * @since 2.43
 */
package com.honda.galc.client.teamleader.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.KickoutPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.KickoutService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class KickoutViewModel extends AbstractModel {
	private static final int SINGLE_BATCH = 1;
	private static final int SMALL_BATCH = 4;
	private static final int MEDIUM_BATCH = 11;
	private static final int LARGE_BATCH = 51;
	private static final int LARGEST_BATCH = 101;
	
	protected String applicationId;

	private Set<KickoutDto> productList = new HashSet<KickoutDto>();
	private Boolean isKickoutReasonRequired = null;

	public KickoutViewModel(ApplicationContext context) {
		this.applicationContext = context;
		applicationId = context.getApplicationId();
	}

	public List<? extends BaseProduct> findProducts(List<String> productIdList, int startPos, int pageSize) {
		return (ProductTypeUtil.getProductDao(getProductType()).findProducts(productIdList, startPos, pageSize));
	}

	public BaseProduct findProduct(String productId) {
		return ProductTypeUtil.getProductDao(getProductType()).findBySn(productId);
	}

	public DataContainer kickoutProducts(DefaultDataContainer data) {
		return getKickoutService().kickoutProducts(data);
	}
	
	public DataContainer releaseKickouts(DefaultDataContainer data) {
		return getKickoutService().releaseKickouts(data);
	}

	public String getProductType() {
		return this.applicationContext.getApplicationPropertyBean().getProductType();
	}
	
	public boolean isKickoutReasonRequired() {
		if(isKickoutReasonRequired == null) {
			isKickoutReasonRequired = getKickoutPropertyBean().isKickoutReasonRequired();
		}
		return isKickoutReasonRequired;
	}
	
	public boolean isDcProduct() {
		return ProductTypeUtil.isDieCast(ProductType.getType(getProductType()));
	}
	
	public DataContainer validateForKickout(DefaultDataContainer data) {
		return getKickoutService().validateProductsForKickout(data);
	}
	
	public DataContainer validateForRelease(DefaultDataContainer data) {
		return getKickoutService().validateProductsForRelease(data);
	}

	public boolean addProductToSet(KickoutDto product) {
		return(!productList.add(product));
	}

	public List<KickoutDto>  getProductsAsList() {
		return this.productList.stream().collect(Collectors.toList());
	}
	
	public List<KickoutDto> addProductsToSet(List<KickoutDto> products) {
		List<KickoutDto> duplicateProducts = new ArrayList<KickoutDto>();
		for(KickoutDto product : products) {
			if(addProductToSet(product)) {
				duplicateProducts.add(product);
			}
		}
		return duplicateProducts;
	}

	public void removeProducts(List<KickoutDto> kickoutDtoList) {
		productList.removeAll(kickoutDtoList);
	}
	
	public int getBatchSize(int resultSize) {
		int batchSize = SINGLE_BATCH;
		if (resultSize >= LARGEST_BATCH) {
			batchSize = LARGEST_BATCH;
		} else if (resultSize >= LARGE_BATCH) {
			batchSize = LARGE_BATCH;
		} else if (resultSize >= MEDIUM_BATCH) {
			batchSize = MEDIUM_BATCH;
		} else if (resultSize >= SMALL_BATCH) {
			batchSize = SMALL_BATCH;
		}
		return batchSize;
	}

	@Override
	public void reset() {
		productList.clear();
	}
	
	public Set<KickoutDto> getProductSet() {
		return this.productList;
	}
	
	public String getApplicationId() {
		return this.applicationId;
	}
	
	private KickoutService getKickoutService() {
		return ServiceFactory.getService(KickoutService.class);
	}

	private KickoutPropertyBean getKickoutPropertyBean() {
		return PropertyService.getPropertyBean(KickoutPropertyBean.class, applicationId);
	}
	
	public boolean isDcStation() {
		return PropertyService.getPropertyBean(QiPropertyBean.class, applicationContext.getProcessPointId()).isDcStation();
	}
	
	public List<String> findProductsByTransactionId(long transactionId,String prodType) {
		List<String> products = ServiceFactory.getDao(QiDefectResultDao.class).findProductIdsByGroupTransId(transactionId,prodType);
		return products;
	}
	
	public List<String> findProductsByTransactionIdProdType(long transactionId,String prodType, int defectStatus) {
		List<String> products = ServiceFactory.getDao(QiDefectResultDao.class).findProductIdsByGroupTransIdProdType(transactionId,prodType,defectStatus);
		return products;
	}
}
