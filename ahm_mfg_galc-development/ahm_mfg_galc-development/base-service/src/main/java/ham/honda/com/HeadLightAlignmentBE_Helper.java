/**
 * HeadLightAlignmentBE_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class HeadLightAlignmentBE_Helper {
    // Type metadata
    private static final com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(HeadLightAlignmentBE.class);

    static {
        typeDesc.setOption("buildNum","cf371106.07");
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("collectDateTime");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "CollectDateTime"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("machineNo");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "MachineNo"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("machineSetting");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "MachineSetting"));
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
        field.setFieldName("preset_Left_HeadLight_Vertical_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Preset_Left_HeadLight_Vertical_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("preset_Left_HeadLight_Horizontal_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Preset_Left_HeadLight_Horizontal_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("preset_Right_HeadLight_Vertical_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Preset_Right_HeadLight_Vertical_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("preset_Right_HeadLight_Horizontal_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Preset_Right_HeadLight_Horizontal_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("preset_Left_Fog_Vertical_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Preset_Left_Fog_Vertical_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("preset_Left_Fog_Horizontal_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Preset_Left_Fog_Horizontal_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("preset_Right_Fog_Vertical_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Preset_Right_Fog_Vertical_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("preset_Right_Fog_Horizontal_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Preset_Right_Fog_Horizontal_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("final_Left_HeadLight_Vertical_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Final_Left_HeadLight_Vertical_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("final_Left_HeadLight_Horizontal_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Final_Left_HeadLight_Horizontal_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("final_Right_HeadLight_Vertical_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Final_Right_HeadLight_Vertical_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("final_Right_HeadLight_Horizontal_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Final_Right_HeadLight_Horizontal_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("final_Left_Fog_Vertical_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Final_Left_Fog_Vertical_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("final_Left_Fog_Horizontal_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Final_Left_Fog_Horizontal_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("final_Right_Fog_Vertical_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Final_Right_Fog_Vertical_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("final_Right_Fog_Horizontal_Qty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Final_Right_Fog_Horizontal_Qty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
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
          new HeadLightAlignmentBE_Ser(
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
          new HeadLightAlignmentBE_Deser(
            javaType, xmlType, typeDesc);
    };

}
