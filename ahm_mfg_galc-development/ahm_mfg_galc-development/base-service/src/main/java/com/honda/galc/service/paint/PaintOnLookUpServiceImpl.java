package com.honda.galc.service.paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.RfidErrorCodes;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.script.DataUtil;
import com.honda.galc.service.PaintOnLookUpService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.KeyValue;

public class PaintOnLookUpServiceImpl implements PaintOnLookUpService {

	private static final String LOGGER_ID = "PaintOnLookUpServiceImpl";
	private Vector<Integer> errorCodes;
	private Vector<String> errorMessages;

	public DataContainer vinLookUpBySeqNum(String seqNum, String processPointId, String plantName) {
		DataContainer dc = new DefaultDataContainer();

		try {

			InProcessProductDao inProcessProductDao = ServiceFactory
					.getDao(InProcessProductDao.class);
			List<Object[]> data = inProcessProductDao
					.findFirstFiveProducts(seqNum, processPointId, plantName);

			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			List<Frame> list = new ArrayList<Frame>();
			for (Object[] obj : data) {
				list.add(frameDao.findByKey(((String) obj[6]).trim()));
			}

			if (list.size() > 0) {

				for (int i = 0; i < list.size() || i < 5; i++) {
					if (i < list.size()) {
						dc.put(DataContainerTag.PRODUCT_ID + i,
								new StringBuilder(list.get(i).getProductId()));
						dc.put(DataContainerTag.PRODUCT_SPEC_CODE + i,
								new StringBuilder(list.get(i)
										.getProductSpecCode()));
					} else {
						dc.put(DataContainerTag.PRODUCT_ID + i,
								new StringBuilder("00000000000000000"));
						dc.put(DataContainerTag.PRODUCT_SPEC_CODE + i,
								new StringBuilder("00000000000000000"));

					}

				}

			}
		} catch (Exception ex) {
			getLogger().info("exception occured at vinLookUpBySeqNum method");
			ex.printStackTrace();
		}
		return dc;
	}

	public DataContainer validateRfidAttributes(DataContainer datacont,
			String applicationId) {
		DataContainer dc = new DefaultDataContainer();
		errorCodes = new Vector<Integer>();
		errorMessages = new Vector<String>();
		try {
			dc = datacont;
			String vin = dc.get(DataContainerTag.PRODUCT_ID).toString().trim();
			getLogger().info("Validating RFID for product id "+vin);
			checkAttributes(dc, applicationId);
			duplicateVinAlarmCheck(vin, applicationId);
			checkVinforWeldOn(vin);
			if (errorCodes.size() != 0) {
				dc.put("errorCode", new StringBuilder(errorCodes.get(0)
						.toString()));
				dc.put("errorMessage", new StringBuilder(errorMessages.get(0)
						.toString()));
			}
		} catch (Exception ex) {
			getLogger().info("Exception occured at RFID validations");
			ex.printStackTrace();
		}
		return dc;
	}

	private void checkAttributes(DataContainer dc, String applicationId) {

		for (KeyValue dataAtt : (ArrayList<KeyValue<String, StringBuilder>>) dc
				.get("DeviceAttributes")) {
			
			if (StringUtils.isEmpty(String.valueOf(dataAtt.getValue()))) {
				String missingst = dataAtt.getKey().toString().trim()
						.toUpperCase()
						+ "_MISSING";
				if (DataUtil.isInEnum(missingst, RfidErrorCodes.class)) {
					errorCodes
							.add(RfidErrorCodes.valueOf(missingst).getValue());
					errorMessages.add(RfidErrorCodes.valueOf(missingst)
							.getErrorMessage());
					getLogger().info(
							"attribute: " + dataAtt.getKey()
									+ ", value: " + dataAtt.getValue()
									+ ", validation status: fail");
					getLogger().info(
							RfidErrorCodes.valueOf(missingst) + " found");

				}
			} else {
				if (dc.get("DeviceSubstitutionList") != null) {
					if (((ConcurrentHashMap<String, StringBuilder>) dc
							.get("DeviceSubstitutionList")).get(dataAtt
							.getKey()) != null) {
						getLogger()
								.info(
										"DeviceFormat RfidAttributeValue:"
												+ ((ConcurrentHashMap<String, StringBuilder>) dc
														.get("DeviceSubstitutionList"))
														.get(dataAtt.getKey()));
						if (!((ConcurrentHashMap<String, StringBuilder>) dc
								.get("DeviceSubstitutionList")).get(
								dataAtt.getKey()).equals(dataAtt.getValue())) {
							String invalidst = dataAtt.getKey().toString()
									.trim().toUpperCase()
									+ "_NOT_VALID";
							if (DataUtil.isInEnum(invalidst,
									RfidErrorCodes.class)) {
								errorCodes.add(RfidErrorCodes
										.valueOf(invalidst).getValue());
								errorMessages.add(RfidErrorCodes.valueOf(
										invalidst).getErrorMessage());
								getLogger().info(
										"attribute: " + dataAtt.getKey()
												+ ", value: " + dataAtt.getValue()
												+ ", validation status: fail");
								getLogger().info(
										RfidErrorCodes.valueOf(invalidst)
												+ " found");
							}
						} else{
							getLogger().info(
									"attribute: " + dataAtt.getKey()
											+ ", value: " + dataAtt.getValue()
											+ ", validation status: pass");
						}
					}else{
						getLogger().info("DeviceFormatTagValue not found");
						getLogger().info(
								"attribute: " + dataAtt.getKey()
										+ ", value: " + dataAtt.getValue()
										+ ", validation status: fail");
					}
				} else {
					getLogger()
							.info(
									"attribute validations at deviceformat tag values not found");
					if (!dataAtt.getValue().equals("0000")) {
						String invalidst = dataAtt.getKey().toString().trim()
								.toUpperCase()
								+ "_NOT_VALID";
						if (DataUtil.isInEnum(invalidst, RfidErrorCodes.class)) {
							errorCodes.add(RfidErrorCodes.valueOf(invalidst)
									.getValue());
							errorMessages.add(RfidErrorCodes.valueOf(invalidst)
									.getErrorMessage());
							getLogger().info(
									"attribute: " + dataAtt.getKey()
											+ ", value: "
											+ dataAtt.getValue()
											+ ", validation status: fail");
							getLogger().info(
									RfidErrorCodes.valueOf(invalidst)
											+ " found");
						}
					}
				}

			}

		}
		if(errorCodes.size()==0)
			getLogger().info("RFID validation passed.");

	}

	private void checkVinforWeldOn(String vin) {
		InProcessProductDao inProcessProductDao = ServiceFactory
				.getDao(InProcessProductDao.class);
		InProcessProduct inProcessProduct = inProcessProductDao.findByKey(vin);
		if (inProcessProduct == null) {
			errorCodes.add(RfidErrorCodes.VIN_NOT_IN_WELD.getValue());
			errorMessages.add(RfidErrorCodes.VIN_NOT_IN_WELD.getErrorMessage());
			getLogger().info("VIN_NOT_IN_WELD found");
		}
	}

	private void duplicateVinAlarmCheck(String productId,
			String applicationId) {
			ProductResultDao productResultDao = ServiceFactory
					.getDao(ProductResultDao.class);
			List<ProductResult> productResults = productResultDao
					.findAllByProductAndProcessPoint(productId, applicationId);
			if (productResults != null && productResults.size() > 0) {
				errorCodes.add(RfidErrorCodes.DUPLICATE_VIN.getValue());
				errorMessages.add(RfidErrorCodes.DUPLICATE_VIN
						.getErrorMessage());
				getLogger().info("Duplicate Vin found");
			} else
				getLogger().info("Direct pass product id "+ productId);
		}
	

	private Logger getLogger() {
		return Logger.getLogger(LOGGER_ID);
	}

}
