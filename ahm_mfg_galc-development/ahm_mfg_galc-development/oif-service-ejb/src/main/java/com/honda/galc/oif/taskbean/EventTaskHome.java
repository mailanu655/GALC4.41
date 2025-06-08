package com.honda.galc.oif.taskbean;

/**
 * Home interface for Enterprise Bean: EventTask
 */
public interface EventTaskHome extends javax.ejb.EJBHome {

	/**
	 * Creates a default instance of Session Bean: EventTask
	 */
	public com.honda.galc.oif.taskbean.EventTask create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
