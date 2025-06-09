package com.honda.ahm.lc.vdb.rest;

import java.sql.Time;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.sql.Timestamp;

import com.honda.ahm.lc.vdb.service.DataService;

public class ProductLastStatusController<S extends DataService>  {
	
	private static Logger logger = LogManager.getLogger(ProductLastStatusController.class);

	@Autowired
	S dataService;
	/*
	 * @GetMapping(path = "getLastTrackingDate") public @ResponseBody Timestamp
	 * findAllProductAge(@RequestParam String productId,@RequestParam String
	 * processPointId) {
	 * 
	 * logger.info("Get Product Last Status Details for Product ID: "+productId);
	 * Timestamp lastTrackingDate =
	 * dataService.getProductLastStatusDetailsDao().getLastTrackingDate(productId,
	 * processPointId); if(lastTrackingDate==null) { return null; } else { return
	 * lastTrackingDate; }
	 * 
	 * }
	 */
}
