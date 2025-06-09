package com.ils_tech.customXML;

import com.ils_tech.dw.ei.DWCustomDataHolder;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class DWJasonXML {

    /**
     * **************************************************************************************************
     * <p/>
     * Need to import this class which contains the object definition of the variable name/value pairs
     * to be exchanged between the host and deviceWISE
     * ****************************************************************************************************
     */

    private XPath xpath = null;
    private Document document;

    private String listenerMapId = "";
    private HashMap jasonFields = new HashMap();
    DWCustomDataHolder data;

    public DWJasonXML() {
    }

    public String getMap(String msg) {
        parseJasonMsg(msg);
        return listenerMapId;
    }

    public void parseJasonMsg(String pJasonMsg) {

        // Strip the beginning {
        String jasonMsg = pJasonMsg.substring(1);

        String[] jasonPairs = jasonMsg.split(",");

        for (int i = 0; i < jasonPairs.length; i++) {

            String[] jasonElements = jasonPairs[i].split(":");

            if (jasonElements.length == 2) {
                String varName = jasonElements[0];
                varName = removeQuotes(varName);
                String value = jasonElements[1];
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
                    jasonFields.put(varName, value);
                }
            }
        }
        data = new DWCustomDataHolder(null, null, jasonFields);
    }

    public DWCustomDataHolder getJasonMsgData(String s) {
        return data;
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
    /**********************************************************************************************
     *
     * The name of this function is used in the listener map output tab to build the reply to be sent
     * to the host.
     *
     * @param ch: DWCustomerHolder: the name/value pairs from deviceWISE that need to be converted
     *            to the appropriate output format (XML in this case)
     * @return
     *********************************************************************************************/


    /**
     * *****************************************************************************************************
     * <p/>
     * To test the parsing of the incoming XML without deviceWISE
     * Replace the fileName variable value with the location of the XML file.
     * <p/>
     * ******************************************************************************************************
     */
    public static void main(String args[]) {
        String fileName = "P:/foo.txt";
        StringBuffer sb = new StringBuffer();
        char[] binput = new char[5000];
        String xml = "";
        try {
            FileReader aFR = new FileReader(fileName);
            BufferedReader br = new BufferedReader(aFR);
            int bytesRead = 0;
            int r = 0;

            while ((r = br.read(binput)) > 0) {
                bytesRead += r;
                sb.append(binput);
            }
            char[] b = new char[bytesRead];
            System.arraycopy(binput, 0, b, 0, bytesRead);

            xml = new String(b);
            br.close();
            aFR.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DWJasonXML testJason = new DWJasonXML();
        testJason.parseJasonMsg(xml);
        System.out.println("listener map id = " + testJason.getMap(xml));
    }
}


