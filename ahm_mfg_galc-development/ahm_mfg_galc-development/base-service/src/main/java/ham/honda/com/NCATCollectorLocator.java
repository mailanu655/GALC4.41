/**
 * NCATCollectorLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class NCATCollectorLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, ham.honda.com.NCATCollector {

    public NCATCollectorLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "http://com.honda.ham/NCATCollector",
           "NCATCollector"));

        context.setLocatorName("ham.honda.com.NCATCollectorLocator");
    }

    public NCATCollectorLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("ham.honda.com.NCATCollectorLocator");
    }

    // Use to get a proxy class for NCATCollectorSoap
    private final java.lang.String NCATCollectorSoap_address = "http://ncatcollector/ncatcollector.asmx";

    public java.lang.String getNCATCollectorSoapAddress() {
        if (context.getOverriddingEndpointURIs() == null) {
            return NCATCollectorSoap_address;
        }
        String overriddingEndpoint = (String) context.getOverriddingEndpointURIs().get("NCATCollectorSoap");
        if (overriddingEndpoint != null) {
            return overriddingEndpoint;
        }
        else {
            return NCATCollectorSoap_address;
        }
    }

    private java.lang.String NCATCollectorSoapPortName = "NCATCollectorSoap";

    // The WSDD port name defaults to the port name.
    private java.lang.String NCATCollectorSoapWSDDPortName = "NCATCollectorSoap";

    public java.lang.String getNCATCollectorSoapWSDDPortName() {
        return NCATCollectorSoapWSDDPortName;
    }

    public void setNCATCollectorSoapWSDDPortName(java.lang.String name) {
        NCATCollectorSoapWSDDPortName = name;
    }

    public ham.honda.com.NCATCollectorSoap getNCATCollectorSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(getNCATCollectorSoapAddress());
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getNCATCollectorSoap(endpoint);
    }

    public ham.honda.com.NCATCollectorSoap getNCATCollectorSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        ham.honda.com.NCATCollectorSoap _stub =
            (ham.honda.com.NCATCollectorSoap) getStub(
                NCATCollectorSoapPortName,
                (String) getPort2NamespaceMap().get(NCATCollectorSoapPortName),
                ham.honda.com.NCATCollectorSoap.class,
                "ham.honda.com.NCATCollectorSoapStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(NCATCollectorSoapWSDDPortName);
        }
        return _stub;
    }


    // Use to get a proxy class for NCATCollectorSoap12
    private final java.lang.String NCATCollectorSoap12_address = "http://ncatcollector/ncatcollector.asmx";

    public java.lang.String getNCATCollectorSoap12Address() {
        if (context.getOverriddingEndpointURIs() == null) {
            return NCATCollectorSoap12_address;
        }
        String overriddingEndpoint = (String) context.getOverriddingEndpointURIs().get("NCATCollectorSoap12");
        if (overriddingEndpoint != null) {
            return overriddingEndpoint;
        }
        else {
            return NCATCollectorSoap12_address;
        }
    }

    private java.lang.String NCATCollectorSoap12PortName = "NCATCollectorSoap12";

    // The WSDD port name defaults to the port name.
    private java.lang.String NCATCollectorSoap12WSDDPortName = "NCATCollectorSoap12";

    public java.lang.String getNCATCollectorSoap12WSDDPortName() {
        return NCATCollectorSoap12WSDDPortName;
    }

    public void setNCATCollectorSoap12WSDDPortName(java.lang.String name) {
        NCATCollectorSoap12WSDDPortName = name;
    }

    public ham.honda.com.NCATCollectorSoap getNCATCollectorSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(getNCATCollectorSoap12Address());
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getNCATCollectorSoap12(endpoint);
    }

    public ham.honda.com.NCATCollectorSoap getNCATCollectorSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        ham.honda.com.NCATCollectorSoap _stub =
            (ham.honda.com.NCATCollectorSoap) getStub(
                NCATCollectorSoap12PortName,
                (String) getPort2NamespaceMap().get(NCATCollectorSoap12PortName),
                ham.honda.com.NCATCollectorSoap.class,
                "ham.honda.com.NCATCollectorSoap12Stub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(NCATCollectorSoap12WSDDPortName);
        }
        return _stub;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ham.honda.com.NCATCollectorSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                javax.xml.namespace.QName portName = getDefaultPortQName(serviceEndpointInterface, getPort2NamespaceMap());
                return getPort(portName, serviceEndpointInterface);
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
        if ("NCATCollectorSoap".equals(inputPortName)) {
            return getNCATCollectorSoap();
        }
        else if ("NCATCollectorSoap12".equals(inputPortName)) {
            return getNCATCollectorSoap12();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        NCATCollectorSoapWSDDPortName = prefix + "/" + NCATCollectorSoapPortName;
        NCATCollectorSoap12WSDDPortName = prefix + "/" + NCATCollectorSoap12PortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollector");
    }

    private java.util.Map port2NamespaceMap = null;

    protected synchronized java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "NCATCollectorSoap",
               "http://schemas.xmlsoap.org/wsdl/soap/");
            port2NamespaceMap.put(
               "NCATCollectorSoap12",
               "null");
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
        if  (portName.getLocalPart().equals("NCATCollectorSoap")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "HeadLightAlignmentInsert", "null"),
                createCall(portName, "KemkraftDataInsert", "null"),
                createCall(portName, "WheelAlignmentInsert", "null"),
            };
        }
        else if  (portName.getLocalPart().equals("NCATCollectorSoap12")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "HeadLightAlignmentInsert", "null"),
                createCall(portName, "KemkraftDataInsert", "null"),
                createCall(portName, "WheelAlignmentInsert", "null"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Error: portName should not be null.");
        }
    }
}
