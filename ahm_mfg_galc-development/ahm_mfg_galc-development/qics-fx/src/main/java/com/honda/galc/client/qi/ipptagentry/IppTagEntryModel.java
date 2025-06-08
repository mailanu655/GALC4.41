package com.honda.galc.client.qi.ipptagentry;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.product.IPPTagDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class IppTagEntryModel extends QiProcessModel {

	/**
	 * This method is used to save IPPtag data
	 */
	public void submitIPP(IPPTag ippTag) {
		getDao(IPPTagDao.class).save(ippTag);
	}

	/**
	 * This method is used to get list of IPPtag data
	 */
	public List<IPPTag> selectIppHistory() {
		List<IPPTag> ippTagList = getDao(IPPTagDao.class).findAllByProductId(getProductId());
		return ippTagList;
	}
	
	/**
	 * This method is used to update Ipp tag number
	 */
	public IPPTag updateIppTagNumber(IPPTag ippTag, String ippTagNumber)  {
		return getDao(IPPTagDao.class).update(ippTag, ippTagNumber);
	}

	/**
	 * This method is used to delete Ipp tag
	 */
	public void deleteIPPTag(IPPTag ippTag) {
		getDao(IPPTagDao.class).remove(ippTag);
	}

	/**
	 * This method is used to get Product History
	 */
	public ProductHistoryDao<? extends ProductHistory, ?> getProductHistory() {
		return ProductTypeUtil.getProductHistoryDao(getProductType());
	}

	/**
	 * This method is used to get Product History
	 */
	public ComponentStatusDao getHttpServiceProvider(String oifServerUrl) {
		return HttpServiceProvider.getDao(oifServerUrl, ComponentStatusDao.class);
	}

	/**
	 * This method is used to get Component Status
	 */
	public ComponentStatusDao getComponentStatusDao() {
		return ServiceFactory.getDao(ComponentStatusDao.class);
	}

	/**
	 * This method is used to get Component status by componnet id and status key
	 */
	public ComponentStatus getAllByComponentIdAndStatusKey(String componentId, String statusKey) {
		return getDao(ComponentStatusDao.class).findByKey(componentId, statusKey);
	}

	/**
	 * This method is used to get component property
	 */
	public ComponentPropertyDao getComponentProperty(String oifServerUrl) {
		return HttpServiceProvider.getDao(oifServerUrl, ComponentPropertyDao.class);
	}
	
	/**
	 * This method is used to find property by id
	 */
	public ComponentProperty findById(ComponentPropertyId id) {
		return getDao(ComponentPropertyDao.class).findByKey(id);
	}
	
	/**
	 * This method is used to get Product History by product and process point
	 */
	public List<? extends ProductHistory> getAllByProductAndProcessPoint(String productId, String processPointId) {
		return getProductHistory().findAllByProductAndProcessPoint(productId, processPointId);
	}
	
	/**
	 * This method is used to check whether Ipp tag is numeric
	 * @return
	 */
	public boolean isIppTagNumberNumeric(){
		return getProperty().isIppTagNumberNumeric();
	}
}
