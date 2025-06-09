package com.honda.mfg.stamp.conveyor.release;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.ReloadStorageStateMessage;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CarrierReleaseProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(CarrierReleaseProcessor.class);

    private Storage storage;
    private ReleaseManagerHelper helper;
    //private Message message;

    public CarrierReleaseProcessor(Storage storage, ReleaseManagerHelper releaseManagerHelper) {
        AnnotationProcessor.process(this);
        this.storage = storage;
        this.helper = releaseManagerHelper;
    }

    @EventSubscriber(eventClass = CarrierStatusMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void process(CarrierStatusMessage msg) {
        //message = msg;
       // Carrier carrier = msg.getCarrier();
        LOG.info(" received status Message" + msg.getCarrierNumber());
        Stop currentLocation = msg.getCurrentLocationAsStop();
        Stop destination = msg.getDestinationAsStop();

        int bufferOne = 1;
        boolean atHeadOfStop = Integer.parseInt(msg.getBuffer()) == bufferOne;
        if (currentLocation != null) {

            boolean reachedDestination = currentLocation.equals(destination);

            if (atHeadOfStop && reachedDestination) {
                Long carrierNumber = Long.parseLong(msg.getCarrierNumber());
                CarrierRelease carrierRelease = CarrierRelease.findCarrierRelease(carrierNumber);
                if (carrierRelease != null && !helper.anyCarriersReleasingInStorageArea(currentLocation)) {
                    LOG.debug("Carrier-" + msg.getCarrierNumber() + " is at head Of stop-" + msg.getCurrentLocation());
                    if (carrierRelease.getCurrentLocation().equals(currentLocation)) {
                        publishCarrierUpdateMessageAndUpdateStorageState(carrierRelease);
                        helper.pause(2);
                    } else {
                        LOG.debug("Carrier-" + msg.getCarrierNumber() + " is not at-" + carrierRelease.getCurrentLocation() + ", deleting carrier from release table");
                        carrierRelease.remove();
                    }
                    if (!helper.anyCarriersSetToReleaseFromThisRow(currentLocation)) {
                        CarrierRelease release = helper.getCarriersSetToReleaseFromThisStorageArea(currentLocation);
                        if (release != null) {
                            if (carrierRelease.getCurrentLocation().equals(currentLocation)) {
                                publishCarrierUpdateMessageAndUpdateStorageState(release);
                            } else {
                                LOG.debug("Carrier-" + msg.getCarrierNumber() + " is not at-" + carrierRelease.getCurrentLocation() + ", deleting carrier from release table");
                                carrierRelease.remove();
                            }
                        }
                    }
                } else {
                    if (carrierRelease == null && currentLocation != null && currentLocation.isRowStop()) {
                        if (helper.anyCarriersSetToReleaseFromThisRow(currentLocation)) {
                            LOG.debug("Carrier-" + msg.getCarrierNumber() + " is at head Of row -" + msg.getCurrentLocation() + " and carriers behind set to release ");
                            LOG.debug("Publishing Stale Data message");
                            StaleDataMessage staleDataMessage = new StaleDataMessage(false);
                            EventBus.publish(staleDataMessage);

                            ReloadStorageStateMessage reloadStorageStateMessage = new ReloadStorageStateMessage(currentLocation);
                            EventBus.publish(reloadStorageStateMessage);
                        }
                    }
                }
            }
        } else{
            LOG.info(" Invalid Stop :"+msg.getCurrentLocation());
        }
    }


    @EventSubscriber(eventClass = CarrierStatusMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void releaseCheckProcessing(CarrierStatusMessage msg) {
        //message = msg;
        //Carrier carrier = msg.getCarrier();
        LOG.info(" received status Message" + msg.getCarrierNumber());
        Stop currentLocation = msg.getCurrentLocationAsStop();

        if (currentLocation != null && (currentLocation.getStopType().equals(StopType.RELEASE_CHECK) || (currentLocation.getStopArea().equals(StopArea.B_AREA)&& currentLocation.getStopType().equals(StopType.RECIRC_TO_ALL_ROWS)))) {
            CarrierRelease release = helper.getCarrierToReleaseFromStorage(currentLocation);
            if (release != null) {
                Integer carrierNumber = Integer.parseInt(release.getId().toString());
                CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
                Stop carrierCurrLocation = Stop.findStop(carrierMes.getCurrentLocation());
                if (!helper.anyCarriersReleasingInStorageArea(carrierCurrLocation)) {
                    if (release.getCurrentLocation().equals(carrierCurrLocation)) {
                        publishCarrierUpdateMessageAndUpdateStorageState(release);
                    } else {
                        LOG.debug("Carrier-" + msg.getCarrierNumber() + " is not at-" + release.getCurrentLocation() + ", deleting carrier from release table");
                        release.remove();
                    }
                }
            }
        }
    }

    void publishCarrierUpdateMessageAndUpdateStorageState(CarrierRelease release) {
        Integer carrierNumber = Integer.parseInt(release.getId().toString());
        Carrier carrierToRelease = new Carrier();
        carrierToRelease.setCarrierNumber(carrierNumber);
        carrierToRelease.setDestination(release.getDestination());
        carrierToRelease.setSource(release.getSource());
      //TODO:CHECK FOR MAINT BITS
        LOG.debug("Releasing carrier-" + carrierNumber);
        EventBus.publish(new CarrierUpdateMessage(carrierToRelease));
        release.remove();


        CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
        Carrier carrier = new Carrier();
        carrier.setCarrierNumber(carrierMes.getCarrierNumber());
        carrier.setCurrentLocation(Stop.findStop(carrierMes.getCurrentLocation()));
        carrier.setBuffer(carrierMes.getBuffer());
        carrier.setMaintenanceBits(carrierMes.getMaintenanceBits());
        updateStorageState(carrier, release);
    }

    void turnOffSubscriber() {
        AnnotationProcessor.unprocess(this);
    }

    public void updateStorageState(Carrier carrier, CarrierRelease carrierRelease) {

        if (carrier.getCurrentLocation().isRowStop() && !carrier.getCurrentLocation().equals(carrierRelease.getDestination())) {
            storage.getStorageState().removeCarrierFromRow(carrier.getCarrierNumber(), carrier.getCurrentLocation().getId());
        }

        if (carrierRelease.getDestination().isRowStop()) {
            storage.getStorageState().storeInLaneIfDestinationIsALaneAndAlreadyNotExistsInStorageSystem(carrier);
        }

        if (!carrier.getCurrentLocation().isRowStop() && !carrierRelease.getDestination().isRowStop()) {
            storage.getStorageState().removeCarrierFromStorageState(carrier);
        }
    }

}
