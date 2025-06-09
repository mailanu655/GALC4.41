package com.honda.mfg.mesproxy;

import com.ils_tech.dw.ei.DWCustomDataHolder;
import com.ils_tech.dw.ei.DWCustomRequestHelper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


/**
 * User: Adam S. Kendell
 * Date: 6/7/11
 */
public class MesProxy implements DWCustomRequestHelper {

    private String listenerMapId = "MesProxyDummyListenerMap";
    private static final String NAME = "MesProxy - ";
    private DWCustomDataHolder data;
    private static MesProxyProperties mesProxyProperties = MesProxyProperties.getInstance();
    private MesProxyDevice mesProxyDevice;

    public MesProxy() {
        startProxyDevice();
    }

    private void restartProxyDevice() {
        SimpleLog.write(NAME + "Stopping Proxy Device");
        stopProxyDevice();
        forceGarbageCollection();
        //startProxyDevice();
    }

    private void startProxyDevice() {
        SimpleLog.write(NAME + "Starting Proxy Device.");
        mesProxyDevice = new MesProxyDevice();
    }

    private void stopProxyDevice() {
        mesProxyDevice.stop();
        mesProxyDevice = null;
    }

    private void forceGarbageCollection() {
        SimpleLog.write(NAME + "Forcing garbage collection.");
        Runtime r = Runtime.getRuntime();
        r.gc();
    }

    public String getMap(String json) {
        parseJsonMsg(json);
        return listenerMapId;
    }

    public String formatErrorMessage(String id, int errCode, String errMsg) {
        StringBuffer sb = new StringBuffer("{");
        if (id != null && id.length() > 0)
            sb.append(id);
        else {
            sb.append("\" errorMsg\"+:" + "\"" + errMsg + "\",");
            sb.append("\" errCode \"+:" + "\"" + errCode + "\"");
            sb.append("}");
        }
        return sb.toString();
    }

    public DWCustomDataHolder processInput(String s) {
       SimpleLog.write(NAME + "Received proxy control message: " + s);
        return data;
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
        SimpleLog.write(NAME + "Parsing JSON proxy control data: " + json);
        restartProxyDevice();
        HashMap map = new HashMap();
        map.put("messageType", "MesProxyDummyListenerMap");
        map.put("message", json);
        data = new DWCustomDataHolder(null, null, map);

    }

}
