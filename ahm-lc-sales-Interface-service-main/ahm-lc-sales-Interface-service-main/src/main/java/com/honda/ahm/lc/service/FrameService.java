package com.honda.ahm.lc.service;

import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.Frame;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("frameService")
public class FrameService extends BaseGalcService<Frame, String> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public Frame getFrame(String galcUrl, final String productId) {
        return findByProductId(galcUrl, productId, GalcDataType.FRAME);
    }

}
