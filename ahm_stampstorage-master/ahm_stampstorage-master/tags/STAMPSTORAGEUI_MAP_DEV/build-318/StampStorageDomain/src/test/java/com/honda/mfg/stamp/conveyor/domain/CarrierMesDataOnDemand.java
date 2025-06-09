package com.honda.mfg.stamp.conveyor.domain;

import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class CarrierMesDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<CarrierMes> data;

	@Autowired
    private DieDataOnDemand dieDataOnDemand;

	@Autowired
    private StopDataOnDemand stopDataOnDemand;

	public CarrierMes getNewTransientCarrierMes(int index) {
        CarrierMes obj = new CarrierMes();
        setCarrierNumber(obj, index);
        setTagId(obj, index);
        setDieNumber(obj, index);
        setQuantity(obj, index);
        setUpdateDate(obj, index);
        setCurrentLocation(obj, index);
        setDestination(obj, index);
        setStatus(obj, index);
        setOriginationLocation(obj, index);
        setBuffer(obj, index);
        setSource(obj, "user");
        return obj;
    }

	public void setCarrierNumber(CarrierMes obj, int index) {
        Integer carrierNumber = new Integer(index);
        obj.setCarrierNumber(carrierNumber);
    }

	public void setTagId(CarrierMes obj, int index) {
        obj.setTagId(index);
    }

	public void setQuantity(CarrierMes obj, int index) {
        Integer quantity = new Integer(index);
        obj.setQuantity(quantity);
    }

	public void setUpdateDate(CarrierMes obj, int index) {
        obj.setUpdateDate(new Date());
    }

	public void setDieNumber(CarrierMes obj, int index) {
        obj.setDieNumber(index % 10);
    }

	public void setCurrentLocation(CarrierMes obj, int index) {
        obj.setCurrentLocation(513L);
    }

	public void setDestination(CarrierMes obj, int index) {
        obj.setCurrentLocation(1200L + index);
    }

	public void setStatus(CarrierMes obj, int index) {
        obj.setStatus(0);
    }

	public void setOriginationLocation(CarrierMes obj, int index) {
        obj.setOriginationLocation(index);
    }

	public void setBuffer(CarrierMes obj, int index) {
        obj.setBuffer(index % 1);
    }

	public void setSource(CarrierMes obj, String source) {
        obj.setSource(source);
    }

	public CarrierMes getSpecificCarrier(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        CarrierMes obj = data.get(index);
        return CarrierMes.findCarrier(obj.getId());
    }

	public CarrierMes getRandomCarrier() {
        init();
        CarrierMes obj = data.get(rnd.nextInt(data.size()));
        return CarrierMes.findCarrier(obj.getId());
    }

	public boolean modifyCarrier(CarrierMes obj) {
        return false;
    }

	public void init() {
        data = CarrierMes.findCarrierEntries(0, 10);
        if (data == null)
            throw new IllegalStateException("Find entries implementation for 'Carrier' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }

        data = new java.util.ArrayList<CarrierMes>();
        for (int i = 0; i < 10; i++) {
            CarrierMes obj = getNewTransientCarrierMes(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
