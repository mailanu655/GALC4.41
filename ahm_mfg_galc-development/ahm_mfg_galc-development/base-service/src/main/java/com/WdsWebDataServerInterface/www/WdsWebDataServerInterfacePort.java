/**
 * WdsWebDataServerInterfacePort.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf130744.23 v111007102553
 */

package com.WdsWebDataServerInterface.www;

public interface WdsWebDataServerInterfacePort extends java.rmi.Remote {
    public com.WdsWebDataServerInterface.www.WdsHttpValue[] currentValue(java.lang.String valueName) throws java.rmi.RemoteException;
    public com.WdsWebDataServerInterface.www.WdsHttpValue[][] currentValues(java.lang.String[] valuesList) throws java.rmi.RemoteException;
    public com.WdsWebDataServerInterface.www.WdsValueDefinition[] availableValues() throws java.rmi.RemoteException;
    public boolean updateStringValue(java.lang.String valueName, java.lang.String newValue) throws java.rmi.RemoteException;
    public boolean updateIntegerValue(java.lang.String valueName, java.math.BigInteger newValue) throws java.rmi.RemoteException;
    public boolean updateFloatValue(java.lang.String valueName, float newValue) throws java.rmi.RemoteException;
    public boolean updateValues(com.WdsWebDataServerInterface.www.WdsHttpValue[] valueName) throws java.rmi.RemoteException;
}
