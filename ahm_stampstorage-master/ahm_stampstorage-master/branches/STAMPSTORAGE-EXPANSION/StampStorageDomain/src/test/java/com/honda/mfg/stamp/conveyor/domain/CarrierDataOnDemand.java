package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

@Configurable
@Component
public class CarrierDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<Carrier> data;

	@Autowired
	private DieDataOnDemand dieDataOnDemand;

	@Autowired
	private StopDataOnDemand stopDataOnDemand;

	public Carrier getNewTransientCarrier(int index) {
		com.honda.mfg.stamp.conveyor.domain.Carrier obj = new com.honda.mfg.stamp.conveyor.domain.Carrier();
		setCarrierNumber(obj, index);
		setQuantity(obj, index);
		setDie(obj, index);
		setCurrentLocation(obj, index);
		setDestinationLocation(obj, index);
		setCarrierStatus(obj, index);
		setPress(obj, index);
		setLoadTimestamp(obj, index);
		setUnloadTimestamp(obj, index);
		return obj;
	}

	public void setCarrierNumber(Carrier obj, int index) {
		Integer carrierNumber = new Integer(index);
		obj.setCarrierNumber(carrierNumber);
	}

	public void setQuantity(Carrier obj, int index) {
		Integer quantity = new Integer(index);
		obj.setQuantity(quantity);
	}

	public void setDie(Carrier obj, int index) {
		com.honda.mfg.stamp.conveyor.domain.Die die = dieDataOnDemand.getRandomDie();
		obj.setDie(die);
	}

	public void setCurrentLocation(Carrier obj, int index) {
		com.honda.mfg.stamp.conveyor.domain.Stop currentLocation = stopDataOnDemand.getSpecificStop(index);
		obj.setCurrentLocation(currentLocation);
	}

	public void setDestinationLocation(Carrier obj, int index) {
		com.honda.mfg.stamp.conveyor.domain.Stop destinationLocation = stopDataOnDemand.getSpecificStop(index);
		obj.setDestination(destinationLocation);
	}

	public void setCarrierStatus(Carrier obj, int index) {
		CarrierStatus carrierStatus = CarrierStatus.class.getEnumConstants()[0];
		obj.setCarrierStatus(carrierStatus);
	}

	public void setPress(Carrier obj, int index) {
		Press press = Press.class.getEnumConstants()[0];
		obj.setPress(press);
	}

	public void setLoadTimestamp(Carrier obj, int index) {
		java.util.Date loadTimestamp = new java.util.GregorianCalendar(
				java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
				java.util.Calendar.getInstance().get(java.util.Calendar.MONTH),
				java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH),
				java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY),
				java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE),
				java.util.Calendar.getInstance().get(java.util.Calendar.SECOND)
						+ new Double(Math.random() * 1000).intValue()).getTime();
		obj.setLoadTimestamp(loadTimestamp);
	}

	public void setUnloadTimestamp(Carrier obj, int index) {
		java.util.Date unloadTimestamp = new java.util.GregorianCalendar(
				java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
				java.util.Calendar.getInstance().get(java.util.Calendar.MONTH),
				java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH),
				java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY),
				java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE),
				java.util.Calendar.getInstance().get(java.util.Calendar.SECOND)
						+ new Double(Math.random() * 1000).intValue()).getTime();
		obj.setUnloadTimestamp(unloadTimestamp);
	}

//	public Carrier getSpecificCarrier(int index) {
//        init();
//        if (index < 0) index = 0;
//        if (index > (data.size() - 1)) index = data.size() - 1;
//        Carrier obj = data.get(index);
//        return Carrier.findCarrier(obj.getId());
//    }
//
//	public Carrier getRandomCarrier() {
//        init();
//        Carrier obj = data.get(rnd.nextInt(data.size()));
//        return Carrier.findCarrier(obj.getId());
//    }

	public boolean modifyCarrier(Carrier obj) {
		return false;
	}
//
//	public void init() {
//        data = com.honda.mfg.stamp.conveyor.domain.Carrier.findCarrierEntries(0, 10);
//        if (data == null)
//            throw new IllegalStateException("Find entries implementation for 'Carrier' illegally returned null");
//        if (!data.isEmpty()) {
//            return;
//        }

//        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.Carrier>();
//        for (int i = 0; i < 10; i++) {
//            com.honda.mfg.stamp.conveyor.domain.Carrier obj = getNewTransientCarrier(i);
//            obj.persist();
//            obj.flush();
//            data.add(obj);
//        }
	// }
}
