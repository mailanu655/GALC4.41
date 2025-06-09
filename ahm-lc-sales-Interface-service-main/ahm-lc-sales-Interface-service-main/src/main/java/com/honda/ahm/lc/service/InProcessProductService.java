package com.honda.ahm.lc.service;

import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.InProcessProduct;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "inProcessProductService")
public class InProcessProductService extends BaseGalcService<InProcessProduct, String> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public void deleteInProcess(String galcUrl, String productId) {
        try {
            getLogger().debug("delete InProcessProduct record by ProductId-" + productId);
            deleteByProductId(galcUrl, productId, GalcDataType.INPROCESS);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
        }
    }

}