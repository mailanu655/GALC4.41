package com.honda.mfg.connection.watchdog;

import com.honda.mfg.connection.processor.messages.MissedPingPassive;
import com.honda.mfg.schedule.Scheduler;

import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public class WatchdogAdapter implements WatchdogAdapterInterface {
    private static final Logger LOG = LoggerFactory.getLogger(WatchdogAdapter.class);

    private Watchdog watchdog;
    private DevicePing devicePing;
    private Scheduler scheduler;
    private volatile boolean firstPass;
    
    private String ownerId = null;    
    public String getOwnerId() {
		return ownerId;
	}

    @Override
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	private volatile boolean passivePing = true;
    public boolean isPassivePing() {
		return passivePing;
	}

	@Override
	public void setPassivePing(boolean passivePing) {
		this.passivePing = passivePing;
	}

    private int pollIntervalSecs = 20;
    public int getPollIntervalSecs() {
		return pollIntervalSecs;
	}

	public void setPollIntervalSecs(int pollIntervalSecs) {
		this.pollIntervalSecs = pollIntervalSecs;
	}

	private int passiveWaitCount = 2;
    @Override
	public int getPassiveWaitCount() {
		return passiveWaitCount;
	}

	@Override
	public void setPassiveWaitCount(int passiveWaitCount) {
		this.passiveWaitCount = passiveWaitCount;
	}
	
	private int currentMaxWaitCount = 2;

	@Override
	public int getCurrentMaxWaitCount() {
		return currentMaxWaitCount;
	}

	@Override
	public void setCurrentMaxWaitCount(int currentMaxWaitCount) {
		this.currentMaxWaitCount = currentMaxWaitCount;
	}

	WatchdogAdapter(Watchdog watchdog, DevicePing devicePing) {
        this.watchdog = watchdog;
        this.devicePing = devicePing;
	}
	
	WatchdogAdapter(Watchdog watchdog, DevicePing devicePing, int pollIntervalSecs, boolean passivePing) {
        this.watchdog = watchdog;
        this.devicePing = devicePing;
        this.pollIntervalSecs = pollIntervalSecs;
        this.passivePing = passivePing;
        this.firstPass = true;
        scheduler = new Scheduler(new Runner(), pollIntervalSecs, "WatchdogAdapter");
    }

    public WatchdogAdapter(Watchdog watchdog, DevicePing devicePing, int pollIntervalSecs) {
    	//startup in passive mode - device owner will switch to active mode when needed
        this(watchdog, devicePing, pollIntervalSecs, true);
    }

	@Override
	public void stopRunning() {
        scheduler.shutdown();
    }

    void run() {
        new Runner().run();
    }

	@Override
	public void roleChange(boolean isPassive) {
		setPassivePing(isPassive);		
	}
	
	@Override
	public void resetMaxWaitCount()  {
		currentMaxWaitCount = passiveWaitCount;
	}
	
	@Override
	public void incrementWaitCount(int delta)  {
		currentMaxWaitCount += delta;
	}
	
	@Override
	public boolean isAdaptiveWaitCount()  {
		return (currentMaxWaitCount != passiveWaitCount);
	}
	
    private class Runner implements Runnable {
        public void run() {
            if (firstPass) {
                firstPass = false;
                return;
            }
            if (!isPassivePing()) {
				long delta = System.currentTimeMillis();
				boolean healthy = devicePing.ping();
				delta = System.currentTimeMillis() - delta;
				if (healthy) {
					LOG.info("Ping:  healthy    --> "
							+ devicePing.getClass().getSimpleName()
							+ " --> Response time: " + delta + " (ms)");
					watchdog.healthyEvent();
				} else {
					LOG.info("Ping:  unhealthy  --> "
							+ devicePing.getClass().getSimpleName()
							+ " --> Response time: " + delta + " (ms)");
					watchdog.unhealthyEvent();
				}
			}
            else  {  //passive, listen-only mode
            	//boolean primaryHealthy = devicePing.passivePing(getPassiveWaitCount(), getPollIntervalSecs());
            	//if(!primaryHealthy)  {
            	float currentWaitCount = devicePing.getWaitCount(pollIntervalSecs);
            	boolean primaryHealthy = (currentWaitCount < currentMaxWaitCount);
            	if(!primaryHealthy)  {
					LOG.info("WatchdogAdapter#run: publishing MissedPingPassive: wait count=" + currentMaxWaitCount);
					MissedPingPassive missedP = new MissedPingPassive(currentWaitCount,currentMaxWaitCount);
            		EventBus.publish(missedP);
            	}
            }
        }
    }
}
