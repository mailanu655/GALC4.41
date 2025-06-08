package com.honda.galc.oif.sched;

/**
 * Home interface for Enterprise Bean: EventScheduleRefresher
 */
public interface EventManagerHome extends javax.ejb.EJBHome {

	/**
	 * Creates a default instance of Session Bean: EventScheduleRefresher
	 */
	public com.honda.galc.oif.sched.EventManager create()
		throws javax.ejb.CreateException,
		java.rmi.RemoteException;
}
