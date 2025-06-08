package com.honda.galc.service.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.DeviceFormatId;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.PrintAttributeFormatId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.ConditionType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.broadcast.AbstractBroadcast;
import com.honda.galc.service.printing.PrintAttributeConvertor;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BroadcastUtil</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jun 8, 2018
 */
public class BroadcastUtil {

	private static Map<ConditionType, ConditionalCheck> conditionalValidators;

	// === init conditional validators === //
	static {
		initializeValidators();
	}

	private static void initializeValidators() {
		conditionalValidators = new HashMap<ConditionType, ConditionalCheck>();
		ConditionalCheck[] validators = { createProductCheckValidator(), createPrintAttributeValidator(), createDeviceFormatValidator() };
		for (ConditionalCheck validator : validators) {
			getConditionalValidators().put(validator.getConditionType(), validator);
		}
	}

	// === public static api === /
	/**
	 * Main conditional verification method. It evaluates if broadcast should be executed. 
	 * If broadcast is not conditional it returns true. 
	 * If broadcast is defined as conditional it evaluates condition and return appropriate result (true/false).
	 * 
	 * @param destination
	 * @param data
	 * @return
	 */
	public static boolean isBroadcast(BroadcastDestination destination, DataContainer data) {

		if (destination == null || data == null) {
			return false;
		}

		if (StringUtils.isBlank(destination.getCondition())) {
			return true;
		}

		String[] condArray = destination.getCondition().trim().split(Delimiter.COMMA);
		if (condArray.length == 0) {
			return true;
		}

		ConditionType conditionType = destination.getConditionType();
		if (conditionType == null) {
			Logger.getLogger(destination.getProcessPointId()).warn("Invalid conditional broadcast configuration - condition " + destination.getCondition() + " has missing condition type, BroadcastDestination : " + destination);
			return false;
		}

		ConditionalCheck validator = getConditionalValidator(conditionType);
		if (validator == null) {
			Logger.getLogger(destination.getProcessPointId()).warn("Invalid conditional broadcast configuration - there is no validator defined for conditionType : " + conditionType + ", BroadcastDestination : " + destination);
			return false;
		}

		return validator.isBroadcast(destination, data, condArray);
	}

	// === supporting api === //
	/**
	 * This method evaluates result of conditional check
	 * 
	 * @param result
	 * @return
	 */
	private static boolean isConditionResultValid(Object result) {
		if (result == null) {
			return false;
		}
		if (result instanceof Number) {
			if (((Number) result).doubleValue() == 0) {
				return false;
			} else {
				return true;
			}
		} else if (result instanceof String) {
			if (Boolean.FALSE.toString().equalsIgnoreCase((String)result) || "0".equals(result)) {
				return false;
			} else {
				return true;
			}
		}
		return ProductCheckUtil.isValid(result);
	}

	// === validation methods === //
	public static boolean isBroadcastProductCheck(BroadcastDestination destination, DataContainer dataContainer, String[] checkTypes) {

		String processPointId = destination.getProcessPointId();
		String productId = dataContainer.getString(DataContainerTag.PRODUCT_ID);
		String productType = PropertyService.getPropertyBean(SystemPropertyBean.class, processPointId).getProductType();
		ProcessPoint processPointObj = (ProcessPoint) dataContainer.get(DataContainerTag.PROCESS_POINT);
		Object productObj = dataContainer.get(DataContainerTag.PRODUCT);
		ProcessPoint processPoint = null;
		BaseProduct product = null;

		if (processPointObj instanceof ProcessPoint && StringUtils.equals(processPointId, ((ProcessPoint) processPointObj).getProcessPointId())) {
			processPoint = (ProcessPoint) processPointObj;
		}
		if (processPoint == null) {
			processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
			if (processPoint == null) {
				Logger.getLogger(destination.getProcessPointId()).warn("Invalid processPointId :" + processPointId);
				return false;
			}
			dataContainer.put(DataContainerTag.PROCESS_POINT, processPoint);
		}

		if (productObj instanceof BaseProduct && StringUtils.equals(productId, ((BaseProduct) productObj).getProductId())) {
			product = (BaseProduct) productObj;
		}
		
		if (product == null) {
			product = ProductTypeUtil.findProduct(productType, productId);
			if (product == null) {
				Logger.getLogger(destination.getProcessPointId()).warn("Invalid productId :" + productId);
				return false;				
			}
			dataContainer.put(DataContainerTag.PRODUCT, product);
		}

		Map<String, Object> result = ProductCheckUtil.check(product, processPoint, checkTypes);
		if ((result != null && !result.isEmpty())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isBroadcastPrintAttribute(BroadcastDestination destination, DataContainer dataContainer, String[] attributeNames) {

		PrintAttributeFormatDao dao = ServiceFactory.getDao(PrintAttributeFormatDao.class);
		PrintAttributeConvertor convertor = new PrintAttributeConvertor(Logger.getLogger(destination.getProcessPointId()));
		AbstractBroadcast.prepareData(destination.getProcessPointId(), dataContainer);		
		for (String name : attributeNames) {
			if (StringUtils.isBlank(name)) {
				continue;
			}
			PrintAttributeFormatId id = new PrintAttributeFormatId();
			id.setFormId(destination.getRequestId());
			id.setAttribute(name);
			PrintAttributeFormat condition = dao.findByKey(id);
			if (condition == null) {
				Logger.getLogger(destination.getProcessPointId()).warn("Invalid conditional broadcast configuration - PrintAttributeFormat : " + name + " does not exist for form : " + destination.getRequestId());
				continue;
			}
			Object result = convertor.basicConvert(condition, dataContainer);
			if (isConditionResultValid(result)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isBroadcastDeviceFormat(BroadcastDestination destination, DataContainer dataContainer, String[] tagNames) {

		DeviceFormatDao dao = ServiceFactory.getDao(DeviceFormatDao.class);
		PrintAttributeConvertor convertor = new PrintAttributeConvertor(Logger.getLogger(destination.getProcessPointId()));
		AbstractBroadcast.prepareData(destination.getProcessPointId(), dataContainer);
		for (String name : tagNames) {
			if (StringUtils.isBlank(name)) {
				continue;
			}
			DeviceFormatId id = new DeviceFormatId(destination.getDestinationId(), name);
			DeviceFormat condition = dao.findByKey(id);
			if (condition == null) {
				Logger.getLogger(destination.getProcessPointId()).warn("Invalid conditional broadcast configuration - DeviceFormat : " + name + " does not exist for Device : " + destination.getDestinationId());
				continue;
			}
			Object result = convertor.convertDeviceFormat(condition, dataContainer);
			if (isConditionResultValid(result)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Internal interface for validators implementation
	 */
	private interface ConditionalCheck {
		public abstract ConditionType getConditionType();
		public abstract boolean isBroadcast(BroadcastDestination destination, DataContainer dataContainer, String[] attributeNames);
	}

	// === conditional broadcast validator factory methods === //
	private static ConditionalCheck createProductCheckValidator() {
		return new ConditionalCheck() {
			public ConditionType getConditionType() {
				return ConditionType.PRODUCT_CHECK;
			}

			public boolean isBroadcast(BroadcastDestination destination, DataContainer dataContainer, String[] checkTypes) {
				return isBroadcastProductCheck(destination, dataContainer, checkTypes);
			}
		};
	}

	private static ConditionalCheck createPrintAttributeValidator() {
		return new ConditionalCheck() {
			public ConditionType getConditionType() {
				return ConditionType.PRINT_ATTRIBUTE;
			}

			public boolean isBroadcast(BroadcastDestination destination, DataContainer dataContainer, String[] attributeNames) {
				return isBroadcastPrintAttribute(destination, dataContainer, attributeNames);
			}
		};
	}

	private static ConditionalCheck createDeviceFormatValidator() {
		return new ConditionalCheck() {
			public ConditionType getConditionType() {
				return ConditionType.DEVICE_FORMAT;
			}

			public boolean isBroadcast(BroadcastDestination destination, DataContainer dataContainer, String[] tagNames) {
				return isBroadcastDeviceFormat(destination, dataContainer, tagNames);
			}
		};
	}	

	// === get === /
	protected static Map<ConditionType, ConditionalCheck> getConditionalValidators() {
		return conditionalValidators;
	}

	protected static ConditionalCheck getConditionalValidator(ConditionType type) {
		return getConditionalValidators().get(type);
	}
}
