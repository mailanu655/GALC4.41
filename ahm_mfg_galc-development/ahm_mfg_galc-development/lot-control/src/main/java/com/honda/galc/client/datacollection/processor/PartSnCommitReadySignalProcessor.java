package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CommitReady;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.device.dataformat.PlcBoolean;
import com.honda.galc.entity.product.InstalledPart;

public class PartSnCommitReadySignalProcessor extends PartSerialNumberProcessor implements DeviceListener, IPartSerialNumberProcessor {
	protected Boolean isWaitingtoCommit = false;
	protected Boolean bypassMode = false;
	protected InstalledPart validInstalledPart;
	
	public PartSnCommitReadySignalProcessor(ClientContext context) {
		super(context);
	}

	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new PartSerialNumber()); 	 
		list.add(new CommitReady()); 		 
		list.add(new PlcBoolean());  		 
		return list;	
	}
	
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		Logger.getLogger().info("Unsolicited data received: " + deviceData.toString());
		if (!this.bypassMode && !this.isWaitingtoCommit && deviceData instanceof PartSerialNumber)
			this.execute((PartSerialNumber) deviceData);
		else if (!this.bypassMode && this.isWaitingtoCommit && deviceData instanceof CommitReady)
			this.processCommitSignal((CommitReady) deviceData);
		else if (deviceData instanceof PlcBoolean)
			this.processBypassSignal((PlcBoolean) deviceData);
		return null;
	}
	
	public synchronized boolean execute(PartSerialNumber partnumber) {
		if (!this.bypassMode && !this.isWaitingtoCommit) {
			try {
				Logger.getLogger().info("Processing part:" + partnumber.getPartSn());
				Logger.getLogger().debug("PartSnCommitReadySignalProcessor : Enter confirmPartSerialNumber");	
				confirmPartSerialNumber(partnumber);
				if (isMultiScanReceived(installedPart, partnumber)) return true;
				setScanCounter(0);
				this.isWaitingtoCommit = true;
				installedPart.setValidPartSerialNumber(false);
				validInstalledPart = installedPart;
				getController().getFsm().partSnOkButWait(installedPart);
				this.context.getCurrentViewManager().getMessagePanel()
						.setWarningMessageArea("Part verified OK. Waiting for load signal.");
				Logger.getLogger().debug("PartSnCommitReadySignalProcessor:: Exit confirmPartSerialNumber ok");
				return true;
			} catch (TaskException te) {
				Logger.getLogger().error(te.getMessage());
				installedPart.setValidPartSerialNumber(false);
				getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, te.getMessage());
			} catch (SystemException se) {
				Logger.getLogger().error(se, se.getMessage());
				installedPart.setValidPartSerialNumber(false);
				getController().getFsm().error(new Message(PART_SN_MESSAGE_ID, se.getMessage()));
			} catch (Exception e) {
				Logger.getLogger().error(e, "ThreadID = " + Thread.currentThread().getName()
						+ " :: execute() : Exception : " + e.toString());
				getController().getFsm().error(new Message("MSG01", e.getMessage()));
			} catch (Throwable t) {
				Logger.getLogger().error(t, "ThreadID = " + Thread.currentThread().getName()
						+ " :: execute() : Exception : " + t.toString());
				getController().getFsm().error(new Message("MSG01", t.getMessage()));
			}
			setScanCounter(0);
			this.isWaitingtoCommit = false;
			Logger.getLogger().debug("PartSnCommitReadySignalProcessor:: Exit confirmPartSerialNumber ng");
		}
		return false;
	}

	public synchronized boolean execute(CommitReady commitSignal) {
		if (!this.bypassMode && this.isWaitingtoCommit) {
			this.processCommitSignal(commitSignal);
			return true;
		}
		return false;
	}
	
	public synchronized boolean execute(PlcBoolean plcSignal) {
		this.processBypassSignal(plcSignal);
		return true;
	}
	
	private void processCommitSignal(CommitReady deviceData) {
		Logger.getLogger().info("Processing commit signal: " + deviceData.getValue());
		try {
			this.isWaitingtoCommit = false;
			validInstalledPart.setValidPartSerialNumber(true);
			getController().getFsm().partSnOk(validInstalledPart);
		} catch (Exception e) {
			Logger.getLogger().error(e.getMessage());
		}
	}
	
	private void processBypassSignal(PlcBoolean deviceData) {
		Logger.getLogger().info("Processing bypass/auto signal: " + deviceData.getValue());
		try {
			this.isWaitingtoCommit = false;
			installedPart.setValidPartSerialNumber(false);
			if (deviceData.getValue() == true) { // Auto
				this.bypassMode = false;
				getController().getFsm().receivedAuto(installedPart);
			} else { // Manual
				this.bypassMode = true;
				getController().getFsm().receivedBypass(installedPart);
				this.context.getCurrentViewManager().getMessagePanel()
						.setWarningMessageArea("Bypass Mode. No PLC or Scan value allowed.");
			}
		} catch (Exception e) {
			Logger.getLogger().error(e.getMessage());
		}
	}
}