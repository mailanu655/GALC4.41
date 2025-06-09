package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.DEFECT_TYPE;
import com.honda.mfg.stamp.conveyor.domain.enums.REWORK_METHOD;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class DefectDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<Defect> data;

	public Defect getNewTransientDefect(int index) {
        com.honda.mfg.stamp.conveyor.domain.Defect obj = new com.honda.mfg.stamp.conveyor.domain.Defect();
        setCarrierNumber(obj, index);
        setProductionRunNo(obj, index);
        setDefectType(obj, index);
        setReworkMethod(obj, index);
        setXArea(obj, index);
        setYArea(obj, index);
        setDefectTimestamp(obj, index);
        setDefectRepaired(obj, index);
        setSource(obj, index);
        setNote(obj, index);
        return obj;
    }

	public void setCarrierNumber(Defect obj, int index) {
        Integer carrierNumber = new Integer(index);
        obj.setCarrierNumber(carrierNumber);
    }

	public void setProductionRunNo(Defect obj, int index) {
        Integer productionRunNo = new Integer(index);
        obj.setProductionRunNo(productionRunNo);
    }

	public void setDefectType(Defect obj, int index) {
        DEFECT_TYPE defectType = DEFECT_TYPE.class.getEnumConstants()[0];
        obj.setDefectType(defectType);
    }

	public void setReworkMethod(Defect obj, int index) {
        REWORK_METHOD reworkMethod = REWORK_METHOD.class.getEnumConstants()[0];
        obj.setReworkMethod(reworkMethod);
    }

	public void setXArea(Defect obj, int index) {
        Integer xArea = new Integer(index);
        obj.setXArea(xArea);
    }

	public void setYArea(Defect obj, int index) {
        String yArea = "yArea_" + index;
        obj.setYArea(yArea);
    }

	public void setNote(Defect obj, int index) {
        String source = "note_" + index;
        obj.setSource(source);
    }

	public void setSource(Defect obj, int index) {
        String source = "user_" + index;
        obj.setSource(source);
    }

	public void setDefectTimestamp(Defect obj, int index) {
        java.util.Date defectTimestamp = new java.util.GregorianCalendar(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR), java.util.Calendar.getInstance().get(java.util.Calendar.MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY), java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE), java.util.Calendar.getInstance().get(java.util.Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDefectTimestamp(defectTimestamp);
    }

	public void setDefectRepaired(Defect obj, int index) {
        Boolean defectRepaired = Boolean.TRUE;
        obj.setDefectRepaired(defectRepaired);
    }

	public Defect getSpecificDefect(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Defect obj = data.get(index);
        return Defect.findDefect(obj.getId());
    }

	public Defect getRandomDefect() {
        init();
        Defect obj = data.get(rnd.nextInt(data.size()));
        return Defect.findDefect(obj.getId());
    }

	public boolean modifyDefect(Defect obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.Defect.findDefectEntries(0, 10);
        if (data == null)
            throw new IllegalStateException("Find entries implementation for 'Defect' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }

        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.Defect>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.Defect obj = getNewTransientDefect(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
