package com.honda.mfg.connection.processor.messages;

/**
 * User: vcc30690
 * Date: 4/12/11
 */
public class PingMessage extends ConnectionMessageBase {
    static final String HEALTHY_STATE_CODE = "101";
    static final String UNHEALTHY_STATE_CODE = "100";
    private static final String LAST_TIME = "LASTSTOREANDFORWARDEVENTTIME";
    private static final String LAST_STATE = "LASTSTOREANDFORWARDSTATE";

    private String lastStoreAndForwardEventTime;
    private String lastStoreAndForwardState;

    public PingMessage() {
        this("", false);
    }

    public PingMessage(String response) {
        super(response);
        parseLastStoreAndForwardEventTime(response);
        parseLastStoreAndForwardState(response);
    }

    public PingMessage(String lastStoreAndForwardEventTime, boolean lastStoreAndForwardState) {
        //EXAMPLE:  {"GeneralMessage":{"lastStoreAndForwardEventTime":"2011-06-18 11:52:37.","lastStoreAndForwardState":"101","messageType":"PING"}}
        super("{\"messageType\":\"PING\",\"lastStoreAndForwardEventTime\":\""
                + lastStoreAndForwardEventTime
                + "\",\"lastStoreAndForwardState\":\""
                + (lastStoreAndForwardState == true ? HEALTHY_STATE_CODE : UNHEALTHY_STATE_CODE)
                + "\",\"serviceId\":\"XXX\"}");
        this.lastStoreAndForwardEventTime = lastStoreAndForwardEventTime;
        this.lastStoreAndForwardState = lastStoreAndForwardState == true ? HEALTHY_STATE_CODE : UNHEALTHY_STATE_CODE;
    }

    private void parseLastStoreAndForwardEventTime(final String response) {
        String responseUpper = response == null ? "" : response.toUpperCase();
        int startIndex = responseUpper.indexOf(LAST_TIME);
        startIndex = startIndex > -1 ? startIndex + LAST_TIME.length() + 1 : startIndex;
        startIndex = startIndex > -1 ? responseUpper.indexOf("\"", startIndex) + 1 : startIndex;
        int endIndex = -1;
        if (startIndex > -1) {
            endIndex = responseUpper.indexOf(",", startIndex);
            endIndex = endIndex < 0 ? responseUpper.indexOf("}", startIndex) : endIndex;
            endIndex = endIndex > -1 ? endIndex - 1 : endIndex;
        }
        if (startIndex > -1 && endIndex >= startIndex) {
            this.lastStoreAndForwardEventTime = responseUpper.substring(startIndex, endIndex);
        }
    }

    private void parseLastStoreAndForwardState(final String response) {
        String responseUpper = response == null ? "" : response.toUpperCase();
        int startIndex = responseUpper.indexOf(LAST_STATE);
        startIndex = startIndex > -1 ? startIndex + LAST_STATE.length() : -1;
        int endIndex = -1;
        if (startIndex > -1) {
            endIndex = responseUpper.indexOf(",", startIndex);
            endIndex = endIndex < 0 ? responseUpper.indexOf("}", startIndex) : endIndex;
        }
        if (endIndex > startIndex) {
            String value = responseUpper.substring(startIndex, endIndex);
            if (value.contains(HEALTHY_STATE_CODE)) {
                this.lastStoreAndForwardState = HEALTHY_STATE_CODE;
            } else {
                this.lastStoreAndForwardState = UNHEALTHY_STATE_CODE;
            }
        }
    }

    public String getLastStoreAndForwardEventTime() {
        return lastStoreAndForwardEventTime;
    }

    public boolean isHealthy() {
        return this.lastStoreAndForwardState != null &&
                this.lastStoreAndForwardState.contains(HEALTHY_STATE_CODE);
    }
}
