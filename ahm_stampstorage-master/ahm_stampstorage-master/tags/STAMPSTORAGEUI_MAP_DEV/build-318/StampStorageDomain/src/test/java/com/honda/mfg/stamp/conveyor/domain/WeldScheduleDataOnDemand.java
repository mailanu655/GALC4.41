package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class WeldScheduleDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<WeldSchedule> data;

	public WeldSchedule getNewTransientWeldSchedule(int index) {
        com.honda.mfg.stamp.conveyor.domain.WeldSchedule obj = new com.honda.mfg.stamp.conveyor.domain.WeldSchedule();
        setWeldLine(obj, index);
        setLeftHandProdPlan(obj, index);
        setLeftHandProdRemaining(obj, index);
        setRightHandProdPlan(obj, index);
        setRightHandProdRemaining(obj, index);
        setCurrentModel(obj, index);
        setNextModel(obj, index);
        setNextQuantity(obj, index);
        return obj;
    }

	public void setWeldLine(WeldSchedule obj, int index) {
        java.lang.Integer weldLine = new Integer(index);
        obj.setWeldLine(weldLine);
    }

	public void setLeftHandProdPlan(WeldSchedule obj, int index) {
        java.lang.Integer leftHandProdPlan = new Integer(index);
        obj.setLeftHandProdPlan(leftHandProdPlan);
    }

	public void setLeftHandProdRemaining(WeldSchedule obj, int index) {
        java.lang.Integer leftHandProdRemaining = new Integer(index);
        obj.setLeftHandProdRemaining(leftHandProdRemaining);
    }

	public void setRightHandProdPlan(WeldSchedule obj, int index) {
        java.lang.Integer rightHandProdPlan = new Integer(index);
        obj.setRightHandProdPlan(rightHandProdPlan);
    }

	public void setRightHandProdRemaining(WeldSchedule obj, int index) {
        java.lang.Integer rightHandProdRemaining = new Integer(index);
        obj.setRightHandProdRemaining(rightHandProdRemaining);
    }

	public void setCurrentModel(WeldSchedule obj, int index) {
        java.lang.Integer currentModel = new Integer(index);
        obj.setCurrentModel(currentModel);
    }

	public void setNextModel(WeldSchedule obj, int index) {
        java.lang.Integer nextModel = new Integer(index);
        obj.setNextModel(nextModel);
    }

	public void setNextQuantity(WeldSchedule obj, int index) {
        java.lang.Integer nextQuantity = new Integer(index);
        obj.setNextQuantity(nextQuantity);
    }

	public WeldSchedule getSpecificWeldSchedule(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        WeldSchedule obj = data.get(index);
        return WeldSchedule.findWeldSchedule(obj.getId());
    }

	public WeldSchedule getRandomWeldSchedule() {
        init();
        WeldSchedule obj = data.get(rnd.nextInt(data.size()));
        return WeldSchedule.findWeldSchedule(obj.getId());
    }

	public boolean modifyWeldSchedule(WeldSchedule obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.WeldSchedule.findWeldScheduleEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'WeldSchedule' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.WeldSchedule>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.WeldSchedule obj = getNewTransientWeldSchedule(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
