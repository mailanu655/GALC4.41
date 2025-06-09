package com.honda.ahm.lc.service;

import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.model.Frame;
import com.honda.ahm.lc.model.FrameShipConfirmation;
import com.honda.ahm.lc.model.FrameShipConfirmationId;
import com.honda.ahm.lc.model.FrameSpec;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("frameShipConfirmationService")
public class FrameShipConfirmationService extends BaseGalcService<FrameShipConfirmation, String> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Autowired
	private FrameService frameService;

	@Autowired
	private FrameSpecService frameSpecService;

	public List<String> processFrameShipConfirmation(String galcUrl, final String productId, final String processPointId,
			final String messageDate, final String messageTime, StatusEnum status) {
		List<String> errorMessages = new ArrayList<String>();
		try {

			Frame frame = frameService.getFrame(galcUrl, productId);
			if (frame == null) {
				logger.info("Unable to find the VIN record - " + productId);
				return errorMessages;
			}
			if (StringUtils.isBlank(frame.getEngineSerialNo())) {
				logger.info("NO Engine Assigned to VIN  - " + productId);
				return errorMessages;
			}
			FrameSpec frameSpec = frameSpecService.getFrameSpec(galcUrl, frame.getProductSpecCode());

			FrameShipConfirmationId frameShipConfirmationId = new FrameShipConfirmationId();
			frameShipConfirmationId.setEngineId(frame.getEngineSerialNo());
			frameShipConfirmationId.setProcessPointId(processPointId);
			frameShipConfirmationId.setProductId(productId);

			FrameShipConfirmation frameShipConfirmation = new FrameShipConfirmation();
			frameShipConfirmation.setId(frameShipConfirmationId);
			frameShipConfirmation.setFrameModel(frameSpec.getModelYearCode() + frameSpec.getModelCode());
			frameShipConfirmation.setFrameOption(frameSpec.getModelOptionCode());
			frameShipConfirmation.setFrameType(frameSpec.getModelTypeCode());
			frameShipConfirmation.setEventDate(messageDate);
			frameShipConfirmation.setEventTime(messageTime);
			frameShipConfirmation.setExtColor(frameSpec.getExtColorCode());
			frameShipConfirmation.setIntColor(frameSpec.getIntColorCode());
			frameShipConfirmation.setRecordType(String.valueOf(status.getStatus()));
			frameShipConfirmation.setSentFlag("I");

			save(galcUrl, frameShipConfirmation, GalcDataType.FRAME_SHIP_CONFIRM);
		} catch (Exception e) {
			logger.error(e.getMessage());
			errorMessages.add(e.getMessage());
		}
		
		return errorMessages;
	}

}
