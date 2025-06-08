/**
 * NCATCollectorInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class NCATCollectorInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {

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
        inner0.put("headLightAlignmentInsert", list0);

        com.ibm.ws.webservices.engine.description.OperationDesc headLightAlignmentInsert0Op = _headLightAlignmentInsert0Op();
        list0.add(headLightAlignmentInsert0Op);

        java.util.List list1 = new java.util.ArrayList();
        inner0.put("kemkraftDataInsert", list1);

        com.ibm.ws.webservices.engine.description.OperationDesc kemkraftDataInsert1Op = _kemkraftDataInsert1Op();
        list1.add(kemkraftDataInsert1Op);

        java.util.List list2 = new java.util.ArrayList();
        inner0.put("wheelAlignmentInsert", list2);

        com.ibm.ws.webservices.engine.description.OperationDesc wheelAlignmentInsert2Op = _wheelAlignmentInsert2Op();
        list2.add(wheelAlignmentInsert2Op);

        operationDescriptions.put("NCATCollectorSoap",inner0);
        operationDescriptions.put("NCATCollectorSoap12",inner0);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _headLightAlignmentInsert0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc headLightAlignmentInsert0Op = null;
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
        headLightAlignmentInsert0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("headLightAlignmentInsert", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentInsert"), _params0, _returnDesc0, _faults0, null);
        headLightAlignmentInsert0Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollectorSoap"));
        headLightAlignmentInsert0Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentInsertSoapOut"));
        headLightAlignmentInsert0Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollector"));
        headLightAlignmentInsert0Op.setOption("buildNum","cf371106.07");
        headLightAlignmentInsert0Op.setOption("ResponseNamespace","http://com.honda.ham/NCATCollector");
        headLightAlignmentInsert0Op.setOption("targetNamespace","http://com.honda.ham/NCATCollector");
        headLightAlignmentInsert0Op.setOption("ResponseLocalPart","HeadLightAlignmentInsertResponse");
        headLightAlignmentInsert0Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentInsertSoapIn"));
        headLightAlignmentInsert0Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return headLightAlignmentInsert0Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _kemkraftDataInsert1Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc kemkraftDataInsert1Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "kemkraftDataBE"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataBE"), ham.honda.com.KemkraftDataBE.class, false, false, false, true, true, false), 
          };
        _params0[0].setOption("inputPosition","0");
        _params0[0].setOption("partQNameString","{http://com.honda.ham/NCATCollector}KemkraftDataBE");
        _params0[0].setOption("partName","KemkraftDataBE");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataInsertResult"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "DatabaseResult"), ham.honda.com.DatabaseResult.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://com.honda.ham/NCATCollector}DatabaseResult");
        _returnDesc0.setOption("partName","DatabaseResult");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        kemkraftDataInsert1Op = new com.ibm.ws.webservices.engine.description.OperationDesc("kemkraftDataInsert", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataInsert"), _params0, _returnDesc0, _faults0, null);
        kemkraftDataInsert1Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollectorSoap"));
        kemkraftDataInsert1Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataInsertSoapOut"));
        kemkraftDataInsert1Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollector"));
        kemkraftDataInsert1Op.setOption("buildNum","cf371106.07");
        kemkraftDataInsert1Op.setOption("ResponseNamespace","http://com.honda.ham/NCATCollector");
        kemkraftDataInsert1Op.setOption("targetNamespace","http://com.honda.ham/NCATCollector");
        kemkraftDataInsert1Op.setOption("ResponseLocalPart","KemkraftDataInsertResponse");
        kemkraftDataInsert1Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataInsertSoapIn"));
        kemkraftDataInsert1Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return kemkraftDataInsert1Op;

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _wheelAlignmentInsert2Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc wheelAlignmentInsert2Op = null;
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "wheelAlignmentBE"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentBE"), ham.honda.com.WheelAlignmentBE.class, false, false, false, true, true, false), 
          };
        _params0[0].setOption("inputPosition","0");
        _params0[0].setOption("partQNameString","{http://com.honda.ham/NCATCollector}WheelAlignmentBE");
        _params0[0].setOption("partName","WheelAlignmentBE");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentInsertResult"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "DatabaseResult"), ham.honda.com.DatabaseResult.class, true, false, false, false, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://com.honda.ham/NCATCollector}DatabaseResult");
        _returnDesc0.setOption("partName","DatabaseResult");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        wheelAlignmentInsert2Op = new com.ibm.ws.webservices.engine.description.OperationDesc("wheelAlignmentInsert", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentInsert"), _params0, _returnDesc0, _faults0, null);
        wheelAlignmentInsert2Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollectorSoap"));
        wheelAlignmentInsert2Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentInsertSoapOut"));
        wheelAlignmentInsert2Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "NCATCollector"));
        wheelAlignmentInsert2Op.setOption("buildNum","cf371106.07");
        wheelAlignmentInsert2Op.setOption("ResponseNamespace","http://com.honda.ham/NCATCollector");
        wheelAlignmentInsert2Op.setOption("targetNamespace","http://com.honda.ham/NCATCollector");
        wheelAlignmentInsert2Op.setOption("ResponseLocalPart","WheelAlignmentInsertResponse");
        wheelAlignmentInsert2Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentInsertSoapIn"));
        wheelAlignmentInsert2Op.setStyle(com.ibm.ws.webservices.engine.enumtype.Style.WRAPPED);
        return wheelAlignmentInsert2Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "HeadLightAlignmentBE"),
                         ham.honda.com.HeadLightAlignmentBE.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "DatabaseResult"),
                         ham.honda.com.DatabaseResult.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "KemkraftDataBE"),
                         ham.honda.com.KemkraftDataBE.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "WheelAlignmentBE"),
                         ham.honda.com.WheelAlignmentBE.class);

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
