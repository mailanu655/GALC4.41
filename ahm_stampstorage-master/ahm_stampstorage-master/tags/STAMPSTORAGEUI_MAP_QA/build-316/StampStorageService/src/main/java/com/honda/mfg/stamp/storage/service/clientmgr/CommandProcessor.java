// CommandProcessor.java

package com.honda.mfg.stamp.storage.service.clientmgr;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.messages.*;
import com.honda.mfg.stamp.conveyor.service.StorageStateUpdateService;
import com.honda.mfg.stamp.storage.service.utils.MessageSinkInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatches client request to StorageStateUpdateService
 */

/**
 * This class processes messages and calls appropriate StorageStateUpdateService operations
 * depending on message type.  Called by {@link com.honda.mfg.stamp.storage.service.utils.MessageTransport#process()}
 * on receiving a complete message
 * @author VCC44349
 *
 */
public final class CommandProcessor implements MessageSinkInterface, CommandProcessorInterface
{
    private static Logger log = LoggerFactory.getLogger(CommandProcessor.class);
	private static CommandProcessor instance;

	private StorageStateUpdateService carrierManager;
	/**
	 * use {@link #getInstance()} to obtain a new instance
	 */
	private CommandProcessor()
	{
	}

	public void put(Object objectSource, Message msg) throws Exception
	{
		if(msg instanceof JsonServiceWrapperMessage)  {
			JsonServiceWrapperMessage jsonWrapper = (JsonServiceWrapperMessage)msg;
			ServiceRequestMessage m = jsonWrapper.getContent();

			try {
				processCommand(m, objectSource, carrierManager);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param msg : message data
	 * @param objSrc : reference to client connection socket interface
	 * @param carrierManager : ref to {@link StorageStateUpdateService} to perform operation
	 */
	public static void processCommand(ServiceRequestMessage msg, Object objSrc, StorageStateUpdateService carrierManager)
	{
        //int ret = 1;
        
        if(msg == null) {return;}
        CarrierUpdateOperations cOp = msg.getTargetOp();
        log.info("CommandProcessor.processCommand(): CommandId: " + cOp.toString());
		//ClientRequestData clientReqData_ = (ClientRequestData)objectSource;
		
		if(cOp == CarrierUpdateOperations.SAVE)  {
			CarrierUpdateRequestMessage req = (CarrierUpdateRequestMessage)msg;
			carrierManager.saveCarrier(req.getCarrier());
		}
		if(cOp == CarrierUpdateOperations.STORE_REWORK)  {
			CarrierUpdateRequestMessage req = (CarrierUpdateRequestMessage)msg;
			carrierManager.storeReworkCarrier(req.getCarrier());
		}
		else if(cOp == CarrierUpdateOperations.RECALC_DEST)  {
			CarrierUpdateRequestMessage req = (CarrierUpdateRequestMessage)msg;
			carrierManager.recalculateCarrierDestination(req.getCarrier());
		}
		else if(cOp == CarrierUpdateOperations.REMOVE_FROM_ROW)  {
			LaneUpdateRequestMessage req = (LaneUpdateRequestMessage)msg;
			if(req.getCarrierNumber() != null)  {
				carrierManager.removeCarrierFromRow(req.getCarrierNumber());
			}
		}
		else if(cOp == CarrierUpdateOperations.ADD_TO_ROW)  {
			LaneUpdateRequestMessage req = (LaneUpdateRequestMessage)msg;
			if(req.getCarrierNumber() != null)  {
				carrierManager.addCarrierToRow(req.getCarrierNumber(),
						req.getPosition(), req.getLaneStop());
			}
		}		
		else if(cOp == CarrierUpdateOperations.ROW_RESEQUENCE)  {
			UpdateLaneCarrierListRequestMessage req = (UpdateLaneCarrierListRequestMessage)msg;
			List<Integer> carrierNumberList = req.getCarrierNumberList();
			if(carrierNumberList != null && !carrierNumberList.isEmpty())  {
				ArrayList<Carrier> carriers = new ArrayList<Carrier>();
				Stop laneStop = req.getLaneStop();
				for (Integer carrierNum : carrierNumberList) {
					Carrier thisCarrier = new Carrier();
					thisCarrier.setCarrierNumber(carrierNum);
                    //current location was being set in the LanesController, presumably to fix carriers that are
                    //messed up, moving that to here.
					thisCarrier.setCurrentLocation(laneStop);
					thisCarrier.setDestination(laneStop);
					carriers.add(thisCarrier);
				}
				carrierManager.saveCarriersInToRow(carriers, laneStop);
			}
		}		
		else if(cOp == CarrierUpdateOperations.RELEASE_EMPTIES_FROM_ROW)  {
			EmptyCarrierReleaseRequest req = (EmptyCarrierReleaseRequest)msg;
			StorageArea area = req.getWhichArea();
			Boolean releaseMgr = req.getReleaseManager();
			String src = req.getSource();
            //LOG.info(" command processor: "+area.name());
			carrierManager.releaseEmptyCarriersFromRows(area, releaseMgr, src);
		}
		else if(cOp == CarrierUpdateOperations.RELEASE_CARRIERS)  {
			ReleaseCarriersRequestMessage req = (ReleaseCarriersRequestMessage)msg;

	        //Stop laneStop = Stop.findStop(req.getLaneStopId());
	       // Stop destStop = Stop.findStop(req.getDestinationStopId());
            Stop laneStop = req.getLaneStop();
            Stop destStop = req.getDestination();
			carrierManager.releaseCarriers(laneStop, req.getReleaseCount(), destStop, req.getSource());
		}
		else if(cOp == CarrierUpdateOperations.GROUP_HOLD)  {
			BulkStatusUpdateRequestMessage req = (BulkStatusUpdateRequestMessage)msg;
			ArrayList<Carrier> carriers = new ArrayList<Carrier>();
	        List<Integer> carrierNumberList = req.getCarrierNumberList();
	        for(Integer carrierNumber : carrierNumberList)  {
	        	Carrier thisCarrier = new Carrier();
	        			//TODO:Group Hold
	        	thisCarrier.setCarrierNumber(carrierNumber);
	        	thisCarrier.setSource(req.getUser());
	        	carriers.add(thisCarrier);
	        }
			carrierManager.sendBulkCarrierStatusUpdate(carriers, req.getNewStatus(), req.getUser());
		}
		else if(cOp == CarrierUpdateOperations.REORDER_CARRIERS_IN_ROW)  {
			LaneUpdateRequestMessage req = (LaneUpdateRequestMessage)msg;
			if(req.getLaneStopId() != null)  {
				carrierManager.reorderCarriersInRow(req.getLaneStopId());
			}
		}else if(cOp == CarrierUpdateOperations.UPDATE_ROW)  {
			LaneUpdateRequestMessage req = (LaneUpdateRequestMessage)msg;
			if(req.getLaneStopId() != null)  {
				carrierManager.updateStorageRow(req.getLaneStopId());
			}
		}else if(cOp == CarrierUpdateOperations.REFRESH_STORAGE_STATE)  {
            //StorageStateRefreshRequestMessage req = (StorageStateRefreshRequestMessage)msg;
            carrierManager.reloadStorageState();
		}else if(cOp == CarrierUpdateOperations.RESET_STORAGE_STATE)  {
            //StorageStateRefreshRequestMessage req = (StorageStateRefreshRequestMessage)msg;
            carrierManager.resetStorageState();
		}
        else if(cOp == CarrierUpdateOperations.CLEAR_ALARM) {
            ClearAlarmRequestMessage req = (ClearAlarmRequestMessage)msg;
            carrierManager.clearAlarm(Integer.parseInt(req.getAlarmNumber()));
        }

	}

	/**
	 * @return {@link MessageSinkInterface}
	 */
	public static MessageSinkInterface getInstance()
	{
		return instance;
	}

	/**
	 * @return {@link CommandProcessorInterface}
	 */
	public static CommandProcessorInterface getCommandProcessorInstance()
	{
		return instance;
	}

//	@Override
//    public Object getMesConnectionStatus()
//	{
//		Object[] argsInfo = new Object[1];
//		argsInfo[0] = new Boolean(true);  //get device ping or healthy/unhealthy status
//		Command cmd = new Command(CommandInterface.__DevicePingStatus__,argsInfo);
//		return cmd;
//
//	}
	
//    @Override
//	public Object getDevicePong()
//	{
//		Object[] args = new Object[2];
//		args[0] = "DevicePong";
//		return new Command(CommandInterface.__Pong__,args);
//	}

	//----------------------------------------------------------------------------------//

	@Override
	public StorageStateUpdateService getCarrierManager() {
		return carrierManager;
	}

	@Override
	public void setCarrierManager(StorageStateUpdateService newManager) {
		carrierManager = newManager;
	}
	static
	{
		instance = new CommandProcessor();
	}
	
}
