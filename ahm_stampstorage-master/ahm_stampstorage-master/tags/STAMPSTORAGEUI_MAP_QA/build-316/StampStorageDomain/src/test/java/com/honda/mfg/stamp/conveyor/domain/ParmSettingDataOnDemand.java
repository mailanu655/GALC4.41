package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class ParmSettingDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<ParmSetting> data;

	public ParmSetting getNewTransientParmSetting(int index) {
        com.honda.mfg.stamp.conveyor.domain.ParmSetting obj = new com.honda.mfg.stamp.conveyor.domain.ParmSetting();
        setFieldname(obj, index);
        setFieldvalue(obj, index);
        setDescription(obj, index);
        setUpdatedby(obj, index);
        setUpdatetstp(obj, index);
        return obj;
    }

	public void setFieldname(ParmSetting obj, int index) {
        java.lang.String fieldname = "fieldname_" + index;
        obj.setFieldname(fieldname);
    }

	public void setFieldvalue(ParmSetting obj, int index) {
        java.lang.String fieldvalue = "fieldvalue_" + index;
        obj.setFieldvalue(fieldvalue);
    }

	public void setDescription(ParmSetting obj, int index) {
        java.lang.String description = "description_" + index;
        obj.setDescription(description);
    }

	public void setUpdatedby(ParmSetting obj, int index) {
        java.lang.String updatedby = "updatedby_" + index;
        obj.setUpdatedby(updatedby);
    }

	public void setUpdatetstp(ParmSetting obj, int index) {
        java.sql.Timestamp updatetstp = new Timestamp(System.currentTimeMillis());
        obj.setUpdatetstp(updatetstp);
    }

	public ParmSetting getSpecificParmSetting(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        ParmSetting obj = data.get(index);
        return ParmSetting.findParmSetting(obj.getId());
    }

	public ParmSetting getRandomParmSetting() {
        init();
        ParmSetting obj = data.get(rnd.nextInt(data.size()));
        return ParmSetting.findParmSetting(obj.getId());
    }

	public boolean modifyParmSetting(ParmSetting obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.ParmSetting.findParmSettingEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'ParmSetting' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.ParmSetting>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.ParmSetting obj = getNewTransientParmSetting(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
