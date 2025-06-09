package com.honda.mfg.stamp.conveyor.helper;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 10/8/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Helper {

    void saveToAuditLog(String nodeId, String message, String source);

    int getParmValue(String name);

    Die getEmptyDie();

    List<Long> getStopsByStorageArea(StorageArea area);

    void generateAlarm(Integer carrierNumber, Integer alarmNumber);

    Die findDieByNumber(Long dieNumber);

    Stop findStopByConveyorId(Long conveyorId);

    List<StorageRow> findAllStorageRowsByStorageArea(StorageArea area);

    Carrier populateCarrier(CarrierMes carrierMes);

    void pause(int pauseSec);

    void resetAlarm(Integer alarmNumber);

    void archiveAlarm(Long id, String user);


}
