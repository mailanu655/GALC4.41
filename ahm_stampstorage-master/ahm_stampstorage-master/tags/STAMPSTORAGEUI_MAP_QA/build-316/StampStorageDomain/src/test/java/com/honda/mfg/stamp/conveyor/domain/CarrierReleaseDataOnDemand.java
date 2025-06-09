package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class CarrierReleaseDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<CarrierRelease> data;

	@Autowired
    private StopDataOnDemand stopDataOnDemand;

	public CarrierRelease getNewTransientCarrierRelease(int index) {
        com.honda.mfg.stamp.conveyor.domain.CarrierRelease obj = new com.honda.mfg.stamp.conveyor.domain.CarrierRelease();
        setId(obj, index);
        setCurrentLocation(obj, index);
        setDestination(obj, index);
        setSource(obj, index);
        setRequestTimestamp(obj, index);
        return obj;
    }

	public void setId(CarrierRelease obj, int index) {
        Long id=new Long(index);
        obj.setId(id);
    }

	public void setCurrentLocation(CarrierRelease obj, int index) {
        com.honda.mfg.stamp.conveyor.domain.Stop currentLocation = stopDataOnDemand.getRandomStop();
        obj.setCurrentLocation(currentLocation);
    }

	public void setDestination(CarrierRelease obj, int index) {
        com.honda.mfg.stamp.conveyor.domain.Stop destination = stopDataOnDemand.getRandomStop();
        obj.setDestination(destination);
    }

	public void setSource(CarrierRelease obj, int index) {
        java.lang.String source = "source_" + index;
        obj.setSource(source);
    }

	public void setRequestTimestamp(CarrierRelease obj, int index) {
      obj.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
    }

	public CarrierRelease getSpecificCarrierRelease(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        CarrierRelease obj = data.get(index);
        return CarrierRelease.findCarrierRelease(obj.getId());
    }

	public CarrierRelease getRandomCarrierRelease() {
        init();
        CarrierRelease obj = data.get(rnd.nextInt(data.size()));
        return CarrierRelease.findCarrierRelease(obj.getId());
    }

	public boolean modifyCarrierRelease(CarrierRelease obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.CarrierRelease.findCarrierReleaseEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'CarrierRelease' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.CarrierRelease>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.CarrierRelease obj = getNewTransientCarrierRelease(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
