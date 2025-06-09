package com.honda.mfg.connection.processor;

import java.util.Calendar;

import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;
import com.honda.mfg.connection.watchdog.WatchdogAdapterInterface;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class AdvancedConnection implements ConnectionDevice {
    private BasicConnection basicConnection;


    private WatchdogAdapterInterface watchdogAdapter;

    public AdvancedConnection(BasicConnection basicConnection,
                             WatchdogAdapterInterface watchdogAdapter) {
        if (basicConnection == null) {
            throw new IllegalArgumentException("basicConnection cannot be null and it was null");
        }
        if (watchdogAdapter == null) {
            throw new IllegalArgumentException("watchdogAdapter cannot be null and it was null");
        }
        this.basicConnection = basicConnection;
        this.watchdogAdapter = watchdogAdapter;
    }


    @Override
    public void sendMessage(ConnectionMessage request) {
        basicConnection.sendMessage(request);
    }

    @Override
    public ConnectionMessage sendMessage(ConnectionMessage request, int timeoutSec) {
        return basicConnection.sendMessage(request, timeoutSec);
    }

    @Override
    public boolean isHealthy() {
        return basicConnection.isHealthy();
    }

    GeneralMessage getGeneralMessage() {
        return basicConnection.getGeneralMessage();
    }


	public Calendar getLatestPing() {
		return basicConnection.getLatestPing();
	}
}
