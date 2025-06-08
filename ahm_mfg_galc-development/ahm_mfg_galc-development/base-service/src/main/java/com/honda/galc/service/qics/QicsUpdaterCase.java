package com.honda.galc.service.qics;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.CaseDao;
import com.honda.galc.entity.product.Case;

public class QicsUpdaterCase extends QicsUpdaterProduct<Case> {
	
	
	@Autowired
	CaseDao caseDao;

	@Override
	protected Case findProductById(String productId) {
		// TODO Auto-generated method stub
		return caseDao.findByKey(productId);

	}

}












