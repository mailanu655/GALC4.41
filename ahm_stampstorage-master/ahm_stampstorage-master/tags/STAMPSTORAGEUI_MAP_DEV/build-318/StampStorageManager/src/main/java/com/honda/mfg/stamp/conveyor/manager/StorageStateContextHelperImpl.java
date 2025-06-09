package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.helper.AbstractHelperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jeffrey M Lutz
 * Date: 6/20/11
 */
public class StorageStateContextHelperImpl extends AbstractHelperImpl implements StorageStateContextHelper {
    private static final Logger LOG = LoggerFactory.getLogger(StorageStateContextHelperImpl.class);
    @Override
    public List<CarrierMes> findAllCarriersInStorage() {
        List<Long> stopNumbers = new ArrayList<Long>();

        List<StorageRow> rows = StorageRow.findAllStorageRows();

        for (StorageRow row : rows) {
            stopNumbers.add(row.getStop().getId());
        }

        return CarrierMes.findAllCarriersWithDestinationIn(stopNumbers);
    }

    @Override
    public boolean spaceAvailable(Stop stop) {
            long count =  CarrierMes.countCarriersWithDestinationStop(stop) ;
            List<StorageRow> rowsInA = StorageRow.findStorageRowsByArea(StorageArea.A_AREA);
            int capacity = 0;
            for(StorageRow row: rowsInA){
                if(!row.isBlocked()){
                    long carrierCount = CarrierMes.countCarriersWithDestinationStop(row.getStop());
                    if (carrierCount < row.getCapacity()){
                         capacity = capacity + (row.getCapacity() - Integer.parseInt(String.valueOf(carrierCount)));
                    }
                }
            }
             LOG.info(" Capacity Available in A-Area-"+ capacity +"   carriers to store in-"+ count);
            return (capacity > count);
        }



}
