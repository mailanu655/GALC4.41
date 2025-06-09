package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.*;

import java.util.List;

/**
 * User: Jeffrey M Lutz
 * Date: 6/20/11
 */
public interface StorageStateContext {
   StorageState getStorageState();
   List<CarrierMes> getCarriersWithInvalidDestination() ;
   Carrier populateCarrier(CarrierMes carrierMes);
   CarrierMes getCarrier(Integer carrierNumber);
   //Die findDieByDieNumber(Long dieNumber);
   void reload();
   void addDie(Long dieNumber, StorageRow  row);
   StorageRow getRow(Die die);
   void saveToAuditLog(String nodeId, String message, String source);
   Die getEmptyDie();
    boolean spaceAvailable(Stop stop);
}
