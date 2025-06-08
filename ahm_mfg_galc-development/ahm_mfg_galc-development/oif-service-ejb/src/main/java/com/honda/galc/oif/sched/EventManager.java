package com.honda.galc.oif.sched;

import java.rmi.RemoteException;

public interface EventManager  extends javax.ejb.EJBObject {

	/**
	 * Re-starts OIF events by attempting to stop them and starting
	 * 
	 * @return status of start() operation
	 */
	public void start() throws RemoteException;

	/**
	 * Stops OIF events when EJB module is stopped by server
	 */
	public void stop() throws RemoteException;
	
	/**
	 * Refreshes the schedule by stopping all events and re-starting them<br>
	 * with a new schedule configuration
	 * 
	 * @throws RemoteException
	 */
	public void refreshSchedule() throws RemoteException;

}