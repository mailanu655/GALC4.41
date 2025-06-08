package com.honda.galc.service.utils;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.task.IHlServiceTask;
import com.honda.galc.service.property.PropertyService;
import com.ibm.ejs.ras.RasHelper;
/**
 * 
 * <h3>ServiceUtiil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ServiceUtiil description </p>
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
 * <TD>Jul 21, 2011</TD>
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
 * @since Jul 21, 2011
 */

public class ServiceUtil {

	public static final String TASK_PACKAGE = "com.honda.galc.service.datacollection.task";
	public static final String JPA_LOG = "JPA Server";
	
	public static BaseProduct validateProductId(String productType, String productId, Map<Object, Object> context, String productName){
		if(!validateProductIdNumber(productType, productId, context, productName))
			return null;
		
		BaseProduct product = getProductFromDataBase(productType, productId);
				
		if(product == null)
			context.put(TagNames.EXCEPTION.name(), productType + ":" + productId + " does not exist.");
		
		return product;
	}

	@SuppressWarnings("unchecked")
	public static BaseProduct getProductFromDataBase(String productTypeStr, String productId) {
	
		ProductType productType = ProductTypeCatalog.getProductType(productTypeStr);
		ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getTypeUtil(productType).getProductDao();
		return isDiecast(productType.toString()) ? ((DiecastDao)productDao).findByMCDCNumber(productId) : productDao.findByKey(productId);
	}

	public static boolean validateProductIdNumber(String productType, String productId, Map<Object, Object> context, String productName) {
		if(StringUtils.isEmpty(productId)){
			context.put(TagNames.EXCEPTION.name(), productType + " product id number is null.");
			return false;
		}
		
		String processPoint = (String) context.get(TagNames.PROCESS_POINT_ID.name());
		if(!PropertyService.getPropertyBoolean(processPoint, "CHECK_PRODUCT_NUMBER_DEF", true)) {
			context.put(getTag(TagNames.VALID_PRODUCT_NUMBER_FORMAT.name(), productName), true);
			return true;
		}
		if(!isProductIdValid(productType, productId)){
			context.put(getTag(TagNames.VALID_PRODUCT_NUMBER_FORMAT.name(), productName), false);
			context.put(TagNames.EXCEPTION.name(), "Invalid " + productType + " number:" + productId);
			return false;
		} 
		else
			context.put(getTag(TagNames.VALID_PRODUCT_NUMBER_FORMAT.name(), productName), true);
		
		return true;
	}
	
	public static boolean isProductIdValid(String productType, String productId) {
		List<ProductNumberDef> productNumberDefs = getProductNumberDefs(productType, productId);
		return (productNumberDefs == null || productNumberDefs.size() == 0) ? true : ProductNumberDef.isNumberValid(productId, productNumberDefs);
	}
	
	public static List<ProductNumberDef> getProductNumberDefs(String productType, String productId) {
		List<ProductNumberDef> productNumberDefs = new ArrayList<ProductNumberDef>();
		ProductTypeData prodType = getDao(ProductTypeDao.class).findByKey(productType);
		return prodType != null ? prodType.getProductNumberDefs() : productNumberDefs;
	}
	
	public static boolean isCheckDigitNeeded(String productType, String productSpecCode) {
		if(!ProductType.FRAME.name().equals(productType)) {
			return false;
		}		
		FrameSpec spec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(productSpecCode);
		if (spec !=null && spec.getFrameNoPrefix() != null && spec.getFrameNoPrefix().trim().length()>9)			
		{
			String checkDigitChar=Character.toString(spec.getFrameNoPrefix().charAt(8));
			if(checkDigitChar.trim().equals("*"))
				return true;
		}
		return false;
	}
	

	public static boolean isDiecast(String productType) {
		ProductType type = ProductTypeCatalog.getProductType(productType);
		return ProductTypeUtil.isInstanceOf(type, DieCast.class);
	}
	
	public static String getTag(String tag, String productName) {
		return StringUtils.isEmpty(productName) ? tag : 
			productName + "." + tag;
	}

	public static Date getCurrentDate() throws ParseException {
		SimpleDateFormat sdf = getDateFormat();
		return sdf.parse(getCurrentDateString());
	}

	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public static String getCurrentDateString() {
		SimpleDateFormat sdf = getDateFormat();
		Calendar calendar = new GregorianCalendar();
		return sdf.format(calendar.getTime());
	}
	
	
	/**
	 * get logger for a process point and set logger level from property
	 * @param loggerName
	 * @return
	 */
	public static Logger getLogger(String loggerName){
		Logger logger;
		if(StringUtils.isEmpty(loggerName))
			logger = Logger.getLogger();
		else{
			logger = Logger.getLogger(PropertyService.getLoggerName(loggerName));
		}
		
		String level =PropertyService.getProperty(loggerName, LogLevel.LOG_LEVEL, LogRecord.defaultLevel.toString());
		logger.setLogLevel(LogLevel.valueOf(level));
		
		return logger;
	}
	
	public static String getShortServerName() {

		String shortServerName = "";
		try {

			// check for RasHelper available
			Class.forName("com.ibm.ejs.ras.RasHelper");

			// Query IBM WebSphere RasHelper whether it runs in server
			// RasHelper.getServerName() does not return null value
			String[] elems = RasHelper.getServerName().split("\\\\");
			if (elems != null && elems.length > 0) {
				shortServerName = elems[elems.length - 1];
			} else {
				shortServerName = "";
			}

		} catch (ClassNotFoundException e) {
			// Could not find RasHelper - cannot be running on server
			throw new SystemException("Could not find Server Name from RasHelper");

		}

		return shortServerName;
	}
	
	@SuppressWarnings("unchecked")
	public static IHlServiceTask createTask(String taskName, Map<Object, Object> context, String processPointId) {
		Class claz;
		try {
			if(!taskName.contains(".")) taskName = TASK_PACKAGE + "." + taskName;
			claz = Class.forName(taskName);
			Class[] parameterTypes = {context.getClass(), String.class};
			Object[] parameters = {context, processPointId};
			Constructor constructor = claz.getConstructor(parameterTypes);
			return (IHlServiceTask)constructor.newInstance(parameters);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to create collector task:" + taskName);
			throw new TaskException("Failed to create collector task:" + taskName);
		}
		
	}
}
