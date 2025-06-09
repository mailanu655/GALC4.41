package com.honda.mfg.stamp.storage.service.messagebuilders;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * User: vcc30690
 * Date: 3/21/11
 */
public class JSonResponseParser {

    private XStream xstream;

    public JSonResponseParser() {
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.registerConverter(new DateConverter());
    }

    public Object parse(String msg, Class clazz) {
//        if (clazz.equals(StopInfoMessage.class)) {
//            xstream.addImplicitCollection(StopInfoMessage.class, "stops");
//            xstream.alias("Stops", Stop.class);
//        } else if (clazz.equals(StorageStateRefreshRequestMessage.class)) {
//            xstream.addImplicitCollection(StorageStateImpl.class, "lanes", LaneImpl.class);
//            xstream.alias("lanes", LaneImpl.class);
//            xstream.addDefaultImplementation(LinkedList.class, Queue.class);
//            xstream.addImplicitCollection(LaneImpl.class, "carrierQueue", Carrier.class);
//            xstream.alias("carrierQueue", Carrier.class);
//        }
        xstream.alias("GeneralMessage", clazz);
        return xstream.fromXML(msg);
    }


    public String toJson(Object message, Class clazz) {
//        if (clazz.equals(StopInfoMessage.class)) {
//            xstream.addImplicitCollection(StopInfoMessage.class, "stops");
//            xstream.alias("Stops", Stop.class);
//        } else if (clazz.equals(StorageStateRefreshRequestMessage.class)) {
//            xstream.addImplicitCollection(StorageStateImpl.class, "lanes");
//            xstream.alias("lanes", LaneImpl.class);
//            xstream.addDefaultImplementation(LinkedList.class, Queue.class);
//            xstream.addImplicitCollection(LaneImpl.class, "carrierQueue");
//            xstream.alias("carrierQueue", Carrier.class);
//        }
        xstream.alias("GeneralMessage", clazz);
        return xstream.toXML(message);
       // return msg;
    }
}
