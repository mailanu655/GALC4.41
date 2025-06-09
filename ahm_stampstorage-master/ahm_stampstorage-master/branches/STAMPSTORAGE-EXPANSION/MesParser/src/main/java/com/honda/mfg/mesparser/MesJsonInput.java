package com.honda.mfg.mesparser;

import com.ils_tech.dw.ei.DWCustomDataHolder;
import com.ils_tech.dw.ei.DWCustomRequestHelper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

public class MesJsonInput implements DWCustomRequestHelper {
    private String listenerMapId = "";
    private DWCustomDataHolder data;

    public MesJsonInput() {
    }

    public String getMap(String json) {
        parseJsonMsg(json);
        return listenerMapId;
    }

    public DWCustomDataHolder processInput(String s) {
        return data;
    }

    public String formatErrorMessage(String id, int errCode, String errMsg) {
        StringBuffer sb = new StringBuffer("{");
        if (id != null && id.length() > 0)
            sb.append(id);
        else
            sb.append("\"errorMsg\":" + "\"" + errMsg + "\",");
        sb.append("\"errorCode\":" + "\"" + errCode + "\"");
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

        StringBuffer sb = new StringBuffer("");

        byte[] formattedReply = null;
        try {
            formattedReply = sb.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        return formattedReply;
    }

    private void parseJsonMsg(String json) {
        Properties p;
        JsonToPropertiesConverter converter = new JsonToPropertiesConverter(json);
        p = converter.getProperties();
        listenerMapId = p.getProperty("messageType");
        HashMap map = new HashMap();
        map.putAll(p);
        data = new DWCustomDataHolder(null, null, map);

    }
}


