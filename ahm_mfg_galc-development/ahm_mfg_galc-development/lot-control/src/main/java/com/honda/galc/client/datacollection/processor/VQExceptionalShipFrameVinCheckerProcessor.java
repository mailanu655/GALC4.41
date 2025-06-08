package com.honda.galc.client.datacollection.processor;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.ServiceFactory;

public class VQExceptionalShipFrameVinCheckerProcessor extends VQShipFrameVinCheckerProcessor {

	protected Device device;
	public ClientContext clientContext;

	public VQExceptionalShipFrameVinCheckerProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
		this.clientContext = lotControlClientContext;
	}

	@Override
	public synchronized boolean execute(ProductId productId) {

		Logger.getLogger().debug("VQExceptionalShipFrameVinCheckerProcessor : Enter execute method");
		try {
			return super.execute(productId);
		} catch (Exception e) {
			Logger.getLogger().error(e, e.getMessage());
			getController().getFsm().error(new Message("MSG01", e.getCause().toString()));
		}

		Logger.getLogger().debug("VQExceptionalShipFrameVinCheckerProcessor : Exit execute method");
		return false;
	}

	@Override
	public boolean executeCheck(BaseProduct product, ProcessPoint processPoint) {
		if(super.executeCheck(product, processPoint)) {
			List<String> checkResults = executeProductChecks(product, processPoint,
					getProductCheckPropertyBean().getWarnIfNotMassProductionProductCheckTypes());
			if (checkResults.size() > 0) {
				showErrorDialog(checkResults);
	
				PreProductionLot preProdLot = (ServiceFactory.getDao(PreProductionLotDao.class))
						.findByKey(product.getProductionLot());
				if (StringUtils.equalsIgnoreCase(preProdLot.getDemandType(), "MP")) {
	
					handleException(product.getProductId() + " Failed Product Checks");
	
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	
}
