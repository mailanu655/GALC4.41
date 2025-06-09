package com.honda.mfg.schedule;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

/**
 * User: Jeffrey M Lutz Date: Feb 7, 2011
 */
public class SchedulerTest implements Runnable {
	private long lastRunTime = System.currentTimeMillis();
	private long largestInterval;

	@Test(timeout = 5000L)
	public void successfullyRun() {
		int expectedIntervalSec = 1;
		long expectedIntervalMilli = expectedIntervalSec * 1000L;
		Scheduler s = new Scheduler(this, expectedIntervalSec, "My Test Thread");

		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < expectedIntervalMilli * 4) {
		}
		long difference = Math.abs(expectedIntervalMilli - largestInterval);
		assertTrue(difference < expectedIntervalMilli * .05);
		s.shutdown();
	}

	@Test(timeout = 5000L)
	public void successfullyRunOnce() {
		int expectedIntervalSec = 1;
		long expectedIntervalMilli = expectedIntervalSec * 1000L;
		Scheduler runOnceScheduler = new Scheduler();
		long start = System.currentTimeMillis();
		lastRunTime = System.currentTimeMillis();
		// Perform test
		runOnceScheduler.runOnce(this);
		// Assert post conditions
		assertEquals(false, runOnceScheduler.isShutdown());
		runOnceScheduler.shutdown();
		assertEquals(true, runOnceScheduler.isShutdown());
	}

	@Override
	public void run() {
		long interval = System.currentTimeMillis() - lastRunTime;
		if (interval > largestInterval) {
			largestInterval = interval;
		}
		lastRunTime = System.currentTimeMillis();
		System.out.println("Time:  " + new Date());
	}
}
