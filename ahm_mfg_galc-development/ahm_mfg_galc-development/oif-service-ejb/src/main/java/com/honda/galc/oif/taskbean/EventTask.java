package com.honda.galc.oif.taskbean;

import java.rmi.RemoteException;

/**
 * Remote interface for Enterprise Bean: EventTask
 */
public interface EventTask extends javax.ejb.EJBObject {
	public void execute(String userName, String oifInterface, Object[] args) throws RemoteException;

}
