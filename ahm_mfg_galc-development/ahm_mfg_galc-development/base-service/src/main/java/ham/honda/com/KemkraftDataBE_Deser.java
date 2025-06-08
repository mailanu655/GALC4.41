/**
 * KemkraftDataBE_Deser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class KemkraftDataBE_Deser extends com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializer {
    /**
     * Constructor
     */
    public KemkraftDataBE_Deser(
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType, 
           com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
        super(_javaType, _xmlType, _typeDesc);
    }
    /**
     * Create instance of java bean
     */
    public void createValue() {
        value = new KemkraftDataBE();
    }
    protected boolean tryElementSetFromString(javax.xml.namespace.QName qName, java.lang.String strValue) {
        if (qName==QName_0_0) {
          ((KemkraftDataBE)value).setCollectDateTime(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseDateTimeToCalendar(strValue));
          return true;}
        else if (qName==QName_0_25) {
          ((KemkraftDataBE)value).setPlantCode(strValue);
          return true;}
        else if (qName==QName_0_3) {
          ((KemkraftDataBE)value).setVIN(strValue);
          return true;}
        else if (qName==QName_0_1) {
          ((KemkraftDataBE)value).setMachineNo(strValue);
          return true;}
        else if (qName==QName_0_26) {
          ((KemkraftDataBE)value).setShiftNo(strValue);
          return true;}
        else if (qName==QName_0_27) {
          ((KemkraftDataBE)value).setTool_ID(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseint(strValue));
          return true;}
        else if (qName==QName_0_28) {
          ((KemkraftDataBE)value).setStart_Time_0_Percent(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseDateTimeToCalendar(strValue));
          return true;}
        else if (qName==QName_0_29) {
          ((KemkraftDataBE)value).setAngle_Result_0_Percent(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseBigDecimal(strValue));
          return true;}
        else if (qName==QName_0_30) {
          ((KemkraftDataBE)value).setAngle_Status_0_Percent(strValue);
          return true;}
        else if (qName==QName_0_31) {
          ((KemkraftDataBE)value).setForce_Result_0_Percent(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseBigDecimal(strValue));
          return true;}
        else if (qName==QName_0_32) {
          ((KemkraftDataBE)value).setForce_Status_0_Percent(strValue);
          return true;}
        else if (qName==QName_0_33) {
          ((KemkraftDataBE)value).setStart_Time_3_Percent(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseDateTimeToCalendar(strValue));
          return true;}
        else if (qName==QName_0_34) {
          ((KemkraftDataBE)value).setSeconds_Difference(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_35) {
          ((KemkraftDataBE)value).setAngle_Result_3_Percent(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseBigDecimal(strValue));
          return true;}
        else if (qName==QName_0_36) {
          ((KemkraftDataBE)value).setAngle_Status_3_Percent(strValue);
          return true;}
        else if (qName==QName_0_37) {
          ((KemkraftDataBE)value).setForce_Result_3_Percent(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseBigDecimal(strValue));
          return true;}
        else if (qName==QName_0_38) {
          ((KemkraftDataBE)value).setForce_Status_3_Percent(strValue);
          return true;}
        else if (qName==QName_0_39) {
          ((KemkraftDataBE)value).setLastUpdated_DateTime(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parseDateTimeToCalendar(strValue));
          return true;}
        else if (qName==QName_0_20) {
          ((KemkraftDataBE)value).setLocationID(strValue);
          return true;}
        else if (qName==QName_0_21) {
          ((KemkraftDataBE)value).setApplicationID(strValue);
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
    private final static javax.xml.namespace.QName QName_0_27 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Tool_ID");
    private final static javax.xml.namespace.QName QName_0_32 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Force_Status_0_Percent");
    private final static javax.xml.namespace.QName QName_0_36 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Angle_Status_3_Percent");
    private final static javax.xml.namespace.QName QName_0_21 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "ApplicationID");
    private final static javax.xml.namespace.QName QName_0_37 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Force_Result_3_Percent");
    private final static javax.xml.namespace.QName QName_0_28 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Start_Time_0_Percent");
    private final static javax.xml.namespace.QName QName_0_26 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "ShiftNo");
    private final static javax.xml.namespace.QName QName_0_3 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "VIN");
    private final static javax.xml.namespace.QName QName_0_30 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Angle_Status_0_Percent");
    private final static javax.xml.namespace.QName QName_0_35 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Angle_Result_3_Percent");
    private final static javax.xml.namespace.QName QName_0_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "LocationID");
    private final static javax.xml.namespace.QName QName_0_31 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Force_Result_0_Percent");
    private final static javax.xml.namespace.QName QName_0_39 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "LastUpdated_DateTime");
    private final static javax.xml.namespace.QName QName_0_34 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Seconds_Difference");
    private final static javax.xml.namespace.QName QName_0_29 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Angle_Result_0_Percent");
    private final static javax.xml.namespace.QName QName_0_25 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "PlantCode");
    private final static javax.xml.namespace.QName QName_0_1 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "MachineNo");
    private final static javax.xml.namespace.QName QName_0_33 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Start_Time_3_Percent");
    private final static javax.xml.namespace.QName QName_0_0 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "CollectDateTime");
    private final static javax.xml.namespace.QName QName_0_38 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Force_Status_3_Percent");
}
