package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class CarrierHistoryDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<CarrierHistory> data;

	public CarrierHistory getNewTransientCarrierHistory(int index) {
        com.honda.mfg.stamp.conveyor.domain.CarrierHistory obj = new com.honda.mfg.stamp.conveyor.domain.CarrierHistory();
        setBuffer(obj, index);
        setCarrierNumber(obj, index);
        setCurrentLocation(obj, index);
        setDestination(obj, index);
        setDieNumber(obj, index);
        setOriginationLocation(obj, index);
        setProductionRunDate(obj, index);
        setProductionRunNumber(obj, index);
        setQuantity(obj, index);
        setStatus(obj, index);
        setTagId(obj, index);
        setUpdateDate(obj, index);
        setSource(obj, index);
        setCarrierMesArchiveTstp(obj, index);
        return obj;
    }

	public void setBuffer(CarrierHistory obj, int index) {
        java.lang.Integer buffer = new Integer(index);
        obj.setBuffer(buffer);
    }

	public void setCarrierNumber(CarrierHistory obj, int index) {
        java.lang.Integer CarrierNumber = new Integer(index);
        obj.setCarrierNumber(CarrierNumber);
    }

	public void setCurrentLocation(CarrierHistory obj, int index) {
        Long CurrentLocation = new Long(index);
        obj.setCurrentLocation(CurrentLocation);
    }

	public void setDestination(CarrierHistory obj, int index) {
        Long destination = new Long(index);
        obj.setDestination(destination);
    }

	public void setDieNumber(CarrierHistory obj, int index) {
        Long DieNumber = new Long(index);
        obj.setDieNumber(DieNumber);
    }

	public void setOriginationLocation(CarrierHistory obj, int index) {
        java.lang.Integer OriginationLocation = new Integer(index);
        obj.setOriginationLocation(OriginationLocation);
    }

	public void setProductionRunDate(CarrierHistory obj, int index) {
        java.sql.Timestamp ProductionRunDate = null;
        obj.setProductionRunDate(ProductionRunDate);
    }

	public void setProductionRunNumber(CarrierHistory obj, int index) {
        java.lang.Integer ProductionRunNumber = new Integer(index);
        obj.setProductionRunNumber(ProductionRunNumber);
    }

	public void setQuantity(CarrierHistory obj, int index) {
        java.lang.Integer Quantity = new Integer(index);
        obj.setQuantity(Quantity);
    }

	public void setStatus(CarrierHistory obj, int index) {
        CarrierStatus status = CarrierStatus.findByType(index);
        obj.setStatus(status);
    }

	public void setTagId(CarrierHistory obj, int index) {
        java.lang.Long TagId = new Long(index);
        obj.setTagId(TagId);
    }

	public void setUpdateDate(CarrierHistory obj, int index) {
        java.sql.Timestamp UpdateDate = null;
        obj.setUpdateDate(UpdateDate);
    }

	public void setSource(CarrierHistory obj, int index) {
        java.lang.String Source = "Source_" + index;
        obj.setSource(Source);
    }

	public void setCarrierMesArchiveTstp(CarrierHistory obj, int index) {
        java.sql.Timestamp CarrierMesArchiveTstp = null;
        obj.setCarrierMesArchiveTstp(CarrierMesArchiveTstp);
    }

	public CarrierHistory getSpecificCarrierHistory(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        CarrierHistory obj = data.get(index);
        return CarrierHistory.findCarrierHistory(obj.getId());
    }

	public CarrierHistory getRandomCarrierHistory() {
        init();
        CarrierHistory obj = data.get(rnd.nextInt(data.size()));
        return CarrierHistory.findCarrierHistory(obj.getId());
    }

	public boolean modifyCarrierHistory(CarrierHistory obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.CarrierHistory.findCarrierHistoryEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'CarrierHistory' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.CarrierHistory>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.CarrierHistory obj = getNewTransientCarrierHistory(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
