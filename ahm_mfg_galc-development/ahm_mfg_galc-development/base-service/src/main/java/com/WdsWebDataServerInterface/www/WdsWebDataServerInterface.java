/**
 * WdsWebDataServerInterface.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf130744.23 v111007102553
 */

package com.WdsWebDataServerInterface.www;

public interface WdsWebDataServerInterface extends javax.xml.rpc.Service {

     // This is the WdsWebDataServerInterface service.
    public com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort getWdsWebDataServerInterfacePort() throws javax.xml.rpc.ServiceException;

    public java.lang.String getWdsWebDataServerInterfacePortAddress();

    public com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort getWdsWebDataServerInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
