package com.honda.ahm.lc.controller;

import com.honda.ahm.lc.task.ReceivingTransactionTask;
import org.springframework.web.bind.annotation.RestController;

import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.model.ShippingTransaction;
import com.honda.ahm.lc.service.IQueueManagerService;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.service.ShippingTransactionService;
import com.honda.ahm.lc.task.ShippingTransactionTask;
import com.honda.ahm.lc.util.PropertyUtil;

import java.util.List;

import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(path = "salesInterface")
public class LCSalesInterfaceController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PropertyUtil propertyUtil;

	@Autowired
	private IQueueManagerService queueManagerService;
	
	@Autowired
	private ShippingStatusService shippingStatusService;
	
	@Autowired
	private ShippingTransactionService shippingTransactionService;
	
	@Autowired
	private ShippingTransactionTask shippingTransactionTask;

	@Autowired
	private ReceivingTransactionTask receivingTransactionTask;

	private String queueName = "LQ.MQFT_RECEIVE.01";

	@GetMapping("send")
	String send() {
		try {
			queueManagerService.send(queueName, getJsonString());
			return "OK";
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("recv")
	String recv() {
		try {
			return queueManagerService.recv(queueName);
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("track")
	String track() {
		try {
			
			String processPointId = propertyUtil.getProcessPoint("AH-SHIP");
			String galcUrl = shippingStatusService.getGalcUrl("1HGCY2F83RA046588", "1");
			shippingStatusService.trackProduct(galcUrl, processPointId, "1HGCY2F83RA046588");
			return "OK";
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}
	
	@GetMapping("sendShippingMessage")
	String sendShippingMessage() {
		try {
			shippingTransactionTask.execute();
			return "OK";
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}
	
	@GetMapping("readStatusMessage")
	String readStatusMessage() {
		try {
			receivingTransactionTask.execute();
			return "OK";
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("test2")
	String testRestCall2() {
		try {
			String galcUrl = shippingStatusService.getGalcUrl("19UDE4H20RA025408", "1");
			ShippingStatus shippingStatus = shippingStatusService.findByProductId( galcUrl, "19UDE4H20RA025408");
			logger.info(shippingStatus.toString());
			shippingStatus.setStatus(4);
			ShippingStatus savedShippingStatus = shippingStatusService.saveShippingStatus( galcUrl, shippingStatus);
			logger.info(savedShippingStatus.toString());
			return "OK";
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}
	
	@GetMapping("test1")
	String testRestCall1() {
		try {
			String galcUrl = shippingStatusService.getGalcUrl("19UDE4H20RA025408", "1");
			List<ShippingTransaction> shippingTransaction = shippingTransactionService.get50ATransactionVin(galcUrl, 0, "AVQ1SH1P00101", 'Y', "VQSHIPCCC");
			logger.info(shippingTransaction.toString());
			
			String afoffTime = shippingTransactionService.getMaxActualTs(galcUrl, "19UDE4H20RA025408", propertyUtil.getAFOffProcessPoint());
			logger.info(afoffTime);
			String partSerialnumber = shippingTransactionService.getPartSerialNumber(galcUrl, "19UDE4H20RA025408", propertyUtil.getKeyNoPartName());
			logger.info(partSerialnumber);
			
			return "OK";
		} catch (JmsException ex) {
			ex.printStackTrace();
			return "FAIL";
		}
	}

	private String getJsonString() {
		return "{" + "\"transaction\": {" + "\"destination_environment\": \"TEST\"," + "\"destination_site\": \"YMS\","
				+ "\"plant_id\": \"A\"," + "\"Line id\": \"1\", " + "\"transaction_code\": \"VQ-SHIP\","
				+ "\"description\": \"50A or VQ SHIP\"," + "\"transaction_timestamp\": \"2023-10-02T12:00:00\"},"
				+ "\"vehicle\": {" + "\"vin\": \"5FPYK3650PB900143\"," + "\"model_id\": \"YK365PJN\","
				+ "\"model_type\": \"KK\"," + "\"model_option\": \"\"," + "\"color_code\": \"B-588P\","
				+ "\"engine_number\": \"8712091\"," + "\"key_number\": \"000L128\"," + "\"issue_date\": \"221101\","
				+ "\"adc_process_code\":\"\"," + "\"product_lot_number\": \"122101801400\","
				+ "\"kd_lot_number\": \"122101833\"," + "\"price\": \"2877743\"," + "\"assembly_off_date\": \"221024\","
				+ "\"ccc_number\":\"\"," + "\"parts_installed\":\"\"," + "\"purchase_contract_number\":\"\","
				+ "\"fif_codes\":\"\"," + "\"timestamp\": \"2023-10-02T12:00:00\"}}";

	}
	
	

}
