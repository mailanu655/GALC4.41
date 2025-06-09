package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.manager.StorageLifeCycle;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.manager.StorageStateContext;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jeffrey M Lutz
 * Date: 6/22/11
 */
public class StorageStateContextMock implements StorageStateContext, StorageLifeCycle {
    private StorageState storageState;

    private Carrier carrier1, carrier11, carrier12, carrier2, carrier3, carrier4, carrier41, carrier42, carrier43, carrier5, carrier51, carrier52, carrier53, carrier54;

    public StorageStateContextMock(StorageState storageState) {
        this.storageState = storageState;
    }

    @Override
    public void reload() {

    }

    List<StorageRow> getStorageRows(){
        List<StorageRow> storageRows = new ArrayList<StorageRow>();

        for(int i=0;i<35;i++){
            StorageRow row = new StorageRow(i,"Row-"+i,10,1);
            row.setStop(new Stop(1200+1+i));
            storageRows.add(row);
        }

        return storageRows;
    }

    @Override
    public void addDie(Long dieNumber, StorageRow row) {

    }

    @Override
    public StorageRow getRow(Die die) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveToAuditLog(String nodeId, String message, String source) {
        System.out.println(message);
    }

    @Override
    public Die getEmptyDie() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean spaceAvailable(Stop stop) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public StorageState getStorageState() {
        return this.storageState;
    }

    @Override
    public List<CarrierMes> getCarriersWithInvalidDestination() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Carrier populateCarrier(CarrierMes carrierMes) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CarrierMes getCarrier(Integer carrierNumber) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }



}
