package com.honda.galc.service;

import java.util.List;

import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.entity.product.Qsr;

public interface ReturnToFactoryService extends IService {
	
	public void processRetrun(List<Frame> products, List<PurchaseContract> purchaseContracts, List<HoldResult> holdResults, Qsr qsr);
	
}