package com.honda.ahm.lc.config.aap1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.honda.ahm.lc.vdb.rest.ProductLastStatusController;

@Controller
@RequestMapping(path = "aap1/productLastStatus")
public class Aap1ProductLastStatusController extends ProductLastStatusController<Aap1DataService> {

	@Autowired
	public Aap1ProductLastStatusController() {
		super();
	}

}