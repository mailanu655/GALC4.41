package com.honda.galc.client;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>InactivityListener monitors AWT/Swing activities. If there is no activity
 * for a period time, the class will terminate the current GALC Client and start
 * a new instance, showing the login window</h3>
 * <p>
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author Hale Xie<br>
 *         Oct 24, 2014
 */
public class InactivityListener implements AWTEventListener, ActionListener {

	/** The interval. */
	private int interval = 300000; // 5 min

	/** The timer. */
	private Timer timer;

	/**
	 * Instantiates a new inactivity listener.
	 * 
	 * @param interval
	 *            the interval
	 */
	public InactivityListener(int interval) {
		super();
		this.interval = interval;
		timer = new Timer(interval, this);
		timer.setRepeats(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		try {
			Logger.getLogger().info(InactivityListener.class.getSimpleName() + " triggered - restarting the terminal");
			RelaunchUtil.restart();
		} catch (IOException e) {
			Logger.getLogger().error(e, "Failed to restart the terminal");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.AWTEventListener#eventDispatched(java.awt.AWTEvent)
	 */
	public void eventDispatched(AWTEvent event) {
		if (timer.isRunning())
			timer.restart();

	}

	/**
	 * Start the listener
	 */
	public void start() {
		long eventMask = AWTEvent.KEY_EVENT_MASK
				+ AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;
		timer.setInitialDelay(interval);
		timer.start();
		Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask);
	}

	/**
	 * Stop the listener;
	 */
	public void stop() {
		Toolkit.getDefaultToolkit().removeAWTEventListener(this);
		timer.stop();
	}

}
