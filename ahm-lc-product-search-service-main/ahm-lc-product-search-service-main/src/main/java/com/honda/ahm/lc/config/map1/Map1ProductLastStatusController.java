package com.honda.ahm.lc.config.map1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.honda.ahm.lc.config.aap1.Aap1DataService;
import com.honda.ahm.lc.vdb.rest.ProductLastStatusController;

@Controller
@RequestMapping(path = "map1/productLastStatus")
public class Map1ProductLastStatusController extends ProductLastStatusController<Aap1DataService> {

	@Autowired
	public Map1ProductLastStatusController() {
		super();
	}

}
