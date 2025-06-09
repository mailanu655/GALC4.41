package com.honda;

import com.honda.eventbus.EventBusConfig;
import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * User: Jeffrey M Lutz
 * Date: 4/5/11
 */
public class DeviceFirstTest {

    @Test
    public void myFirstTest() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
        assertTrue(EventBusConfig.isSingleThreadMode());
    }
}
