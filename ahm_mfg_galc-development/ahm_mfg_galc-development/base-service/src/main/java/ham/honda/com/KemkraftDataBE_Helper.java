/**
 * KemkraftDataBE_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class KemkraftDataBE_Helper {
    // Type metadata
    private static final com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(KemkraftDataBE.class);

    static {
        typeDesc.setOption("buildNum","cf371106.07");
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("collectDateTime");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "CollectDateTime"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("plantCode");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PlantCode"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("VIN");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "VIN"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("machineNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "MachineNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("shiftNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "ShiftNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("tool_ID");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Tool_ID"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("start_Time_0_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Start_Time_0_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("angle_Result_0_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Angle_Result_0_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("angle_Status_0_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Angle_Status_0_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("force_Result_0_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Force_Result_0_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("force_Status_0_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Force_Status_0_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("start_Time_3_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Start_Time_3_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("seconds_Difference");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Seconds_Difference"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("angle_Result_3_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Angle_Result_3_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("angle_Status_3_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Angle_Status_3_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("force_Result_3_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Force_Result_3_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "decimal"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("force_Status_3_Percent");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Force_Status_3_Percent"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("lastUpdated_DateTime");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "LastUpdated_DateTime"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("locationID");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "LocationID"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("applicationID");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "ApplicationID"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
    };

    /**
     * Return type metadata object
     */
    public static com.ibm.ws.webservices.engine.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class javaType,  
           javax.xml.namespace.QName xmlType) {
        return 
          new KemkraftDataBE_Ser(
            javaType, xmlType, typeDesc);
    };

    /**
     * Get Custom Deserializer
     */
    public static com.ibm.ws.webservices.engine.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class javaType,  
           javax.xml.namespace.QName xmlType) {
        return 
          new KemkraftDataBE_Deser(
            javaType, xmlType, typeDesc);
    };

}
