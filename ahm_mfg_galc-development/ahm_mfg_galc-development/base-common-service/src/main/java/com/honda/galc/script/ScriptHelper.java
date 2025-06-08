package com.honda.galc.script;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockBuildResultId;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.HeadBuildResultId;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.IProperty;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IService;
import com.honda.galc.service.QicsService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;

/**
 * 
 * <h3>ScriptHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ScriptHelper description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Apr 24, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 24, 2012
 */

public class ScriptHelper {
	public static final String DAO_CONF_PACKAGE="com.honda.galc.dao.conf";
	public static final String DAO_PRODUCT_PACKAGE="com.honda.galc.dao.product";
	public static final String SERVICE_PACKAGE="com.honda.galc.service";
	private String processPointId;
	
	public ScriptHelper() {
		super();
	}
	
	public ScriptHelper(String processPointId) {
		super();
		this.processPointId = processPointId;
	}

	public Logger getLogger(String processPointId) {
		return StringUtils.isEmpty(processPointId)? 
				Logger.getLogger() : Logger.getLogger(PropertyService.getLoggerName(processPointId)); 
	}

	@SuppressWarnings("unchecked")
	public IDaoService getDaoByClassName(String dao){
		Class<? extends IDaoService> clazz = findDaoClass(dao);
		return ServiceFactory.getDao(clazz);
	}

	@SuppressWarnings("unchecked")
	private Class<? extends IDaoService> findDaoClass(String dao) {
		Class<?> clzz = null;
		try {
			clzz = Class.forName(DAO_PRODUCT_PACKAGE + "." + dao);
		} catch (Exception e) {
			try {
				clzz = Class.forName(DAO_CONF_PACKAGE + "." + dao);
			} catch (Exception e2) {
				getLogger(processPointId).error(e2, "Exception to find dao class: " + dao);
				throw new TaskException("Exception to find dao class: " + dao);
			}
		}
		return (Class<? extends IDaoService>)clzz;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IProperty> T getPropertyBean(String componentId, String beanName ) 
	{
		try {
			return PropertyService.getPropertyBean((Class<T>) getPropertyBeanInterface(beanName), componentId);
		} catch (Exception e) {
			getLogger(processPointId).error(e, " Exception to get PropertyBean:" + beanName + " for component:" + componentId);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	private Class getPropertyBeanInterface(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			getLogger(processPointId).error(e, " Exception to find class for:" + className );
			return null;
		}
	}
	
	public IService getServiceByClassName(String service){
		Class<? extends IService> clazz = findServiceClass(service);
		return ServiceFactory.getService(clazz);
	}

	@SuppressWarnings("unchecked")
	private Class<? extends IService> findServiceClass(String service) {
		Class<?> clzz = null;
		try {
			clzz = Class.forName(SERVICE_PACKAGE + "." + service);
		} catch (Exception e) {

			getLogger(processPointId).error(e, "Exception to find service class: " + service);
			throw new TaskException("Exception to find service class: " + service);
		}
		return (Class<? extends IService>)clzz;
	}
	
	/**
	 * Create Object for shell script
	 * @param className
	 * @return
	 */
	public Object create(String className){
		
		if(InstalledPart.class.getSimpleName().equals(className)){
			return new InstalledPart(new InstalledPartId());
		} else if(Measurement.class.getSimpleName().equals(className)){
			return new Measurement(new MeasurementId());
		} else if(HeadBuildResult.class.getSimpleName().equals(className))
			return new HeadBuildResult(new HeadBuildResultId());
		else if(BlockBuildResult.class.getSimpleName().equals(className))
			return new BlockBuildResult(new BlockBuildResultId());
			
		return null;
	}
	
	public ProductBuildResult createBuildResult(String productType){
		return ProductTypeUtil.createBuildResult(productType);
	}
	
	
	public ProductCheckUtil getProductCheckUtil(BaseProduct product, String processPointId){
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		return new ProductCheckUtil(product, processPoint);
	}
	
	public ProductCheckUtil getProductCheckUtil(BaseProduct product, ProcessPoint processPoint){
		return new ProductCheckUtil(product, processPoint);
	}
	
	
	public ProductTypeUtil getProductTypeUtil(String productType){
		return ProductTypeUtil.getTypeUtil(productType);
	}

	public TrackingService getTrackingService() {
		return getService(TrackingService.class);
	}
	
	public QicsService getQicsService() {
		return getService(QicsService.class);
	}
	
	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	public boolean checkProductDigit(String numDefNames, String productId){
		List<ProductNumberDef> productNumberDefs = ProductNumberDef.getProductNumberDefs(numDefNames);
		for(ProductNumberDef def : productNumberDefs){
			if(ProductDigitCheckUtil.check(def, productId))
				return true;
		}
		
		return false;
	}
	
}
