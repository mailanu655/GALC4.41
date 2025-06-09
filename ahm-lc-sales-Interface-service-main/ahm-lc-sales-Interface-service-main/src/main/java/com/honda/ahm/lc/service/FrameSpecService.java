package com.honda.ahm.lc.service;

import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.FrameSpec;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("frameSpecService")
public class FrameSpecService extends BaseGalcService<FrameSpec, String> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public FrameSpec getFrameSpec(String galcUrl, final String productSpecCode) {
        return findByProductId(galcUrl, productSpecCode, GalcDataType.FRAME_SPEC);
    }

}
