/**
 * WdsWebDataServerInterfaceInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf130744.23 v111007102553
 */

package com.WdsWebDataServerInterface.www;

public class WdsWebDataServerInterfaceInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {

     // This is the WdsWebDataServerInterface service.

    private static java.util.Map operationDescriptions;
    private static java.util.Map typeMappings;

    static {
         initOperationDescriptions();
         initTypeMappings();
    }

    private static void initOperationDescriptions() { 
        operationDescriptions = new java.util.HashMap();

        java.util.Map inner0 = new java.util.HashMap();

        java.util.List list0 = new java.util.ArrayList();
        inner0.put("availableValues", list0);

        com.ibm.ws.webservices.engine.description.OperationDesc availableValues0Op = _availableValues0Op();
        list0.add(availableValues0Op);

        java.util.List list1 = new java.util.ArrayList();
        inner0.put("currentValue", list1);

        com.ibm.ws.webservices.engine.description.OperationDesc currentValue1Op = _currentValue1Op();
        list1.add(currentValue1Op);

        java.util.List list2 = new java.util.ArrayList();
        inner0.put("currentValues", list2);

        com.ibm.ws.webservices.engine.description.OperationDesc currentValues2Op = _currentValues2Op();
        list2.add(currentValues2Op);

        java.util.List list3 = new java.util.ArrayList();
        inner0.put("updateFloatValue", list3);

        com.ibm.ws.webservices.engine.description.OperationDesc updateFloatValue3Op = _updateFloatValue3Op();
        list3.add(updateFloatValue3Op);

        java.util.List list4 = new java.util.ArrayList();
        inner0.put("updateIntegerValue", list4);

        com.ibm.ws.webservices.engine.description.OperationDesc updateIntegerValue4Op = _updateIntegerValue4Op();
        list4.add(updateIntegerValue4Op);

        java.util.List list5 = new java.util.ArrayList();
        inner0.put("updateStringValue", list5);

        com.ibm.ws.webservices.engine.description.OperationDesc updateStringValue5Op = _updateStringValue5Op();
        list5.add(updateStringValue5Op);

        java.util.List list6 = new java.util.ArrayList();
        inner0.put("updateValues", list6);

        com.ibm.ws.webservices.engine.description.OperationDesc updateValues6Op = _updateValues6Op();
        list6.add(updateValues6Op);

        operationDescriptions.put("WdsWebDataServerInterfacePort",inner0);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _availableValues0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc availableValues0Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
          };
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueList"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsAvailableValues"), com.WdsWebDataServerInterface.www.WdsValueDefinition[].class, true, false, false, false, true, false); 
        _returnDesc0.setOption("partName","WdsAvailableValues");
        _returnDesc0.setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsAvailableValues");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        availableValues0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("availableValues", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "availableValues"), _params0, _returnDesc0, _faults0, null);
        availableValues0Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "availableValuesRequest"));
        availableValues0Op.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        availableValues0Op.setOption("buildNum","cf130744.23");
        availableValues0Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        availableValues0Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "availableValuesResponse"));
        availableValues0Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        availableValues0Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return availableValues0Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _currentValue1Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc currentValue1Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("partName","string");
        _params0[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "currentValues"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueCollection"), com.WdsWebDataServerInterface.www.WdsHttpValue[].class, true, false, false, false, true, false); 
        _returnDesc0.setOption("partName","WdsValueCollection");
        _returnDesc0.setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsValueCollection");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        currentValue1Op = new com.ibm.ws.webservices.engine.description.OperationDesc("currentValue", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "currentValue"), _params0, _returnDesc0, _faults0, null);
        currentValue1Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "currentValueRequest"));
        currentValue1Op.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        currentValue1Op.setOption("buildNum","cf130744.23");
        currentValue1Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        currentValue1Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "currentValueResponse"));
        currentValue1Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        currentValue1Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return currentValue1Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _currentValues2Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc currentValues2Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valuesList"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueNameList"), java.lang.String[].class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("partName","WdsValueNameList");
        _params0[0].setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsValueNameList");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "currentValues"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueList"), com.WdsWebDataServerInterface.www.WdsHttpValue[][].class, true, false, false, false, true, false); 
        _returnDesc0.setOption("partName","WdsValueList");
        _returnDesc0.setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsValueList");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        currentValues2Op = new com.ibm.ws.webservices.engine.description.OperationDesc("currentValues", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "currentValues"), _params0, _returnDesc0, _faults0, null);
        currentValues2Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "currentValuesRequest"));
        currentValues2Op.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        currentValues2Op.setOption("buildNum","cf130744.23");
        currentValues2Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        currentValues2Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "currentValuesResponse"));
        currentValues2Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        currentValues2Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return currentValues2Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _updateFloatValue3Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc updateFloatValue3Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "newValue"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "float"), float.class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("partName","string");
        _params0[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        _params0[1].setOption("partName","float");
        _params0[1].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}float");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("partName","boolean");
        _returnDesc0.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}boolean");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        updateFloatValue3Op = new com.ibm.ws.webservices.engine.description.OperationDesc("updateFloatValue", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "updateFloatValue"), _params0, _returnDesc0, _faults0, null);
        updateFloatValue3Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateFloatValueRequest"));
        updateFloatValue3Op.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        updateFloatValue3Op.setOption("buildNum","cf130744.23");
        updateFloatValue3Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        updateFloatValue3Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValueResponse"));
        updateFloatValue3Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        updateFloatValue3Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return updateFloatValue3Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _updateIntegerValue4Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc updateIntegerValue4Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "newValue"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "integer"), java.math.BigInteger.class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("partName","string");
        _params0[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        _params0[1].setOption("partName","integer");
        _params0[1].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}integer");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("partName","boolean");
        _returnDesc0.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}boolean");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        updateIntegerValue4Op = new com.ibm.ws.webservices.engine.description.OperationDesc("updateIntegerValue", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "updateIntegerValue"), _params0, _returnDesc0, _faults0, null);
        updateIntegerValue4Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateIntegerValueRequest"));
        updateIntegerValue4Op.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        updateIntegerValue4Op.setOption("buildNum","cf130744.23");
        updateIntegerValue4Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        updateIntegerValue4Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValueResponse"));
        updateIntegerValue4Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        updateIntegerValue4Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return updateIntegerValue4Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _updateStringValue5Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc updateStringValue5Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "newValue"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("partName","string");
        _params0[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        _params0[1].setOption("partName","string");
        _params0[1].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("partName","boolean");
        _returnDesc0.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}boolean");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        updateStringValue5Op = new com.ibm.ws.webservices.engine.description.OperationDesc("updateStringValue", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "updateStringValue"), _params0, _returnDesc0, _faults0, null);
        updateStringValue5Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateStringValueRequest"));
        updateStringValue5Op.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        updateStringValue5Op.setOption("buildNum","cf130744.23");
        updateStringValue5Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        updateStringValue5Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValueResponse"));
        updateStringValue5Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        updateStringValue5Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return updateStringValue5Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _updateValues6Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc updateValues6Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "valueName"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueCollection"), com.WdsWebDataServerInterface.www.WdsHttpValue[].class, false, false, false, false, true, false), 
          };
        _params0[0].setOption("partName","WdsValueCollection");
        _params0[0].setOption("partQNameString","{http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface}WdsValueCollection");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "status"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("partName","boolean");
        _returnDesc0.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}boolean");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        updateValues6Op = new com.ibm.ws.webservices.engine.description.OperationDesc("updateValues", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("urn:WdsWebDataServerInterface", "updateValues"), _params0, _returnDesc0, _faults0, null);
        updateValues6Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValuesRequest"));
        updateValues6Op.setOption("targetNamespace","http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl");
        updateValues6Op.setOption("buildNum","cf130744.23");
        updateValues6Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-impl", "WdsWebDataServerInterface"));
        updateValues6Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "updateValueResponse"));
        updateValues6Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface-interface", "WdsWebDataServerInterfacePort"));
        updateValues6Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.RPC);
        return updateValues6Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueCollection"),
                         com.WdsWebDataServerInterface.www.WdsHttpValue[].class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsAvailableValues"),
                         com.WdsWebDataServerInterface.www.WdsValueDefinition[].class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueNameList"),
                         java.lang.String[].class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueList"),
                         com.WdsWebDataServerInterface.www.WdsHttpValue[][].class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsValueDefinition"),
                         com.WdsWebDataServerInterface.www.WdsValueDefinition.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.WdsWebDataServerInterface.com/WdsWebDataServerInterface", "WdsHttpValue"),
                         com.WdsWebDataServerInterface.www.WdsHttpValue.class);

        typeMappings = java.util.Collections.unmodifiableMap(typeMappings);
    }

    public java.util.Map getTypeMappings() {
        return typeMappings;
    }

    public Class getJavaType(javax.xml.namespace.QName xmlName) {
        return (Class) typeMappings.get(xmlName);
    }

    public java.util.Map getOperationDescriptions(String portName) {
        return (java.util.Map) operationDescriptions.get(portName);
    }

    public java.util.List getOperationDescriptions(String portName, String operationName) {
        java.util.Map map = (java.util.Map) operationDescriptions.get(portName);
        if (map != null) {
            return (java.util.List) map.get(operationName);
        }
        return null;
    }

}
