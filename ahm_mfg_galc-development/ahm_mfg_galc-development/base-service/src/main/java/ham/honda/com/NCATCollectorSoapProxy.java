package ham.honda.com;

public class NCATCollectorSoapProxy implements ham.honda.com.NCATCollectorSoap {
  private boolean _useJNDI = true;
  private boolean _useJNDIOnly = false;
  private String _endpoint = null;
  private ham.honda.com.NCATCollectorSoap __nCATCollectorSoap = null;
  
  public NCATCollectorSoapProxy() {
    _initNCATCollectorSoapProxy();
  }
  
  private void _initNCATCollectorSoapProxy() {
  
    if (_useJNDI || _useJNDIOnly) {
      try {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        __nCATCollectorSoap = ((ham.honda.com.NCATCollector)ctx.lookup("java:comp/env/service/NCATCollector")).getNCATCollectorSoap();
      }
      catch (javax.naming.NamingException namingException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("JNDI lookup failure: javax.naming.NamingException: " + namingException.getMessage());
          namingException.printStackTrace(System.out);
        }
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__nCATCollectorSoap == null && !_useJNDIOnly) {
      try {
        __nCATCollectorSoap = (new ham.honda.com.NCATCollectorLocator()).getNCATCollectorSoap();
        
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__nCATCollectorSoap != null) {
      if (_endpoint != null)
        ((javax.xml.rpc.Stub)__nCATCollectorSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
      else
        _endpoint = (String)((javax.xml.rpc.Stub)__nCATCollectorSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
    }
    
  }
  
  
  public void useJNDI(boolean useJNDI) {
    _useJNDI = useJNDI;
    __nCATCollectorSoap = null;
    
  }
  
  public void useJNDIOnly(boolean useJNDIOnly) {
    _useJNDIOnly = useJNDIOnly;
    __nCATCollectorSoap = null;
    
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (__nCATCollectorSoap == null)
      _initNCATCollectorSoapProxy();
    if (__nCATCollectorSoap != null)
      ((javax.xml.rpc.Stub)__nCATCollectorSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ham.honda.com.DatabaseResult headLightAlignmentInsert(ham.honda.com.HeadLightAlignmentBE headLightAlignmentBE) throws java.rmi.RemoteException{
    if (__nCATCollectorSoap == null)
      _initNCATCollectorSoapProxy();
    return __nCATCollectorSoap.headLightAlignmentInsert(headLightAlignmentBE);
  }
  
  public ham.honda.com.DatabaseResult kemkraftDataInsert(ham.honda.com.KemkraftDataBE kemkraftDataBE) throws java.rmi.RemoteException{
    if (__nCATCollectorSoap == null)
      _initNCATCollectorSoapProxy();
    return __nCATCollectorSoap.kemkraftDataInsert(kemkraftDataBE);
  }
  
  public ham.honda.com.DatabaseResult wheelAlignmentInsert(ham.honda.com.WheelAlignmentBE wheelAlignmentBE) throws java.rmi.RemoteException{
    if (__nCATCollectorSoap == null)
      _initNCATCollectorSoapProxy();
    return __nCATCollectorSoap.wheelAlignmentInsert(wheelAlignmentBE);
  }
  
  
  public ham.honda.com.NCATCollectorSoap getNCATCollectorSoap() {
    if (__nCATCollectorSoap == null)
      _initNCATCollectorSoapProxy();
    return __nCATCollectorSoap;
  }
  
}