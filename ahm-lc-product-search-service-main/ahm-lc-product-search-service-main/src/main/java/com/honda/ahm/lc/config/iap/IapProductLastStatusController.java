package com.honda.ahm.lc.config.iap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.honda.ahm.lc.config.aap1.Aap1DataService;
import com.honda.ahm.lc.vdb.rest.ProductLastStatusController;

@Controller
@RequestMapping(path = "iap/productLastStatus")
public class IapProductLastStatusController extends ProductLastStatusController<Aap1DataService> {

	@Autowired
	public IapProductLastStatusController() {
		super();
	}

}
