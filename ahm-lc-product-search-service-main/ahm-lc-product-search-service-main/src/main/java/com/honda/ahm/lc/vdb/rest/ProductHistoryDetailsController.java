package com.honda.ahm.lc.vdb.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.honda.ahm.lc.vdb.entity.ProductHistoryDetails;
import com.honda.ahm.lc.vdb.service.DataService;

public class ProductHistoryDetailsController<S extends DataService> {

	private Logger logger = LogManager.getLogger(ProductAgeDetailsController.class);

	@Autowired
	S dataService;

	@GetMapping(path = "by")
	public @ResponseBody List<ProductHistoryDetails> findHistoryByProduct(@RequestParam String productId) {
		logger.info("Get Product History Details for Product ID: " + productId);
		List<ProductHistoryDetails> historyList = dataService.getProductHistoryDetailsDao()
				.findHistoryByProduct(productId);
		return historyList;
	}

}
