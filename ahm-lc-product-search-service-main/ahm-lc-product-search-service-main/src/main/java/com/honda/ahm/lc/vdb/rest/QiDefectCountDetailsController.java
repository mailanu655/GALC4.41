package com.honda.ahm.lc.vdb.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.ahm.lc.vdb.service.DataService;

public class QiDefectCountDetailsController<S extends DataService> {
	
	@SuppressWarnings("unused")
	private Logger logger = LogManager.getLogger(ProductAgeDetailsController.class);
	
	@Autowired
	S dataService;
	
}
