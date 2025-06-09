package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ValidDestinationDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<ValidDestination> data;

	@Autowired
	private StopDataOnDemand stopDataOnDemand;

	public ValidDestination getNewTransientValidDestination(int index) {
		com.honda.mfg.stamp.conveyor.domain.ValidDestination obj = new com.honda.mfg.stamp.conveyor.domain.ValidDestination();
		setStop(obj, index);
		setDestination(obj, index);
		return obj;
	}

	public void setStop(ValidDestination obj, int index) {
		com.honda.mfg.stamp.conveyor.domain.Stop stop = stopDataOnDemand.getRandomStop();
		obj.setStop(stop);
	}

	public void setDestination(ValidDestination obj, int index) {
		com.honda.mfg.stamp.conveyor.domain.Stop destination = stopDataOnDemand.getRandomStop();
		obj.setDestination(destination);
	}

	public ValidDestination getSpecificValidDestination(int index) {
		init();
		if (index < 0)
			index = 0;
		if (index > (data.size() - 1))
			index = data.size() - 1;
		ValidDestination obj = data.get(index);
		return ValidDestination.findValidDestination(obj.getId());
	}

	public ValidDestination getRandomValidDestination() {
		init();
		ValidDestination obj = data.get(rnd.nextInt(data.size()));
		return ValidDestination.findValidDestination(obj.getId());
	}

	public boolean modifyValidDestination(ValidDestination obj) {
		return false;
	}

	public void init() {
		data = com.honda.mfg.stamp.conveyor.domain.ValidDestination.findValidDestinationEntries(0, 10);
		if (data == null)
			throw new IllegalStateException(
					"Find entries implementation for 'ValidDestination' illegally returned null");
		if (!data.isEmpty()) {
			return;
		}

		data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.ValidDestination>();
		for (int i = 0; i < 10; i++) {
			com.honda.mfg.stamp.conveyor.domain.ValidDestination obj = getNewTransientValidDestination(i);
			obj.persist();
			obj.flush();
			data.add(obj);
		}
	}
}
