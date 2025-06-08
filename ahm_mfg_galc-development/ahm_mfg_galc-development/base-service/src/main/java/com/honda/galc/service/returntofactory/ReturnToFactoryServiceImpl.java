package com.honda.galc.service.returntofactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ReturnToFactoryService;

public class ReturnToFactoryServiceImpl implements ReturnToFactoryService {
	@Autowired
	public FrameDao frameDao;
	@Autowired
	public PurchaseContractDao purchaseContractDao;
	@Autowired
	public QsrDao qsrDao;
	
	@Transactional
	public void processRetrun(List<Frame> products, List<PurchaseContract> purchaseContracts, List<HoldResult> holdResults, Qsr qsr) {
		this.frameDao.updateAll(products);
		this.purchaseContractDao.updateAll(purchaseContracts);
		this.qsrDao.holdProducts(ProductType.FRAME, holdResults, qsr);
	}
}