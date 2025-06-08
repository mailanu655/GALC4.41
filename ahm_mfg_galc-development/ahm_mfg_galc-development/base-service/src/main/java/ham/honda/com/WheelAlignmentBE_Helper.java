/**
 * WheelAlignmentBE_Helper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class WheelAlignmentBE_Helper {
    // Type metadata
    private static final com.ibm.ws.webservices.engine.description.TypeDesc typeDesc =
        new com.ibm.ws.webservices.engine.description.TypeDesc(WheelAlignmentBE.class);

    static {
        typeDesc.setOption("buildNum","cf371106.07");
        com.ibm.ws.webservices.engine.description.FieldDesc field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("YMDHNS");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "YMDHNS"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("MACHINE_NUM");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "MACHINE_NUM"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("CAR_IDNO");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "CAR_IDNO"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("BAR_CODE");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "BAR_CODE"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_CAM_LFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_CAM_LFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_CAM_RFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_CAM_RFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_CAM_LRR");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_CAM_LRR"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_CAM_RRR");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_CAM_RRR"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_CV_TOE_LFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_CV_TOE_LFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_CV_TOE_RFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_CV_TOE_RFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_TOE_LRR");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_TOE_LRR"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_TOE_RRR");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_TOE_RRR"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_ST_WHEEL_ANGLE");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_ST_WHEEL_ANGLE"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_SYMMETRY");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_SYMMETRY"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("ACCEPTED_REJECTED");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "ACCEPTED_REJECTED"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_CAM_LFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_CAM_LFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_CAM_RFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_CAM_RFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_CAM_LRR");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_CAM_LRR"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_CAM_RRR");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_CAM_RRR"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_CV_TOE_LFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_CV_TOE_LFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_CV_TOE_RFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_CV_TOE_RFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_TOE_LRR");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_TOE_LRR"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_TOE_RRR");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_TOE_RRR"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_ST_WHEEL_ANGLE");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_ST_WHEEL_ANGLE"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_SYMMETRY");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_SYMMETRY"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("LFRT_TORQUE");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "LFRT_TORQUE"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("RFRT_TORQUE");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "RFRT_TORQUE"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("LRR_TORQUE");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "LRR_TORQUE"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("RRR_TORQUE");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "RRR_TORQUE"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FRT_ATA_TIME");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FRT_ATA_TIME"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("RR_ATA_TIME");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "RR_ATA_TIME"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("TOTAL_TIME");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "TOTAL_TIME"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("timeDriveOn");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "TimeDriveOn"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("timeSetup");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "TimeSetup"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("timeLeaving");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "TimeLeaving"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("timeEmpty");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "TimeEmpty"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("rearTries");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "RearTries"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("frontTries");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FrontTries"));
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
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_WOCV_TOE_LFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_WOCV_TOE_LFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("PRST_WOCV_TOE_RFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "PRST_WOCV_TOE_RFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_WOCV_TOE_LFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_WOCV_TOE_LFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("FNAL_WOCV_TOE_RFRT");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FNAL_WOCV_TOE_RFRT"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("predictor_Mode");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "Predictor_Mode"));
        field.setXmlType(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"));
        field.setMinOccursIs0(true);
        typeDesc.addFieldDesc(field);
        field = new com.ibm.ws.webservices.engine.description.ElementDesc();
        field.setFieldName("frontAuto_ReCheck");
        field.setXmlName(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://com.honda.ham/NCATCollector", "FrontAuto_ReCheck"));
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
          new WheelAlignmentBE_Ser(
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
          new WheelAlignmentBE_Deser(
            javaType, xmlType, typeDesc);
    };

}
