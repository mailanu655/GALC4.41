/**
 * 
 */
package com.honda.galc.client.device.plc;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Dec 5, 2012
 */
public class AttributeValidator implements IPlcDataValidator {
	
	protected Logger _logger;
	protected String _applicationId = "";
	private BuildAttributeDao _buildAttributeDao = null;
	
	public boolean validate(PlcDataCollectionBean bean, IPlcDataField field) {
		if (bean.getProductSpecCode() == null) {
			getLogger().error("MTOC not defined for product: " + bean.getProductId());
			return false;
		}
		
		BuildAttributeId attribId = new BuildAttributeId(field.getId(), bean.getProductSpecCode());
		BuildAttribute buildAttribute = getBuildAttributeDao().findByKey(attribId);
		
		if (buildAttribute != null) {
			return field.getValue().equals(buildAttribute.getAttributeValue());
		} else {
			getLogger().error("Unable to locate record for build attribute: " + field.getId());
			return false;
		}
	}
	
	protected BuildAttributeDao getBuildAttributeDao() {
		if (_buildAttributeDao == null) {
			_buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		}
		return _buildAttributeDao;
	}
	
  	protected Logger getLogger() {
		if (_logger == null) {
			_logger = Logger.getLogger(getApplicationId());
			_logger.getLogContext().setApplicationInfoNeeded(true);
			_logger.getLogContext().setMultipleLine(false);
			_logger.getLogContext().setCenterLog(false);
		}
		_logger.getLogContext().setThreadName(getApplicationId() + "-" + Thread.currentThread().getName());
		return _logger;
	}
  	
	public String getApplicationId() {
		return _applicationId;
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}
}
