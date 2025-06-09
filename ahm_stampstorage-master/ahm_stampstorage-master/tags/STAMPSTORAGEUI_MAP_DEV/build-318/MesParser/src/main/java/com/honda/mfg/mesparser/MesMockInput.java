package com.honda.mfg.mesparser;

import com.ils_tech.dw.ei.DWCustomDataHolder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MesMockInput implements MesCustomMessageHandler, MesListenerMapInput {

    /**
     * **************************************************************************************************
     * <p/>
     * Need to import this class which contains the object definition of the variable name/value pairs
     * to be exchanged between the host and deviceWISE
     * ****************************************************************************************************
     */

    private String listenerMapId = "";
    private HashMap jsonFields = new HashMap();
    DWCustomDataHolder data;

    public MesMockInput() {
    }

    public DWCustomDataHolder processInput(String s) {
        System.out.println("MesMockInput.processInput(): " + s);
        System.err.println("MesMockInput.processInput(): " + s);
        return data;
    }

    public String getMap(String msg) {
        System.out.println("MesMockInput.getMap(): " + msg);
        System.err.println("MesMockInput.getMap(): " + msg);
        parseJsonMsg(msg);
        return listenerMapId;
    }

    public void parseJsonMsg(String pJsonMsg) {

        // Strip the beginning {
        String jsonMsg = pJsonMsg.substring(1);

        String[] jsonPairs = jsonMsg.split(",");

        for (int i = 0; i < jsonPairs.length; i++) {

            String[] jsonElements = jsonPairs[i].split(":");

            if (jsonElements.length == 2) {
                String varName = jsonElements[0];
                varName = removeQuotes(varName);
                String value = jsonElements[1];
                value = removeQuotes(value);
                if (varName.endsWith("Date")) {
                    // Convert to date time if required or
                    // leave as string and convert in the trigger using action
                    // String/Time formatter
                }
                //if ("tagID".equals(varName)) {
                if ("messageType".equals(varName)) {
                    listenerMapId = value;
                } else {
                    jsonFields.put(varName, value);
                }
            }
        }
        data = new DWCustomDataHolder(null, null, jsonFields);
    }

    private String removeQuotes(String var) {
        String lessQuotes = "";
        int quotePos = var.indexOf("\"");
        lessQuotes = var.substring(quotePos + 1);
        quotePos = lessQuotes.indexOf("\"");
        lessQuotes = lessQuotes.substring(0, quotePos);
        return lessQuotes;
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
    public byte[] formatReply(MesCustomDataHolder ch) {
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

    private String buildItemXMLpairs(String[] tags, HashMap fields) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tags.length; i++) {
            sb.append("  <item>\n");

            String value = (String) fields.get(tags[i].replaceAll(":", "_"));
            if (value == null) {
//					sb.append  ("      <" + tags[i].substring(2) + "/>\n");
            } else {
//					sb.append("      <" + tags[i].substring(2) + ">" + value + "</" + tags[i].substring(2) + ">\n");
                sb.append("    <NAME>" + tags[i] + "</NAME>\n");
                sb.append("    <VALUE>" + value + "</VALUE>\n");
            }
            sb.append("  </item>\n");
        }
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
    public byte[] formatReplyEmpty(MesCustomDataHolder ch) {
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
}


