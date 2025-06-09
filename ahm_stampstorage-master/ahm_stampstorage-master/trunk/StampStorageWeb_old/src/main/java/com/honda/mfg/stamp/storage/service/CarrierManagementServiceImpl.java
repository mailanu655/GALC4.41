package com.honda.mfg.stamp.storage.service;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vcc30690
 * Date: 5/25/11
 */
public class CarrierManagementServiceImpl implements CarrierManagementService {
    private static final Logger LOG = LoggerFactory.getLogger(CarrierManagementServiceImpl.class);

    public CarrierManagementServiceImpl() {
    	super();
        AnnotationProcessor.process(this);
        setConnected(true);
    }

    @Override
       public Integer getPositionInLane(Long currentLocation, Integer carrierNumber) {

           int position = 0;
           List<StorageRow> lanes = StorageRow.findAllStorageRows();

           for (StorageRow lane : lanes) {
               if (lane.getStop().getId().equals(currentLocation)) {
                   List<CarrierMes> carrierMesList = CarrierMes.getAllCarriersInLane(lane.getStop().getId());
                   if (carrierMesList != null) {
                       for (int i = 0; i < carrierMesList.size(); i++) {
                           if (carrierNumber.equals(carrierMesList.get(i).getCarrierNumber())) {
                               position = i + 1;
                               break;
                            }
                       }
                    }
                }
           }
           return position;
      }

    public List<Stop> getManualOrderCarrierDeliveryStops() {
        List<Stop> stops = new ArrayList<Stop>();
        stops.add(Stop.findStop(500L));
        stops.add(Stop.findStop(800L));
        stops.add(Stop.findStop(10100L));

        stops.add(Stop.findStop(704L));
        stops.add(Stop.findStop(5200L));

        stops.add(Stop.findStop(13400L));

        stops.add(Stop.findStop(3003L));
        stops.add(Stop.findStop(3004L));
        stops.add(Stop.findStop(3018L));

        return stops;
    }

    public List<Stop> getValidDestinationStops(Stop stop) {
        return ValidDestination.findValidDestinationsForGivenStop(stop);
    }

    public void reloadStorageState() {
        //2013-02-01:VB:TBD: storage.reloadStorageState();
    }

    public Carrier getCarrier(CarrierMes carrierMes) {
        Carrier carrier = new Carrier();
        carrier.setId(carrierMes.getId());
        carrier.setCarrierNumber(carrierMes.getCarrierNumber());
        carrier.setQuantity(carrierMes.getQuantity());
        carrier.setDie(Die.findDie(Long.valueOf(carrierMes.getDieNumber())));
        carrier.setCurrentLocation(Stop.findStop(carrierMes.getCurrentLocation()));
        carrier.setDestination(Stop.findStop(carrierMes.getDestination()));
        carrier.setPress(Press.findByType(carrierMes.getOriginationLocation()));
        carrier.setCarrierStatus(CarrierStatus.findByType(carrierMes.getStatus()));
        carrier.setProductionRunNo(carrierMes.getProductionRunNumber());
        carrier.setStampingProductionRunTimestamp(carrierMes.getProductionRunDate());
        carrier.setBuffer(carrierMes.getBuffer());
        carrier.setReprocess(false);
        carrier.setUpdateDate(carrierMes.getUpdateDate());
        carrier.setSource(carrierMes.getSource());

        if (carrierMes.getCurrentLocation() > 1200 && carrierMes.getCurrentLocation() < 1246) {
            carrier.setPositionInLane(getPositionInLane(carrierMes.getCurrentLocation(), carrierMes.getCarrierNumber()));
        } else {
            carrier.setPositionInLane(0);
        }
        return carrier;
    }


    @Override
    public List<Carrier> getGroupHoldCarriers(GroupHoldFinderCriteria finderCriteria, Integer page, Integer size) {
        List<CarrierMes> carrierMeslist = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(finderCriteria, page, size);
        List<Carrier> carrierList = new ArrayList<Carrier>();
        for (CarrierMes carrierMes : carrierMeslist) {
            carrierList.add(getCarrier(carrierMes));
        }

        return carrierList;
    }

    public AlarmEvent getAlarmEventToDisplay() {
        AlarmEvent alarmEventToDisplay = null;
        List<AlarmEvent> alarmEvents = AlarmEvent.findCurrentUnClearedAlarmsForAppNotify(50);
        LOG.info("uncleared alarms found......."+ alarmEvents.size());
        if (alarmEvents != null && alarmEvents.size() > 0) {
             LOG.info("uncleared alarms found.......");
            for (AlarmEvent alarmEvent : alarmEvents) {
                LOG.info(" alarm event- "+alarmEvent.getId());
                AlarmDefinition alarmDefinition = AlarmDefinition.findNotificationRequiredAlarmsByAlarmNumberAndLocation(alarmEvent.getAlarmNumber(), alarmEvent.getLocation());

                if (alarmDefinition != null) {
                     LOG.info("uncleared notification required alarm found.......");
                     alarmEventToDisplay =  alarmEvent;
                    break;
                }
            }
        }

        return alarmEventToDisplay;
    }

    @Override
    public List<OrderFulfillment> getOrderFulfillmentsByOrder(WeldOrder order) {

        List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(order);

        List<OrderFulfillment> fulfillmentList = new ArrayList<OrderFulfillment>();
        for(OrderFulfillment fulfillment: fulfillments){
           CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(fulfillment.getId().getCarrierNumber());
           Stop currentLocation  = carrierMes == null? null:Stop.findStop(carrierMes.getCurrentLocation());
           fulfillment.setCurrentLocation(currentLocation);

            fulfillmentList.add(fulfillment);
        }
        return fulfillmentList;
    }

//    @Override
//    public List<LaneImpl> getStorageRowsForDetailedInventory() {
//        return StorageConfig.getStorageRowsForDetailedInventory();
//    }

    private volatile boolean connected = false;

    @Override
	public boolean isConnected() {
		return connected;
	}

    @Override
	public boolean isDisconnected() {
		return !connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	@EventSubscriber(eventClass = ConnectionEventMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void setConnectionState(ConnectionEventMessage msg) {
        LOG.info("received connection event: " + msg.isConnected());
        setConnected(msg.isConnected());
    }
}
