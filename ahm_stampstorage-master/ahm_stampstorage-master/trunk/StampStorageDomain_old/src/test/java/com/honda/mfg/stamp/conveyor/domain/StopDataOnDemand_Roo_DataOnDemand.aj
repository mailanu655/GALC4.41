package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

privileged aspect StopDataOnDemand_Roo_DataOnDemand {

    declare @type: StopDataOnDemand:@Component;

    private Random StopDataOnDemand.rnd = new java.security.SecureRandom();

    private List<Stop> StopDataOnDemand.data;

    public Stop StopDataOnDemand.getNewTransientStop(int index) {
        Stop obj = new Stop();
        setName(obj, index);
        setStopArea(obj, index);
        setStopType(obj, index);
        setId(obj, index);
//        setStopCapacity(obj,index);
//        setStopAvailability(obj,index);
        return obj;
    }

    public void StopDataOnDemand.setName(Stop obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

    public void StopDataOnDemand.setStopArea(Stop obj, int index) {
        StopArea stopArea = StopArea.class.getEnumConstants()[0];
        obj.setStopArea(stopArea);
    }

    public void StopDataOnDemand.setStopType(Stop obj, int index) {
        StopType stopType = StopType.class.getEnumConstants()[0];
        obj.setStopType(stopType);
    }

    public void StopDataOnDemand.setId(Stop obj, int index) {
        obj.setId(new Long(index));
    }

//    public void StopDataOnDemand.setStopCapacity(Stop obj, int index) {
//        obj.setCapacity(index);
//    }
//
//     public void StopDataOnDemand.setStopAvailability(Stop obj, int index) {
//        StopAvailability stopAvailability = StopAvailability.class.getEnumConstants()[0];
//        obj.setStopAvailability(stopAvailability);
//    }

    public Stop StopDataOnDemand.getSpecificStop(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Stop obj = data.get(index);
        return Stop.findStop(obj.getId());
    }

    public Stop StopDataOnDemand.getRandomStop() {
        init();
        Stop obj = data.get(rnd.nextInt(data.size()));
        return Stop.findStop(obj.getId());
    }

    public boolean StopDataOnDemand.modifyStop(Stop obj) {
        return false;
    }

    public void StopDataOnDemand.init() {
        data = Stop.findStopEntries(0, 10);
        if (data == null)
            throw new IllegalStateException("Find entries implementation for 'Stop' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }

        data = new java.util.ArrayList<Stop>();
        for (int i = 0; i < 10; i++) {
            Stop obj = getNewTransientStop(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
