package com.honda.galc.service.qics;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.SubProduct;

public class QicsUpdaterSubProduct extends QicsUpdaterProduct<SubProduct>{

	@Autowired
	SubProductDao dao;
	
	@Override
	protected SubProduct findProductById(String productId) {
		return dao.findByKey(productId);
	}

}
