package com.honda.galc.client.datacollection.strategy;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.service.property.PropertyService;

/**
 * SerialNumberTrim - Used to trim the MTOC and Check digit off the scanned barcode
 * @author Joseph Allen (VN030304)
 *
 */
public class SerialNumberTrim extends PartSerialNumberProcessor{
	private static String serialNumberTrimLength = "SERIAL_NUMBER_TRIM_LENGTH";
	
	public SerialNumberTrim(ClientContext context) {
		super(context);
	}
	
	/**
	 * This function is being overridden to trim off the MTOC
	 * and the check digit from the scanned barcode.
	 */
	@Override
	public synchronized boolean execute(PartSerialNumber partnumber) {
		Logger.getLogger().debug("SerialNumberTrim : Enter confirmPartSerialNumber");
		
		String prop = PropertyService.getProperty(context.getAppContext().getTerminalId(), serialNumberTrimLength);
		if(prop != null){
			partnumber.setPartSn(partnumber.getPartSn().substring(0, Integer.parseInt(prop)));
		}
		else{
			Logger.getLogger().error("Missing property: " + serialNumberTrimLength);
			getController().getFsm().error(new Message("Missing property: " + serialNumberTrimLength));
			return false;
		}
				
		try {
			Logger.getLogger().info("Process part:" + partnumber.getPartSn());
			confirmPartSerialNumber(partnumber);
			getController().getFsm().partSnOk(installedPart);
			Logger.getLogger().debug("SerialNumberTrim:: Exit confirmPartSerialNumber ok");
			
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
		Logger.getLogger().debug("SerialNumberTrim:: Exit confirmPartSerialNumber ng");
		return false;
	}

}
