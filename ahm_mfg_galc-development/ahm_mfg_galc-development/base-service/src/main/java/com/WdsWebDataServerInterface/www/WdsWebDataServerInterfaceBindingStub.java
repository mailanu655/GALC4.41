/**
 * WdsWebDataServerInterfaceBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf130744.23 v111007102553
 */

package com.WdsWebDataServerInterface.www;

public class WdsWebDataServerInterfaceBindingStub extends com.ibm.ws.webservices.engine.client.Stub implements com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort {
    public WdsWebDataServerInterfaceBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
        if (service == null) {
            super.service = new com.ibm.ws.webservices.engine.client.Service();
        }
        else {
            super.service = service;
        }
        super.engine = ((com.ibm.ws.webservices.engine.client.Service) super.service).getEngine();
        initTypeMapping();
        super.cachedEndpoint = endpointURL;
        super.connection = ((com.ibm.ws.webservices.engine.client.Service) super.service).getConnection(endpointURL);
        super.messageContexts = new com.ibm.ws.webservices.engine.MessageContext[7];
    }

    private void initTypeMapping() {
        javax.xml.rpc.encoding.TypeMapping tm = super.getTypeMapping(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
        java.lang.Class javaType = null;
        javax.xml.namespace.QName xmlType = null;
        javax.xml.namespace.QName compQName = null;
        javax.xml.namespace.QName compTypeQName = null;
        com.ibm.ws.webservices.engine.encoding.SerializerFactory sf = null;
        com.ibm.ws.webservices.engine.encoding.DeserializerFactory df = null;
        javaType = com.WdsWebDataServerInterface.www.WdsHttpValue[].class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueCollection");
        compQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "item");
        compTypeQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsHttpValue");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.ArraySerializerFactory.class, javaType, xmlType, compQName, compTypeQName);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.ArrayDeserializerFactory.class, javaType, xmlType, compQName, compTypeQName);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = com.WdsWebDataServerInterface.www.WdsValueDefinition[].class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsAvailableValues");
        compQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "definition");
        compTypeQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueDefinition");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.ArraySerializerFactory.class, javaType, xmlType, compQName, compTypeQName);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.ArrayDeserializerFactory.class, javaType, xmlType, compQName, compTypeQName);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = java.lang.String[].class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueNameList");
        compQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "item");
        compTypeQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.ArraySerializerFactory.class, javaType, xmlType, compQName, compTypeQName);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.ArrayDeserializerFactory.class, javaType, xmlType, compQName, compTypeQName);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = com.WdsWebDataServerInterface.www.WdsHttpValue[][].class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueList");
        compQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "item");
        compTypeQName = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueCollection");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.ArraySerializerFactory.class, javaType, xmlType, compQName, compTypeQName);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.ArrayDeserializerFactory.class, javaType, xmlType, compQName, compTypeQName);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = com.WdsWebDataServerInterface.www.WdsValueDefinition.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueDefinition");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = com.WdsWebDataServerInterface.www.WdsHttpValue.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsHttpValue");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _availableValuesOperation0 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getavailableValuesOperation0() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
          };
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueList"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsAvailableValues"), com.WdsWebDataServerInterface.www.WdsValueDefinition[].class, true, false, false, false, true, false); 
        _returnDesc0.setOption("partName","WdsAvailableValues");
        _returnDesc0.setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsAvailableValues");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _availableValuesOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("availableValues", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "availableValues"), _params0, _returnDesc0, _faults0, "http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/availableValues");
        _availableValuesOperation0.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "availableValuesRequest"));
        _availableValuesOperation0.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        _availableValuesOperation0.setOption("buildNum","cf130744.23");
        _availableValuesOperation0.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        _availableValuesOperation0.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "availableValuesResponse"));
        _availableValuesOperation0.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        _availableValuesOperation0.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _availableValuesOperation0.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return _availableValuesOperation0;

    }

    private int _availableValuesIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getavailableValuesInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_availableValuesIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(WdsWebDataServerInterfaceBindingStub._availableValuesOperation0);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/availableValues");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_availableValuesIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public com.WdsWebDataServerInterface.www.WdsValueDefinition[] availableValues() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getavailableValuesInvoke0(new java.lang.Object[] {}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return (com.WdsWebDataServerInterface.www.WdsValueDefinition[]) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (com.WdsWebDataServerInterface.www.WdsValueDefinition[]) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), com.WdsWebDataServerInterface.www.WdsValueDefinition[].class);
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _currentValueOperation1 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getcurrentValueOperation1() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params1 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
          };
        _params1[0].setOption("partName","string");
        _params1[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc1 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "currentValues"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueCollection"), com.WdsWebDataServerInterface.www.WdsHttpValue[].class, true, false, false, false, true, false); 
        _returnDesc1.setOption("partName","WdsValueCollection");
        _returnDesc1.setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsValueCollection");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults1 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _currentValueOperation1 = new com.ibm.ws.webservices.engine.description.OperationDesc("currentValue", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "currentValue"), _params1, _returnDesc1, _faults1, "http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValue");
        _currentValueOperation1.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "currentValueRequest"));
        _currentValueOperation1.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        _currentValueOperation1.setOption("buildNum","cf130744.23");
        _currentValueOperation1.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        _currentValueOperation1.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "currentValueResponse"));
        _currentValueOperation1.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        _currentValueOperation1.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _currentValueOperation1.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return _currentValueOperation1;

    }

    private int _currentValueIndex1 = 1;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getcurrentValueInvoke1(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_currentValueIndex1];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(WdsWebDataServerInterfaceBindingStub._currentValueOperation1);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValue");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_currentValueIndex1] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public com.WdsWebDataServerInterface.www.WdsHttpValue[] currentValue(java.lang.String valueName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getcurrentValueInvoke1(new java.lang.Object[] {valueName}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return (com.WdsWebDataServerInterface.www.WdsHttpValue[]) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (com.WdsWebDataServerInterface.www.WdsHttpValue[]) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), com.WdsWebDataServerInterface.www.WdsHttpValue[].class);
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _currentValuesOperation2 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getcurrentValuesOperation2() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params2 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valuesList"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueNameList"), java.lang.String[].class, false, false, false, false, true, false), 
          };
        _params2[0].setOption("partName","WdsValueNameList");
        _params2[0].setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsValueNameList");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc2 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "currentValues"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueList"), com.WdsWebDataServerInterface.www.WdsHttpValue[][].class, true, false, false, false, true, false); 
        _returnDesc2.setOption("partName","WdsValueList");
        _returnDesc2.setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsValueList");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults2 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _currentValuesOperation2 = new com.ibm.ws.webservices.engine.description.OperationDesc("currentValues", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "currentValues"), _params2, _returnDesc2, _faults2, "http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValues");
        _currentValuesOperation2.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "currentValuesRequest"));
        _currentValuesOperation2.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        _currentValuesOperation2.setOption("buildNum","cf130744.23");
        _currentValuesOperation2.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        _currentValuesOperation2.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "currentValuesResponse"));
        _currentValuesOperation2.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        _currentValuesOperation2.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _currentValuesOperation2.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return _currentValuesOperation2;

    }

    private int _currentValuesIndex2 = 2;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getcurrentValuesInvoke2(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_currentValuesIndex2];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(WdsWebDataServerInterfaceBindingStub._currentValuesOperation2);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValues");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_currentValuesIndex2] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public com.WdsWebDataServerInterface.www.WdsHttpValue[][] currentValues(java.lang.String[] valuesList) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getcurrentValuesInvoke2(new java.lang.Object[] {valuesList}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return (com.WdsWebDataServerInterface.www.WdsHttpValue[][]) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (com.WdsWebDataServerInterface.www.WdsHttpValue[][]) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), com.WdsWebDataServerInterface.www.WdsHttpValue[][].class);
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _updateStringValueOperation3 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getupdateStringValueOperation3() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params3 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "newValue"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
          };
        _params3[0].setOption("partName","string");
        _params3[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        _params3[1].setOption("partName","string");
        _params3[1].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc3 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, true, false, false, false, true, false); 
        _returnDesc3.setOption("partName","boolean");
        _returnDesc3.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}boolean");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults3 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _updateStringValueOperation3 = new com.ibm.ws.webservices.engine.description.OperationDesc("updateStringValue", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "updateStringValue"), _params3, _returnDesc3, _faults3, "http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValues");
        _updateStringValueOperation3.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateStringValueRequest"));
        _updateStringValueOperation3.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        _updateStringValueOperation3.setOption("buildNum","cf130744.23");
        _updateStringValueOperation3.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        _updateStringValueOperation3.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValueResponse"));
        _updateStringValueOperation3.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        _updateStringValueOperation3.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _updateStringValueOperation3.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return _updateStringValueOperation3;

    }

    private int _updateStringValueIndex3 = 3;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getupdateStringValueInvoke3(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_updateStringValueIndex3];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(WdsWebDataServerInterfaceBindingStub._updateStringValueOperation3);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValues");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_updateStringValueIndex3] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public boolean updateStringValue(java.lang.String valueName, java.lang.String newValue) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getupdateStringValueInvoke3(new java.lang.Object[] {valueName, newValue}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return ((java.lang.Boolean) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue()).booleanValue();
        } catch (java.lang.Exception _exception) {
            return ((java.lang.Boolean) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), boolean.class)).booleanValue();
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _updateIntegerValueOperation4 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getupdateIntegerValueOperation4() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params4 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "newValue"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "integer"), java.math.BigInteger.class, false, false, false, false, true, false), 
          };
        _params4[0].setOption("partName","string");
        _params4[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        _params4[1].setOption("partName","integer");
        _params4[1].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}integer");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc4 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, true, false, false, false, true, false); 
        _returnDesc4.setOption("partName","boolean");
        _returnDesc4.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}boolean");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults4 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _updateIntegerValueOperation4 = new com.ibm.ws.webservices.engine.description.OperationDesc("updateIntegerValue", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "updateIntegerValue"), _params4, _returnDesc4, _faults4, "http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValues");
        _updateIntegerValueOperation4.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateIntegerValueRequest"));
        _updateIntegerValueOperation4.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        _updateIntegerValueOperation4.setOption("buildNum","cf130744.23");
        _updateIntegerValueOperation4.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        _updateIntegerValueOperation4.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValueResponse"));
        _updateIntegerValueOperation4.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        _updateIntegerValueOperation4.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _updateIntegerValueOperation4.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return _updateIntegerValueOperation4;

    }

    private int _updateIntegerValueIndex4 = 4;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getupdateIntegerValueInvoke4(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_updateIntegerValueIndex4];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(WdsWebDataServerInterfaceBindingStub._updateIntegerValueOperation4);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValues");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_updateIntegerValueIndex4] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public boolean updateIntegerValue(java.lang.String valueName, java.math.BigInteger newValue) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getupdateIntegerValueInvoke4(new java.lang.Object[] {valueName, newValue}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return ((java.lang.Boolean) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue()).booleanValue();
        } catch (java.lang.Exception _exception) {
            return ((java.lang.Boolean) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), boolean.class)).booleanValue();
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _updateFloatValueOperation5 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getupdateFloatValueOperation5() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params5 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "newValue"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "float"), float.class, false, false, false, false, true, false), 
          };
        _params5[0].setOption("partName","string");
        _params5[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        _params5[1].setOption("partName","float");
        _params5[1].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}float");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc5 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, true, false, false, false, true, false); 
        _returnDesc5.setOption("partName","boolean");
        _returnDesc5.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}boolean");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults5 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _updateFloatValueOperation5 = new com.ibm.ws.webservices.engine.description.OperationDesc("updateFloatValue", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "updateFloatValue"), _params5, _returnDesc5, _faults5, "http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValues");
        _updateFloatValueOperation5.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateFloatValueRequest"));
        _updateFloatValueOperation5.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        _updateFloatValueOperation5.setOption("buildNum","cf130744.23");
        _updateFloatValueOperation5.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        _updateFloatValueOperation5.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValueResponse"));
        _updateFloatValueOperation5.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        _updateFloatValueOperation5.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _updateFloatValueOperation5.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return _updateFloatValueOperation5;

    }

    private int _updateFloatValueIndex5 = 5;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getupdateFloatValueInvoke5(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_updateFloatValueIndex5];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(WdsWebDataServerInterfaceBindingStub._updateFloatValueOperation5);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/currentValues");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_updateFloatValueIndex5] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public boolean updateFloatValue(java.lang.String valueName, float newValue) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getupdateFloatValueInvoke5(new java.lang.Object[] {valueName, new java.lang.Float(newValue)}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return ((java.lang.Boolean) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue()).booleanValue();
        } catch (java.lang.Exception _exception) {
            return ((java.lang.Boolean) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), boolean.class)).booleanValue();
        }
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _updateValuesOperation6 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getupdateValuesOperation6() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params6 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueCollection"), com.WdsWebDataServerInterface.www.WdsHttpValue[].class, false, false, false, false, true, false), 
          };
        _params6[0].setOption("partName","WdsValueCollection");
        _params6[0].setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsValueCollection");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc6 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, true, false, false, false, true, false); 
        _returnDesc6.setOption("partName","boolean");
        _returnDesc6.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}boolean");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults6 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _updateValuesOperation6 = new com.ibm.ws.webservices.engine.description.OperationDesc("updateValues", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "updateValues"), _params6, _returnDesc6, _faults6, "http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/updateValues");
        _updateValuesOperation6.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValuesRequest"));
        _updateValuesOperation6.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        _updateValuesOperation6.setOption("buildNum","cf130744.23");
        _updateValuesOperation6.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        _updateValuesOperation6.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValueResponse"));
        _updateValuesOperation6.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        _updateValuesOperation6.setUse(com.ibm.ws.webservices.engine.enumtype.Use.LITERAL);
        _updateValuesOperation6.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return _updateValuesOperation6;

    }

    private int _updateValuesIndex6 = 6;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getupdateValuesInvoke6(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_updateValuesIndex6];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(WdsWebDataServerInterfaceBindingStub._updateValuesOperation6);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface/updateValues");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_updateValuesIndex6] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public boolean updateValues(com.WdsWebDataServerInterface.www.WdsHttpValue[] valueName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getupdateValuesInvoke6(new java.lang.Object[] {valueName}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return ((java.lang.Boolean) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue()).booleanValue();
        } catch (java.lang.Exception _exception) {
            return ((java.lang.Boolean) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), boolean.class)).booleanValue();
        }
    }

    private static void _staticInit() {
        _updateFloatValueOperation5 = _getupdateFloatValueOperation5();
        _updateValuesOperation6 = _getupdateValuesOperation6();
        _updateIntegerValueOperation4 = _getupdateIntegerValueOperation4();
        _currentValuesOperation2 = _getcurrentValuesOperation2();
        _availableValuesOperation0 = _getavailableValuesOperation0();
        _updateStringValueOperation3 = _getupdateStringValueOperation3();
        _currentValueOperation1 = _getcurrentValueOperation1();
    }

    static {
       _staticInit();
    }
}
