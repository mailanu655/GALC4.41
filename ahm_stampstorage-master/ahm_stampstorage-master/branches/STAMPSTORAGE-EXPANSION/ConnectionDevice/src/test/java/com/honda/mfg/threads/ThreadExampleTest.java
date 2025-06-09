package com.honda.mfg.threads;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.schedule.Scheduler;

/**
 * User: Jeffrey M Lutz Date: 4/10/11 Time: 10:31 AM
 */
public class ThreadExampleTest {
	private final Logger LOG = LoggerFactory.getLogger(ThreadExampleTest.class);
	private final int LOOP_START = 1;
	private final int LOOP_SIZE = 10;
	private final int LOOP_SWITCH_COUNT = LOOP_SIZE / 2;
	private final long WAIT = 100L;

	@Test
	public void testNonVolatileField() throws InterruptedException {
		ThreadExample me = new ThreadExample();

		Scheduler sched = new Scheduler(me, "me thread");
		me.flag = true;
		for (int i = LOOP_START; i < LOOP_SIZE; i++) {
			if (i % LOOP_SWITCH_COUNT == 0)
				me.flag = false;
			LOG.info("inLoop? " + me.inLoop);
			Thread.sleep(WAIT);
		}
//        assertEquals("A non-volatile boolean flag will never be reliable within a threaded environment!", true, me.inLoop);
	}

	@Test
	public void testVolatileFieldShutdownThread() throws InterruptedException {
		ThreadExample me = new ThreadExample();

		Scheduler sched = new Scheduler(me, "me thread");
		me.flag = true;
		for (int i = LOOP_START; i < LOOP_SIZE; i++) {
			if (i % LOOP_SWITCH_COUNT == 0) {
				sched.shutdown();
				me.inLoop = false;
			}
			LOG.info("inLoop? " + me.inLoop);
			Thread.sleep(WAIT);
		}
		assertEquals("A forced shutdown of thread and setting inLoop to false did not result in inLoop remaining false",
				false, me.inLoop);
	}

	@Test
	public void testShutdownMultipleTimes() {
		ThreadExample me = new ThreadExample();

		Scheduler sched = new Scheduler(me, "me thread");
		me.flag = true;
		sched.shutdown();
		sched.shutdown();
		sched.shutdown();
	}

	@Test
	public void testVolatileField() throws InterruptedException {
		ThreadExample me = new ThreadExample();

		Scheduler sched = new Scheduler(me, "me thread");
		me.volatileFlag = true;
		for (int i = LOOP_START; i < LOOP_SIZE; i++) {
			if (i % LOOP_SWITCH_COUNT == 0)
				me.volatileFlag = false;
			LOG.info("inLoop? " + me.inLoop);
			Thread.sleep(WAIT);
		}
		assertEquals("A volatile boolean flag will be reliable within a threaded environment!", false, me.inLoop);
	}
}
