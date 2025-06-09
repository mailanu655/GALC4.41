// ObjectQueue.java: Generic queue which stores Object instances.

package com.honda.mfg.stamp.storage.service.utils;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic queue which stores Object instances. On arrival of a new Object, all
 * interested Observers are notified and the events are dequeued.
 */
public class ObjectQueue extends Observable implements Runnable, Observer {
	public static final int VECTOR_INITIAL = 40;
	public static final int VECTOR_INCREMENT = 10;
	private static final Logger LOG = LoggerFactory.getLogger(ObjectQueue.class);

	private Vector storage = null;
	protected boolean isStarted = false;
	protected boolean continueProcessing = true;
	protected static int instanceNumber = 0;

	/**
	 * Construct main queue and start thread of execution.
	 */
	public ObjectQueue() {
		// Create variable length storage space.
		storage = new Vector(VECTOR_INITIAL, VECTOR_INCREMENT);

		// Start this instance on a new thread.
		Thread t = new Thread(this, "ObjectQueue[" + (instanceNumber++) + "]." + hashCode());
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Construct main queue and start thread of execution.
	 * 
	 * @param name of thread.
	 */
	public ObjectQueue(String name) {
		// Create variable length storage space.
		storage = new Vector(VECTOR_INITIAL, VECTOR_INCREMENT);

		// Start this instance on a new thread.
		Thread t = new Thread(this, name);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Add new listener to list.
	 * 
	 * @param listener object to be added.
	 */
	public void addListener(Observer listener) {
		this.addObserver(listener);
	}

	/**
	 * Remove listener from list.
	 * 
	 * @param listener object to be removed.
	 */
	public void removeListener(Observer listener) {
		this.deleteObserver(listener);
	}

	/**
	 * Wakes up when new Object is enqueued. This Object is sent to interested
	 * listeners.
	 */
	public synchronized void run() {
		isStarted = true;
		this.notifyAll();

		while (continueProcessing()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			processEvent();
		}
	}

	public boolean continueProcessing() {
		return continueProcessing;
	}

	public void continueProcessing(boolean continueProcessingFlag) {
		continueProcessing = continueProcessingFlag;

		if (continueProcessing == false) {
			synchronized (this) {
				LOG.info("ObjectQueue.continueProcessing(): Notifying waiting thread to die.");
				this.notify();
			}
		}
	}

	/**
	 * Update method.
	 */
	public void update(Observable observable, Object object) {
		synchronized (this) {
			postObject(object);
		}
	}

	/**
	 * Process new Object by informing all interested Observer objects. This runs on
	 * a thread separate from postObject().
	 */
	synchronized void processEvent() {
		Object object = null;

		LOG.info("ObjectQueue.processEvent(): Notifying Listeners.", 250);
		LOG.info("ObjectQueue.processEvent(): # of listeners " + countObservers(), 250);

		// Repeat until queue is empty.
		while ((object = getNextObject()) != null) {
			LOG.info("ObjectQueue.processEvent(): Sending " + object, 250);
			super.setChanged();
			super.notifyObservers(object);
		}
	}

	/**
	 * Enqueue new Object and notify waiting threads.
	 * 
	 * @param object to be posted.
	 */
	public void postObject(Object object) {
		LOG.info("ObjectQueue.postObject(): Posting Event " + object, 250);

		storage.addElement(object);

		synchronized (this) {
			if (isStarted == false) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			this.notify();
		}
	}

	/**
	 * Pop next Object.
	 * 
	 * @return Object at head of queue.
	 */
	public Object getNextObject() {
		Object object = null;

		try {
			if (storage.size() > 0) {
				object = (Object) storage.firstElement();
				storage.removeElement(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}

	/**
	 * Examine but do not pop element at specified index.
	 * 
	 * @param index of element to be examined.
	 * @return Object at that index.
	 */
	public Object peekObjectAt(int index) {
		Object object = null;

		try {
			object = (Object) storage.elementAt(index);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}

	/**
	 * Examine but do not pop next element.
	 * 
	 * @return Object at head of queue.
	 */
	public Object peekNextObject() {
		Object object = null;

		try {
			object = (Object) storage.firstElement();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}

	/**
	 * Return number of Objects in queue.
	 * 
	 * @return length of queue.
	 */
	public int getNumberOfObjects() {
		int ret = 0;

		try {
			ret = storage.size();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Convenient location for test code.
	 */
	public static void main(String argv[]) {
	}

}
