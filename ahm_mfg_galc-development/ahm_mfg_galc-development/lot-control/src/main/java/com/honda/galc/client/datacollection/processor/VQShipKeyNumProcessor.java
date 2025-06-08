package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.UpdateEntity;
import com.honda.galc.service.ServiceFactory;

public class VQShipKeyNumProcessor extends PartSerialNumberProcessor {

	private final static String MESSAGE_ID = "PART_SN";

	private final static int MAX_LENGTH_KEY = 4;
	private final static String ENTITIES_FOR = "VQShipKeyNumProcessor";

	public VQShipKeyNumProcessor(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized boolean execute(PartSerialNumber partnumber) {
		boolean result = false;
		try {
			result = checkkeyNo(partnumber.getPartSn());
			if (result == false) {
				getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
						"Key Number format is incorrect");
				return result;
			}

			String productId = getController().getState().getProductId();
			EntityList<AbstractEntity> entityList = new EntityList<AbstractEntity>(ENTITIES_FOR, productId, partnumber.getPartSn());
			

			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			Frame frame = frameDao.findByKey(productId);

			frame.setKeyNo(partnumber.getPartSn());

			entityList.addEntity(new UpdateEntity(frame, frame.toString(),frameDao));
			getController().getState().getProduct().setMasterEntityList(entityList);
			Logger.getLogger().info(
					"Updated the Key Number for VIN:" + productId);

			result = super.execute(partnumber);
		} catch (Exception e) {
			getController().getFsm().partSnNg(installedPart, MESSAGE_ID,
					"An Exception occured");
			result = false;
			Logger.getLogger().error(e.getMessage());

			return result;
		}
		return result;
	}

	private boolean checkkeyNo(String keyNo) {

		if (keyNo.length() != MAX_LENGTH_KEY) {

			return false;
		}

		String firstChar = keyNo.substring(0, 1);
		firstChar.trim();

		for (int i = 0; i < firstChar.length(); i++) {
			if (!Character.isLetter(firstChar.charAt(i))) {
				return false;
			}
		}

		String restChar = keyNo.substring(1);
		restChar.trim();
		for (int i = 0; i < restChar.length(); i++) {
			if (!Character.isDigit(restChar.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
