/**
 * WdsWebDataServerInterfaceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf130744.23 v111007102553
 */

package com.WdsWebDataServerInterface.www;

public class WdsWebDataServerInterfaceLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, com.WdsWebDataServerInterface.www.WdsWebDataServerInterface {

     // This is the WdsWebDataServerInterface service.

    public WdsWebDataServerInterfaceLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl",
           "WdsWebDataServerInterface"));

        context.setLocatorName("com.WdsWebDataServerInterface.www.WdsWebDataServerInterfaceLocator");
    }

    public WdsWebDataServerInterfaceLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("com.WdsWebDataServerInterface.www.WdsWebDataServerInterfaceLocator");
    }

    // Use to get a proxy class for wdsWebDataServerInterfacePort
    private final java.lang.String wdsWebDataServerInterfacePort_address = "http://p2alcpg:30582/SstWSServlet";

    public java.lang.String getWdsWebDataServerInterfacePortAddress() {
        if (context.getOverriddingEndpointURIs() == null) {
            return wdsWebDataServerInterfacePort_address;
        }
        String overriddingEndpoint = (String) context.getOverriddingEndpointURIs().get("WdsWebDataServerInterfacePort");
        if (overriddingEndpoint != null) {
            return overriddingEndpoint;
        }
        else {
            return wdsWebDataServerInterfacePort_address;
        }
    }

    private java.lang.String wdsWebDataServerInterfacePortPortName = "WdsWebDataServerInterfacePort";

    // The WSDD port name defaults to the port name.
    private java.lang.String wdsWebDataServerInterfacePortWSDDPortName = "WdsWebDataServerInterfacePort";

    public java.lang.String getWdsWebDataServerInterfacePortWSDDPortName() {
        return wdsWebDataServerInterfacePortWSDDPortName;
    }

    public void setWdsWebDataServerInterfacePortWSDDPortName(java.lang.String name) {
        wdsWebDataServerInterfacePortWSDDPortName = name;
    }

    public com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort getWdsWebDataServerInterfacePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(getWdsWebDataServerInterfacePortAddress());
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getWdsWebDataServerInterfacePort(endpoint);
    }

    public com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort getWdsWebDataServerInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort _stub =
            (com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort) getStub(
                wdsWebDataServerInterfacePortPortName,
                (String) getPort2NamespaceMap().get(wdsWebDataServerInterfacePortPortName),
                com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort.class,
                "com.WdsWebDataServerInterface.www.WdsWebDataServerInterfaceBindingStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(wdsWebDataServerInterfacePortWSDDPortName);
        }
        return _stub;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort.class.isAssignableFrom(serviceEndpointInterface)) {
                return getWdsWebDataServerInterfacePort();
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("WSWS3273E: Error: There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        String inputPortName = portName.getLocalPart();
        if ("WdsWebDataServerInterfacePort".equals(inputPortName)) {
            return getWdsWebDataServerInterfacePort();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        wdsWebDataServerInterfacePortWSDDPortName = prefix + "/" + wdsWebDataServerInterfacePortPortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface");
    }

    private java.util.Map port2NamespaceMap = null;

    protected synchronized java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "WdsWebDataServerInterfacePort",
               "http://schemas.xmlsoap.org/wsdl/soap/");
        }
        return port2NamespaceMap;
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            String serviceNamespace = getServiceName().getNamespaceURI();
            for (java.util.Iterator i = getPort2NamespaceMap().keySet().iterator(); i.hasNext(); ) {
                ports.add(
                    com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                        serviceNamespace,
                        (String) i.next()));
            }
        }
        return ports.iterator();
    }

    public javax.xml.rpc.Call[] getCalls(javax.xml.namespace.QName portName) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Error: portName should not be null.");
        }
        if  (portName.getLocalPart().equals("WdsWebDataServerInterfacePort")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "availableValues", "null"),
                createCall(portName, "currentValue", "null"),
                createCall(portName, "currentValues", "null"),
                createCall(portName, "updateStringValue", "null"),
                createCall(portName, "updateIntegerValue", "null"),
                createCall(portName, "updateFloatValue", "null"),
                createCall(portName, "updateValues", "null"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Error: portName should not be null.");
        }
    }
}
