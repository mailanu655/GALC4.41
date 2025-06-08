/**
 * KemkraftDataBE_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class KemkraftDataBE_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public KemkraftDataBE_Ser(
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
           elemQName = QName_0_0;
           context.qName2String(elemQName, true);
           elemQName = QName_0_25;
           context.qName2String(elemQName, true);
           elemQName = QName_0_3;
           context.qName2String(elemQName, true);
           elemQName = QName_0_1;
           context.qName2String(elemQName, true);
           elemQName = QName_0_26;
           context.qName2String(elemQName, true);
           elemQName = QName_0_27;
           context.qName2String(elemQName, true);
           elemQName = QName_0_28;
           context.qName2String(elemQName, true);
           elemQName = QName_0_29;
           context.qName2String(elemQName, true);
           elemQName = QName_0_30;
           context.qName2String(elemQName, true);
           elemQName = QName_0_31;
           context.qName2String(elemQName, true);
           elemQName = QName_0_32;
           context.qName2String(elemQName, true);
           elemQName = QName_0_33;
           context.qName2String(elemQName, true);
           elemQName = QName_0_34;
           context.qName2String(elemQName, true);
           elemQName = QName_0_35;
           context.qName2String(elemQName, true);
           elemQName = QName_0_36;
           context.qName2String(elemQName, true);
           elemQName = QName_0_37;
           context.qName2String(elemQName, true);
           elemQName = QName_0_38;
           context.qName2String(elemQName, true);
           elemQName = QName_0_39;
           context.qName2String(elemQName, true);
           elemQName = QName_0_20;
           context.qName2String(elemQName, true);
           elemQName = QName_0_21;
           context.qName2String(elemQName, true);
        return attributes;
    }
    protected void addElements(
        java.lang.Object value,
        com.ibm.ws.webservices.engine.encoding.SerializationContext context)
        throws java.io.IOException
    {
        KemkraftDataBE bean = (KemkraftDataBE) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_0_0;
          propValue = bean.getCollectDateTime();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_22,
              true,null,context);
          propQName = QName_0_25;
          propValue = bean.getPlantCode();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_3;
          propValue = bean.getVIN();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_1;
          propValue = bean.getMachineNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_26;
          propValue = bean.getShiftNo();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_27;
          propValue = new java.lang.Integer(bean.getTool_ID());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_40,
              true,null,context);
          propQName = QName_0_28;
          propValue = bean.getStart_Time_0_Percent();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_22,
              true,null,context);
          propQName = QName_0_29;
          propValue = bean.getAngle_Result_0_Percent();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_41,
              true,null,context);
          propQName = QName_0_30;
          propValue = bean.getAngle_Status_0_Percent();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_31;
          propValue = bean.getForce_Result_0_Percent();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_41,
              true,null,context);
          propQName = QName_0_32;
          propValue = bean.getForce_Status_0_Percent();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_33;
          propValue = bean.getStart_Time_3_Percent();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_22,
              true,null,context);
          propQName = QName_0_34;
          propValue = new java.lang.Double(bean.getSeconds_Difference());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_35;
          propValue = bean.getAngle_Result_3_Percent();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_41,
              true,null,context);
          propQName = QName_0_36;
          propValue = bean.getAngle_Status_3_Percent();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_37;
          propValue = bean.getForce_Result_3_Percent();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_41,
              true,null,context);
          propQName = QName_0_38;
          propValue = bean.getForce_Status_3_Percent();
          if (propValue != null && !context.shouldSendXSIType()) {
            context.simpleElement(propQName, null, propValue.toString()); 
          } else {
            serializeChild(propQName, null, 
              propValue, 
              QName_1_23,
              false,null,context);
          }
          propQName = QName_0_39;
          propValue = bean.getLastUpdated_DateTime();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_22,
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
        }
    }
    private final static javax.xml.namespace.QName QName_0_27 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Tool_ID");
    private final static javax.xml.namespace.QName QName_1_24 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "double");
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
    private final static javax.xml.namespace.QName QName_1_40 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "int");
    private final static javax.xml.namespace.QName QName_1_23 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
    private final static javax.xml.namespace.QName QName_0_20 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "LocationID");
    private final static javax.xml.namespace.QName QName_1_22 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
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
    private final static javax.xml.namespace.QName QName_1_41 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "decimal");
    private final static javax.xml.namespace.QName QName_0_0 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "CollectDateTime");
    private final static javax.xml.namespace.QName QName_0_38 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Force_Status_3_Percent");
}
