/**
 * HeadLightAlignmentBE_Ser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class HeadLightAlignmentBE_Ser extends com.ibm.ws.webservices.engine.encoding.ser.BeanSerializer {
    /**
     * Constructor
     */
    public HeadLightAlignmentBE_Ser(
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
           elemQName = QName_0_1;
           context.qName2String(elemQName, true);
           elemQName = QName_0_2;
           context.qName2String(elemQName, true);
           elemQName = QName_0_3;
           context.qName2String(elemQName, true);
           elemQName = QName_0_4;
           context.qName2String(elemQName, true);
           elemQName = QName_0_5;
           context.qName2String(elemQName, true);
           elemQName = QName_0_6;
           context.qName2String(elemQName, true);
           elemQName = QName_0_7;
           context.qName2String(elemQName, true);
           elemQName = QName_0_8;
           context.qName2String(elemQName, true);
           elemQName = QName_0_9;
           context.qName2String(elemQName, true);
           elemQName = QName_0_10;
           context.qName2String(elemQName, true);
           elemQName = QName_0_11;
           context.qName2String(elemQName, true);
           elemQName = QName_0_12;
           context.qName2String(elemQName, true);
           elemQName = QName_0_13;
           context.qName2String(elemQName, true);
           elemQName = QName_0_14;
           context.qName2String(elemQName, true);
           elemQName = QName_0_15;
           context.qName2String(elemQName, true);
           elemQName = QName_0_16;
           context.qName2String(elemQName, true);
           elemQName = QName_0_17;
           context.qName2String(elemQName, true);
           elemQName = QName_0_18;
           context.qName2String(elemQName, true);
           elemQName = QName_0_19;
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
        HeadLightAlignmentBE bean = (HeadLightAlignmentBE) value;
        java.lang.Object propValue;
        javax.xml.namespace.QName propQName;
        {
          propQName = QName_0_0;
          propValue = bean.getCollectDateTime();
          serializeChild(propQName, null, 
              propValue, 
              QName_1_22,
              true,null,context);
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
          propQName = QName_0_2;
          propValue = bean.getMachineSetting();
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
          propQName = QName_0_4;
          propValue = new java.lang.Double(bean.getPreset_Left_HeadLight_Vertical_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_5;
          propValue = new java.lang.Double(bean.getPreset_Left_HeadLight_Horizontal_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_6;
          propValue = new java.lang.Double(bean.getPreset_Right_HeadLight_Vertical_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_7;
          propValue = new java.lang.Double(bean.getPreset_Right_HeadLight_Horizontal_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_8;
          propValue = new java.lang.Double(bean.getPreset_Left_Fog_Vertical_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_9;
          propValue = new java.lang.Double(bean.getPreset_Left_Fog_Horizontal_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_10;
          propValue = new java.lang.Double(bean.getPreset_Right_Fog_Vertical_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_11;
          propValue = new java.lang.Double(bean.getPreset_Right_Fog_Horizontal_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_12;
          propValue = new java.lang.Double(bean.getFinal_Left_HeadLight_Vertical_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_13;
          propValue = new java.lang.Double(bean.getFinal_Left_HeadLight_Horizontal_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_14;
          propValue = new java.lang.Double(bean.getFinal_Right_HeadLight_Vertical_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_15;
          propValue = new java.lang.Double(bean.getFinal_Right_HeadLight_Horizontal_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_16;
          propValue = new java.lang.Double(bean.getFinal_Left_Fog_Vertical_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_17;
          propValue = new java.lang.Double(bean.getFinal_Left_Fog_Horizontal_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_18;
          propValue = new java.lang.Double(bean.getFinal_Right_Fog_Vertical_Qty());
          serializeChild(propQName, null, 
              propValue, 
              QName_1_24,
              true,null,context);
          propQName = QName_0_19;
          propValue = new java.lang.Double(bean.getFinal_Right_Fog_Horizontal_Qty());
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
        }
    }
    private final static javax.xml.namespace.QName QName_0_14 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://com.honda.ham/NCATCollector",
                  "Final_Right_HeadLight_Vertical_Qty");
    private final static javax.xml.namespace.QName QName_1_24 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "double");
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
    private final static javax.xml.namespace.QName QName_1_23 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "string");
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
    private final static javax.xml.namespace.QName QName_1_22 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "http://www.w3.org/2001/XMLSchema",
                  "dateTime");
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
