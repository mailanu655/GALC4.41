/**
 * HeadLightAlignmentBE_Deser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class HeadLightAlignmentBE_Deser extends com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializer {
    /**
     * Constructor
     */
    public HeadLightAlignmentBE_Deser(
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType, 
           com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
        super(_javaType, _xmlType, _typeDesc);
    }
    /**
     * Create instance of java bean
     */
    public void createValue() {
        value = new HeadLightAlignmentBE();
    }
    protected boolean tryElementSetFromString(javax.xml.namespace.QName qName, java.lang.String strValue) {
        if (qName==QName_0_0) {
          ((HeadLightAlignmentBE)value).setCollectDateTime(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseDateTimeToCalendar(strValue));
          return true;}
        else if (qName==QName_0_1) {
          ((HeadLightAlignmentBE)value).setMachineNo(strValue);
          return true;}
        else if (qName==QName_0_2) {
          ((HeadLightAlignmentBE)value).setMachineSetting(strValue);
          return true;}
        else if (qName==QName_0_3) {
          ((HeadLightAlignmentBE)value).setVIN(strValue);
          return true;}
        else if (qName==QName_0_4) {
          ((HeadLightAlignmentBE)value).setPreset_Left_HeadLight_Vertical_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_5) {
          ((HeadLightAlignmentBE)value).setPreset_Left_HeadLight_Horizontal_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_6) {
          ((HeadLightAlignmentBE)value).setPreset_Right_HeadLight_Vertical_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_7) {
          ((HeadLightAlignmentBE)value).setPreset_Right_HeadLight_Horizontal_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_8) {
          ((HeadLightAlignmentBE)value).setPreset_Left_Fog_Vertical_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_9) {
          ((HeadLightAlignmentBE)value).setPreset_Left_Fog_Horizontal_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_10) {
          ((HeadLightAlignmentBE)value).setPreset_Right_Fog_Vertical_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_11) {
          ((HeadLightAlignmentBE)value).setPreset_Right_Fog_Horizontal_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_12) {
          ((HeadLightAlignmentBE)value).setFinal_Left_HeadLight_Vertical_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_13) {
          ((HeadLightAlignmentBE)value).setFinal_Left_HeadLight_Horizontal_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_14) {
          ((HeadLightAlignmentBE)value).setFinal_Right_HeadLight_Vertical_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_15) {
          ((HeadLightAlignmentBE)value).setFinal_Right_HeadLight_Horizontal_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_16) {
          ((HeadLightAlignmentBE)value).setFinal_Left_Fog_Vertical_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_17) {
          ((HeadLightAlignmentBE)value).setFinal_Left_Fog_Horizontal_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_18) {
          ((HeadLightAlignmentBE)value).setFinal_Right_Fog_Vertical_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_19) {
          ((HeadLightAlignmentBE)value).setFinal_Right_Fog_Horizontal_Qty(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_20) {
          ((HeadLightAlignmentBE)value).setLocationID(strValue);
          return true;}
        else if (qName==QName_0_21) {
          ((HeadLightAlignmentBE)value).setApplicationID(strValue);
          return true;}
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
    private final static javax.xml.namespace.QName QName_0_14 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Right_HeadLight_Vertical_Qty");
    private final static javax.xml.namespace.QName QName_0_21 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "ApplicationID");
    private final static javax.xml.namespace.QName QName_0_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Preset_Left_HeadLight_Horizontal_Qty");
    private final static javax.xml.namespace.QName QName_0_6 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Preset_Right_HeadLight_Vertical_Qty");
    private final static javax.xml.namespace.QName QName_0_9 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Preset_Left_Fog_Horizontal_Qty");
    private final static javax.xml.namespace.QName QName_0_19 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Right_Fog_Horizontal_Qty");
    private final static javax.xml.namespace.QName QName_0_3 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "VIN");
    private final static javax.xml.namespace.QName QName_0_2 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "MachineSetting");
    private final static javax.xml.namespace.QName QName_0_7 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Preset_Right_HeadLight_Horizontal_Qty");
    private final static javax.xml.namespace.QName QName_0_8 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Preset_Left_Fog_Vertical_Qty");
    private final static javax.xml.namespace.QName QName_0_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "LocationID");
    private final static javax.xml.namespace.QName QName_0_10 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Preset_Right_Fog_Vertical_Qty");
    private final static javax.xml.namespace.QName QName_0_13 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Left_HeadLight_Horizontal_Qty");
    private final static javax.xml.namespace.QName QName_0_15 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Right_HeadLight_Horizontal_Qty");
    private final static javax.xml.namespace.QName QName_0_1 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "MachineNo");
    private final static javax.xml.namespace.QName QName_0_12 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Left_HeadLight_Vertical_Qty");
    private final static javax.xml.namespace.QName QName_0_17 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Left_Fog_Horizontal_Qty");
    private final static javax.xml.namespace.QName QName_0_18 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Right_Fog_Vertical_Qty");
    private final static javax.xml.namespace.QName QName_0_11 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Preset_Right_Fog_Horizontal_Qty");
    private final static javax.xml.namespace.QName QName_0_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Preset_Left_HeadLight_Vertical_Qty");
    private final static javax.xml.namespace.QName QName_0_0 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "CollectDateTime");
    private final static javax.xml.namespace.QName QName_0_16 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Left_Fog_Vertical_Qty");
}
