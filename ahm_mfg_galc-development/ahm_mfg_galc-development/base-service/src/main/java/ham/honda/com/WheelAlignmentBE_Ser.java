/**
 * WheelAlignmentBE_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class WheelAlignmentBE_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public WheelAlignmentBE_Ser(
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType, 
           com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
        super(_javaType, _xmlType, _typeDesc);
    }
    public void serialize(
        javax.xml.namespace.QName name,
        org.xml.sax.Attributes attributes,
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        context.startElement(name, addAttributes(attributes, value, context));
        addElements(value, context);
        context.endElement();
    }
    protected org.xml.sax.Attributes addAttributes(
        org.xml.sax.Attributes attributes,
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
           javax.xml.namespace.QName
           elemQName = QName_0_42;
           context.qName2String(elemQName, true);
           elemQName = QName_0_43;
           context.qName2String(elemQName, true);
           elemQName = QName_0_44;
           context.qName2String(elemQName, true);
           elemQName = QName_0_45;
           context.qName2String(elemQName, true);
           elemQName = QName_0_46;
           context.qName2String(elemQName, true);
           elemQName = QName_0_47;
           context.qName2String(elemQName, true);
           elemQName = QName_0_48;
           context.qName2String(elemQName, true);
           elemQName = QName_0_49;
           context.qName2String(elemQName, true);
           elemQName = QName_0_50;
           context.qName2String(elemQName, true);
           elemQName = QName_0_51;
           context.qName2String(elemQName, true);
           elemQName = QName_0_52;
           context.qName2String(elemQName, true);
           elemQName = QName_0_53;
           context.qName2String(elemQName, true);
           elemQName = QName_0_54;
           context.qName2String(elemQName, true);
           elemQName = QName_0_55;
           context.qName2String(elemQName, true);
           elemQName = QName_0_56;
           context.qName2String(elemQName, true);
           elemQName = QName_0_57;
           context.qName2String(elemQName, true);
           elemQName = QName_0_58;
           context.qName2String(elemQName, true);
           elemQName = QName_0_59;
           context.qName2String(elemQName, true);
           elemQName = QName_0_60;
           context.qName2String(elemQName, true);
           elemQName = QName_0_61;
           context.qName2String(elemQName, true);
           elemQName = QName_0_62;
           context.qName2String(elemQName, true);
           elemQName = QName_0_63;
           context.qName2String(elemQName, true);
           elemQName = QName_0_64;
           context.qName2String(elemQName, true);
           elemQName = QName_0_65;
           context.qName2String(elemQName, true);
           elemQName = QName_0_66;
           context.qName2String(elemQName, true);
           elemQName = QName_0_67;
           context.qName2String(elemQName, true);
           elemQName = QName_0_68;
           context.qName2String(elemQName, true);
           elemQName = QName_0_69;
           context.qName2String(elemQName, true);
           elemQName = QName_0_70;
           context.qName2String(elemQName, true);
           elemQName = QName_0_71;
           context.qName2String(elemQName, true);
           elemQName = QName_0_72;
           context.qName2String(elemQName, true);
           elemQName = QName_0_73;
           context.qName2String(elemQName, true);
           elemQName = QName_0_74;
           context.qName2String(elemQName, true);
           elemQName = QName_0_75;
           context.qName2String(elemQName, true);
           elemQName = QName_0_76;
           context.qName2String(elemQName, true);
           elemQName = QName_0_77;
           context.qName2String(elemQName, true);
           elemQName = QName_0_78;
           context.qName2String(elemQName, true);
           elemQName = QName_0_79;
           context.qName2String(elemQName, true);
           elemQName = QName_0_20;
           context.qName2String(elemQName, true);
           elemQName = QName_0_21;
           context.qName2String(elemQName, true);
           elemQName = QName_0_80;
           context.qName2String(elemQName, true);
           elemQName = QName_0_81;
           context.qName2String(elemQName, true);
           elemQName = QName_0_82;
           context.qName2String(elemQName, true);
           elemQName = QName_0_83;
           context.qName2String(elemQName, true);
           elemQName = QName_0_84;
           context.qName2String(elemQName, true);
           elemQName = QName_0_85;
           context.qName2String(elemQName, true);
        return attributes;
    }
    protected void addElements(
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        WheelAlignmentBE bean = (WheelAlignmentBE) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_0_42;
          propValue = bean.getYMDHNS();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_22,
              true,null,context);
          propQName = QName_0_43;
          propValue = bean.getMACHINE_NUM();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_44;
          propValue = bean.getCAR_IDNO();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_45;
          propValue = bean.getBAR_CODE();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_46;
          propValue = new java.lang.Double(bean.getPRST_CAM_LFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_47;
          propValue = new java.lang.Double(bean.getPRST_CAM_RFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_48;
          propValue = new java.lang.Double(bean.getPRST_CAM_LRR());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_49;
          propValue = new java.lang.Double(bean.getPRST_CAM_RRR());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_50;
          propValue = new java.lang.Double(bean.getPRST_CV_TOE_LFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_51;
          propValue = new java.lang.Double(bean.getPRST_CV_TOE_RFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_52;
          propValue = new java.lang.Double(bean.getPRST_TOE_LRR());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_53;
          propValue = new java.lang.Double(bean.getPRST_TOE_RRR());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_54;
          propValue = new java.lang.Double(bean.getPRST_ST_WHEEL_ANGLE());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_55;
          propValue = new java.lang.Double(bean.getPRST_SYMMETRY());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_56;
          propValue = new java.lang.Boolean(bean.isACCEPTED_REJECTED());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_86,
              true,null,context);
          propQName = QName_0_57;
          propValue = new java.lang.Double(bean.getFNAL_CAM_LFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_58;
          propValue = new java.lang.Double(bean.getFNAL_CAM_RFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_59;
          propValue = new java.lang.Double(bean.getFNAL_CAM_LRR());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_60;
          propValue = new java.lang.Double(bean.getFNAL_CAM_RRR());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_61;
          propValue = new java.lang.Double(bean.getFNAL_CV_TOE_LFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_62;
          propValue = new java.lang.Double(bean.getFNAL_CV_TOE_RFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_63;
          propValue = new java.lang.Double(bean.getFNAL_TOE_LRR());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_64;
          propValue = new java.lang.Double(bean.getFNAL_TOE_RRR());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_65;
          propValue = new java.lang.Double(bean.getFNAL_ST_WHEEL_ANGLE());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_66;
          propValue = new java.lang.Double(bean.getFNAL_SYMMETRY());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_67;
          propValue = new java.lang.Double(bean.getLFRT_TORQUE());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_68;
          propValue = new java.lang.Double(bean.getRFRT_TORQUE());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_69;
          propValue = new java.lang.Double(bean.getLRR_TORQUE());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_70;
          propValue = new java.lang.Double(bean.getRRR_TORQUE());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_71;
          propValue = new java.lang.Double(bean.getFRT_ATA_TIME());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_72;
          propValue = new java.lang.Double(bean.getRR_ATA_TIME());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_73;
          propValue = new java.lang.Double(bean.getTOTAL_TIME());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_74;
          propValue = new java.lang.Double(bean.getTimeDriveOn());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_75;
          propValue = new java.lang.Double(bean.getTimeSetup());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_76;
          propValue = new java.lang.Double(bean.getTimeLeaving());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_77;
          propValue = new java.lang.Double(bean.getTimeEmpty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_78;
          propValue = new java.lang.Double(bean.getRearTries());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_79;
          propValue = new java.lang.Double(bean.getFrontTries());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_20;
          propValue = bean.getLocationID();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_21;
          propValue = bean.getApplicationID();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_80;
          propValue = new java.lang.Double(bean.getPRST_WOCV_TOE_LFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_81;
          propValue = new java.lang.Double(bean.getPRST_WOCV_TOE_RFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_82;
          propValue = new java.lang.Double(bean.getFNAL_WOCV_TOE_LFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_83;
          propValue = new java.lang.Double(bean.getFNAL_WOCV_TOE_RFRT());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_84;
          propValue = bean.getPredictor_Mode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_85;
          propValue = bean.getFrontAuto_ReCheck();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
        }
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
    private final static javax.xml.namespace.QName QName_1_24 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "double");
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
    private final static javax.xml.namespace.QName QName_1_23 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
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
    private final static javax.xml.namespace.QName QName_1_86 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "boolean");
    private final static javax.xml.namespace.QName QName_0_71 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "FRT_ATA_TIME");
    private final static javax.xml.namespace.QName QName_0_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "LocationID");
    private final static javax.xml.namespace.QName QName_1_22 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
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
