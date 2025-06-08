/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import java.util.List;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.view.AlertPopUp;
import com.honda.galc.client.device.plc.omron.FinsMemoryManager;
import com.honda.galc.client.ui.MonitorAlertDialog;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.PlcMemoryMapItemDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Jan 11, 2012
 */
public class PlcDataReadyMonitorProcessor extends AbstractPlcCommunicationProcessor 
	implements IDataCollectionTaskProcessor<ProductId> {
	
	protected volatile AlertPopUp alertPopUp = AlertPopUp.notShown;
	protected volatile boolean active = false;
	
	public static final String BUTTON_TEXT = "Override";
	
	public PlcDataReadyMonitorProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
		AnnotationProcessor.process(this);
	}
	
	public void init() {
		setActive(true);
		alertPopUp = AlertPopUp.notShown;
	}

	/**
	 * Checks the equipment data ready Plc memory location and shows a pop-up to the 
	 * user if its not set.  This method is invoked when the rule is executed by the state machine.
	 */
	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().info("PlcDataReadyMonitorProcessor : Enter execute()");
		try {
			rule = getController().getState().getCurrentLotControlRule();
			if (!checkEquipmentDataReady()) {
				playNgSound();
				startEquipmentDataReadyMonitor();
				showPopUp();
			}
				
			getController().getFsm().partSnOk(updateInstalledPart(productId));
			setActive(false);
			Logger.getLogger().info("PlcDataReadyMonitorProcessor: Exit execute() ok");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex.getMessage());
		} finally {
			if (finsPlcDevice != null)
				finsPlcDevice.unregisterListener(this);
		}
		Logger.getLogger().info("PlcDataReadyMonitorProcessor: Exit execute() ng");
		return false;
	}

	/**
	 * displays the 'equipment not ready' alert pop-up
	 */
	private void showPopUp() {
		alertPopUp = AlertPopUp.shown;
		final MonitorAlertDialog monitorAlertDialog = new MonitorAlertDialog(context.getFrame(), getPartSpec().getPartDescription(), getPartSpec().getComment(), BUTTON_TEXT);
		monitorAlertDialog.start();
	}

	/**
	 * starts the equipment data ready monitor
	 */
	private void startEquipmentDataReadyMonitor() {
		Runnable monitorWorker = new Runnable() {
			public void run() {
				try {
					while (isActive() && !checkEquipmentDataReady()) {
						Thread.sleep(10);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		Thread worker = new Thread(monitorWorker);
		worker.start();
	}

	/**
	 * receives the message dialog overriden event and updates the part serial number
	 * with the 'overriden' message
	 * @param event
	 */
	@EventSubscriber(eventClass = Event.class)
	public void processMonitorAlertEvent(Event event){
		if (event.getEventType() == EventType.WARNING_OVERRIDDEN && event.getSource().equals(getPartSpec().getPartDescription())) {
			Logger.getLogger().info("Received Event :", event.getSource().getClass() + ": " + EventType.WARNING_OVERRIDDEN);
			alertPopUp = AlertPopUp.overridden;
			playDisconnectedSound();
		}
	}
	
	/**
	 * updates the installed part attributes
	 * 
	 * @param productId
	 */
	private InstalledPart updateInstalledPart(ProductId productId) {
		InstalledPart installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setProductId(productId.getProductId());
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setPartSerialNumber("Warning Pop-up "  + alertPopUp.toString());
		installedPart.setPartName(rule.getPartName().getPartName());
		installedPart.setPartId(rule.getParts().get(0).getId().getPartId());
		installedPart.setValidPartSerialNumber(true);
		installedPart.setPartIndex(getController().getState().getCurrentPartIndex());
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		return installedPart;
	}
	
	/**
	 * checks equipment data ready and generates an event if
	 * data ready 
	 * @return
	 */
	private boolean checkEquipmentDataReady() {
		// read equipment DataReady
		StringBuilder eqDataReady = FinsMemoryManager.getInstance().readBit(getEqDataReadyLoc(), getPlc());
		
		char charValue = StringUtil.toCharArray(eqDataReady)[0];
		if (charValue == 0x0) {
			return false;
		} else {
			if (alertPopUp.equals(AlertPopUp.shown)) {
				Event event = new Event(getPartSpec().getPartDescription(), this, EventType.PLC_READY);
				EventBus.publish(event);
				playOkSound();
			}
			return true;
		}
	}
	
	/**
	 * retrieves the equipment data ready plc memory location
	 * 
	 * @return
	 */
	private PlcMemoryMapItem getEqDataReadyLoc() {
		List<PlcMemoryMapItem> items = ServiceFactory.getDao(PlcMemoryMapItemDao.class).findAllByPrefix(getPartSpec().getId().getPartName());
		return items.get(0);
	}
	
	private void setActive(boolean active) {
		this.active = active;
	}

	private boolean isActive() {
		return active;
	}
}
