/**
 * NCATCollectorSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class NCATCollectorSoapStub extends com.ibm.ws.webservices.engine.client.Stub implements ham.honda.com.NCATCollectorSoap {
    public NCATCollectorSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
        if (service == null) {
            super.service = new com.ibm.ws.webservices.engine.client.Service();
        }
        else {
            super.service = service;
        }
        super.engine = ((com.ibm.ws.webservices.engine.client.Service) super.service).getEngine();
        super.messageContexts = new com.ibm.ws.webservices.engine.MessageContext[3];
        java.lang.String theOption = (java.lang.String)super._getProperty("lastStubMapping");
        if (theOption == null || !theOption.equals("ham.honda.com.NCATCollectorSoap")) {
                initTypeMapping();
                super._setProperty("lastStubMapping","ham.honda.com.NCATCollectorSoap");
        }
        super.cachedEndpoint = endpointURL;
        super.connection = ((com.ibm.ws.webservices.engine.client.Service) super.service).getConnection(endpointURL);
    }

    private void initTypeMapping() {
        javax.xml.rpc.encoding.TypeMapping tm = super.getTypeMapping(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
        java.lang.Class javaType = null;
        javax.xml.namespace.QName xmlType = null;
        javax.xml.namespace.QName compQName = null;
        javax.xml.namespace.QName compTypeQName = null;
        com.ibm.ws.webservices.engine.encoding.SerializerFactory sf = null;
        com.ibm.ws.webservices.engine.encoding.DeserializerFactory df = null;
        javaType = ham.honda.com.HeadLightAlignmentBE.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentBE");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = ham.honda.com.DatabaseResult.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "DatabaseResult");
        compTypeQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumSerializerFactory.class, javaType, xmlType, null, compTypeQName);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.EnumDeserializerFactory.class, javaType, xmlType, null, compTypeQName);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = ham.honda.com.KemkraftDataBE.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataBE");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = ham.honda.com.WheelAlignmentBE.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentBE");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _headLightAlignmentInsertOperation0 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getheadLightAlignmentInsertOperation0() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "headLightAlignmentBE"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentBE"), ham.honda.com.HeadLightAlignmentBE.class, false, false, false, true, true, false), 
          };
        _params0[0].setOption("inputPosition","0");
        _params0[0].setOption("partQNameString","{http://com.honda.ham/NCATCollector}HeadLightAlignmentBE");
        _params0[0].setOption("partName","HeadLightAlignmentBE");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentInsertResult"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "DatabaseResult"), ham.honda.com.DatabaseResult.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://com.honda.ham/NCATCollector}DatabaseResult");
        _returnDesc0.setOption("partName","DatabaseResult");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _headLightAlignmentInsertOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("headLightAlignmentInsert", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentInsert"), _params0, _returnDesc0, _faults0, "http://com.honda.ham/NCATCollector/HeadLightAlignmentInsert");
        _headLightAlignmentInsertOperation0.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollectorSoap"));
        _headLightAlignmentInsertOperation0.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentInsertSoapOut"));
        _headLightAlignmentInsertOperation0.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollector"));
        _headLightAlignmentInsertOperation0.setOption("buildNum","cf371106.07");
        _headLightAlignmentInsertOperation0.setOption("ResponseNamespace","http://com.honda.ham/NCATCollector");
        _headLightAlignmentInsertOperation0.setOption("targetNamespace","http://com.honda.ham/NCATCollector");
        _headLightAlignmentInsertOperation0.setOption("ResponseLocalPart","HeadLightAlignmentInsertResponse");
        _headLightAlignmentInsertOperation0.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentInsertSoapIn"));
        _headLightAlignmentInsertOperation0.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _headLightAlignmentInsertOperation0.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return _headLightAlignmentInsertOperation0;

    }

    private int _headLightAlignmentInsertIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getheadLightAlignmentInsertInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_headLightAlignmentInsertIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(NCATCollectorSoapStub._headLightAlignmentInsertOperation0);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://com.honda.ham/NCATCollector/HeadLightAlignmentInsert");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.SEND_TYPE_ATTR_PROPERTY, Boolean.FALSE);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.ENGINE_DO_MULTI_REFS_PROPERTY, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_headLightAlignmentInsertIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public ham.honda.com.DatabaseResult headLightAlignmentInsert(ham.honda.com.HeadLightAlignmentBE headLightAlignmentBE) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getheadLightAlignmentInsertInvoke0(new java.lang.Object[] {headLightAlignmentBE}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return (ham.honda.com.DatabaseResult) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (ham.honda.com.DatabaseResult) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), ham.honda.com.DatabaseResult.class);
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _kemkraftDataInsertOperation1 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getkemkraftDataInsertOperation1() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params1 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "kemkraftDataBE"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataBE"), ham.honda.com.KemkraftDataBE.class, false, false, false, true, true, false), 
          };
        _params1[0].setOption("inputPosition","0");
        _params1[0].setOption("partQNameString","{http://com.honda.ham/NCATCollector}KemkraftDataBE");
        _params1[0].setOption("partName","KemkraftDataBE");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc1 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataInsertResult"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "DatabaseResult"), ham.honda.com.DatabaseResult.class, true, false, false, false, true, false); 
        _returnDesc1.setOption("outputPosition","0");
        _returnDesc1.setOption("partQNameString","{http://com.honda.ham/NCATCollector}DatabaseResult");
        _returnDesc1.setOption("partName","DatabaseResult");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults1 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _kemkraftDataInsertOperation1 = new com.ibm.ws.webservices.engine.description.OperationDesc("kemkraftDataInsert", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataInsert"), _params1, _returnDesc1, _faults1, "http://com.honda.ham/NCATCollector/KemkraftDataInsert");
        _kemkraftDataInsertOperation1.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollectorSoap"));
        _kemkraftDataInsertOperation1.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataInsertSoapOut"));
        _kemkraftDataInsertOperation1.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollector"));
        _kemkraftDataInsertOperation1.setOption("buildNum","cf371106.07");
        _kemkraftDataInsertOperation1.setOption("ResponseNamespace","http://com.honda.ham/NCATCollector");
        _kemkraftDataInsertOperation1.setOption("targetNamespace","http://com.honda.ham/NCATCollector");
        _kemkraftDataInsertOperation1.setOption("ResponseLocalPart","KemkraftDataInsertResponse");
        _kemkraftDataInsertOperation1.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataInsertSoapIn"));
        _kemkraftDataInsertOperation1.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _kemkraftDataInsertOperation1.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return _kemkraftDataInsertOperation1;

    }

    private int _kemkraftDataInsertIndex1 = 1;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getkemkraftDataInsertInvoke1(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_kemkraftDataInsertIndex1];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(NCATCollectorSoapStub._kemkraftDataInsertOperation1);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://com.honda.ham/NCATCollector/KemkraftDataInsert");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.SEND_TYPE_ATTR_PROPERTY, Boolean.FALSE);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.ENGINE_DO_MULTI_REFS_PROPERTY, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_kemkraftDataInsertIndex1] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public ham.honda.com.DatabaseResult kemkraftDataInsert(ham.honda.com.KemkraftDataBE kemkraftDataBE) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getkemkraftDataInsertInvoke1(new java.lang.Object[] {kemkraftDataBE}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return (ham.honda.com.DatabaseResult) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (ham.honda.com.DatabaseResult) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), ham.honda.com.DatabaseResult.class);
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _wheelAlignmentInsertOperation2 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getwheelAlignmentInsertOperation2() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params2 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "wheelAlignmentBE"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentBE"), ham.honda.com.WheelAlignmentBE.class, false, false, false, true, true, false), 
          };
        _params2[0].setOption("inputPosition","0");
        _params2[0].setOption("partQNameString","{http://com.honda.ham/NCATCollector}WheelAlignmentBE");
        _params2[0].setOption("partName","WheelAlignmentBE");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc2 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentInsertResult"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "DatabaseResult"), ham.honda.com.DatabaseResult.class, true, false, false, false, true, false); 
        _returnDesc2.setOption("outputPosition","0");
        _returnDesc2.setOption("partQNameString","{http://com.honda.ham/NCATCollector}DatabaseResult");
        _returnDesc2.setOption("partName","DatabaseResult");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults2 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _wheelAlignmentInsertOperation2 = new com.ibm.ws.webservices.engine.description.OperationDesc("wheelAlignmentInsert", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentInsert"), _params2, _returnDesc2, _faults2, "http://com.honda.ham/NCATCollector/WheelAlignmentInsert");
        _wheelAlignmentInsertOperation2.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollectorSoap"));
        _wheelAlignmentInsertOperation2.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentInsertSoapOut"));
        _wheelAlignmentInsertOperation2.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollector"));
        _wheelAlignmentInsertOperation2.setOption("buildNum","cf371106.07");
        _wheelAlignmentInsertOperation2.setOption("ResponseNamespace","http://com.honda.ham/NCATCollector");
        _wheelAlignmentInsertOperation2.setOption("targetNamespace","http://com.honda.ham/NCATCollector");
        _wheelAlignmentInsertOperation2.setOption("ResponseLocalPart","WheelAlignmentInsertResponse");
        _wheelAlignmentInsertOperation2.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentInsertSoapIn"));
        _wheelAlignmentInsertOperation2.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _wheelAlignmentInsertOperation2.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return _wheelAlignmentInsertOperation2;

    }

    private int _wheelAlignmentInsertIndex2 = 2;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getwheelAlignmentInsertInvoke2(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_wheelAlignmentInsertIndex2];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(NCATCollectorSoapStub._wheelAlignmentInsertOperation2);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://com.honda.ham/NCATCollector/WheelAlignmentInsert");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.SEND_TYPE_ATTR_PROPERTY, Boolean.FALSE);
            mc.setProperty(com.ibm.wsspi.webservices.Constants.ENGINE_DO_MULTI_REFS_PROPERTY, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_wheelAlignmentInsertIndex2] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public ham.honda.com.DatabaseResult wheelAlignmentInsert(ham.honda.com.WheelAlignmentBE wheelAlignmentBE) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getwheelAlignmentInsertInvoke2(new java.lang.Object[] {wheelAlignmentBE}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return (ham.honda.com.DatabaseResult) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (ham.honda.com.DatabaseResult) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), ham.honda.com.DatabaseResult.class);
        }
    }

    private static void _staticInit() {
        _kemkraftDataInsertOperation1 = _getkemkraftDataInsertOperation1();
        _wheelAlignmentInsertOperation2 = _getwheelAlignmentInsertOperation2();
        _headLightAlignmentInsertOperation0 = _getheadLightAlignmentInsertOperation0();
    }

    static {
       _staticInit();
    }
}
