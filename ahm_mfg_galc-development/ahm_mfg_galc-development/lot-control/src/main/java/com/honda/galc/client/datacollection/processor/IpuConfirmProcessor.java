package com.honda.galc.client.datacollection.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;

public class IpuConfirmProcessor extends PartSerialNumberProcessor {
	List<InstalledPart> derivedParts = new ArrayList<InstalledPart>();
	protected DeviceFormatDao deviceFormatDao = null;
	protected PartSpecDao partSpecDao = null;
	
	public IpuConfirmProcessor(ClientContext context) {
		super(context);
		
	}
	
	@Override
	public synchronized boolean execute(PartSerialNumber partnumber) {
		
		Logger.getLogger().debug("IpuConfirmProcessor : Enter confirmPartSerialNumber");
		try {
			Logger.getLogger().info("Process part:" + partnumber.getPartSn());
			super.confirmPartSerialNumber(partnumber);
			derivedParts = prepareForSave();
			getController().getFsm().partsSnOk(derivedParts, installedPart);
			Logger.getLogger().debug("IpuConfirmProcessor:: Exit confirmPartSerialNumber ok");
			
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
		Logger.getLogger().debug("IpuConfirmProcessor:: Exit confirmPartSerialNumber ng");
		return false;

	} 
	
	
	private List<InstalledPart> prepareForSave(){
			List<InstalledPart> resultList = new ArrayList<InstalledPart>();
			String currentPartName = getController().getCurrentLotControlRule().getPartName().getPartName().toString();
			List<DeviceFormat> deviceFormats = getDeviceFormatDao().findAllByDeviceId(currentPartName);
			for (DeviceFormat deviceFormat : deviceFormats) {
				InstalledPart installPart = createNewInstalledPart(
						deviceFormat, installedPart.getPartSerialNumber(), getController().getState().getProduct().getProductId());
				resultList.add(installPart);
			}
		
		return resultList;
	}
	
	public DeviceFormatDao getDeviceFormatDao() {
		if (deviceFormatDao == null) {
			deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		}
		return deviceFormatDao;
	}

	private PartSpecDao getPartSpecDao() {
		if (partSpecDao == null) {
			partSpecDao = ServiceFactory.getDao(PartSpecDao.class);
		}
		return partSpecDao;
	}

	private InstalledPart createNewInstalledPart(DeviceFormat deviceFormat,
			String scannedIPUBarcode, String productId) {
		List<Measurement> measurementList = new ArrayList<Measurement>();
		InstalledPart installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setPartId("A0000");

		installedPart.setValidPartSerialNumber(true);
		List<PartSpec> partSpec = getPartSpecDao().findAllByPartName(
				deviceFormat.getId().getTag());
		Iterator<PartSpec> it = partSpec.iterator();
		PartSpec part = null;
		while (it.hasNext()) {
			part = it.next();
			installedPart.setId(new InstalledPartId(productId,
					part.getId().getPartName()));

		}
		String partSnNumber = scannedIPUBarcode.substring(deviceFormat
				.getOffset() - 1, deviceFormat.getOffset()
				+ deviceFormat.getLength() - 1);

		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setActualTimestamp(new Timestamp(System
				.currentTimeMillis()));
		if (deviceFormat.getDeviceDataType() == DeviceDataType.FLOAT) {
			Measurement torque = new Measurement(productId, part
					.getId().getPartName(), 1);
			torque.setMeasurementValue(Double.valueOf(partSnNumber));
			torque.setMeasurementStatus(MeasurementStatus.OK);
			torque.setPartSerialNumber("-");
			measurementList.add(torque);
			installedPart.setMeasurements(measurementList);
			installedPart.setPartSerialNumber("-");

		} else {
			installedPart.setMeasurements(measurementList);
			installedPart.setPartSerialNumber(partSnNumber);
		}
		return installedPart;
	}
	
	
}
