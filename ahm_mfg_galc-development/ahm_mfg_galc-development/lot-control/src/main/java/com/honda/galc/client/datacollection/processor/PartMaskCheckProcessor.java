package com.honda.galc.client.datacollection.processor;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.ServiceFactory;

public class PartMaskCheckProcessor extends PartSerialNumberProcessor {
	
	public PartMaskCheckProcessor(ClientContext context) {
		super(context);

	}

	@Override
	public synchronized boolean execute(PartSerialNumber partnumber) {
		Logger.getLogger().debug("PartMaskCheckProcessor : Enter confirmPartSerialNumber");
		try {
			Logger.getLogger().info("Process part:" + partnumber.getPartSn());
			validatePartSn(partnumber.getPartSn());
			Logger.getLogger().debug("PartMaskCheckProcessor:: Exit confirmPartSerialNumber ok");
			return true;

		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, se.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().error(new Message(PART_SN_MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, "ThreadID = "+Thread.currentThread().getName()+" :: execute() : Exception : "+e.toString());
			getController().getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t){
			Logger.getLogger().error(t, "ThreadID = "+Thread.currentThread().getName()+" :: execute() : Exception : "+t.toString());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		}
		Logger.getLogger().debug("PartMaskCheckProcessor:: Exit confirmPartSerialNumber ng");
		return false;
	}
	
	private void validatePartSn(String partSn) {
		try {
			String vin = getController().getState().getProduct().getProductId()
					.trim();
			Map<String, String> partNames = context.getProperty()
					.getPartMarkCheck();
			if (partNames == null || partNames.isEmpty()) {
				Logger.getLogger().error("ComponentProperty not found in db");
				getController().getFsm().message(
						new Message(PART_SN_MESSAGE_ID,
								"ComponentProperty not found",
								MessageType.ERROR));
				LotControlAudioManager.getInstance().playNGSound();
				return;
			}

			for (String st : partNames.keySet()) {
				String previousPartName = st.trim();
				if (StringUtils.isEmpty(previousPartName)) {
					Logger.getLogger().error(
							"PartName not found in configuration");
					getController().getFsm().message(
							new Message(PART_SN_MESSAGE_ID,
									"PartName not found in configuration",
									MessageType.ERROR));
					LotControlAudioManager.getInstance().playNGSound();
					return;
				}
				InstalledPartId installedPartId = new InstalledPartId(vin,
						previousPartName);
				InstalledPart installPart = new InstalledPart();
				installPart = ServiceFactory.getDao(InstalledPartDao.class)
						.findByKey(installedPartId);
				if (installPart == null) {
					Logger.getLogger().error(
							"InstalledPart record not found in db");
					getController().getFsm().message(
							new Message(PART_SN_MESSAGE_ID,
									"InstalledPart record not found in db",
									MessageType.ERROR));
					LotControlAudioManager.getInstance().playNGSound();
					return;
				}
				String partSerialNumber = installPart.getPartSerialNumber().trim();

				if (!(partSn.equals(partSerialNumber))) {
					Logger.getLogger().error("PartName does not match");
					getController().getFsm().message(
							new Message(PART_SN_MESSAGE_ID,
									"PartName does not match",
									MessageType.ERROR));
					LotControlAudioManager.getInstance().playNGSound();
					return;
				}

				// check installedpart status and measurement status must be
				// "OK"
				List<Measurement> measurements = ServiceFactory.getDao(
						MeasurementDao.class).findAll(vin, previousPartName);
				for (Measurement measurement : measurements) {
					if (!(measurement.getMeasurementStatus().equals(InstalledPartStatus.OK))) {
						Logger.getLogger().error("MeasurementStatus is NG");
						getController().getFsm().message(
								new Message(PART_SN_MESSAGE_ID,
										"MeasurementStatus is NG",
										MessageType.ERROR));
						LotControlAudioManager.getInstance().playNGSound();
						return;
					}
				}
				if (!(installPart.getInstalledPartStatus().equals(InstalledPartStatus.OK))) {
					Logger.getLogger().error("InstalledPartStatus is NG");
					getController().getFsm().message(
							new Message(PART_SN_MESSAGE_ID,
									"InstalledPartStatus is NG",
									MessageType.ERROR));
					LotControlAudioManager.getInstance().playNGSound();
					return;
				}

				installedPart.setPartSerialNumber(partSn);
				installedPart.setInstalledPartStatusId(1);
				installedPart.setPartId(getController().getState()
						.getCurrentLotControlRulePartList().get(0).getId()
						.getPartId());
				installedPart.setValidPartSerialNumber(true);
				getController().getFsm().partSnOk(installedPart);
			}
			
		} catch (Exception ex) {
			Logger.getLogger().error(ex, ex.getMessage());
			getController().getFsm().error(
					new Message(PART_SN_MESSAGE_ID,
							"PartSerialNumber validation failed",
							MessageType.ERROR));
			LotControlAudioManager.getInstance().playNGSound();
			getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, ex.getMessage());
		}
		return;

	}

}