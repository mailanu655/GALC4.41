package com.honda.ahm.lc.task;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.DataContainer;
import com.honda.ahm.lc.messages.ShippingMessage;
import com.honda.ahm.lc.messages.ShippingVehicle;
import com.honda.ahm.lc.messages.Transaction;
import com.honda.ahm.lc.model.Frame;
import com.honda.ahm.lc.model.FrameSpec;
import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.model.ShippingTransaction;
import com.honda.ahm.lc.service.FrameService;
import com.honda.ahm.lc.service.FrameSpecService;
import com.honda.ahm.lc.service.IQueueManagerService;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.service.ShippingTransactionService;
import com.honda.ahm.lc.util.EmailSender;
import com.honda.ahm.lc.util.JSONUtil;
import com.honda.ahm.lc.util.PropertyUtil;

@Service(value = "shippingTransactionTask")
public class ShippingTransactionTask implements ITransactionTask {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IQueueManagerService queueManagerService;

	@Autowired
	PropertyUtil propertyUtil;

	@Autowired
	private EmailSender emailSender;

	@Autowired
	private ShippingStatusService shippingStatusService;

	@Autowired
	private ShippingTransactionService shippingTransactionService;

	@Autowired
	private FrameService frameService;

	@Autowired
	private FrameSpecService frameSpecService;

	@Override
	public void execute() {
		List<String> errorMessages = new ArrayList<String>();
		try {
			// Read and parse message received from GALC Queue
			String message = queueManagerService.recv(propertyUtil.getLCReceivingQueueName());

			if (StringUtils.isNotBlank(message)) {
				logger.info("Message read from Queue-" + message);
				DataContainer dataContainer = JSONUtil.getDataContainerFromJSON(message);

				if (dataContainer != null) {
					logger.info("Received LC Shipping message", dataContainer.toString());
					String productId = dataContainer.getProduct_Id();
					String lineId = dataContainer.getLine_Id();
					if (StringUtils.isNotBlank(productId)) {
						String galcUrl = shippingStatusService.getGalcUrl(productId, lineId);

						Map<ShippingTransaction, List<String>> map;
						if (dataContainer.getStatusType().equals(StatusEnum.VQ_SHIP.getType())) {
							map = populateShippingData(galcUrl, dataContainer, errorMessages);
						} else {
							map = populateAFOffData(galcUrl, dataContainer, errorMessages);
						}

						if (map != null && errorMessages.isEmpty()) {

							for (ShippingTransaction shippingTransaction : map.keySet()) {

								// Generate Message
								ShippingMessage shippingMessage = generateMessage(galcUrl, shippingTransaction,
										dataContainer);

								// Put message to YMS input Queue.
								String outPutMessage = JSONUtil.convertShippingMessageToJSON(shippingMessage);
								if (StringUtils.isNotBlank(outPutMessage)) {
									logger.info(" sending the message to MQ -" + outPutMessage);
									String result = queueManagerService.send(propertyUtil.getSalesShippingQueueName(),
											outPutMessage);

									if (shippingTransactionService.isOK(result)) {
										updateGalc(galcUrl, shippingTransaction, dataContainer.getStatusType());
									} else {
										String msg = "Error while sending the message to MQ-" + outPutMessage;
										logger.error(msg);
										errorMessages.add(msg);
									}
								} else {
									String msg = "Error converting message to json-" + shippingMessage.toString();
									logger.error(msg);
									errorMessages.add(msg);
								}
							}
						} else {
							String msg = "Cannot not retrieve shipping information for -" + productId;
							logger.error(msg);
							errorMessages.add(msg);
						}
					} else {
						String msg = " No ProductId in message, received -" + message;
						logger.error(msg);
						errorMessages.add(msg);
					}
				} else {
					String msg = "Invalid Message in Queue, received -" + message;
					logger.error(msg);
					errorMessages.add(msg);
				}
			} else {
				logger.info("No Message in Queue to read");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			errorMessages.add(e.getMessage());
		}

		if (!errorMessages.isEmpty()) {
			emailSender.sendEmail(getClass().getName() + " : ", errorMessages);
		}
	}

	private ShippingMessage generateMessage(String galcUrl, ShippingTransaction shippingTransaction,
			DataContainer dataContainer) {
		ShippingMessage shippingMessage = new ShippingMessage();
		ShippingVehicle shippingVehicle = new ShippingVehicle();

		// copy entity properties
		shippingVehicle.setVin(shippingTransaction.getVin());
		shippingVehicle.setAdc_process_code(propertyUtil.getAdcProcessCode());
		shippingVehicle.setAssembly_off_date(shippingTransaction.getAfOffDate());
		shippingVehicle.setCcc_number(shippingTransaction.getCccRegNbr());
		shippingVehicle.setColor_code(shippingTransaction.getSalesModelColorCode());
		shippingVehicle.setEngine_number(shippingTransaction.getEngineNumber());
		shippingVehicle.setIssue_date(shippingTransaction.getCicIssuData());
		shippingVehicle.setKd_lot_number(getKdLotNumber(dataContainer, shippingTransaction));
		shippingVehicle.setKey_number(shippingTransaction.getKeyNumber());
		shippingVehicle.setModel_id(shippingTransaction.getSalesModelCode());
		shippingVehicle.setModel_option(shippingTransaction.getSalesModelOptionCode());
		shippingVehicle.setModel_type(shippingTransaction.getSalesModelTypeCode());
		shippingVehicle.setParts_installed(propertyUtil.getPartInstalled());
		shippingVehicle.setPrice(shippingTransaction.getPriceString());
		shippingVehicle.setPrint_loc(dataContainer.getPrint_location());
		shippingVehicle.setProduct_lot_number(getProdLotNumber(dataContainer, shippingTransaction));
		shippingVehicle.setPurchase_contract_number(dataContainer.getPurchase_contract_number());
		shippingVehicle.setTimestamp(getTransactionTimeStamp(shippingTransaction));

		if (isPMC()) {
			// put the FIF codes and fillers
			String fifCode = shippingTransactionService.getFIFCodeBySpecCode(galcUrl, shippingTransaction.getVin());
			if (fifCode == null) {
				logger.error("Error: Unable to get the FIF CODE to the VIN = " + shippingTransaction.getVin()
						+ ", check the SALES ORDER FIF CODES Data");
			} else {
				List<String> fifCodes = new ArrayList<String>();
				fifCodes.add(fifCode);
				shippingVehicle.setFif_codes(fifCodes);
			}
		} else {
			shippingVehicle.setFif_codes(new ArrayList<String>());
		}
		StatusEnum status = StatusEnum.getStatusByType(dataContainer.getStatusType());
		Transaction transaction = new Transaction();
		transaction.setLine_id(dataContainer.getLine_Id());
		transaction.setPlant_id(dataContainer.getPlant_Id());
		transaction.setTransaction_code(status.getType());
		transaction.setDestination_site(propertyUtil.getDestinationSite());
		transaction.setDestination_environment(propertyUtil.getDestinationEnv());
		transaction.setDescription(status.getName());
		transaction.setTransaction_timestamp(getTransactionTimeStamp(shippingTransaction));
		shippingMessage.setTransaction(transaction);
		shippingMessage.setVehicle(shippingVehicle);
		return shippingMessage;
	}

	private boolean isPMC() {

		return propertyUtil.getPlantName().equalsIgnoreCase("PMC");
	}

	private Map<ShippingTransaction, List<String>> populateAFOffData(String galcUrl, DataContainer dataContainer,
			List<String> errorMessages) {
		String productId = dataContainer.getProduct_Id();

		Map<ShippingTransaction, List<String>> map = new HashMap<ShippingTransaction, List<String>>();
		ShippingTransaction shippingTransaction = new ShippingTransaction();

		logger.info("Shipping Transaction record for productId-" + productId);

		Frame frame = frameService.getFrame(galcUrl, productId);
		if (frame == null) {
			String msg = String.format("Error : No Shipping Transaction VIN:%s found.", dataContainer.getProduct_Id());
			logger.error(msg);
			errorMessages.add(msg);
			map.put(shippingTransaction, errorMessages);
			return map;
		}

		FrameSpec frameSpec = frameSpecService.getFrameSpec(galcUrl, frame.getProductSpecCode());

		if (frameSpec == null) {
			String msg = String.format(
					"Error : Shipping Transaction VIN:%s has missing FrameSpec and it will not be processed.",
					frame.getProductId());
			logger.error(msg);
			errorMessages.add(msg);
			map.put(shippingTransaction, errorMessages);
			return map;
		}

		shippingTransaction.setVin(frame.getProductId());
		shippingTransaction.setSalesModelCode(frameSpec.getSalesModelCode());
		shippingTransaction.setSalesModelTypeCode(frameSpec.getSalesModelTypeCode());
		shippingTransaction.setSalesModelOptionCode("");
		shippingTransaction.setSalesModelColorCode(frameSpec.getSalesExtColorCode());
		shippingTransaction.setEngineNumber("");
		shippingTransaction.setKeyNumber("");
		shippingTransaction.setCccRegNbr("");
		shippingTransaction.setCicIssuData("");
		shippingTransaction.setPriceString("");

		String afOffDate = frame.getActualOffDate();
		if (StringUtils.isEmpty(afOffDate)) {
			String afOffProcessPt = dataContainer.getProcess_Point_Id();
			afOffDate = shippingTransactionService.getMaxActualTs(galcUrl, shippingTransaction.getVin(),
					afOffProcessPt);
		}
		map = populateAfOffDate(afOffDate, shippingTransaction, errorMessages);

		return map;
	}

	private Map<ShippingTransaction, List<String>> populateAfOffDate(String afOffTs,
			ShippingTransaction shippingTransaction, List<String> errorMessages) {

		String afOffDate = "";
		if (afOffTs != null) {

			try {
				afOffDate = getParsedOffDate(afOffTs);
				shippingTransaction.setAfOffDate(afOffDate);
			} catch (ParseException e) {
				String msg = String.format("Error : Shipping Transaction VIN:%s, Unable parse OFF Date.",
						shippingTransaction.getVin());
				logger.error(msg);
				errorMessages.add(msg);
			}
		}

		if (StringUtils.isBlank(afOffDate)) {
			String msg = String.format(
					"Error : Shipping Transaction VIN:%s, has missing AF OFF Date and it will not be processed.",
					shippingTransaction.getVin());
			logger.error(msg);
			errorMessages.add(msg);

		}
		logger.info("Shipping Transaction record -" + shippingTransaction.toString());
		Map<ShippingTransaction, List<String>> map = new HashMap<ShippingTransaction, List<String>>();
		map.put(shippingTransaction, errorMessages);
		return map;
	}

	private Map<ShippingTransaction, List<String>> populateShippingData(String galcUrl, DataContainer dataContainer,
			List<String> errorMessages) {
		final Integer status = 0;
		final Character sendFlag = 'Y';
		String cccPartName = propertyUtil.getCCCPartName();
		String productId = dataContainer.getProduct_Id();
		String keyNoPartName = propertyUtil.getKeyNoPartName();

		Map<ShippingTransaction, List<String>> map = new HashMap<ShippingTransaction, List<String>>();

		logger.info("Shipping Transaction record for productId-" + productId);

		List<ShippingTransaction> vins = shippingTransactionService.get50ATransactionVin(galcUrl, status,
				dataContainer.getProcess_Point_Id(), sendFlag, cccPartName);

		for (ShippingTransaction shippingTransaction : vins) {
			if (!shippingTransaction.getVin().equalsIgnoreCase(productId)) {
				continue;
			} else {

				logger.info("VIN: " + shippingTransaction.getVin() + " = " + shippingTransaction.getPriceString());
				if (StringUtils.isBlank(shippingTransaction.getPriceString())) {
					String msg = " VIN: " + shippingTransaction.getVin()
							+ " doesn't have PRICE, it is not possible to send.";
					shippingTransaction.setPriceString(String.format("%09d", 0));
					logger.info(msg);
				} else {
					shippingTransaction.setPriceString(String.format("%09d", 0));
				}
			}

			if (StringUtils.isBlank(shippingTransaction.getEngineNumber())) {
				String msg = String.format(
						" Shipping Transaction VIN:%s, has missing EIN and it will not be processed.",
						shippingTransaction.getVin());
				logger.info(msg);
			}

			String keyNo = shippingTransaction.getKeyNumber();
			if (keyNoPartName != null && !keyNoPartName.isEmpty()) {
				String keyVal = shippingTransactionService.getPartSerialNumber(galcUrl, productId, keyNoPartName);
				if (!StringUtils.isEmpty(keyVal)) {
					keyNo = StringUtils.leftPad(keyVal, 7, '0');
					shippingTransaction.setKeyNumber(keyNo);
				}

			} else {
				if (StringUtils.isBlank(keyNo)) {
					String msg = String.format(
							"Error : Shipping Transaction VIN:%s, has missing key number and it will not be processed.",
							shippingTransaction.getVin());
					logger.error(msg);
					errorMessages.add(msg);
					continue;
				}
			}

			String afOffDate = shippingTransaction.getAfOffDate();

			if (StringUtils.isEmpty(afOffDate)) {
				String afOffProcessPt = propertyUtil.getAFOffProcessPoint();
				String[] processPtList = afOffProcessPt.split(",");
				for (String processPt : processPtList) {
					afOffDate = shippingTransactionService.getMaxActualTs(galcUrl, shippingTransaction.getVin(),
							processPt);
					if (!StringUtils.isEmpty(afOffDate))
						break;
				}
				map = populateAfOffDate(afOffDate, shippingTransaction, errorMessages);
			} else {
				shippingTransaction.setAfOffDate(afOffDate);
				logger.info("Shipping Transaction record -" + shippingTransaction.toString());
				map.put(shippingTransaction, errorMessages);
			}
			return map;
		}

		return null;
	}

	private String getTransactionTimeStamp(ShippingTransaction shippingTransaction) {
		String dateString = shippingTransaction.getDateString();
		String timeString = shippingTransaction.getTime();
		String now="";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (StringUtils.isNotBlank(dateString) && StringUtils.isNotBlank(timeString)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd" + " " + "HHmmss");
			try {
				Date date = dateFormat.parse(dateString + " " + timeString);
				return simpleDateFormat.format(date);
			} catch (ParseException e) {
				String msg = String.format("Error : Shipping Transaction VIN:%s, Unable parse Date.",
						shippingTransaction.getVin());
				logger.error(msg);
			}
		}else {
			
			now = simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
		}

		return now;
	}

	private String getParsedOffDate(String actualOffDate) throws ParseException {
		
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date offDate;
		offDate = inputFormat.parse(actualOffDate);
		return new SimpleDateFormat("yyMMdd").format(offDate);
	}

	private void updateGalc(String galcUrl, ShippingTransaction shippingTransaction, String statusType) {
		if (statusType.equals(StatusEnum.VQ_SHIP.getType())) {
			// Update Shipping Transaction Table
			ShippingTransaction newTransaction = new ShippingTransaction();
			shippingTransaction.setSendFlag('Y');
			BeanUtils.copyProperties(shippingTransaction, newTransaction);
			shippingTransactionService.saveShippingTransaction(galcUrl, newTransaction);

			// Update Shipping Status Table
			ShippingStatus shippingStatus = shippingStatusService.findByProductId(galcUrl,
					shippingTransaction.getVin());
			// Change the status value 0 to 1 (already sent to AH)
			shippingStatus.setStatus(1);

			shippingStatusService.saveShippingStatus(galcUrl, shippingStatus);
		}
	}

	private String getProdLotNumber(DataContainer dataContainer, ShippingTransaction shippingTransaction) {
		String prodLotNumber = "";
		if (StringUtils.isNotBlank(shippingTransaction.getProductionSequenceNumber())) {
			prodLotNumber = shippingTransaction.getLineNumber() + shippingTransaction.getProductionDate()
					+ shippingTransaction.getProductionSequenceNumber() + shippingTransaction.getProductionSuffix();
		} else {
			String lineId = dataContainer.getLine_Id();
			String dateString = dataContainer.getProduction_lot().substring(10, 16);
			String seq = dataContainer.getProduction_lot().substring(16, 19);
			String suffix = dataContainer.getProduction_lot().substring(19, 20);
			prodLotNumber = lineId + dateString + seq + "0" + suffix;
		}

		return prodLotNumber;
	}

	private String getKdLotNumber(DataContainer dataContainer, ShippingTransaction shippingTransaction) {
		String kdLotNumber = "";
		if (StringUtils.isNotEmpty(shippingTransaction.getKdLotSequenceNumber())) {
			kdLotNumber = shippingTransaction.getKdLotLineNumber() + shippingTransaction.getKdLotDate()
					+ shippingTransaction.getKdLotSequenceNumber() + shippingTransaction.getKdLotSuffix();
		} else {
			String lineId = dataContainer.getLine_Id();
			String dateString = dataContainer.getKd_lot().substring(8, 12);
			String seq = dataContainer.getKd_lot().substring(13, 16);
			String suffix = dataContainer.getKd_lot().substring(17, 18);
			kdLotNumber = lineId + dateString + seq + suffix;
		}

		return kdLotNumber;
	}
}
