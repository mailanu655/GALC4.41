package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class PressActivityDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<PressActivity> data;

	public PressActivity getNewTransientPressActivity(int index) {
        com.honda.mfg.stamp.conveyor.domain.PressActivity obj = new com.honda.mfg.stamp.conveyor.domain.PressActivity();
        setPressName(obj, index);
        setProdRunNumber(obj, index);
        setDieNumber(obj, index);
        setQuantityProduced(obj, index);
        return obj;
    }

	public void setPressName(PressActivity obj, int index) {
        java.lang.String pressName = "pressName_" + index;
        obj.setPressName(pressName);
    }

	public void setProdRunNumber(PressActivity obj, int index) {
        java.lang.Integer prodRunNumber = new Integer(index);
        obj.setProdRunNumber(prodRunNumber);
    }

	public void setDieNumber(PressActivity obj, int index) {
        java.lang.Integer dieNumber = new Integer(index);
        obj.setDieNumber(dieNumber);
    }

	public void setQuantityProduced(PressActivity obj, int index) {
        java.lang.Integer quantityProduced = new Integer(index);
        obj.setQuantityProduced(quantityProduced);
    }

	public PressActivity getSpecificPressActivity(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        PressActivity obj = data.get(index);
        return PressActivity.findPressActivity(obj.getId());
    }

	public PressActivity getRandomPressActivity() {
        init();
        PressActivity obj = data.get(rnd.nextInt(data.size()));
        return PressActivity.findPressActivity(obj.getId());
    }

	public boolean modifyPressActivity(PressActivity obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.PressActivity.findPressActivityEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'PressActivity' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.PressActivity>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.PressActivity obj = getNewTransientPressActivity(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
