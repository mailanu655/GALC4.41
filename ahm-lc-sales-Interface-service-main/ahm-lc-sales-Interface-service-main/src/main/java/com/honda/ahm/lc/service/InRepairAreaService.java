package com.honda.ahm.lc.service;

import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.InRepairArea;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "inRepairAreaService")
public class InRepairAreaService extends BaseGalcService<InRepairArea,String> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected Logger getLogger() {
    	  return logger;
    }

    public InRepairArea saveinRepairArea(String galcUrl, InRepairArea entity) {

        try {
            getLogger().info("Save InRepairArea record -" + entity.toString());
            return save(galcUrl, entity, GalcDataType.INREPAIR);

        } catch (Exception e) {
            getLogger().error(e.getMessage());
        }
        return null;
    }
}
