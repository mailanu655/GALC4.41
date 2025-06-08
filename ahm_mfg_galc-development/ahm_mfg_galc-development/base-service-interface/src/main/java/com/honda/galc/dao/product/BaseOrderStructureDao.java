package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.MfgCtrlMadeFrom;
import com.honda.galc.entity.conf.BaseMCOrderStructure;
import com.honda.galc.service.IDaoService;

public interface BaseOrderStructureDao<E extends BaseMCOrderStructure, K> extends IDaoService<E, K>{

	public List<MfgCtrlMadeFrom> getMadeFromByOrderNoAndProdSpecCode(String orderNo, String prodSpecCode);
	
	public E findByKey(String orderId, String modeId);
	
	public void removeByKey(String orderId, String modeId);
	
	public List<MfgCtrlMadeFrom> getMadeFrom(String orderNo, String processPointId);

}

