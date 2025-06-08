/**
 * WheelAlignmentBE_Deser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class WheelAlignmentBE_Deser extends com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializer {
    /**
     * Constructor
     */
    public WheelAlignmentBE_Deser(
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType, 
           com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
        super(_javaType, _xmlType, _typeDesc);
    }
    /**
     * Create instance of java bean
     */
    public void createValue() {
        value = new WheelAlignmentBE();
    }
    protected boolean tryElementSetFromString(javax.xml.namespace.QName qName, java.lang.String strValue) {
        return false;
    }
    protected boolean tryAttributeSetFromString(javax.xml.namespace.QName qName, java.lang.String strValue) {
        return false;
    }
    protected boolean tryElementSetFromObject(javax.xml.namespace.QName qName, java.lang.Object objValue) {
        return false;
    }
    protected boolean tryElementSetFromList(javax.xml.namespace.QName qName, java.util.List listValue) {
        return false;
    }
    private final static javax.xml.namespace.QName QName_0_64 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_TOE_RRR");
    private final static javax.xml.namespace.QName QName_0_74 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "TimeDriveOn");
    private final static javax.xml.namespace.QName QName_0_69 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "LRR_TORQUE");
    private final static javax.xml.namespace.QName QName_0_73 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "TOTAL_TIME");
    private final static javax.xml.namespace.QName QName_0_67 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "LFRT_TORQUE");
    private final static javax.xml.namespace.QName QName_0_49 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_CAM_RRR");
    private final static javax.xml.namespace.QName QName_0_55 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_SYMMETRY");
    private final static javax.xml.namespace.QName QName_0_53 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_TOE_RRR");
    private final static javax.xml.namespace.QName QName_0_42 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "YMDHNS");
    private final static javax.xml.namespace.QName QName_0_84 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Predictor_Mode");
    private final static javax.xml.namespace.QName QName_0_76 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "TimeLeaving");
    private final static javax.xml.namespace.QName QName_0_47 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_CAM_RFRT");
    private final static javax.xml.namespace.QName QName_0_83 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_WOCV_TOE_RFRT");
    private final static javax.xml.namespace.QName QName_0_70 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "RRR_TORQUE");
    private final static javax.xml.namespace.QName QName_0_63 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_TOE_LRR");
    private final static javax.xml.namespace.QName QName_0_51 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_CV_TOE_RFRT");
    private final static javax.xml.namespace.QName QName_0_50 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_CV_TOE_LFRT");
    private final static javax.xml.namespace.QName QName_0_77 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "TimeEmpty");
    private final static javax.xml.namespace.QName QName_0_46 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_CAM_LFRT");
    private final static javax.xml.namespace.QName QName_0_80 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_WOCV_TOE_LFRT");
    private final static javax.xml.namespace.QName QName_0_21 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "ApplicationID");
    private final static javax.xml.namespace.QName QName_0_58 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_CAM_RFRT");
    private final static javax.xml.namespace.QName QName_0_65 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_ST_WHEEL_ANGLE");
    private final static javax.xml.namespace.QName QName_0_79 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FrontTries");
    private final static javax.xml.namespace.QName QName_0_52 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_TOE_LRR");
    private final static javax.xml.namespace.QName QName_0_82 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_WOCV_TOE_LFRT");
    private final static javax.xml.namespace.QName QName_0_66 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_SYMMETRY");
    private final static javax.xml.namespace.QName QName_0_72 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "RR_ATA_TIME");
    private final static javax.xml.namespace.QName QName_0_62 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_CV_TOE_RFRT");
    private final static javax.xml.namespace.QName QName_0_44 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "CAR_IDNO");
    private final static javax.xml.namespace.QName QName_0_48 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_CAM_LRR");
    private final static javax.xml.namespace.QName QName_0_56 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "ACCEPTED_REJECTED");
    private final static javax.xml.namespace.QName QName_0_81 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_WOCV_TOE_RFRT");
    private final static javax.xml.namespace.QName QName_0_43 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "MACHINE_NUM");
    private final static javax.xml.namespace.QName QName_0_60 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_CAM_RRR");
    private final static javax.xml.namespace.QName QName_0_78 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "RearTries");
    private final static javax.xml.namespace.QName QName_0_71 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FRT_ATA_TIME");
    private final static javax.xml.namespace.QName QName_0_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "LocationID");
    private final static javax.xml.namespace.QName QName_0_57 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_CAM_LFRT");
    private final static javax.xml.namespace.QName QName_0_54 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PRST_ST_WHEEL_ANGLE");
    private final static javax.xml.namespace.QName QName_0_61 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_CV_TOE_LFRT");
    private final static javax.xml.namespace.QName QName_0_85 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FrontAuto_ReCheck");
    private final static javax.xml.namespace.QName QName_0_59 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FNAL_CAM_LRR");
    private final static javax.xml.namespace.QName QName_0_75 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "TimeSetup");
    private final static javax.xml.namespace.QName QName_0_68 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "RFRT_TORQUE");
    private final static javax.xml.namespace.QName QName_0_45 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "BAR_CODE");
}
