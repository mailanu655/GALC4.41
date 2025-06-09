package com.honda.mfg.stamp.conveyor.helper;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 10/8/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractHelperImpl implements Helper {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractHelperImpl.class);

    @Override
    public void saveToAuditLog(String nodeId, String message, String source) {
        AuditErrorLog.save(nodeId, message, source);
    }

    public int getParmValue(String name) {
        ParmSetting parmSetting = ParmSetting.findParmSettingsByFieldName(name);
        try {
            return Integer.parseInt(parmSetting.getFieldvalue());
        } catch (Exception exception) {
            LOG.info(" Cannot Parse :" + parmSetting.getFieldvalue());
        }
        return 0;
    }

    @Override
    public Die getEmptyDie() {
        return Die.findDie(999l);
    }

    public List<Long> getStopsByStorageArea(StorageArea area) {
        List<Long> stopNumbers = new ArrayList<Long>();

        List<StorageRow> rows = StorageRow.findStorageRowsByArea(area);

        for (StorageRow row : rows) {
            stopNumbers.add(row.getStop().getId());
        }
        return stopNumbers;
    }

    public void generateAlarm(Integer location, Integer alarmNumber) {
        try {
            List<AlarmEvent> events = AlarmEvent.findCurrentUnClearedAlarmsByType(alarmNumber.intValue(), 50);
            if (events != null && events.size() > 0) {
                 LOG.info("uncleared alarm of type "+alarmNumber +" exists no need to insert new alarm");
            } else {
                AlarmEvent alarmEvent = new AlarmEvent();
                alarmEvent.setAlarmNumber(alarmNumber);
                alarmEvent.setLocation(location);
                alarmEvent.setEventTimestamp(new Timestamp(System.currentTimeMillis()));

                alarmEvent.persist();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Die findDieByNumber(Long dieNumber) {
        if (dieNumber == null || dieNumber < 0) {
            return null;
        }
        return Die.findDie(dieNumber);
    }

    @Override
    public Stop findStopByConveyorId(Long conveyorId) {
        if (conveyorId == null || conveyorId < 0) {
            return null;
        }
        return Stop.findStop(conveyorId);
    }

    @Override
    public List<StorageRow> findAllStorageRowsByStorageArea(StorageArea area) {
        return StorageRow.findStorageRowsByArea(area);
    }

    public Carrier populateCarrier(CarrierMes carrierMes) {

        Carrier carrier = new Carrier();
        carrier.setCarrierNumber(carrierMes.getCarrierNumber());
        carrier.setQuantity(carrierMes.getQuantity());
        int dieNumber = carrierMes.getDieNumber() != null ? carrierMes.getDieNumber() : 0;

        Long dieId = new Long(dieNumber);
        carrier.setDie(findDieByNumber(dieId));
        carrier.setCurrentLocation(findStopByConveyorId(carrierMes.getCurrentLocation()));
        carrier.setDestination(findStopByConveyorId(carrierMes.getDestination()));
        carrier.setPress(findPressById(carrierMes.getOriginationLocation()));
        carrier.setCarrierStatus(findCarrierStatus(carrierMes));
        carrier.setProductionRunNo(carrierMes.getProductionRunNumber());
        carrier.setStampingProductionRunTimestamp(carrierMes.getProductionRunDate());
        carrier.setBuffer(carrierMes.getBuffer());
        if (carrierMes.getCurrentLocation().equals(carrierMes.getDestination())) {
            carrier.setAlreadyInLane(true);
        } else {
            carrier.setAlreadyInLane(false);
        }
        return carrier;
    }

    private CarrierStatus findCarrierStatus(CarrierMes carrierMes) {
        if (carrierMes == null || carrierMes.getStatus() == null) {
            return CarrierStatus.SHIPPABLE;
        }
        return CarrierStatus.findByType(carrierMes.getStatus());
    }

    private Press findPressById(Integer id) {
        if (id == null || id < 0) {
            return Press.REWORK_C_LINE;
        }
        return Press.findByType(id);
    }

    public void pause(int pauseSec) {
        long delta = System.currentTimeMillis();
        while (System.currentTimeMillis() - delta < (pauseSec * 1000L)) {
        }
    }
}
