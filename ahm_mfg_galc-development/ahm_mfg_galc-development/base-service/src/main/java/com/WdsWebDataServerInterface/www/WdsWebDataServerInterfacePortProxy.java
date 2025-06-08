package com.WdsWebDataServerInterface.www;

public class WdsWebDataServerInterfacePortProxy implements com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort {
  private boolean _useJNDI = true;
  private String _endpoint = null;
  private com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort __wdsWebDataServerInterfacePort = null;
  
  public WdsWebDataServerInterfacePortProxy() {
    _initWdsWebDataServerInterfacePortProxy();
  }
  
  private void _initWdsWebDataServerInterfacePortProxy() {
  
    if (_useJNDI) {
      try {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        __wdsWebDataServerInterfacePort = ((com.WdsWebDataServerInterface.www.WdsWebDataServerInterface)ctx.lookup("java:comp/env/service/WdsWebDataServerInterface")).getWdsWebDataServerInterfacePort();
      }
      catch (javax.naming.NamingException namingException) {}
      catch (javax.xml.rpc.ServiceException serviceException) {}
    }
    if (__wdsWebDataServerInterfacePort == null) {
      try {
        __wdsWebDataServerInterfacePort = (new com.WdsWebDataServerInterface.www.WdsWebDataServerInterfaceLocator()).getWdsWebDataServerInterfacePort();
        
      }
      catch (javax.xml.rpc.ServiceException serviceException) {}
    }
    if (__wdsWebDataServerInterfacePort != null) {
      if (_endpoint != null)
        ((javax.xml.rpc.Stub)__wdsWebDataServerInterfacePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
      else
        _endpoint = (String)((javax.xml.rpc.Stub)__wdsWebDataServerInterfacePort)._getProperty("javax.xml.rpc.service.endpoint.address");
    }
    
  }
  
  
  public void useJNDI(boolean useJNDI) {
    _useJNDI = useJNDI;
    __wdsWebDataServerInterfacePort = null;
    
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (__wdsWebDataServerInterfacePort != null)
      ((javax.xml.rpc.Stub)__wdsWebDataServerInterfacePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort getWdsWebDataServerInterfacePort() {
    if (__wdsWebDataServerInterfacePort == null)
      _initWdsWebDataServerInterfacePortProxy();
    return __wdsWebDataServerInterfacePort;
  }
  
  public com.WdsWebDataServerInterface.www.WdsHttpValue[] currentValue(java.lang.String valueName) throws java.rmi.RemoteException{
    if (__wdsWebDataServerInterfacePort == null)
      _initWdsWebDataServerInterfacePortProxy();
    return __wdsWebDataServerInterfacePort.currentValue(valueName);
  }
  
  public com.WdsWebDataServerInterface.www.WdsHttpValue[][] currentValues(java.lang.String[] valuesList) throws java.rmi.RemoteException{
    if (__wdsWebDataServerInterfacePort == null)
      _initWdsWebDataServerInterfacePortProxy();
    return __wdsWebDataServerInterfacePort.currentValues(valuesList);
  }
  
  public com.WdsWebDataServerInterface.www.WdsValueDefinition[] availableValues() throws java.rmi.RemoteException{
    if (__wdsWebDataServerInterfacePort == null)
      _initWdsWebDataServerInterfacePortProxy();
    return __wdsWebDataServerInterfacePort.availableValues();
  }
  
  public boolean updateStringValue(java.lang.String valueName, java.lang.String newValue) throws java.rmi.RemoteException{
    if (__wdsWebDataServerInterfacePort == null)
      _initWdsWebDataServerInterfacePortProxy();
    return __wdsWebDataServerInterfacePort.updateStringValue(valueName, newValue);
  }
  
  public boolean updateIntegerValue(java.lang.String valueName, java.math.BigInteger newValue) throws java.rmi.RemoteException{
    if (__wdsWebDataServerInterfacePort == null)
      _initWdsWebDataServerInterfacePortProxy();
    return __wdsWebDataServerInterfacePort.updateIntegerValue(valueName, newValue);
  }
  
  public boolean updateFloatValue(java.lang.String valueName, float newValue) throws java.rmi.RemoteException{
    if (__wdsWebDataServerInterfacePort == null)
      _initWdsWebDataServerInterfacePortProxy();
    return __wdsWebDataServerInterfacePort.updateFloatValue(valueName, newValue);
  }
  
  public boolean updateValues(com.WdsWebDataServerInterface.www.WdsHttpValue[] valueName) throws java.rmi.RemoteException{
    if (__wdsWebDataServerInterfacePort == null)
      _initWdsWebDataServerInterfacePortProxy();
    return __wdsWebDataServerInterfacePort.updateValues(valueName);
  }
  
  
}