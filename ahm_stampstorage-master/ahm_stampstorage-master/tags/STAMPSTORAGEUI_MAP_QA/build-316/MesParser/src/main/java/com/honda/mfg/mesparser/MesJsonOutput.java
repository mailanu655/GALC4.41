package com.honda.mfg.mesparser;

import com.ils_tech.dw.ei.DWCustomDataHolder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;


public class MesJsonOutput {
    private static final String END_OF_MESSAGE = "{END_OF_MESSAGE}";

    private String listenerMapId = "";
    private DWCustomDataHolder data;

    public MesJsonOutput() {
    }

    public String getMap(String s) {
        parseJsonMsg(s);
        return listenerMapId;
    }

    public String processOutput(DWCustomDataHolder ch) {
        HashMap map = ch.getItems();
        return mapToJsonMsg(map);
    }

    public String formatErrorMessage(String id, int errCode, String errMsg) {
        StringBuffer sb = new StringBuffer("{");
        if (id != null && id.length() > 0)
            sb.append(id);
        else
            sb.append("\"errorMsg\"+:" + "\"" + errMsg + "\",");
        sb.append("\"errorCode\"+:" + "\"errCode\"");
        sb.append("}");
        return sb.toString();
    }

    /**
     * *******************************************************************************************
     * <p/>
     * The name of this function is used in the listener map output tab to build the reply to be sent
     * to the host.
     *
     * @param ch: DWCustomerHolder: the name/value pairs from deviceWISE that need to be converted
     *            to the appropriate output format (XML in this case)
     * @return *******************************************************************************************
     */
    public byte[] formatReply(DWCustomDataHolder ch) {
        HashMap fields = ch.getItems();
        StringBuffer sb = new StringBuffer();
        byte[] formattedReply = null;
        try {
            formattedReply = sb.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        return formattedReply;
    }

    /**
     * *******************************************************************************************
     * <p/>
     * The name of this function is used in the listener map output tab to build the reply to be sent
     * to the host.
     *
     * @param ch: DWCustomerHolder: the name/value pairs from deviceWISE that need to be converted
     *            to the appropriate output format (XML in this case)
     * @return *******************************************************************************************
     */
    public byte[] formatReplyEmpty(DWCustomDataHolder ch) {
        HashMap fields = ch.getItems();

        StringBuffer sb = new StringBuffer();

        byte[] formattedReply = null;
        try {
            formattedReply = sb.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        return formattedReply;
    }

    void parseJsonMsg(String json) {
        Properties p;
        JsonToPropertiesConverter converter = new JsonToPropertiesConverter(json);
        p = converter.getProperties();
        listenerMapId = p.getProperty("messageType");
        HashMap map = new HashMap();
        map.putAll(p);
        data = new DWCustomDataHolder(null, null, map);
    }

    private String mapToJsonMsg(HashMap map) {
        Properties p = new Properties();
        p.putAll(map);
        JsonFromPropertiesConverter converter = new JsonFromPropertiesConverter(p);

        listenerMapId = p.getProperty("GeneralMessage_messageType");
        return "" + converter.getJson() + END_OF_MESSAGE;
    }
}


