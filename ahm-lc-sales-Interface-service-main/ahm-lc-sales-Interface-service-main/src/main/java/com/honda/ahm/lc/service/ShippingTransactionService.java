package com.honda.ahm.lc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.DailyDepartmentSchedule;
import com.honda.ahm.lc.model.ProcessPoint;
import com.honda.ahm.lc.model.ShippingTransaction;
import org.json.JSONObject;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service(value = "shippingTransactionService")
public class ShippingTransactionService extends BaseGalcService<ShippingTransaction, String> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public ShippingTransaction findByProductId(String galcUrl, String productId) {

		try {
			getLogger().info("Find ShippingTransaction record by ProductId-" + productId);
			ShippingTransaction shippingStatus = findByProductId(galcUrl, productId, GalcDataType.SHIPPING_TRANSACTION);
			return shippingStatus;
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

	public void deleteShippingTransaction(String galcUrl, String productId) {
		try {
			getLogger().debug("delete ShippingTransaction record by ProductId-" + productId);
			deleteByProductId(galcUrl, productId, GalcDataType.SHIPPING_TRANSACTION);
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	public ShippingTransaction saveShippingTransaction(String galcUrl, ShippingTransaction entity) {

		try {
			getLogger().info("Save ShippingTransaction record -" + entity.toString());
			return save(galcUrl, entity, GalcDataType.SHIPPING_TRANSACTION);

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ShippingTransaction> get50ATransactionVin(String galcUrl, Integer status, String processPointId,
			Character sendFlag, String cccPartName) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("java.lang.Integer", status);
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("java.lang.Integer", getEffectiveDate(galcUrl, processPointId));
			JSONObject jsonObject3 = new JSONObject();
			jsonObject3.put("java.lang.Character", sendFlag);
			JSONObject jsonObject4 = new JSONObject();
			jsonObject4.put("java.lang.String", cccPartName);

			jsonObjects.add(jsonObject1);
			jsonObjects.add(jsonObject2);
			jsonObjects.add(jsonObject3);
			jsonObjects.add(jsonObject4);

			List<LinkedHashMap> results = (List<LinkedHashMap>) getRestTemplate()
					.postForObject(getExternalSystemUrl(galcUrl, GalcDataType.SHIPPING_TRANSACTION.getDao(),
							"get50ATransactionVin"), jsonObjects.toString(), List.class);

			List<ShippingTransaction> shippingTransactions = new ArrayList<ShippingTransaction>();
			for (LinkedHashMap m : results) {
				ShippingTransaction st = new ObjectMapper().convertValue(m, ShippingTransaction.class);
				shippingTransactions.add(st);
			}

			return shippingTransactions;

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}

		return null;
	}

	public Integer getEffectiveDate(String galcUrl, String processPointId) {
		Date productionDate = new Date(System.currentTimeMillis());
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("java.lang.String", processPointId);
		jsonObjects.add(jsonObject1);

		ProcessPoint processPoint = getRestTemplate().postForObject(
				getExternalSystemUrl(galcUrl, GalcDataType.PROCESS_POINT.getDao(), "findByKey"),
				jsonObjects.toString(), ProcessPoint.class);

		jsonObjects.clear();
		if (processPoint != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("java.lang.String", processPoint.getDivisionId());
			map.put("java.sql.Timestamp", getTimestampNow());

			jsonObject1 = new JSONObject();
			jsonObject1.put("java.lang.String", processPoint.getDivisionId());
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("java.sql.Timestamp", getTimestampNow());
			jsonObjects.add(jsonObject1);
			jsonObjects.add(jsonObject2);

			DailyDepartmentSchedule schedule = getRestTemplate().postForObject(
					getExternalSystemUrl(galcUrl, GalcDataType.DEPT_SCHEDULE.getDao(), "find"),
					jsonObjects.toString(), DailyDepartmentSchedule.class);

			if (schedule != null) {
				try {
					productionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z")
							.parse(schedule.getId().getProductionDate());
				} catch (ParseException e) {
					getLogger().error(e.getMessage());
				}
			} else {
				getLogger().warn("Failed to find schedule use system date for production date");
			}
		}

		return Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(productionDate));
	}

	public String getTimestampNow() {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z").format(new Timestamp(System.currentTimeMillis())));
	}

	public String getMaxActualTs(String galcUrl, String productId, String afOffProcessPt) {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("java.lang.String", productId);
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("java.lang.String", afOffProcessPt);
		jsonObjects.add(jsonObject1);
		jsonObjects.add(jsonObject2);

		String afOffTimestamp = (String)getRestTemplate().postForObject(
				getExternalSystemUrl(galcUrl, GalcDataType.PRODUCT_RESULT.getDao(), "getMaxActualTs"),
				jsonObjects.toString(), Object.class);
		return afOffTimestamp;
	}

	public String getPartSerialNumber(String galcUrl, String productId, String partName) {
		String keyVal = "";

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("java.lang.String", productId);
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("java.lang.String", partName);
		jsonObjects.add(jsonObject1);
		jsonObjects.add(jsonObject2);
		keyVal = (String)getRestTemplate().postForObject(
				getExternalSystemUrl(galcUrl, GalcDataType.INSTALLED_PART.getDao(), "getLatestPartSerialNumber"),
				jsonObjects.toString(), Object.class);

		return keyVal;
	}

	public String getFIFCodeBySpecCode(String galcUrl, String productSpeccode) {
		String fifCode = null;
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("java.lang.String", productSpeccode);

		jsonObjects.add(jsonObject1);
		fifCode = getRestTemplate().postForObject(
				getExternalSystemUrl(galcUrl, GalcDataType.FIF_CODE.getDao(), "getFIFCodeByProductSpec"),
				jsonObjects.toString(), String.class);

		return fifCode;
	}
}
