package com.honda.ahm.lc.config.hcl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.honda.ahm.lc.config.aap1.Aap1DataService;
import com.honda.ahm.lc.vdb.rest.ProductLastStatusController;

@Controller
@RequestMapping(path = "hcl/productLastStatus")
public class HclProductLastStatusController extends ProductLastStatusController<Aap1DataService> {

	@Autowired
	public HclProductLastStatusController() {
		super();
	}

}
