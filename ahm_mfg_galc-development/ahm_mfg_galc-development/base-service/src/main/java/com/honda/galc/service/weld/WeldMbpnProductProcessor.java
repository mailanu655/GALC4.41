package com.honda.galc.service.weld;

import com.honda.galc.dto.rest.ProductTrackDTO;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.service.tracking.MbpnProductProcessor;

/**
 * 
 * @author Wade Pei <br>
 * @date Oct 22, 2013
 */
public class WeldMbpnProductProcessor extends MbpnProductProcessor<ProductTrackDTO> {

	@Override
	protected String validateProduct(String productId, PreProductionLot productionLot, ProductStampingSequence pss) {
		// TODO Implement this method.
		return super.validateProduct(productId, productionLot, pss);
	}

	@Override
	protected PreProductionLot getNextOrder(String planCode) {
		return getPreProductionLotDao().getNextPreProductionLot(planCode, PreProductionLotSendStatus.SENT.getId());
	}

}
