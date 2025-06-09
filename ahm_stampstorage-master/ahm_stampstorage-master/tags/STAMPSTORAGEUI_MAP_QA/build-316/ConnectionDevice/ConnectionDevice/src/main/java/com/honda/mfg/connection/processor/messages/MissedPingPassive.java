package com.honda.mfg.connection.processor.messages;

public class MissedPingPassive {
	
	/**
	 * @param currentWaitCount
	 * @param passiveWaitCount
	 */
	public MissedPingPassive(float currentWaitCount, int passiveWaitCount) {
		super();
		this.currentWaitCount = currentWaitCount;
		this.passiveWaitCount = passiveWaitCount;
	}

	float currentWaitCount = 0;
	
	public float getCurrentWaitCount() {
		return currentWaitCount;
	}
	public void setCurrentWaitCount(float currentWaitCount) {
		this.currentWaitCount = currentWaitCount;
	}

	int passiveWaitCount = 2;
	public int getPassiveWaitCount() {
		return passiveWaitCount;
	}
	public void setPassiveWaitCount(int passiveWaitCount) {
		this.passiveWaitCount = passiveWaitCount;
	}

}
