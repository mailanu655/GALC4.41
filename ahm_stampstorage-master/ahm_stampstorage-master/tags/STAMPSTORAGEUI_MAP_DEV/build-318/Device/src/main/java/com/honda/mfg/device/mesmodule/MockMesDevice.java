package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.mesmodule.messages.GeneralMessage;
import com.honda.mfg.device.mesmodule.messages.MesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 7/7/11
 */
public class MockMesDevice implements MesDevice {
    private static final Logger LOG = LoggerFactory.getLogger(MockMesDevice.class);

    public MockMesDevice() {
    }

    @Override
    public void sendMessage(MesMessage message) throws CommunicationsException, NetworkCommunicationException {
        LOG.debug("Mock sending:  " + message.getMessage());
    }

    @Override
    public GeneralMessage sendMessage(MesMessage request, int timeoutSec) {
        LOG.trace("Sending MES MESSAGE request()");
        return null;
    }

    @Override
    public boolean isHealthy() {
        return true;
    }
}
