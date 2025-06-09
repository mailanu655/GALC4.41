package com.honda.ahm.lc.config.hcm3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.honda.ahm.lc.config.aap1.Aap1DataService;
import com.honda.ahm.lc.vdb.rest.ProductLastStatusController;

@Controller
@RequestMapping(path = "hcm3/productLastStatus")
public class Hcm3ProductLastStatusController extends ProductLastStatusController<Aap1DataService> {

	@Autowired
	public Hcm3ProductLastStatusController() {
		super();
	}

}