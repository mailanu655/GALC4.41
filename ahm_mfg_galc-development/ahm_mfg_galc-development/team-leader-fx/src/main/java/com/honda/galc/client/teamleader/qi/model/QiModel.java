package com.honda.galc.client.teamleader.qi.model;

import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QIMaintenanceModel Class description</h3>
 * <p> QIMaintenanceModel description </p>
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
 * @author Justin Jiang<br>
 * May 16, 2016
 *
 *
 */
public class QiModel extends AbstractModel {
	
	private String productKind = QiConstant.AUTOMOBILE;
	
	public QiModel() {
		super();
	}
	
	public void reset() {
		
	}
	
	public String getProductKind() {
		return productKind;
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		super.setApplicationContext(applicationContext);
		setProductKind(PropertyService.getProperty(applicationContext.getApplicationId(),
				QiConstant.PRODUCT_KIND, QiConstant.AUTOMOBILE));
	}
	
	/** This is used to get the instance of Logger
	 * @return the logger
	 */
	public Logger getLogger() {
		return getApplicationContext().getLogger();
	}
	
	public String getApplicationId() {
		return getApplicationContext().getApplicationId();
	}
	/**
	 * This method fetches the Site Name.
	 */
	public String getSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}
	public String getProductType() {
		return getApplicationContext().getApplicationPropertyBean().getProductType();
	}
	
	public int getLazyLoadDisplayRows() {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class, applicationContext.getApplicationId()).getLazyLoadDisplayRows();
	} 
	
	public boolean isUpdated(AuditEntry entity) {
		if (entity == null || entity.getId() == null) {
			return false;
		}
		return ServiceFactory.getService(GenericDaoService.class).isUpdated(entity);
	}
	
	public boolean isUpdated(List<? extends AuditEntry> entities) {
		if (entities == null || entities.isEmpty()) {
			return false;
		}
		return ServiceFactory.getService(GenericDaoService.class).isUpdated(entities);
	}
	
	public AuditEntry find(AuditEntry entity) {
		if (entity == null || entity.getId() == null) {
			return null;
		}
		return ServiceFactory.getService(GenericDaoService.class).find(entity.getClass(), entity.getId());
	}
}


