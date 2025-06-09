package com.honda.ahm.lc.vdb.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.honda.ahm.lc.vdb.entity.ProductAgeDetails;
import com.honda.ahm.lc.vdb.service.DataService;

public class ProductAgeDetailsController<S extends DataService> {
	
	private Logger logger = LogManager.getLogger(ProductAgeDetailsController.class);
	
	@Autowired
	S dataService;

	public ProductAgeDetailsController() {
		super();
	}

	@GetMapping(path = "age")
	public @ResponseBody String findAllProductAge(@RequestParam String productId, @RequestParam String processPointId) {
		logger.info("Get Product Age Details for Product ID: "+productId+", Process Point ID: "+processPointId);
		List<ProductAgeDetails> productAgeList = dataService.getProductAgeDetailsDao().findAllBy(productId, processPointId);
		if(productAgeList.isEmpty()) {
			return null;
		} else {
			return productAgeList.get(0).getAge();
		}
	}

}
