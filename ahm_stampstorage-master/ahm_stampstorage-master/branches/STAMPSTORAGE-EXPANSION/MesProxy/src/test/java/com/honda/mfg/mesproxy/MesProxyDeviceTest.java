package com.honda.mfg.mesproxy;

import junit.framework.TestCase;

/**
 * User: Adam S. Kendell
 * Date: 7/7/11
 */
public class MesProxyDeviceTest extends TestCase {
    private String pingMessage = "{\"GeneralMessage\":{\"messageType\":\"PING\"}}{END_OF_MESSAGE}";

    public void testProxyDeviceRestart() {
        MesProxy mesProxy = new MesProxy();
        mesProxy.processInput(pingMessage);
//        boolean test;
//        while (true) {
//            test = true;
//        }
    }
}
