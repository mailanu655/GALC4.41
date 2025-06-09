package com.honda.mfg.schedule;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 5, 2010
 */
/**
 * @author VCC44349
 *
 */
public class Scheduler {
	private ScheduledExecutorService s;
	private long delayIntervalSec;
	private long samplingIntervalSec;
	private String threadName;
	private Runnable runnable;
	private boolean daemon = false;

	public long getDelayIntervalSec() {
		return delayIntervalSec;
	}

	public void setDelayIntervalSec(long delayIntervalSec) {
		this.delayIntervalSec = delayIntervalSec;
	}

	public long getSamplingIntervalSec() {
		return samplingIntervalSec;
	}

	public void setSamplingIntervalSec(long samplingIntervalSec) {
		this.samplingIntervalSec = samplingIntervalSec;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	public boolean isDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	public Scheduler(Runnable runnable, long samplingIntervalSec, String threadName) {
		this(runnable, samplingIntervalSec, threadName, false);
	}

	public Scheduler(Runnable runnable, long delayIntervalSec, long samplingIntervalSec, String threadName) {
		this.delayIntervalSec = delayIntervalSec;
		setSamplingIntervalSec(samplingIntervalSec);
		setThreadName(threadName);
		ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(threadName, false);
		this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
		Runnable runMe = new MyRunnable(runnable, samplingIntervalSec);
		new Scheduler(runMe, "private party");
	}

	public Scheduler(Runnable runnable, long samplingIntervalSec, String threadName, boolean daemon) {
		ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(threadName, daemon);
		this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
		this.s.scheduleAtFixedRate(runnable, 0, samplingIntervalSec, SECONDS);
	}

	public Scheduler(Runnable runnable, String threadName) {
		boolean daemon = false;
		ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(threadName, daemon);
		this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
		this.s.execute(runnable);
	}

	public Scheduler() {
		String threadName = "Unknown";
		Boolean daemon = false;
		ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(threadName, daemon);
		this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
	}

	class MyRunnable implements Runnable {
		Runnable runnable;
		long samplingIntervalSec;

		public MyRunnable(Runnable runnable, long samplingIntervalSec) {
			this.runnable = runnable;
			this.samplingIntervalSec = samplingIntervalSec;
		}

		@Override
		public void run() {
			long delta = System.currentTimeMillis();
			while ((System.currentTimeMillis() - delta) < (delayIntervalSec * 1000L)) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			s.scheduleAtFixedRate(runnable, 0, samplingIntervalSec, SECONDS);
		}
	}

	public void runOnce(Runnable runnable) {
		this.s.execute(runnable);
	}

	public void shutdown() {
		this.s.shutdown();
	}

	boolean isShutdown() {
		return s.isShutdown();
	}

	public void restart() {
		ThreadFactoryImpl threadFactory = new ThreadFactoryImpl(getThreadName(), false);
		this.s = Executors.newSingleThreadScheduledExecutor(threadFactory);
		Runnable runMe = new MyRunnable(getRunnable(), samplingIntervalSec);
		this.s.execute(runMe);
	}

	/**
	 * pauses calling thread by a specified duration, similar to sleep
	 * 
	 * @param t    pause duration
	 * @param unit time unit for pause duration
	 * 
	 */
	public static void pause(int t, TimeUnit unit) {
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

		try {
			exec.schedule(new Runnable() {
				@Override
				public void run() {
					// do nothing
				}
			}, t, unit).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			if (exec != null)
				exec.shutdown();
		}

	}

}
