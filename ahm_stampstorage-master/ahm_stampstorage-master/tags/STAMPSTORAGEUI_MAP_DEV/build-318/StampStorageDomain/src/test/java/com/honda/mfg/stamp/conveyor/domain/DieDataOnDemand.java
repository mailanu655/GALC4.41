package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class DieDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<Die> data;

	public Die getNewTransientDie(int index) {
        Die obj = new Die();
        setId(obj, index) ;
        //setDieNumber(obj, index);
        setDescription(obj, index);
        return obj;
    }

	public void setId(Die obj, int index) {
        Long dieId = new Long(index);
        obj.setId(dieId);
    }

	public void setDieNumber(Die obj, int index) {
        Integer dieNumber = new Integer(index);
        //obj.setDieNumber(dieNumber);
    }

	public void setDescription(Die obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }

	public Die getSpecificDie(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Die obj = data.get(index);
        return Die.findDie(obj.getId());
    }

	public Die getRandomDie() {
        init();
        Die obj = data.get(rnd.nextInt(data.size()));
        return Die.findDie(obj.getId());
    }

	public boolean modifyDie(Die obj) {
        return false;
    }

	public void init() {
        data = Die.findDieEntries(0, 10);
        if (data == null)
            throw new IllegalStateException("Find entries implementation for 'Die' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }

        data = new java.util.ArrayList<Die>();
        for (int i = 0; i < 10; i++) {
            Die obj = getNewTransientDie(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
