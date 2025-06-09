package com.honda.mfg.threads;

/**
 * It is important to understand how threads can handle class level fields The
 * developer can use either: - synchronize around the field - volatile modifier
 * if the field is of the right type
 * <p/>
 * User: Jeffrey M Lutz Date: 4/10/11 Time: 10:31 AM
 */
public class ThreadExample implements Runnable {
	volatile boolean volatileFlag;
	boolean flag;
	boolean inSyncLoop;
	String someValue;

	volatile boolean inLoop;

	@Override
	public void run() {
		inLoop = true;
		while (flag || volatileFlag) {
		}
		inLoop = false;
	}

	SynchronizedExample getSyncExample() {
		return new SynchronizedExample();
	}
}
