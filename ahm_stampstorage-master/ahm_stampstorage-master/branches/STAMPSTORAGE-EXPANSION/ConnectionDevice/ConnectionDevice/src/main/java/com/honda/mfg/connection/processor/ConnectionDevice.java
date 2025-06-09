package com.honda.mfg.connection.processor;

import com.honda.mfg.connection.processor.messages.ConnectionMessage;

import java.util.Calendar;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public interface ConnectionDevice {

    void sendMessage(ConnectionMessage request);

    ConnectionMessage sendMessage(ConnectionMessage request, int timeoutSec);

    boolean isHealthy();

    Calendar getLatestPing();
}
