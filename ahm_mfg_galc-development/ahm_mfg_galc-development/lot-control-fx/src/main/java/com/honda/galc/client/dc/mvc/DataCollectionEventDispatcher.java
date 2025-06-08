package com.honda.galc.client.dc.mvc;

import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.client.dc.action.IDataCollectionAction;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.event.TorqueDeviceErrorEvent;
import com.honda.galc.client.dc.event.TorqueEvent;
import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.enumtype.OperationEfficiencyStatus;
import com.honda.galc.service.property.PropertyService;

import javafx.application.Platform;

/**
 * @author Subu Kathiresan
 * @date Sep 25, 2014
 */
public class DataCollectionEventDispatcher {

	private Logger logger;
	private DataCollectionController controller;
	private Timer delayProcessCompleteTimer;
	
	DataCollectionEventDispatcher(DataCollectionController controller) {
		this.controller = controller;
		EventBusUtil.register(this);
	}
	
	@Subscribe
	public void received(DataCollectionResultEvent event) {
		try {
			controller.getModel().setOpEfficiencyStatus(OperationEfficiencyStatus.UNIT_INCOMPLETE);
			switch(event.getType()) {
	
			case VALID_PART_SCAN_RECEIVED:
			case VALID_MEASUREMENT_RECEIVED:
				controller.getAudioManager().playOkSound();
				break;
			case INVALID_PART_SCAN_RECEIVED:
			case INVALID_MEASUREMENT_RECEIVED:
				controller.getAudioManager().playNGSound();
				break;
			case REJECT_PART_SCAN_RECEIVED:
			case REJECT_MEASUREMENT_RECEIVED:
				controller.performOpEfficiencyHistReject(event.getOperation());
				rejectInput(event);
				break;
				
			case SKIP_PART_SCAN_RECEIVED:
			case SKIP_MEASUREMENT_RECEIVED:
				controller.selectCurrentOperation();
				break;
				
			case DC_REJECTED_FOR_PART:
				controller.performOpEfficiencyHistReject(event.getOperation());
				rejectProcess(event);
				break;
			case DC_COMPLETED_FOR_PART:
				completeProcess(event);
				break;
			default:
				break;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to process " + event + " for operation " + event.getOperation().getId().getOperationName());
		}
	}

	private void rejectProcess(final DataCollectionResultEvent dcrEvent) {
		if (DataCollectionModel.hasMeasurements(dcrEvent.getOperation())) {
			delayRejectProcess(dcrEvent);
		} else {
			controller.processOperationReject(dcrEvent.getOperation());
		}
	}

	private void delayRejectProcess(final DataCollectionResultEvent dcrEvent) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		    public void run() {
		         Platform.runLater(new Runnable() {
		            public void run() {
		                controller.processOperationReject(dcrEvent.getOperation());
		            }
		        });
		    }
		}, getDataCollectionPropertyBean().lastMeasurementDisplayTime());
	}
	
	private void completeProcess(final DataCollectionResultEvent dcrEvent) {
		
		// TODO should always have a delay to facilitate "REJECT" functionality
		if (DataCollectionModel.hasMeasurements(dcrEvent.getOperation())) {
			delayProcessComplete(dcrEvent);
		} else {
			 controller.processOperationComplete(dcrEvent.getOperation());
		}
	}

	private void delayProcessComplete(final DataCollectionResultEvent dcrEvent) {
		delayProcessCompleteTimer = new Timer();
		delayProcessCompleteTimer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						controller.processOperationComplete(dcrEvent.getOperation());
					}
				});
			}
		}, getDataCollectionPropertyBean().lastMeasurementDisplayTime());
		EventBusUtil.publish(new UnitNavigatorEvent(UnitNavigatorEventType.PREPARE_FOR_MOVE));
	}

	@SuppressWarnings("unchecked")
	@Subscribe
	public void received(DataCollectionEvent event) {
		try {
			MCOperationRevision operation = null;
			getLogger().info("DataCollection Event received ::"+event.getType());
			if(event.getOperation() == null){
				operation = controller.getModel().getOperationsMap().get(controller.getModel().getCurrentOperationName());
				event.setOperation(operation);
			}else{
				if(StringUtils.equals(event.getOperation().getId().getOperationName(),controller.getModel().getCurrentOperationName()))
				{
					operation = event.getOperation();
				}else{
					getLogger().info("Ignoring the event as the current operartion is : "+controller.getModel().getCurrentOperationName() +" But the event is for : "+event.getOperation().getId().getOperationName());
					return;
				}
			}
			getLogger().info(" Processing Data collection event for Operation ::"+operation.getId().getOperationName());
			IDataCollectionAction action = getActionHandler(event);
			action.setOperation(operation);
			action.setModel(controller.getModel());
			action.setView(controller.getView().getCurrentDataCollectionPanel());
			if (CheckPointsRegistry.getInstance().isCheckPointConfigured(action)) {
				getLogger().info("At CheckPoint: " + action.getCheckPointName());
				if (!action.executeCheckers(event.getInputData())) {
						return;
				}
				action.unregisterCheckPoint();
			}
			action.perform(controller.getModel(), event); 
			
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to process " + event + " for operation " + event.getOperation().getId().getOperationName());
		}
	}

	@Subscribe
	public void received(TorqueEvent torqueEvent) {
		try {
			if (controller.getView().getCurrentDataCollectionPanel() == null) {
				return;
			}
			
			if (controller.getView().getCurrentDataCollectionPanel() instanceof ITorqueCollectionView) {
				((ITorqueCollectionView) controller.getView().getCurrentDataCollectionPanel()).setTorque(torqueEvent.getTorque());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to process event " + torqueEvent);
		}
	}
	
	public void rejectInput(DataCollectionResultEvent event) {
		getLogger().check("Reject input received. Canceling moving to next operation");
		if (delayProcessCompleteTimer != null) {
			delayProcessCompleteTimer.cancel();
		}
		EventBusUtil.publish(new UnitNavigatorEvent(UnitNavigatorEventType.CANCEL_MOVE));
	}
	
	@SuppressWarnings("unchecked")
	protected IDataCollectionAction<InputData> getActionHandler(DataCollectionEvent event) {
		try {
			Class<?>[] parameterTypes = {};
			Object[] parameters = {};
			Constructor<?> constructor = event.getType().getActionClass().getConstructor(parameterTypes);

			/*IDataCollectionAction action = (IDataCollectionAction) constructor.newInstance(parameters);
			action.setPersistenceManager(controller.getPersistenceManager());*/
			IDataCollectionAction<InputData> action = (IDataCollectionAction<InputData>) constructor.newInstance(parameters);

			

			action.setPersistenceManager(controller.getPersistenceManager());
			action.setView(controller.getView().getCurrentDataCollectionPanel());
			return action;
		
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not locate action handler for event " + event);
			return null;
		}
	}
	
	private DataCollectionPropertyBean getDataCollectionPropertyBean() {
		return PropertyService.getPropertyBean(DataCollectionPropertyBean.class, controller.getProcessPointId());
	}
	
	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}
	
	@Subscribe
	public void received(TorqueDeviceErrorEvent torqueDeviceErrorEvent) {
		if (torqueDeviceErrorEvent.getStatusInfo().getCommandError() != null) {
			controller.getAudioManager().playNGSound();
			EventBusUtil.publish(new StatusMessageEvent(torqueDeviceErrorEvent.getStatusInfo().getMessage(), StatusMessageEventType.ERROR));
		}
	}
}
