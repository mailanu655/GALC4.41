package com.honda.mfg.stamp.storage.service;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.messages.*;
import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class "marshals" the update request into an update message and publishes on event bus; the subscriber is
 * ServiceSendMessageProcessor
 * User: vcc44349
 * Date: 01/09/2013
 */
public class CarrierManagementServiceProxyImpl implements CarrierManagementServiceProxy {
    private static final Logger LOG = LoggerFactory.getLogger(CarrierManagementServiceProxyImpl.class);

    @Override
    public void saveCarrier(Carrier carrier) {
        LOG.info("CarrierManagementServiceProxyImpl:saveCarrier() Saving carrier:  " + carrier);
        Carrier c = new Carrier();
      //TODO:SaveCarrier in UI need maint bits need to save maint bits
        c.setCarrierNumber(carrier.getCarrierNumber());
        c.setDestination(carrier.getDestination());
        c.setDie(carrier.getDie());
        c.setQuantity(carrier.getQuantity());
        c.setCarrierStatus(carrier.getCarrierStatus());
        c.setCurrentLocation(carrier.getCurrentLocation());
        c.setBuffer(carrier.getBuffer());
        c.setReprocess(carrier.getReprocess());
        c.setSource(carrier.getSource());
        c.setProductionRunNo(carrier.getProductionRunNo());
        c.setStampingProductionRunTimestamp(carrier.getStampingProductionRunTimestamp());
        c.setPress(carrier.getPress());
        c.setMaintRequired(zeroForNull(carrier.getMaintRequired()));
        c.setMaintenanceBits(carrier.getMaintenanceBits());
        ServiceRequestMessage carrierUpdateReqMsg = new CarrierUpdateRequestMessage(c,CarrierUpdateOperations.SAVE);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(carrierUpdateReqMsg, JsonServiceMessageTypes.CarrierUpdateReqeustMessage);
        EventBus.publish(jsonWrapper);

    }

private Integer zeroForNull(Integer i){
	return (i==null)?0:i;
}
    @Override
    public void sendBulkCarrierStatusUpdate(List<Carrier> carriers, CarrierStatus status, String user) {

        List<Integer> numbers = new ArrayList<Integer>();
        for (Carrier carrier : carriers) {
            numbers.add(carrier.getCarrierNumber());
        }
        ServiceRequestMessage bulkReq = new BulkStatusUpdateRequestMessage(numbers, status, user, CarrierUpdateOperations.GROUP_HOLD);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(bulkReq, JsonServiceMessageTypes.BulkStatusUpdateRequest);
        EventBus.publish(jsonWrapper);
    }
    @Override
    public void recalculateCarrierDestination(Carrier carrier) {
        //storage.recalculateCarrierDestination(carrier);
        LOG.info("CarrierManagementServiceProxyImpl:recalculateCarrierDestination " + carrier);
        Carrier c = new Carrier();
      //TODO:Recalc Destination
        c.setCarrierNumber(carrier.getCarrierNumber());
        c.setDestination(carrier.getDestination());
        c.setDie(carrier.getDie());
        c.setQuantity(carrier.getQuantity());
        c.setCarrierStatus(carrier.getCarrierStatus());
        c.setCurrentLocation(carrier.getCurrentLocation());
        c.setBuffer(carrier.getBuffer());
        c.setReprocess(carrier.getReprocess());
        c.setSource(carrier.getSource());
        c.setProductionRunNo(carrier.getProductionRunNo());
        c.setStampingProductionRunTimestamp(carrier.getStampingProductionRunTimestamp());
        c.setPress(carrier.getPress());
        
        ServiceRequestMessage carrierUpdateReqMsg = new CarrierUpdateRequestMessage(c,CarrierUpdateOperations.RECALC_DEST);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(carrierUpdateReqMsg, JsonServiceMessageTypes.CarrierUpdateReqeustMessage);
        EventBus.publish(jsonWrapper);

    }

    public void removeCarrierFromRow(Integer carrierNum) {
        LOG.info("CarrierManagementServiceProxyImpl:removeCarrierFromRow() Removing carrier:  " + carrierNum);
        Carrier car = new Carrier();
        car.setCarrierNumber(carrierNum);
        ServiceRequestMessage laneUpdateReqMsg = new LaneUpdateRequestMessage(carrierNum, CarrierUpdateOperations.REMOVE_FROM_ROW);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(laneUpdateReqMsg, JsonServiceMessageTypes.LaneUpdateRequestMessage);
        EventBus.publish(jsonWrapper);
    }
    
    @Override
	public void addCarrierToRow(Integer carrierNumber, Integer position, Stop laneStop) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("CarrierManagementServiceProxyImpl:addCarrierToRow() Adding carrier:  ");
    	sb.append(carrierNumber).append(", laneStop: ");
    	if(laneStop != null) {sb.append(laneStop.getName()).append(", position: ");}
    	if(position != null) {sb.append(position);}
        LOG.info(sb.toString());
        Long laneStopId = null;
        if(laneStop != null)  {laneStopId = laneStop.getId();}
        ServiceRequestMessage laneUpdateReqMsg = new LaneUpdateRequestMessage(carrierNumber, position, laneStopId, CarrierUpdateOperations.ADD_TO_ROW);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(laneUpdateReqMsg, JsonServiceMessageTypes.LaneUpdateRequestMessage);
        
         EventBus.publish(jsonWrapper);
    }

    @Override
	public void saveCarriersInToRow(List<Integer> carrierNumberList, Stop laneStop) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("CarrierManagementServiceProxyImpl:saveCarriersInToRow(): list of carriers:  ");
    	sb.append(carrierNumberList).append(", laneStop: ");
    	if(laneStop != null) {sb.append(laneStop.getName());}
        LOG.info(sb.toString());
        Long laneStopId = null;
        if(laneStop != null)  {laneStopId = laneStop.getId();}
        ServiceRequestMessage laneUpdateReqMsg = new UpdateLaneCarrierListRequestMessage(carrierNumberList, laneStopId, CarrierUpdateOperations.ROW_RESEQUENCE);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(laneUpdateReqMsg, JsonServiceMessageTypes.UpdateLaneCarrierListMessage);
        
        EventBus.publish(jsonWrapper);
    }
    
    @Override
	public void releaseEmptyCarriersFromRows(StorageArea area, boolean releaseMgr, String source) {
        LOG.info("CarrierManagementServiceProxyImpl:releaseEmptyCarriersFromRows storage area:  " + area);
        EmptyCarrierReleaseRequest releaseEmptyFromRowsReq = new EmptyCarrierReleaseRequest(area, releaseMgr, CarrierUpdateOperations.RELEASE_EMPTIES_FROM_ROW);
        releaseEmptyFromRowsReq.setSource(source);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(releaseEmptyFromRowsReq, JsonServiceMessageTypes.ReleaseEmptiesFromRowRequest);
        EventBus.publish(jsonWrapper);
    }
    
    @Override
	public void releaseCarriers(StorageRow lane, Integer releaseCount, Stop destination, String source) {

		Long laneStopId = 0L;
		Long destStopId = 0L;
		if (lane != null && lane.getStop() != null)  {
			laneStopId = lane.getStop().getId(); }
		if (destination != null)  {
			destStopId = destination.getId();}
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("CarrierManagementServiceProxyImpl:releaseCarriers: laneStop: ");
    	sb.append(laneStopId).append(", release count: ");
    	sb.append(releaseCount).append(", destination stop: ");
    	sb.append(destStopId).append(", source: ").append(source);
        LOG.info(sb.toString());
        
		ReleaseCarriersRequestMessage releaseCarriersReq = new ReleaseCarriersRequestMessage(
				laneStopId, releaseCount, destStopId,
				CarrierUpdateOperations.RELEASE_CARRIERS);
		releaseCarriersReq.setSource(source);
		JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(
				releaseCarriersReq,
				JsonServiceMessageTypes.ReleaseCarriersRequest);
		EventBus.publish(jsonWrapper);
    }

    /* (non-Javadoc)
     * unused method, not implementing
     * @deprecated
     */
    @Override
	public void storeReworkCarrier(Carrier carrier) {
        //2013-02-01:VB:TBD
        //storage.store(carrier);
        LOG.info("CarrierManagementServiceProxyImpl:saveCarrier() Saving carrier:  " + carrier);
        Carrier c = new Carrier();
      //TODO:Store Rework Carrier
        c.setCarrierNumber(carrier.getCarrierNumber());
        c.setDestination(carrier.getDestination());
        c.setDie(carrier.getDie());
        c.setQuantity(carrier.getQuantity());
        c.setCarrierStatus(carrier.getCarrierStatus());
        c.setCurrentLocation(carrier.getCurrentLocation());
        c.setBuffer(carrier.getBuffer());
        c.setReprocess(carrier.getReprocess());
        c.setSource(carrier.getSource());
        c.setProductionRunNo(carrier.getProductionRunNo());
        c.setStampingProductionRunTimestamp(carrier.getStampingProductionRunTimestamp());
        c.setPress(carrier.getPress());
        
        ServiceRequestMessage carrierUpdateReqMsg = new CarrierUpdateRequestMessage(c,CarrierUpdateOperations.STORE_REWORK);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(carrierUpdateReqMsg, JsonServiceMessageTypes.CarrierUpdateReqeustMessage);
        EventBus.publish(jsonWrapper);

    }

    @Override
    public void updateRow(StorageRow row) {
        StringBuilder sb = new StringBuilder();
    	sb.append("CarrierManagementServiceProxyImpl:update StorageStateRow :  ");
    	if(row != null)  {
    		sb.append(row.getId());
    	}
        LOG.info(sb.toString());
        ServiceRequestMessage laneUpdateReqMsg = new LaneUpdateRequestMessage(0,0, row.getStop().getId(), CarrierUpdateOperations.UPDATE_ROW);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(laneUpdateReqMsg, JsonServiceMessageTypes.LaneUpdateRequestMessage);

        EventBus.publish(jsonWrapper);
    }

    @Override
    public void refreshStorageState() {
        StringBuilder sb = new StringBuilder();
    	sb.append("CarrierManagementServiceProxyImpl:refresh StorageState :  ");

        LOG.info(sb.toString());
        ServiceRequestMessage storageStateRefreshReqMsg = new StorageStateRefreshRequestMessage(CarrierUpdateOperations.REFRESH_STORAGE_STATE);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(storageStateRefreshReqMsg, JsonServiceMessageTypes.StorageStateRefreshRequestMessage);
        EventBus.publish(jsonWrapper);
    }

     @Override
    public void resetStorageState() {
        StringBuilder sb = new StringBuilder();
    	sb.append("CarrierManagementServiceProxyImpl:reset StorageState :  ");

        LOG.info(sb.toString());
        ServiceRequestMessage storageStateResetReqMsg = new StorageStateResetRequestMessage(CarrierUpdateOperations.RESET_STORAGE_STATE);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(storageStateResetReqMsg, JsonServiceMessageTypes.StorageStateResetRequestMessage);
        EventBus.publish(jsonWrapper);
    }

    @Override
	public void reorderCarriersInRow(Long stopId)  {
    	StringBuilder sb = new StringBuilder();
    	sb.append("CarrierManagementServiceProxyImpl:reorderCarriersInRow Reorder carriers laneStop:  ");
    	if(stopId != null)  {
    		sb.append(stopId);
    	}
        LOG.info(sb.toString());
        ServiceRequestMessage laneUpdateReqMsg = new LaneUpdateRequestMessage(0,0, stopId, CarrierUpdateOperations.REORDER_CARRIERS_IN_ROW);
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(laneUpdateReqMsg, JsonServiceMessageTypes.LaneUpdateRequestMessage);
        
        EventBus.publish(jsonWrapper);
    }

    @Override
    public void clearAlarm(Integer alarmNumber){
        StringBuilder sb = new StringBuilder();
    	sb.append("CarrierManagementServiceProxyImpl:clear Alarm :  ");

        LOG.info(sb.toString());
        ServiceRequestMessage clearAlarmRequestMessage = new ClearAlarmRequestMessage(CarrierUpdateOperations.CLEAR_ALARM,"", alarmNumber.toString());
        JsonServiceWrapperMessage jsonWrapper = new JsonServiceWrapperMessage(clearAlarmRequestMessage, JsonServiceMessageTypes.ClearAlarmRequestMessage);
        EventBus.publish(jsonWrapper);
    }

}
