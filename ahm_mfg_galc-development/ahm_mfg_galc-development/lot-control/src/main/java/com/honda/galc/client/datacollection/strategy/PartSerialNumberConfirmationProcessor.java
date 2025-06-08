package com.honda.galc.client.datacollection.strategy;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.InstalledPart;

/**
 * 
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartSerialNumberConfirmationProcessor</code> is a strategy that allows user to validate installed SN by comparing new SN scan(s) with SN that is already stored in DB.
 * </p>
 * <h4>Usage and Example</h4> <h4>Configuration : <br />
 * Lot Control Rule - Scan : PART , Strategy : SN Confirmation <br />
 * Part Spec - Max Attempts : N - number of scans, optional, default : 1</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created May 3, 2017
 */
public class PartSerialNumberConfirmationProcessor extends PartSerialNumberProcessor {

	private int scanCounter;
	private InstalledPart confirmationPart;

	public PartSerialNumberConfirmationProcessor(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized boolean execute(PartSerialNumber partNumber) {
		Logger.getLogger().debug("PartSerialNumberConfirmationProcessor : Enter execute()");
		try {
			Logger.getLogger().info("Process part:" + partNumber.getPartSn());
			confirmPartSerialNumber(partNumber);
			if (getConfirmationPart() == null || !getController().getState().getProductId().equals(getConfirmationPart().getProductId())) {
				resetPart();
				confirmPart();
			}
			confirmScan(partNumber);
			Logger.getLogger().debug("PartSerialNumberConfirmationProcessor:: Exit execute() OK");
			return true;
		} catch (TaskException te) {
			resetPart();
			Logger.getLogger().error(te.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, te.getMessage());
		} catch (SystemException se) {
			resetPart();
			Logger.getLogger().error(se, se.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().error(new Message(PART_SN_MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			resetPart();
			Logger.getLogger().error(e, "ThreadID = " + Thread.currentThread().getName() + " :: execute() : Exception : " + e.toString());
			getController().getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t) {
			resetPart();
			Logger.getLogger().error(t, "ThreadID = " + Thread.currentThread().getName() + " :: execute() : Exception : " + t.toString());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		}
		Logger.getLogger().debug("PartSerialNumberConfirmationProcessor:: Exit execute() NG");
		return false;
	}

	protected void resetPart() {
		setConfirmationPart(null);
		setScanCounter(0);
	}

	protected void confirmPart() {
		String productId = getController().getState().getProductId();
		String partName = getController().getState().getCurrentLotControlRule().getPartNameString();
		InstalledPartDao ipDao = getDao(InstalledPartDao.class);
		InstalledPart part = ipDao.findById(productId, partName);
		if (part == null) {
			handleException("Part to be confirmed " + partName + " is not installed");
		}
		setConfirmationPart(part);
	}

	protected void confirmScan(PartSerialNumber partNumber) {
		int maxCount = getController().getState().getCurrentPartScanCount();
		if (maxCount < 1) {
			maxCount = 1;
		}
		setScanCounter(getScanCounter() + 1);
		if (!partNumber.getPartSn().equals(getConfirmationPart().getPartSerialNumber())) {
			String msg = "Scanned SN : " + partNumber + " does not match Installed Part SN : " + getConfirmationPart().getPartSerialNumber();
			handleException(msg);
		} else {
			if (getScanCounter() < maxCount) {
				getController().getFsm().receivedPartSn(installedPart);
			} else {
				getController().getFsm().partSnOk(installedPart);
				resetPart();
			}
		}
	}

	// === get/set === //
	protected InstalledPart getConfirmationPart() {
		return confirmationPart;
	}

	protected void setConfirmationPart(InstalledPart confirmationPart) {
		this.confirmationPart = confirmationPart;
	}

	protected int getScanCounter() {
		return scanCounter;
	}

	protected void setScanCounter(int scanCounter) {
		this.scanCounter = scanCounter;
	}
}
