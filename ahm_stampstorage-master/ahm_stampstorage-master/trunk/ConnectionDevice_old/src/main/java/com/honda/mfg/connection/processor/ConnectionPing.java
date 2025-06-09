package com.honda.mfg.connection.processor;

import java.util.Calendar;

import com.honda.mfg.connection.watchdog.DevicePing;
import com.honda.mfg.connection.watchdog.WatchdogAdapter;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
/**
 * @author VCC44349
 *
 */
public class ConnectionPing implements DevicePing {
    private ConnectionDevice connectionDevice;

    public ConnectionPing(ConnectionDevice connectionDevice) {
        this.connectionDevice = connectionDevice;
    }

    @Override
    public boolean ping() {
        boolean pingReading;
        try {
            pingReading = connectionDevice.isHealthy();
        } catch (RuntimeException e) {
            return false;
        }
        return pingReading;
    }
    
    /**
	 * this method called from {@link WatchdogAdapter#run()}
	 * it checks the elapsed time since latestPing.  If elapsed time as a multiple
	 * of pollIntervalSec is greater than passiveWaitCount, returns false
	 * {@see WatchdogAdapter}
	 * @throws
     * @param passiveWaitCount : multiple of pollIntervalSec to wait
     * @return
     */
    @Override
	public boolean passivePing(int passiveWaitCount, int pollIntervalSec)  {
    	Calendar latestPing = connectionDevice.getLatestPing();
    	Calendar now = Calendar.getInstance();
    	long delta = now.getTimeInMillis() - latestPing.getTimeInMillis();
    	float waitCount = delta/(1000 * pollIntervalSec);
    	if(waitCount > passiveWaitCount) return false;
    	return true;
    }

    /**
	 * this method called from {@link WatchdogAdapter#run()}
	 * it computes the elapsed time since latestPing.  returns elapsed time as a multiple
	 * of pollIntervalSec
	 * {@see WatchdogAdapter}
	 * @throws
     * @param passiveWaitCount : multiple of pollIntervalSec to wait
     * @return
     */
    @Override
	public float getWaitCount(int pollIntervalSec)  {
    	Calendar latestPing = connectionDevice.getLatestPing();
    	Calendar now = Calendar.getInstance();
    	long delta = now.getTimeInMillis() - latestPing.getTimeInMillis();
    	float waitCount = delta/(1000 * pollIntervalSec);
    	return waitCount;
    }

}
