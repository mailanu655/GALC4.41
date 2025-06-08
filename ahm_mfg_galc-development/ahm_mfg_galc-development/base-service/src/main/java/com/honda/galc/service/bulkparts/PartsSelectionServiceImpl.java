/**
 * 
 */
package com.honda.galc.service.bulkparts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.PartsSelectionService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartByProductSpecCodeDao;
import com.honda.galc.dao.product.PartsLoadingMaintenanceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartsLoadingMaintenance;
import com.honda.galc.entity.product.ProductSpec;

/**
 * @author vf031824
 *
 */
public class PartsSelectionServiceImpl implements PartsSelectionService{

	protected DataContainer retList = new DefaultDataContainer();

	private String PRODUCT_TYPE = "PRODUCT_TYPE";
	
	protected PartsLoadingMaintenance partsLoadingMaintenance = new PartsLoadingMaintenance();

	protected Logger logger;
	protected String processPointId = null;
	protected String productId = null;

	protected static final String INVALID_PROCESS_POINT =  "01";
	protected static final String RULE_NOT_ATTACHED_TO_BIN = "02";
	protected static final String DB_ERROR = "03";
	protected static final String UNKNOWN_ERROR = "04";

	protected String STR_INVALID_PROCESS_POINT = "DataContainer contains invalid process point";
	protected String DB_ERROR_OCCURED = "A database error has occured in method: ";
	protected String UNKNOWN_ERROR_OCCURED = "An unknown error had occured in method: ";

	private void init(DataContainer dc) {
		getLogger().info("collector received data:", dc.toString());
		retList.clear();

		processPointId = dc.getString(TagNames.PROCESS_POINT_ID.name().trim());
		productId = dc.getString(TagNames.PRODUCT_ID.name().trim());
	}

	public DataContainer getBinDataByProductId(DefaultDataContainer data) {
		try{
			@SuppressWarnings("rawtypes")
			BaseProductSpecDao productSpecDao = null;
			init(data);

			ProcessPoint pp = getProcessPoint(processPointId);
			if(pp == null) {
				addError(INVALID_PROCESS_POINT, STR_INVALID_PROCESS_POINT);
				return retList;
			}
			String productType = PropertyService.getProperty(processPointId, PRODUCT_TYPE,
					PropertyService.getProductType());

			BaseProduct product = ProductTypeUtil.getProductDao(productType).findByKey(productId);

			productSpecDao = ProductTypeUtil.getProductSpecDao(productType);
			ProductSpec spec=(ProductSpec)ProductTypeUtil.getProductSpecDao(productType).findByProductSpecCode(product.getProductSpecCode(),productType);

			List<LotControlRule> ruleList = findByProcessPointAndProductSpec(processPointId, productType, spec);

			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			List<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();

			for(LotControlRule rule : ruleList) {
				tempMap = createOutput(rule);
				if(!(partsLoadingMaintenance == null))
					tempList.add(tempMap);
			}
			retList.put(TagNames.BIN_LIST.name(), tempList);
			retList.put(TagNames.PROCESS_COMPLETE.name(), LineSideContainerValue.COMPLETE);

			getLogger().info("Parts Selection completed successfully: " + retList);
		} catch(Exception e) {
			String str = UNKNOWN_ERROR_OCCURED + "getBinDataByProductId";
			addError(UNKNOWN_ERROR, str);
			e.printStackTrace();
			return retList;
		}
		return retList;
	}

	public HashMap<String, Object> createOutput(LotControlRule rule) {

		HashMap<String, Object> tempMap = new HashMap<String, Object>();

		try {

			PartByProductSpecCode partbyProductSpecCode = ServiceFactory.getDao(PartByProductSpecCodeDao.class)
					.getPartId(rule.getId().getProductSpecCode(), rule.getPartNameString());

			partsLoadingMaintenance = ServiceFactory.getDao(PartsLoadingMaintenanceDao.class)
					.findByPartNameAndPartId(rule.getPartNameString(), partbyProductSpecCode.getId().getPartId());

			if(partsLoadingMaintenance == null) {
				tempMap.put(TagNames.ERROR_CODE.name(), RULE_NOT_ATTACHED_TO_BIN);
			} else {
				tempMap.put(TagNames.PART_NAME.name(), rule.getPartNameString());
				tempMap.put(TagNames.PRODUCT_SPEC_CODE.name(), rule.getId().getProductSpecCode());
				tempMap.put(TagNames.SEQUENCE.name(), rule.getSequenceNumber());
				tempMap.put(TagNames.BIN_NAME.name(), partsLoadingMaintenance.getBinName());
				tempMap.put(TagNames.PART_ID.name(), partbyProductSpecCode.getId().getPartId());
			}
		} catch (Exception e) {
			String str = UNKNOWN_ERROR_OCCURED + "createOutput";
			addError(UNKNOWN_ERROR, str);
			e.printStackTrace();
			return tempMap;
		}
		return tempMap;
	}

	private ProcessPoint getProcessPoint(String processPointId) {
		ProcessPoint processPoint = null;

		try{
			processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		} catch (Exception e) {
			String msg = DB_ERROR_OCCURED + "findById";
			addError(DB_ERROR, msg);
			e.printStackTrace();
		}
		return processPoint;
	}

	private List<LotControlRule> findByProcessPointAndProductSpec(String processPointId, String productType,  ProductSpec spec) {
		List<LotControlRule> ruleList = new ArrayList<LotControlRule>();

		try{
			ruleList = ServiceFactory.getDao(LotControlRuleDao.class)
					.findAllByProcessPointAndProductSpec(processPointId, productType, spec);
		}catch (Exception e) {
			String msg = DB_ERROR_OCCURED + "findAllByProcessPointAndProductSpec";
			addError(DB_ERROR, msg);
			e.printStackTrace();
		}
		return ruleList;
	}

	public void addError(String code, String msg) {
		getLogger().error(msg);
		retList.put(TagNames.ERROR_CODE.name(), code);
		retList.put("ERROR_MSG", msg);
		retList.put(TagNames.PROCESS_COMPLETE.name(),
				LineSideContainerValue.NOT_COMPLETE);
	}

	public Logger getLogger() {
		if (processPointId == null || processPointId.equals(""))
			return Logger.getLogger("ProductionScheduleDownload");
		else
			return Logger.getLogger(processPointId);
	}
}