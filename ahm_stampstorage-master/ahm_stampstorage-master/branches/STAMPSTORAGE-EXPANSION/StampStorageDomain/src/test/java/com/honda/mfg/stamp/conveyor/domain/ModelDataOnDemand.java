package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ModelDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<Model> data;

	public Model getNewTransientModel(int index) {
		Model obj = new Model();
		setPartType(obj, index);
		setDescription(obj, index);
		setLeftDie(obj, index);
		setRightDie(obj, index + 10);
		return obj;
	}

	public void setPartType(Model obj, int index) {
		String name = "name_" + index;
		obj.setName(name);
	}

	public void setDescription(Model obj, int index) {
		String description = "description_" + index;
		obj.setDescription(description);
	}

	private Die createDie(int index) {
		Die die = new Die();
		long id = index;
		Integer dieNumber = index;
		// dieNumber = dieNumber.substring(dieNumber.length() - 3, dieNumber.length());
		// die.setDieNumber(dieNumber);
		die.setId(id);
		die.setDescription("Die: " + index);
		return die;
	}

	public void setLeftDie(Model obj, int index) {
		obj.setLeftDie(createDie(index));
	}

	public void setRightDie(Model obj, int index) {
		obj.setRightDie(createDie(index));
	}

	public Model getSpecificModel(int index) {
		init();
		if (index < 0)
			index = 0;
		if (index > (data.size() - 1))
			index = data.size() - 1;
		Model obj = data.get(index);
		return Model.findModel(obj.getId());
	}

	public Model getRandomModel() {
		init();
		Model obj = data.get(rnd.nextInt(data.size()));
		return Model.findModel(obj.getId());
	}

	public boolean modifyModel(Model obj) {
		return false;
	}

	public void init() {
		data = Model.findModelEntries(0, 10);
		if (data == null)
			throw new IllegalStateException("Find entries implementation for 'Model' illegally returned null");
		if (!data.isEmpty()) {
			return;
		}

		data = new java.util.ArrayList<Model>();
		for (int i = 0; i < 10; i++) {
			Model obj = getNewTransientModel(i);
			obj.persist();
			obj.flush();
			data.add(obj);
		}
	}
}
